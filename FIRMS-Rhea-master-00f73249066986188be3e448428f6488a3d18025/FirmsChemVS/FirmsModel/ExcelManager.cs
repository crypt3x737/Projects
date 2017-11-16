using System;
using System.Collections.Generic;
using System.Data;
using System.Data.OleDb;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace FirmsChemVS
{
    public class ExcelManager
    {
        private static ExcelManager s_instance = null;

        public ExcelManager()
        {

        }

        public static ExcelManager Instance
        {
            get
            {
                if (s_instance == null)
                {
                    s_instance = new ExcelManager();
                }

                return s_instance;
            }
        }

        public void ImportExcel(string filePath)
        {
            
        }

        public void ExportExcel(string filePath, System.Data.DataTable data)
        {
            
        }
    }
}
