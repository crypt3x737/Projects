using System;
using System.Collections.Generic;
using System.Collections;
using System.Windows.Forms;

namespace FirmsChemVS.Services
{
    /// <summary>
    /// This class is a helper class to convert data in grid views.  It contains two functions.
    /// One converts a single column into an array list.  The other converts a presumably 2 column
    /// grid into a dictionary
    /// </summary>
    class DataGridViewHelper
    {
        /// <summary>
        /// Name: DataGridViewHelper
        /// Purpose:  As far as I surmise, this function takes elements from the grid
        /// and returns them in an ArrayList.  It is unclear as of yet what these are used
        /// for later.
        /// </summary>
        /// <param name="grid">The grid to be converted</param>
        /// <param name="ColumnName">The name of the one column</param>
        /// <returns>The column in a list</returns>
        public static ArrayList ColumnToList(DataGridView grid, String ColumnName)
        {
            ArrayList returnList = new ArrayList();

            foreach (DataGridViewRow row in grid.Rows)
            {
                    if(row.Cells[ColumnName].Value != null)
                    {
                        returnList.Add(row.Cells[ColumnName].Value);
                    }
            }

            return returnList;
        }
        /// <summary>
        /// Name: elementDataGridToDictionary
        /// Purpose:  As near as discernable, this function 
        /// converts a data grid view (presumably a two-column view)
        /// into a dictionary.  
        /// </summary>
        /// <param name="elementGrid">Grid to be converted</param>
        /// <returns>returns the dictionary of the grid view</returns>
        public static Dictionary<string, int> elementDataGridToDictionary(DataGridView elementGrid)
        {
            var elementMap = new Dictionary<string, int>();
            foreach (DataGridViewRow row in elementGrid.Rows)
            {
                int numberOfAtoms = Convert.ToInt32(row.Cells["numberOfAtoms"].Value);

                if (numberOfAtoms != 0)
                {
                    elementMap.Add(row.Cells[0].Value as String, Convert.ToInt32(row.Cells[1].Value));
                }
            }
            return elementMap;
        }
    }
}
