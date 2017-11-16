using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace FirmsChemVS
{
    public class ViewModel
    {
        public ViewModel()
        {

        }

        public void ExportExcel(string filePath, DataTable data)
        {
            ExcelManager excelManager = ExcelManager.Instance;

            excelManager.ExportExcel(filePath, data);
        }
    }
}
