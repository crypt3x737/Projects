﻿// Importing system library
using System;
using System.IO;
using System.Collections.Generic;
using System.Collections;
using System.Collections.Specialized;
using System.Text.RegularExpressions;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using FirmsChemVS.Repositories;
using FirmsChemVS.Services;
using DoddleReport;
using DoddleReport.OpenXml;
using System.Windows;
using System.Data.OleDb;





namespace FirmsChemVS
{
    public partial class Form1 : Form
    {
        FirmsChemFacade facade = FirmsChemFacade.Instance;
        // Declearing all the variables as strings, doubles or integers
        String IsCompoundName; // compound name
        IsotopeService isotopeService = IsotopeService.Instance;
        Calculations calculationSevice = Calculations.Instance;
        BaseTableMassService baseTableService = BaseTableMassService.Instance;

        int[] rowWindow = new int[2] { -1, -1 };



        // Integer values for the number of each atom present in the compound

        public Form1()
        {
            this.KeyPreview = true;
            this.KeyUp += new KeyEventHandler(TextBox1_KeyPress);
            //new KeyPressEventHandler(TextBox1_KeyPress);
            //TextBox1.KeyPress +=
            //    new KeyPressEventHandler(TextBox1_KeyPress);
            InitializeComponent();
            facade.InitGrids(elementGrid);
        }

        private void LayoutCompoundTable() // Creates the data for the isotopes and fragments 
        {

            int lastColumn = dataGridView2.Columns.Count;
            foreach (DataGridViewRow entry in elementGrid.Rows)
            {
                int numberOfAtoms = Convert.ToInt32(entry.Cells["numberOfAtoms"].Value);
                if (numberOfAtoms > 0)
                {
                    for (int index = 1; index <= numberOfAtoms; index++)
                    {
                        string elementSymbol = (string)entry.Cells["elements"].Value;
                        DataGridViewCheckBoxColumn columnToAdd = new DataGridViewCheckBoxColumn();
                        columnToAdd.HeaderText = elementSymbol + index;
                        columnToAdd.Name = elementSymbol + index;
                        int columnId = dataGridView2.Columns.Add(columnToAdd);
                    }
                }
            }

        }



        private void clearColumnsForGrid2()
        {
            for (int columnIndx = dataGridView2.Columns.Count - 1; columnIndx >= 5; columnIndx--)
            {
                dataGridView2.Columns.RemoveAt(columnIndx);
            }
        }

        private void btnProcessInformation_Click(object sender, EventArgs e)
        {
            if (dataGridView2.Columns.Count > 5)
            {
                clearColumnsForGrid2();
            }

            IsCompoundName = tbCompound.Text;
            // TryParse converts string to integer to represent the type of what you are looking for
            molwei.Text = facade.CalculateMolecularWeight(elementGrid).ToString();
            molfur.Text = facade.CalculateMolecularFormula(elementGrid);
            //facade.CalculateIsotopeCombinations(elementGrid);
            LayoutCompoundTable();

        }

        private void btnExportExcel_Click(object sender, EventArgs e)
        {
            bool isReportSaved = facade.exportDataSetToExcelFile(dataGridView2);
            if (isReportSaved)
            {
                MessageBox.Show("File has been saved under My Documents");
            }
            else
            {
                MessageBox.Show("Unable to save file");
            }
        }

        private void btnSpectrum_Click(object sender, EventArgs e)
        {
            for (int n = 0; n < baseMassCalcTable.Rows.Count; n++)
            {
                DataGridViewRow row = baseMassCalcTable.Rows[n];
                {

                    this.chart1.Series[0].Points.AddXY("m/z" + (n + 1), Convert.ToDouble(baseMassCalcTable.Rows[n].Cells[3].Value));

                }
            }
        }

        private void btnNext_Click(object sender, EventArgs e)
        {
            chart1.Series[0].Points.Clear();
            chart1.Series[1].Points.Clear();
            if (rowWindow[0] == -1)
            {
                foreach (DataGridViewRow row in dataGridView2.Rows)
                {
                    if (row.Cells[0].Value != null && rowWindow[0] == -1)
                    {
                        rowWindow[0] = row.Index;
                    }
                    else if (row.Cells[0].Value != null && rowWindow[1] == -1)
                    {
                        rowWindow[1] = row.Index;
                    }

                    if (rowWindow[0] != -1 && rowWindow[1] != -1)
                    {
                        break;
                    }
                }
            }

            for (int index = 0; index < rowWindow[1] - rowWindow[0]; index++)
            {
                int currentRow = rowWindow[0] + index;
                chart1.Series[0].Points.AddXY(index, dataGridView2.Rows[currentRow].Cells[2].Value);
                chart1.Series[1].Points.AddXY(index, dataGridView2.Rows[currentRow].Cells[4].Value);
            }
        }

        private ArrayList processCurrentColumn(DataGridViewColumn column)
        {
            ArrayList numberList = new ArrayList();
            for (int rowCount = 0; rowCount < dataGridView2.Rows.Count; rowCount++)
            {
                if (dataGridView2.Rows[rowCount].Cells["Abundance"].Value != null)
                {
                    numberList.Add(dataGridView2.Rows[rowCount].Cells["Abundance"].Value);
                }
            }

            return numberList;
        }
      


        private void dataGridView1_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {
            foreach (DataGridViewRow row in baseMassCalcTable.Rows)
            {
                double sumadd = 0;
                int n = 0;
                for (long i = 0; i < baseMassCalcTable.Rows.Count; ++i)
                {
                    sumadd += Convert.ToDouble(baseMassCalcTable.Rows[n].Cells[3].Value);
                }

            }

        }



        private void baseMassCalcTable_RowLeave(object sender, DataGridViewCellEventArgs e)
        {
            var grid = sender as DataGridView;
            facade.abundanceForRow(baseMassCalcTable);
        }

        private void btnAddRowToBassMassTable_Click(object sender, EventArgs e)
        {
            Console.WriteLine("Printing");
            baseMassCalcTable.Rows.Add();
            baseTableService.TableCount = baseMassCalcTable.Rows.Count;
        }


        private void btnChooseFile_Click(object sender, EventArgs e)
        {
            OpenFileDialog openFileDialog1 = new OpenFileDialog();

            if (openFileDialog1.ShowDialog() == System.Windows.Forms.DialogResult.OK)
            {
                this.textBox1path.Text = openFileDialog1.FileName;
            }
        }

        private Dictionary<int, double> sumUpAbundances()
        {
            var result = new Dictionary<int, double>();
            int lastIndex = 0;
            List<double> abundances = DataGridViewHelper.ColumnToList(dataGridView2, "Abundance").ToArray().Select(s => Convert.ToDouble(s)).ToList();
            List<int> baseParentIon = DataGridViewHelper.ColumnToList(dataGridView2, "Baseparentionmass").ToArray().Select(s => Convert.ToInt32(s)).ToList();


            foreach (int ion in baseParentIon)
            {
                List<double> subsetOfAbundance = abundances.GetRange(lastIndex, baseTableService.TableCount);
                result.Add(ion, subsetOfAbundance.Sum());
                lastIndex += baseTableService.TableCount;
            }


            return result;
        }


        //elements is the number of every element to add
        private void importData(object sender, int[] elements, int addRows, int[] baseIon, int[] daughterIon, string[] abundance,
                                            int[] baseIonL, int[] daughterIonL, string[] abundanceL, int width, int height, int[,] isChecked)
        {
            for (int i = 0; i <= 10; i++)
            {
                elementGrid.Rows[i].Cells[1].Value = elements[i];
            }
            elementGrid.Refresh();
            baseMassCalcTable.Rows.Clear();
            for (int i = 0; i < addRows; i++)
            {
                baseMassCalcTable.Rows.Add();
                baseTableService.TableCount = baseMassCalcTable.Rows.Count;
            }

            for (int i = 0; i < addRows; i++)
            {
                if (baseIon[i] != -1)
                {
                    baseMassCalcTable.Rows[i].Cells[0].Value = baseIon[i];
                }
                if (daughterIon[i] != -1)
                {
                    baseMassCalcTable.Rows[i].Cells[1].Value = daughterIon[i];
                }
                if (abundance[i] != "")
                {
                    baseMassCalcTable.Rows[i].Cells[2].Value = abundance[i];
                }
            }

            foreach (DataGridViewRow row in baseMassCalcTable.Rows)
            {
                double sumadd = 0;
                int n = 0;
                for (long i = 0; i < baseMassCalcTable.Rows.Count; ++i)
                {
                    sumadd += Convert.ToDouble(baseMassCalcTable.Rows[n].Cells[3].Value);
                }

            }
            var grid = sender as DataGridView;
            facade.abundanceForRow(baseMassCalcTable);
            baseMassCalcTable.Refresh();
            dataGridView2.Rows.Clear();

            if (dataGridView2.Columns.Count > 5)
            {
                clearColumnsForGrid2();
            }

            IsCompoundName = tbCompound.Text;
            // TryParse converts string to integer to represent the type of what you are looking for
            molwei.Text = facade.CalculateMolecularWeight(elementGrid).ToString();
            molfur.Text = facade.CalculateMolecularFormula(elementGrid);
            //facade.CalculateIsotopeCombinations(elementGrid);
            LayoutCompoundTable();

            for (int i = 0; i < addRows; i++)
            {
                dataGridView2.Rows.Add();
            }


            for (int i = 0; i < addRows; i++)
            {
                if (baseIonL[i] != -1)
                {
                    dataGridView2.Rows[i].Cells[0].Value = baseIonL[i];
                }
                if (daughterIonL[i] != -1)
                {
                    dataGridView2.Rows[i].Cells[1].Value = daughterIonL[i];
                }
                if (abundanceL[i] != "")
                {
                    dataGridView2.Rows[i].Cells[2].Value = abundanceL[i];
                }
            }
            for (int i = 0; i < width; i++)
            {
                for (int j = 0; j < height; j++)
                {
                    if (isChecked[j, i] == 1)
                    {
                        dataGridView2.Rows[j].Cells[5 + i].Value = true;
                    }
                    else
                    {
                        dataGridView2.Rows[j].Cells[5 + i].Value = false;
                    }
                }

            }
            //dataGridView2.Rows[0].Cells[5].Value = 1;

        }

        void TextBox1_KeyPress(object sender, KeyEventArgs e)
        {

            if ((e.Shift && e.KeyCode == Keys.Insert) || (e.Control && e.KeyCode == Keys.V))
            {

                char[] rowSplitter = { '\r', '\n' };
                char[] columnSplitter = { '\t' };
                //get the text from clipboard
                IDataObject dataInClipboard = Clipboard.GetDataObject();
                string stringInClipboard = (string)dataInClipboard.GetData(DataFormats.Text);
                //split it into lines
                string[] rowsInClipboard = stringInClipboard.Split(rowSplitter, StringSplitOptions.RemoveEmptyEntries);
                //get the row and column of selected cell in grid
                int r = baseMassCalcTable.SelectedCells[0].RowIndex;
                int c = baseMassCalcTable.SelectedCells[0].ColumnIndex;
                //add rows into grid to fit clipboard lines
                if (baseMassCalcTable.Rows.Count < (r + rowsInClipboard.Length))
                {
                    baseMassCalcTable.Rows.Add(r + rowsInClipboard.Length - baseMassCalcTable.Rows.Count);
                }
                // loop through the lines, split them into cells and place the values in the corresponding cell.
                for (int iRow = 0; iRow < rowsInClipboard.Length; iRow++)
                {
                    //split row into cell values
                    string[] valuesInRow = rowsInClipboard[iRow].Split(columnSplitter);
                    //cycle through cell values
                    for (int iCol = 0; iCol < valuesInRow.Length; iCol++)
                    {
                        //assign cell value, only if it within columns of the grid
                        if (baseMassCalcTable.ColumnCount - 1 >= c + iCol)
                        {
                            baseMassCalcTable.Rows[r + iRow].Cells[c + iCol].Value = valuesInRow[iCol];
                        }
                    }
                }
                Console.WriteLine("Yes!!! I am running :) ");

            }
            Console.WriteLine("run ");
            // Console.WriteLine("Yes!!! I am running :) : " + e.KeyChar.ToString());

        }

        private void addData(object sender, EventArgs e)
        {
            IDataObject iData = Clipboard.GetDataObject();
            Console.WriteLine("Start.");
            if (iData.GetDataPresent(DataFormats.Text))
                Console.WriteLine((String)iData.GetData(DataFormats.Text));
            else
                Console.WriteLine("Data not found.");
            if (importDataDropDown.SelectedItem.ToString() == "BrO3")
            {
                int[] elements = { 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 3 };
                int addRows = 4;
                int[] baseIon = { 127, -1, -1, -1 };
                int[] daughterIon = { 79, 95, 111, 127 };
                string[] abundance = { "145110000", "249910000", "430750000", "344330000" };
                int[] baseIonL = { 129, -1, -1, -1 };
                int[] daughterIonL = { 81, 97, 113, 129 };
                string[] abundanceL = { "133310000", "229020000", "291270000", "105020000" };
                int width = 4, height = 4;
                int[,] isChecked = { { 1, 0, 0, 0},
                                    { 1, 1, 0, 0},
                                    { 1, 1, 1, 0},
                                    { 1, 1, 1, 1} };

                importData(sender, elements, addRows, baseIon, daughterIon, abundance, baseIonL, daughterIonL, abundanceL,
                                                                        width, height, isChecked);




            }
            if (importDataDropDown.SelectedItem.ToString() == "CH2Cl2")
            {
                int[] elements = { 1, 2, 0, 2, 0, 0, 0, 0, 0, 0, 0 };
                int addRows = 4;
                int[] baseIon = { 85, -1, -1, -1 };
                int[] daughterIon = { 48, 49, 50, 85 };
                string[] abundance = { "88310000", "179900000", "151800000", "1560000" };
                int[] baseIonL = { 87, -1, -1, -1 };
                int[] daughterIonL = { 50, 51, 52, 87 };
                string[] abundanceL = { "6790000", "9840000", "7680000", "320000" };
                int width = 5, height = 4;
                int[,] isChecked = { { 1, 1, 0, 1,0},
                                    { 1, 1, 1, 1,0},
                                    { 1, 1, 1, 1,0},
                                    { 1, 1, 1, 1,1} };

                importData(sender, elements, addRows, baseIon, daughterIon, abundance, baseIonL, daughterIonL, abundanceL,
                                                                        width, height, isChecked);




            }
            if (importDataDropDown.SelectedItem.ToString() == "C8H7ClO2")
            {
                int[] elements = { 8, 7, 0, 1, 0, 0, 0, 0, 0, 0, 2 };
                int addRows = 3;
                int[] baseIon = { 153, -1, -1 };
                int[] daughterIon = { 73, 99, 125 };
                string[] abundance = { "1352990000", "1687990000", "262720000" };
                int[] baseIonL = { 155, -1, -1 };
                int[] daughterIonL = { 75, 101, 127 };
                string[] abundanceL = { "1124090000", "1462620000", "264560000" };
                int width = 18, height = 3;
                int[,] isChecked = { { 1,1,1,0,0,0,0,0,1,1,0,0,0,0,0,1,0,0},
                    { 1,1,1,1,1,0,0,0,1,1,1,1,0,0,0,1,0,0},
                    { 1,1,1,1,1,1,1,0,1,1,1,1,1,1,0,1,0,0}
                                     };

                importData(sender, elements, addRows, baseIon, daughterIon, abundance, baseIonL, daughterIonL, abundanceL,
                                                                        width, height, isChecked);




            }
            if (importDataDropDown.SelectedItem.ToString() == "CH3Cl2")
            {
                int[] elements = { 1, 3, 0, 2, 0, 0, 0, 0, 0, 0, 0 };
                int addRows = 4;
                int[] baseIon = { 85, -1, -1, -1 };
                int[] daughterIon = { 48, 49, 50, 85 };
                string[] abundance = { "88310000", "179900000", "151800000", "1560000" };
                int[] baseIonL = { 87, -1, -1, -1 };
                int[] daughterIonL = { 50, 51, 52, 87 };
                string[] abundanceL = { "6790000", "9840000", "7680000", "320000" };
                int width = 6, height = 4;
                int[,] isChecked = { { 1, 1, 0, 0,1,0},
                                    { 1, 1, 1, 0,1,0},
                                    { 1, 1, 1, 1,1,0},
                                    { 1, 1, 1, 1,1,1} };

                importData(sender, elements, addRows, baseIon, daughterIon, abundance, baseIonL, daughterIonL, abundanceL,
                                                                        width, height, isChecked);




            }
            System.Console.WriteLine("Function got called :" + importDataDropDown.SelectedItem);
            // MessageBox.Show("Typical installation is strongly recommended.",
            //      "Install information", MessageBoxButtons.OK,
            // MessageBoxIcon.Information);


        }



        private void btnCalculateAbundance_Click(object sender, EventArgs e)
        {
            ///How many times it generates optimization
            Console.WriteLine("Solution Error\tData:");
            //for (int i = 0; i < 100; i++)
            //{
            Abundance data = new Abundance();
            int parentIonTotal = 0;
            double alpha;
            int positionOfBaseMassCalcTable;
            Dictionary<string, int> elemGrid = DataGridViewHelper.elementDataGridToDictionary(elementGrid);
            isotopeService.getAllAbundancesInIsotopeSymbols(elemGrid, data);
            var allIsotopes = isotopeService.getAllIsotopesInIsotopeSymbols(DataGridViewHelper.elementDataGridToDictionary(elementGrid).Keys.ToArray());
            var abundanceTotals = sumUpAbundances();
            foreach (DataGridViewRow row in dataGridView2.Rows)
            {
                if (row.Cells["Abundance"].Value != null)
                {

                    positionOfBaseMassCalcTable = baseTableService.incrementBaseTableMassPosition();
                    if (positionOfBaseMassCalcTable == -9)
                    {
                        MessageBox.Show("Seems like there are no available alphas, please enter abundances in the above table");
                        return;
                    }
                    alpha = Convert.ToDouble(baseMassCalcTable.Rows[positionOfBaseMassCalcTable].Cells[3].Value);

                    if (row.Cells["Baseparentionmass"].Value != null)
                    {
                        parentIonTotal = Convert.ToInt32(row.Cells["Baseparentionmass"].Value);
                    }

                    Dictionary<string, int> rowElements = new Dictionary<string, int>();
                    Dictionary<string, object> possibleIsotopesPerElement = new Dictionary<string, object>();
                    List<int> temp = new List<int>();

                    for (int index = 5; index < dataGridView2.ColumnCount; index++)
                    {
                        if (row.Cells[index].Value == null)
                        {
                            break;
                        }
                        if ((bool)(row.Cells[index] as DataGridViewCheckBoxCell).Value == true)
                        {
                            //JB - 6/29/2015
                            temp.Add(index - 5);
                            //---------------




                            int retrievedValue;
                            string headerElement = new String(dataGridView2.Columns[index].HeaderText.Where(Char.IsLetter).ToArray()); //dataGridView2.Columns[index].HeaderText.Substring(0, (dataGridView2.Columns[index].HeaderText.Length - 1));
                            if (rowElements.TryGetValue(headerElement, out retrievedValue))
                            {
                                rowElements[headerElement] = retrievedValue + 1;
                            }
                            else
                            {
                                rowElements.Add(headerElement, 1);
                            }
                        }
                    }
                    data.addChems(temp);

                    foreach (string item in rowElements.Keys)
                    {

                        int[] listofIsotopeInts = isotopeService.getAllIsotopesForAtom(item).Select(x => x.isotopeNumber ?? 0).OrderByDescending(value => value).ToArray();

                        possibleIsotopesPerElement.Add(item, listofIsotopeInts);
                    }
                    int[] isotopeList = new int[] { };
                    foreach (int[] item in possibleIsotopesPerElement.Values)
                    {
                        isotopeList = isotopeList.Concat(item).ToArray();
                    }
                    int[] counts = Enumerable.Repeat(0, isotopeList.Length).ToArray();
                    int daughterIon = Convert.ToInt32(row.Cells["Daughterionmass"].Value);
                    List<Dictionary<int, int>> numerator = new List<Dictionary<int, int>>();
                    List<Dictionary<int, int>> denominator = new List<Dictionary<int, int>>();
                    calculationSevice.CurrentRowElements = rowElements;
                    calculationSevice.isotopeCombinations(isotopeList.OrderByDescending(ic => ic).ToArray(), counts.ToArray(), 0, daughterIon, numerator);
                    calculationSevice.CurrentRowElements = DataGridViewHelper.elementDataGridToDictionary(elementGrid);
                    calculationSevice.isotopeCombinations(allIsotopes, Enumerable.Repeat(0, allIsotopes.Length).ToArray(), 0, parentIonTotal, denominator);
                    calculationSevice.CurrentRowElements = null;
                    row.Cells["CalculatedAbundance"].Value = abundanceTotals[parentIonTotal] * (alpha * calculationSevice.calculateIsotopeDividend(numerator, allIsotopes)) / calculationSevice.calculateIsotopeDividend(denominator, allIsotopes);

                    //Julian B - 6/28/2015

                    //set the denominator, should be the same for all.
                    if (!data.isSigmaTSet())
                    {
                        data.setSigmaT(calculationSevice.calculateIsotopeDividend(denominator, allIsotopes));
                    }

                    //Fill numerator list with zeroes, maybe not best idea but it works for now
                    data.setAbundanceTotal(abundanceTotals[parentIonTotal]);
                    data.addNumerator(1);
                    data.addMu(0);
                    data.addAlpha(alpha);
                    data.addExperimental(Convert.ToDouble(row.Cells["Abundance"].Value));
                    data.addCalculated(Convert.ToDouble(row.Cells["CalculatedAbundance"].Value));


                }
            }
            Optimization.PerformOptimization(data);
            //}

            baseTableService.resetBaseTablePosition();
        }


        private void elementGrid_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {

        }

        private void dataGridView2_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {

        }


        private void baseMassCalcTable_CellValidating(object sender, DataGridViewCellValidatingEventArgs e)
        {
            DataGridViewTextBoxCell cell = baseMassCalcTable[e.ColumnIndex, e.RowIndex] as DataGridViewTextBoxCell;

            if (cell != null)
            {
                char[] chars = e.FormattedValue.ToString().ToCharArray();
                foreach (char c in chars)
                {
                    if (char.IsDigit(c) == false && c != '.')
                    {
                        MessageBox.Show("Please make sure that the entered data is numeric");

                        e.Cancel = true;
                        break;
                    }
                }
            }
        }

        private void baseMassCalcTable_CellEndEdit(object sender, DataGridViewCellEventArgs e)
        {
            // Clear the row error in case the user presses ESC.   
            baseMassCalcTable.Rows[e.RowIndex].ErrorText = String.Empty;
        }

        private void dataGridView2_CellValidating(object sender, DataGridViewCellValidatingEventArgs e)
        {
            DataGridViewTextBoxCell cell = dataGridView2[e.ColumnIndex, e.RowIndex] as DataGridViewTextBoxCell;

            if (cell != null)
            {
                char[] chars = e.FormattedValue.ToString().ToCharArray();
                foreach (char c in chars)
                {
                    if (char.IsDigit(c) == false && c != '.')
                    {
                        MessageBox.Show("Please make sure that the entered data is numeric");

                        e.Cancel = true;
                        break;
                    }
                }
            }
        }

        private void dataGridView2_CellEndEdit(object sender, DataGridViewCellEventArgs e)
        {
            // Clear the row error in case the user presses ESC.   
            dataGridView2.Rows[e.RowIndex].ErrorText = String.Empty;
        }


        private void molwei_KeyPress(object sender, KeyPressEventArgs e)
        {
            if (e.KeyChar != '.' && e.KeyChar != '\b')
            {
                if (e.KeyChar < '0' || e.KeyChar > '9')
                {
                    MessageBox.Show("Please make sure that the entered data is numeric");
                    e.KeyChar = (char)0;
                }
            }
        }

        private void molwei_TextChanged(object sender, EventArgs e)
        {

        }

        private void Form1_Load(object sender, EventArgs e)
        {

        }
    }
}