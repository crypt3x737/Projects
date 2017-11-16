using System;
using System.Collections.Generic;
using System.Collections;
using System.Data;
using System.Windows.Forms;
using System.Linq;
using System.Collections.ObjectModel;

namespace FirmsChemVS.Services
{
    /// <summary>
    /// This file sounds like it is the forward facing part that interfaces between the backend and the
    /// GUI.  There is a data writing, initializing, and reading mechanism.  
    /// </summary>
    public class FirmsChemFacade
    {
        private static FirmsChemFacade instance;
        private DataReporter dataReporter;
        private DataWriter dataWriter;
        private DataInitializer initializer;
        private Calculations calculations;
        private IsotopeService isotopeService;
        /// <summary>
        /// Constructor for the facade.  It creates a writing mechanism, an initializer
        /// and a reading mechanism.  It also creates an isotopeService object which I think
        /// is a manipulator for the isotopes of different atoms
        /// </summary>
        private FirmsChemFacade()
        {
            dataWriter = new DataWriter();
            initializer = new DataInitializer();
            calculations = new Calculations();
            isotopeService = new IsotopeService();
        }

        /// <summary>
        /// This creates the intance property for the facade singleton
        /// </summary>
        public static FirmsChemFacade Instance 
        {
            get
            {
                if(instance == null)
                {
                    instance = new FirmsChemFacade();
                }
                return instance;
            }
        }

        /// <summary>
        /// This function initializes a DataGrid element with 0's
        /// </summary>
        /// <param name="elementGrid">an objec that needs to sent in to initialize to 0's</param>
        public void InitGrids(Object elementGrid)
        {
            initializer.InitializeElementGrid(elementGrid as DataGridView);
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="elementGrid"></param>
        /// <returns></returns>
        public double CalculateMolecularWeight(DataGridView elementGrid)
        {
            DataTable dt = DataReporter.convertDatagridToDatatable(elementGrid);
            return calculations.CalculateMolecularWeight(dt);
        }

        public string CalculateMolecularFormula(DataGridView elementGrid)
        {
            DataTable dt = DataReporter.convertDatagridToDatatable(elementGrid);
            return calculations.CalculateMolecularFormula(dt);
        }

        public bool exportDataSetToExcelFile(DataGridView grid)
        {
            dataReporter = new DataReporter(grid);
            return DataReporter.exportDataSetToExcelFile(dataReporter.DataForGrid2);
        }

        public void abundanceForRow(DataGridView grid)
        {
            ArrayList Abundances = DataGridViewHelper.ColumnToList(grid, "abundances");
            double totalAbundance = 0;

            foreach (String abundance in Abundances)
            {
                totalAbundance += Convert.ToDouble(abundance);
            }

            foreach (DataGridViewRow row in grid.Rows)
            {
                if (row.Cells["abundances"].Value != null)
                {
                    row.Cells["alphas"].Value = calculations.calculateAlphaForRow(totalAbundance, Convert.ToDouble(row.Cells["abundances"].Value));
                }
            }
        }

        public void abundanceForRow(ObservableCollection<IonMassGridBinder> abundances)
        {
            double totalAbundance = abundances.Sum(e => e.Abundance);

            foreach (IonMassGridBinder binder in abundances)
            {
                binder.Alpha = calculations.calculateAlphaForRow(totalAbundance, binder.Abundance);
                Console.WriteLine(binder.Alpha);
            }
        }
    }
}
