using System;
using System.Collections.Generic;
using System.Windows.Forms;
using System.Data;
using System.IO;
using DoddleReport;
using DoddleReport.OpenXml;

namespace FirmsChemVS.Services
{
    class DataReporter
    {
        DataTable dataForGrid2;

        public DataTable DataForGrid2
        {
            get {
                 return dataForGrid2;
                }
        }

        public DataReporter(DataGridView gridview)
        {
            dataForGrid2 = convertDatagridToDatatable(gridview);
        }

        public static DataTable convertDatagridToDatatable(DataGridView dgv)
        {
            var dt = new DataTable();
            foreach (DataGridViewColumn column in dgv.Columns)
            {
                if (column.Visible)
                {
                    // You could potentially name the column based on the DGV column name (beware of dupes)
                    // or assign a type based on the data type of the data bound to this DGV column.
                    dt.Columns.Add(column.HeaderText);
                }
            }

            object[] cellValues = new object[dgv.Columns.Count];
            foreach (DataGridViewRow row in dgv.Rows)
            {
                for (int i = 0; i < row.Cells.Count; i++)
                {
                    cellValues[i] = row.Cells[i].Value;     
                }
                dt.Rows.Add(cellValues);
            }
            return dt;
        }

        // Exporting data from data grid view to excel files.
        public static bool exportDataSetToExcelFile(DataTable gridToReport)
        {
            Random generator = new Random();
            string path = System.IO.Path.Combine(Environment.GetFolderPath(Environment.SpecialFolder.MyDocuments), (String.Format("{0}.xlsx", generator.Next(500, 1000))));

            if (gridToReport != null)
            {
                FileStream reportOutput = new FileStream(path, FileMode.Create);
                var report = new Report(gridToReport.ToReportSource());
                report.RenderHints.BooleansAsYesNo = true;
                report.RenderHints.Orientation = ReportOrientation.Landscape;
                var writer = new ExcelReportWriter();
                writer.WriteReport(report, reportOutput);
                reportOutput.Close();
                return true;
            } 

            else
            {
                return false;
            }
        }
    }
}
