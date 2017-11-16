using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace FirmsChemVS.Services
{
    class DataInitializer
    {
        private string[] Elements;
        /// <summary>
        /// This simply puts a hard-coded array of elements into the 
        /// Elements array.  This is used to load into the grid view
        /// Question, do we even need this?  What is in the DB 'cause couldn't
        /// we parse that?
        /// </summary>
        public DataInitializer()
        {
            Elements = new string[] {"C","H","Br","Cl","F","I","N","S","P","Si","O" };
        }
        /// <summary>
        /// This simply puts 0s in a new row on a data grid view
        /// Seems like this could be done easier...
        /// </summary>
        /// <param name="elementGrid"></param>
        public void InitializeElementGrid(DataGridView elementGrid)
        {
            foreach (string element in Elements)
            {
                elementGrid.Rows.Add(new object[] { element, "0" });
            }
        }  
    }
}
