using System;
using System.Collections.Generic;
using System.Collections;
using System.Data;
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
        private Calculations calculations;
        private IsotopeService isotopeService;
        /// <summary>
        /// Constructor for the facade.  It creates a writing mechanism, an initializer
        /// and a reading mechanism.  It also creates an isotopeService object which I think
        /// is a manipulator for the isotopes of different atoms
        /// </summary>
        private FirmsChemFacade()
        {
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
