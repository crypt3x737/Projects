using FirmsChemVS.Services;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Windows;
using System.Threading.Tasks;
using System.Threading;
using System.Windows.Controls;
using System.Windows.Controls.DataVisualization.Charting;
//using System.Windows.Controls.DataVisualization;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using System.IO;
using Excel = Microsoft.Office.Interop.Excel;
using Microsoft.Win32;
using System.Runtime.InteropServices;

namespace FirmsChemVS
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window, INotifyPropertyChanged
    {
        public event PropertyChangedEventHandler PropertyChanged;
        FirmsChemFacade facade = FirmsChemFacade.Instance;
        IsotopeService isotopeService = IsotopeService.Instance;
        Calculations calculationSevice = Calculations.Instance;
        BaseTableMassService baseTableService = BaseTableMassService.Instance;

        private ObservableCollection<ElementGridBinder> _Elements = new ObservableCollection<ElementGridBinder>();
        private ObservableCollection<IsotopeGridBinder> _Isotopes = new ObservableCollection<IsotopeGridBinder>();
        private ObservableCollection<IonMassGridBinder> _IonMasses = new ObservableCollection<IonMassGridBinder>();

        string IsCompoundName; // compound name

        public int IonMassCount
        {
            get { return _IonMasses.Count; }
        }

        public MainWindow()
        {
            this.DataContext = this;
            InitializeComponent();

            InitializeElementGrid();
            InitializeIonMassGrid();
            InitializeIsotopeGrid();
        }

        private void InitializeIonMassGrid()
        {
            dgIonMassGrid.ItemsSource = _IonMasses;
        }

        private void InitializeIsotopeGrid()
        {
            dgIsotopeGrid.ItemsSource = _Isotopes;
        }

        #region Datagrid Pasting Code

        private void OnCanExecutePaste(object sender, CanExecuteRoutedEventArgs e)
        {
            e.CanExecute = true;
            e.Handled = true;
        }

        private void OnExecutedPaste(object sender, ExecutedRoutedEventArgs args)
        {
            try
            {
                int counter = 0;
                bool isOdd = true;
                List<string> rowData = Clipboard.GetText().Split('\r', '\n', '\t').ToList();
                List<IDataGridBinder> dataBinder = new List<IDataGridBinder>();

                if ((DataGrid)sender == dgIonMassGrid)
                {
                    dataBinder = _IonMasses.ToList<IDataGridBinder>();
                }
                else if ((DataGrid)sender == dgIsotopeGrid)
                {
                    dataBinder = _Isotopes.ToList<IDataGridBinder>();
                }

                rowData.RemoveAll(e => e == "");

                foreach (string data in rowData)
                {
                    if (isOdd)
                    {
                        dataBinder[counter].DaughterIonMass = Convert.ToDouble(data);
                    }
                    else
                    {
                        dataBinder[counter].Abundance = Convert.ToDouble(data);
                        counter++;
                    }

                    isOdd = !isOdd;
                }
            }
            catch
            {
                MessageBox.Show("Incorrect data pasted into datagrid", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
            }
        }

        #endregion

        #region Button Click Events


        private void btnImportExcel_Click(object sender, RoutedEventArgs e)
        {
            OpenFileDialog dlg = new OpenFileDialog();
            dlg.Filter = "Excel documents (.xlsx)|*.xlsx|All files (*.*)|*.*";

            // Show open file dialog box
            Nullable<bool> result = dlg.ShowDialog();


            // Process open file dialog box results
            if (result == true)
            {

                // Open document
                string filename = dlg.FileName;

                Excel.Application xlApp;
                Excel.Workbook xlWorkBook;
                Excel.Worksheet xlWorkSheet;
                Excel.Range range;

                int rCnt = 0;
                int cCnt = 0;

                xlApp = new Excel.Application();
                xlWorkBook = xlApp.Workbooks.Open(filename, 0, true, 5, "", "", true, Microsoft.Office.Interop.Excel.XlPlatform.xlWindows, "\t", false, false, 0, true, 1, 0);
                xlWorkSheet = (Excel.Worksheet)xlWorkBook.Worksheets.get_Item(1);
                range = xlWorkSheet.UsedRange;

                int i = 1, j = 1;
                        string ins = (string)((range.Cells[i, j] as Excel.Range).Text);
                        do
                        {
                            txbMolecularFormula.Text = ins;
                            j++;
                    if (j == range.Columns.Count)
                              {
                                 j = 1;
                        i++;
                              } 
                } while (xlWorkSheet.Cells[i, j].Value2 != null);

                SetupElementGrid();

                _IonMasses.RemoveAt(0);

                Microsoft.Office.Interop.Excel.Range oRng = GetSpecifiedRange("Base Ion Mass", xlWorkSheet);
                string str2 = (string)((range.Cells[oRng.Row, oRng.Column + 1] as Excel.Range).Text);

                oRng = GetSpecifiedRange("Daughter ion fragments", xlWorkSheet);
                rCnt = oRng.Row;
                cCnt = oRng.Column;
                int column = cCnt;
                            int temp = rCnt + 1;
                            while ((range.Cells[temp, cCnt].Value2 != null))
                            {
                                if (temp - rCnt != 1)
                                {
                        string str = (string)((range.Cells[temp, cCnt] as Excel.Range).Text);
                        string str1 = (string)((range.Cells[temp, cCnt + 1] as Excel.Range).Text);
                        string alpha = (string)((range.Cells[temp, cCnt + 2] as Excel.Range).Text);
                                    IonMassGridBinder dataBinder = new IonMassGridBinder();
                                    dataBinder.DaughterIonMass = Convert.ToDouble(str);
                                    dataBinder.Abundance = Convert.ToDouble(str1);
                                    dataBinder.Alpha = Convert.ToDouble(alpha);
                                    _IonMasses.Add(dataBinder);
                                    temp++;
                                }
                                else
                                {
                        string str = (string)((range.Cells[temp, cCnt] as Excel.Range).Text);
                        string str1 = (string)((range.Cells[temp, cCnt + 1] as Excel.Range).Text);
                        string alpha = (string)((range.Cells[temp, cCnt + 2] as Excel.Range).Text);
                                    IonMassGridBinder dataBinder = new IonMassGridBinder();
                                    dataBinder.DaughterIonMass = Convert.ToDouble(str);
                                    dataBinder.Abundance = Convert.ToDouble(str1);
                                    dataBinder.BaseIonMass = Convert.ToInt32(str2);
                                    dataBinder.Alpha = Convert.ToDouble(alpha);
                                    _IonMasses.Add(dataBinder);
                                    temp++;
                                }
                            }


                oRng = GetSpecifiedRange("Parent ion mass", xlWorkSheet);
                rCnt = oRng.Row;
                cCnt = oRng.Column;
                int temp1 = rCnt + 2;


                while ((range.Cells[temp1, column].Value2 != null))
                {
                    Dictionary<string, int> elementCount = new Dictionary<string, int>();
                    string mass = (string)((range.Cells[rCnt, cCnt + 1] as Excel.Range).Text);
                    string formula = (string)((range.Cells[temp1, column - 1] as Excel.Range).Text);
                    string daughter = (string)((range.Cells[temp1, column] as Excel.Range).Text);
                    string abundance = (string)((range.Cells[temp1, column + 1] as Excel.Range).Text);
                    IsotopeGridBinder dataBinder1 = new IsotopeGridBinder();
                    dataBinder1.ParentIonMass = Convert.ToInt32(mass);
                    dataBinder1.IsotopeFormula = formula;
                    dataBinder1.DaughterIonMass = Convert.ToDouble(daughter);
                    dataBinder1.Abundance = Convert.ToDouble(abundance);
                    FormulaFromString(formula, out elementCount);
                    dataBinder1.ElementCount = elementCount;
                    _Isotopes.Add(dataBinder1);
                    temp1++;

                }



                    xlWorkBook.Close(true, null, null);
                    xlApp.Quit();

                    releaseObject(xlWorkSheet);
                    releaseObject(xlWorkBook);
                    releaseObject(xlApp);
                }

            }

        private Microsoft.Office.Interop.Excel.Range GetSpecifiedRange(string matchStr, Microsoft.Office.Interop.Excel.Worksheet objWs)
        {
            object missing = System.Reflection.Missing.Value;
            Microsoft.Office.Interop.Excel.Range currentFind = null;
            currentFind = objWs.get_Range("A1", "AM100").Find(matchStr, missing,
                           Microsoft.Office.Interop.Excel.XlFindLookIn.xlValues,
                           Microsoft.Office.Interop.Excel.XlLookAt.xlPart,
                           Microsoft.Office.Interop.Excel.XlSearchOrder.xlByRows,
                           Microsoft.Office.Interop.Excel.XlSearchDirection.xlNext, false, missing, missing);
            return currentFind;
        }
        private void releaseObject(object obj)
        {
            try
            {
                System.Runtime.InteropServices.Marshal.ReleaseComObject(obj);
                obj = null;
            }
            catch (Exception ex)
            {
                obj = null;
                MessageBox.Show("Unable to release the Object " + ex.ToString());
            }
            finally
            {
                GC.Collect();
            }
        }

        private void btnExportExcel_Click(object sender, RoutedEventArgs e)
        {
            SaveFileDialog saveFileDialog = new SaveFileDialog();
            string fullFileName;

            saveFileDialog.InitialDirectory = "c:\\";
            saveFileDialog.Filter = "Excel Workbook (*.xlsx)|*.xlsx";
            saveFileDialog.FilterIndex = 1;
            bool? didSave = saveFileDialog.ShowDialog();

            fullFileName = saveFileDialog.FileName;

            if (!didSave.HasValue || !didSave.Value)
            {
                return;
            }

            if (fullFileName != String.Empty || fullFileName != null)
                ExcelManager.ExportExcel(fullFileName, _Elements, _Isotopes, _IonMasses);
        }

        private void btnProcessInformation_Click(object sender, RoutedEventArgs e)
        {

            IsCompoundName = txbCompoundName.Text;

            // TryParse converts string to integer to represent the type of what you are looking for
            //molwei.Text = facade.CalculateMolecularWeight(dgElementGrid).ToString();
            //molfur.Text = facade.CalculateMolecularFormula(dgElementGrid);
            //facade.CalculateIsotopeCombinations(elementGrid);

            facade.abundanceForRow(_IonMasses);

            LayoutCompoundTable();
        }

        private void txbMolecularFormula_KeyDown(object sender, KeyEventArgs e)
        {
            if (e.Key == Key.Enter)
            {
                SetupElementGrid();
            }
        }

        private void SetupElementGrid()
        {
                foreach (ElementGridBinder element in _Elements)
                {
                    element.Count = 0;
                }

                _IonMasses.Clear();

                CompoundParser();
            }

        private void btnCalculateAbundance_Click(object sender, RoutedEventArgs e)
        {
            if (_Isotopes.Count == 0)
            {
                MessageBox.Show("Please fill out previous fields before clicking [Calculate Abundance]", "Error", MessageBoxButton.OK, MessageBoxImage.Information);
                return;
            }

            try
            {
                baseTableService.TableCount = _Isotopes.Count;

                ///How many times it generates optimization
                Console.WriteLine("Solution Error\tData:");
                Abundance data = new Abundance();

                int parentIonTotal = 0;
                double alpha;
                int positionOfBaseMassCalcTable;
                Dictionary<string, int> elemGrid = DataGridViewHelper.elementDataGridToDictionary(_Elements.ToList());
                isotopeService.getAllAbundancesInIsotopeSymbols(elemGrid, data);
                List<int> allIsotopes = isotopeService.getAllIsotopesInIsotopeSymbols(DataGridViewHelper.elementDataGridToDictionary(_Elements.ToList()).Keys.ToArray());
                var abundanceTotals = SumUpAbundances();
                foreach (IsotopeGridBinder isotope in _Isotopes)
                {
                    positionOfBaseMassCalcTable = baseTableService.incrementBaseTableMassPosition();

                    if (positionOfBaseMassCalcTable == -9)
                    {
                        MessageBox.Show("Seems like there are no available alphas, please enter abundances in the above table");
                        return;
                    }

                    alpha = _IonMasses[positionOfBaseMassCalcTable].Alpha;

                    parentIonTotal = _Isotopes[0].ParentIonMass.Value;

                    Dictionary<string, int> rowElements = new Dictionary<string, int>();
                    //Dictionary<string, object> possibleIsotopesPerElement = new Dictionary<string, object>();
                    Dictionary<string, int> allIsotopesElement = new Dictionary<string, int>();
                    Dictionary<string, int[]> possibleIsotopesPerElementAll = new Dictionary<string, int[]>();
                    Dictionary<string, int[]> possibleIsotopesPerElement = new Dictionary<string, int[]>();

                    List<ElementGridBinder> elements = _Elements.ToList().FindAll(c => c.Count > 0).ToList();

                    foreach (ElementGridBinder element in elements)
                    {
                        allIsotopesElement[element.Symbol] = element.Count;
                        int[] listofIsotopeInts = isotopeService.getAllIsotopesForAtom(element.Symbol).Select(x => x.isotopeNumber ?? 0).OrderByDescending(value => value).ToArray();
                        possibleIsotopesPerElementAll.Add(element.Symbol, listofIsotopeInts);
                    }

                    //for (int l = 0; l < totalElements; l++)
                    //{
                    //    //System.Console.WriteLine("Start");
                    //    //System.Console.WriteLine(k);
                    //    //System.Console.WriteLine(elmentName[k]);
                    //    allIsotopesElement[elements[l].Symbol] = elements[l].Count;
                    //    int[] listofIsotopeInts = isotopeService.getAllIsotopesForAtom(elements[l].Symbol).Select(x => x.isotopeNumber ?? 0).OrderByDescending(value => value).ToArray();
                    //    possibleIsotopesPerElementAll.Add(elements[l].Symbol, listofIsotopeInts);
                    //}

                    List<int> temp = new List<int>();

                    int counter = 0;

                    foreach (KeyValuePair<string, int> element in isotope.ElementCount)
                    {
                        for (int index = 0; index < element.Value; index++)
                        {
                            temp.Add(counter);
                            counter++;
                        }

                        rowElements[element.Key] = element.Value;
                    }

                    data.addChems(temp);

                    foreach (string item in rowElements.Keys)
                    {
                        int[] listofIsotopeInts = isotopeService.getAllIsotopesForAtom(item).Select(x => x.isotopeNumber ?? 0).OrderByDescending(value => value).ToArray();

                        possibleIsotopesPerElement.Add(item, listofIsotopeInts);
                    }

                    int[] isotopeList = new int[] { };
                    string[] elementSymbols = elements.Select(s => s.Symbol).ToArray();

                    foreach (int[] item in possibleIsotopesPerElement.Values)
                    {
                        isotopeList = isotopeList.Concat(item).ToArray();
                    }


                    int[] counts = Enumerable.Repeat(0, isotopeList.Length).ToArray();
                    int daughterIon = (int)isotope.DaughterIonMass;
                    List<Dictionary<int, int>> numerator = new List<Dictionary<int, int>>();
                    List<Dictionary<int, int>> denominator = new List<Dictionary<int, int>>();
                    calculationSevice.CurrentRowElements = rowElements;
                    //calculationSevice.isotopeCombinations(isotopeList.OrderByDescending(ic => ic).ToList(), counts.ToArray(), 0, daughterIon, numerator);
                    calculationSevice.isotopeCombinations(rowElements.Count, elementSymbols, 0, rowElements, possibleIsotopesPerElement, counts.ToArray(), 0, daughterIon, numerator, "", 0, 0);

                    calculationSevice.CurrentRowElements = DataGridViewHelper.elementDataGridToDictionary(_Elements.ToList());
                    //calculationSevice.isotopeCombinations(allIsotopes, Enumerable.Repeat(0, allIsotopes.Count).ToArray(), 0, parentIonTotal, denominator);
                    calculationSevice.isotopeCombinations(allIsotopesElement.Count, elementSymbols, 0, allIsotopesElement, possibleIsotopesPerElementAll, Enumerable.Repeat(0, allIsotopes.Count).ToArray(), 0, parentIonTotal, denominator, "", 0, 0);

                    calculationSevice.CurrentRowElements = null;
                    isotope.CalculatedAbundance = abundanceTotals[parentIonTotal] * (alpha * calculationSevice.calculateIsotopeDividend(numerator, allIsotopes.ToArray())) / calculationSevice.calculateIsotopeDividend(denominator, allIsotopes.ToArray());

                    //Julian B - 6/28/2015

                    //set the denominator, should be the same for all.
                    if (!data.isSigmaTSet())
                    {
                        data.setSigmaT(calculationSevice.calculateIsotopeDividend(denominator, allIsotopes.ToArray()));
                    }

                    //Fill numerator list with zeroes, maybe not best idea but it works for now
                    data.setAbundanceTotal(abundanceTotals[parentIonTotal]);
                    data.addNumerator(1);
                    data.addMu(0);
                    data.addAlpha(alpha);
                    data.addExperimental(isotope.Abundance); //Convert.ToDouble(isotope.Cells["Abundance"].Value));
                    data.addCalculated(isotope.CalculatedAbundance); //Convert.ToDouble(isotope.Cells["CalculatedAbundance"].Value));
                }

                Optimization.PerformOptimization(data);

                baseTableService.resetBaseTablePosition();
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.ToString());
            }
        }

        private void btnSpectrum_Click(object sender, RoutedEventArgs e)
        {
            Dictionary<string, double> dataListExp = new Dictionary<string, double>();
            Dictionary<string, double> dataListCalc = new Dictionary<string, double>();

            foreach (IsotopeGridBinder isotope in _Isotopes)
            {
                dataListExp.Add(isotope.IsotopeFormula, isotope.Abundance);
                dataListCalc.Add(isotope.IsotopeFormula, isotope.CalculatedAbundance);
            }

            ((ColumnSeries)mcChart.Series[0]).ItemsSource = dataListExp;

            ((ColumnSeries)mcChart.Series[1]).ItemsSource = dataListCalc;

        }

        private void Row_DoubleClick(object sender, MouseButtonEventArgs e)
        {
            int rowSelected;
            string oldVal = string.Empty;
            IsotopeGridBinder isotope;
            rowSelected = dgIsotopeGrid.SelectedIndex;
            isotope = _Isotopes[rowSelected];

            if (isotope.ElementCount.Count != 0)
            {
                oldVal = isotope.IsotopeFormula;
            }

            IsotopeChooser isotopeChooser = new IsotopeChooser(_Elements.ToList(), oldVal);
            isotopeChooser.Owner = this;
            
            isotopeChooser.ShowDialog();

            isotope.IsotopeFormula = isotopeChooser.IsotopeFormula;
            isotope.ElementCount = isotopeChooser.ElementCounter;
        }

        private void btnLoadBaseIon_Click(object sender, RoutedEventArgs e)
        {
            int rows = 0;
            string isotopeFormula = string.Empty;
            Dictionary<string, int> elementList = new Dictionary<string, int>();

            foreach (ElementGridBinder binder in _Elements)
            {
                if (binder.Count > 0)
                {
                    elementList.Add(binder.Symbol, binder.Count);
                    rows += binder.Count;
                }
            }

            foreach (string elmt in elementList.Keys)
            {
                if (elementList[elmt] == 1)
                {
                    isotopeFormula += elmt;
                }
                else
                {
                    isotopeFormula += elmt + elementList[elmt];
                }
            }

            txbMolecularFormula.Text = isotopeFormula;

            SetupIonMassGridBinder(elementList);
        }

        private void btnAddRow_Click(object sender, RoutedEventArgs e)
        {
            IonMassGridBinder newBinder = new IonMassGridBinder();

            newBinder.BaseIonMass = null;
            newBinder.DaughterIonMass = 0;
            newBinder.Abundance = 0;
            newBinder.Alpha = 0;

            _IonMasses.Add(newBinder);

            NotifyPropertyChanged("IonMassCount");
        }

        private void btnDeleteRow_Click(object sender, RoutedEventArgs e)
        {
            _IonMasses.Remove(_IonMasses.Last());

            NotifyPropertyChanged("IonMassCount");
        }

        #endregion

        private void LayoutCompoundTable() // Creates the data for the isotopes and fragments 
        {
            IsotopeGridBinder binder;
            string isotopeFormula = string.Empty;
            int numOfIsotopes = _IonMasses.Count;
            _Isotopes.Clear();

            for (int i = 0; i < numOfIsotopes; i++)
            {
                binder = new IsotopeGridBinder();

                binder.IsotopeFormula = "Set Isotope";

                if (i != 0)
                {
                    binder.ParentIonMass = null;
                }

                _Isotopes.Add(binder);
            }
        }

        private void InitializeElementGrid()
        {
            List<string> elementSymbols = new List<string>() { "C", "H", "Br", "Cl", "F", "I", "N", "S", "P", "Si", "O" };

            foreach (string element in elementSymbols)
            {
                _Elements.Add(new ElementGridBinder() { Symbol = element, Count = 0 });
            }

            dgElementGrid.ItemsSource = _Elements;
        }

        private List<double> ProcessCurrentColumn(DataGridColumn column)
        {
            List<double> numberList = new List<double>();

            foreach (IsotopeGridBinder isotope in _Isotopes)
            {
                if (isotope.Abundance > 0)
                {
                    numberList.Add(isotope.Abundance);
                }
            }

            return numberList;
        }

        private Dictionary<int, double> SumUpAbundances()
        {
            var result = new Dictionary<int, double>();
            int lastIndex = 0;

            List<double> abundances = _Isotopes.Select(e => e.Abundance).ToList();
            int baseParentIon = _Isotopes[0].ParentIonMass.Value;

            List<double> subsetOfAbundance = abundances.GetRange(lastIndex, baseTableService.TableCount);
            result.Add(baseParentIon, subsetOfAbundance.Sum());
            lastIndex += baseTableService.TableCount;
            
            return result;
        }

        private void CompoundParser()
        {
            Dictionary<string, int> elementList;
            bool validFormula;
            string elementsNotFound = string.Empty;

            try
            {
                validFormula = FormulaFromString(txbMolecularFormula.Text, out elementList);

                if (validFormula == true && elementList.Count != 0)
                {
                    foreach (string element in elementList.Keys)
                    {
                        if (_Elements.ToList().Exists(e => e.Symbol == element))
                        {
                            _Elements.ToList().Find(e => e.Symbol == element).Count = elementList[element];
                        }
                        else
                        {
                            elementsNotFound += " " + element;
                        }
                    }

                    if (string.IsNullOrEmpty(elementsNotFound))
                    {
                        SetupIonMassGridBinder(elementList);
                    }
                    else
                    {
                        MessageBox.Show("Element(s) were not found in DB: " + elementsNotFound, "Not found", MessageBoxButton.OK, MessageBoxImage.Error);
                    }
                }
            }
            catch
            {
                MessageBox.Show("Element not found", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
            }
        }

        private bool FormulaFromString(string chemicalFormula, out Dictionary<string, int> formula)
        {
            formula = new Dictionary<string, int>();
            string elementRegex = "([A-Z][a-z]*)([0-9]*)";
            string validateRegex = "^(" + elementRegex + ")+$";

            if (!Regex.IsMatch(chemicalFormula, validateRegex))
            {
                MessageBox.Show("Invalid molecular formula", "Error", MessageBoxButton.OK, MessageBoxImage.Error);

                return false;
            }

            foreach (Match match in Regex.Matches(chemicalFormula, elementRegex))
            {
                string name = match.Groups[1].Value;

                int count =
                    match.Groups[2].Value != "" ?
                    int.Parse(match.Groups[2].Value) :
                    1;

                formula.Add(name, count);
            }

            return true;
        }

        private void SetupIonMassGridBinder(Dictionary<string, int> elementList)
        {
            IonMassGridBinder newBinder = new IonMassGridBinder();

            newBinder.BaseIonMass = isotopeService.CalculateIonMass(elementList);
            newBinder.DaughterIonMass = 0;
            newBinder.Abundance = 0;
            newBinder.Alpha = 0;

            _IonMasses.Add(newBinder);

            txbMolecularWeight.Text = isotopeService.CalculateMolecularWeight(_Elements.ToList()).ToString();
        }

        private void NotifyPropertyChanged(string propertyName = "")
        {
            if (PropertyChanged != null)
            {
                PropertyChanged(this, new PropertyChangedEventArgs(propertyName));
            }
        }
        private void btnExptGraph_Click(object sender, RoutedEventArgs e)
        {
            SaveFileDialog savespot = new SaveFileDialog();
            savespot.DefaultExt = ".png";

            savespot.Filter = "PNG|*.png";

            bool? didSave = savespot.ShowDialog();

            string filename = savespot.FileName;

            if (!didSave.HasValue || !didSave.Value)
            {
                return;
            }

            Size size = new Size(mcChart.ActualWidth, mcChart.ActualHeight);
            if (size.IsEmpty)
                return;

            size.Height *= 2;
            size.Width *= 2;

            RenderTargetBitmap result = new RenderTargetBitmap((int)size.Width, (int)size.Height, 96, 96, PixelFormats.Pbgra32);

            DrawingVisual drawingvisual = new DrawingVisual();
            using (DrawingContext context = drawingvisual.RenderOpen())
            {
                context.DrawRectangle(new VisualBrush(mcChart), null, new Rect(new Point(), size));
                context.Close();
            }

            result.Render(drawingvisual);

            FileStream fout = new FileStream(filename, FileMode.Create);

            PngBitmapEncoder encoder = new PngBitmapEncoder();
            encoder.Frames.Add(BitmapFrame.Create(result));

            encoder.Save(fout);

            fout.Close();
        }

        private void txbMolecularFormula_TextChanged(object sender, TextChangedEventArgs e)
        {

        }

        private void dgIsotopeGrid_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {

        }

        private void dgElementGrid_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {

        }

        private void dgIonMassGrid_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {

        }
    }
}
