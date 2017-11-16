using FirmsChemVS.Repositories;
/// <summary>
/// 
/// </summary>
namespace FirmsChemVS.Services
{
    class DataWriter
    {
        FirmsChemDBEntities database; //actual instance of the database
        private string[] Elements;
        /// <summary>
        /// This is the constructor function for this class.  
        /// It creates an array of strings of element symbols.  
        /// </summary>
        public DataWriter()
        {
            Elements = new string[] {"C","H","N","O","F","Cl","Br","I","S","P","Si" };
            database = new FirmsChemDBEntities();
        }
    }
}
