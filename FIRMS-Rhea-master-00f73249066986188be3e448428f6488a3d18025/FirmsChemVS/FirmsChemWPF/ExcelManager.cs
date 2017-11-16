using Microsoft.Office.Interop.Excel;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Reflection;

namespace FirmsChemVS
{
    public class ExcelManager
    {
        public ExcelManager()
        {

        }

        public static void ImportExcel(string filePath)
        {
        } 

        public static void ExportExcel(string fullFileName, ObservableCollection<ElementGridBinder> _Elements, ObservableCollection<IsotopeGridBinder> _Isotopes,
            ObservableCollection<IonMassGridBinder> _IonMass)
        {
            Application xlApp = new Application();
            Workbook xlWorkBook = xlApp.Workbooks.Add(XlWBATemplate.xlWBATWorksheet);
            Worksheet xlSheet = (Worksheet)xlWorkBook.Worksheets[1];
            string upperLimit;
            string compoundName = "";

            foreach(var elem in _Elements)
            {
                if ( elem.Count == 1 )
                {
                    compoundName += elem.Symbol;
                }
                else if (elem.Count > 1)
                {
                    compoundName += elem.Symbol + elem.Count;
                }
            }

            Range range = xlSheet.get_Range("A1");
            object[] printValue = new object[1];
            printValue[0] = compoundName;
            range.GetType().InvokeMember("Value", BindingFlags.SetProperty, null, range, printValue);

            range = xlSheet.get_Range("A2");
            printValue = new object[1];
            printValue[0] = "Base Ion Mass";
            range.GetType().InvokeMember("Value", BindingFlags.SetProperty, null, range, printValue);

            if (_IonMass.Count > 0)
            {
                if (_IonMass[0].BaseIonMass != null)
                {
                    range = xlSheet.get_Range("B2");
                    printValue[0] = _IonMass[0].BaseIonMass.ToString();
                    range.GetType().InvokeMember("Value", BindingFlags.SetProperty, null, range, printValue);
                }
            }

            var temp = 4;                                 

            range = xlSheet.get_Range("C3");
            printValue[0] = "Fragements";
            range.GetType().InvokeMember("Value", BindingFlags.SetProperty, null, range, printValue);

            temp = 4;
            for (int i = 0; i < _Isotopes.Count; i++)
            {
                if (_Isotopes[i].IsotopeFormula != String.Empty && _Isotopes[i].IsotopeFormula == "Set Isotope")
                {
                    upperLimit = "C" + temp;
                    range = xlSheet.get_Range(upperLimit);
                    printValue[0] = _Isotopes[i].IsotopeFormula.ToString();
                    range.GetType().InvokeMember("Value", BindingFlags.SetProperty, null, range, printValue);
                    temp++;
                }
            }

            range = xlSheet.get_Range("D3");
            printValue[0] = "Daughter Ion Fragements";
            range.GetType().InvokeMember("Value", BindingFlags.SetProperty, null, range, printValue);

            temp = 4;
            for (int i = 0; i < _IonMass.Count; i++)
            {
                if (_IonMass[i].DaughterIonMass != 0.0)
                {
                    upperLimit = "D" + temp;
                    range = xlSheet.get_Range(upperLimit);
                    printValue[0] = _IonMass[i].DaughterIonMass.ToString();
                    range.GetType().InvokeMember("Value", BindingFlags.SetProperty, null, range, printValue);
                    temp++;
                }
            }

            range = xlSheet.get_Range("E3");
            printValue[0] = "Abundances";
            range.GetType().InvokeMember("Value", BindingFlags.SetProperty, null, range, printValue);

            temp = 4;
            for (int i = 0; i < _IonMass.Count; i++)
            {
                if (_IonMass[i].Abundance != 0.0)
                {
                    upperLimit = "E" + temp;
                    range = xlSheet.get_Range(upperLimit);
                    printValue[0] = _IonMass[i].Abundance.ToString();
                    range.GetType().InvokeMember("Value", BindingFlags.SetProperty, null, range, printValue);
                    temp++;
                }
            }

            range = xlSheet.get_Range("F3");
            printValue[0] = "Alphas";
            range.GetType().InvokeMember("Value", BindingFlags.SetProperty, null, range, printValue);

            temp = 4;
            for (int i = 0; i < _IonMass.Count; i++)
            {
                if (_IonMass[i].Alpha != 0.0)
                {
                    upperLimit = "F" + temp;
                    range = xlSheet.get_Range(upperLimit);
                    printValue[0] = _IonMass[i].Alpha.ToString();
                    range.GetType().InvokeMember("Value", BindingFlags.SetProperty, null, range, printValue);
                    temp++;
                }
            }

            temp = temp + 2;
            upperLimit = "A" + temp;
            range = xlSheet.get_Range(upperLimit);
            printValue[0] = "Parent Ion Mass";
            range.GetType().InvokeMember("Value", BindingFlags.SetProperty, null, range, printValue);

            if (_Isotopes.Count > 0)
            {
                if (_Isotopes[0].ParentIonMass != null)
                {
                    upperLimit = "B" + temp;
                    range = xlSheet.get_Range(upperLimit);
                    printValue[0] = _Isotopes[0].ParentIonMass.ToString();
                    range.GetType().InvokeMember("Value", BindingFlags.SetProperty, null, range, printValue);
                }
            }

            temp++;
            var bottomStart = temp;
            for (int i = 0; i < _Isotopes.Count; i++)
            {
                if (_Isotopes[i].IsotopeFormula != String.Empty && _Isotopes[i].IsotopeFormula == "Set Isotope")
                {
                    upperLimit = "C" + temp;
                    range = xlSheet.get_Range(upperLimit);
                    printValue[0] = _Isotopes[i].IsotopeFormula.ToString();
                    range.GetType().InvokeMember("Value", BindingFlags.SetProperty, null, range, printValue);
                    temp++;
                }
            }

            temp = bottomStart;
            for (int i = 0; i < _IonMass.Count; i++)
            {
                if (_Isotopes[i].DaughterIonMass != 0.0)
                {
                    upperLimit = "D" + temp;
                    range = xlSheet.get_Range(upperLimit);
                    printValue[0] = _Isotopes[i].DaughterIonMass.ToString();
                    range.GetType().InvokeMember("Value", BindingFlags.SetProperty, null, range, printValue);
                    temp++;
                }
            }

            temp = bottomStart;
            for (int i = 0; i < _IonMass.Count; i++)
            {
                if (_Isotopes[i].Abundance != 0.0)
                {
                    upperLimit = "E" + temp;
                    range = xlSheet.get_Range(upperLimit);
                    printValue[0] = _Isotopes[i].Abundance.ToString();
                    range.GetType().InvokeMember("Value", BindingFlags.SetProperty, null, range, printValue);
                    temp++;
                }
            }

            temp = bottomStart;
            for (int i = 0; i < _IonMass.Count; i++)
            {
                if (_IonMass[i].Alpha != 0.0)
                {
                    upperLimit = "F" + temp;
                    range = xlSheet.get_Range(upperLimit);
                    printValue[0] = _IonMass[i].Alpha.ToString();
                    range.GetType().InvokeMember("Value", BindingFlags.SetProperty, null, range, printValue);
                    temp++;
                }
            }

            xlSheet.SaveAs(fullFileName, XlFileFormat.xlWorkbookDefault, null, null, false);

            xlWorkBook.Close(true);

            return;
        }
    }
}
