
namespace FirmsChemVS.Services
{
    /// <summary>
    /// This file does things yup....
    /// 
    /// It's a singleton!
    /// </summary>
   public class BaseTableMassService
    {

       private static BaseTableMassService instance;
       int tablePosition = -1;
       int tableCount;
       /// <summary>
       /// TableCount Property
       /// Purpose: unknown
       /// </summary>
       public int TableCount{get{return tableCount;}set{tableCount = value;}}
       
        /// <summary>
        /// Instance property (I believe property is the correct term)
        /// This is the thing which tells whether or not a new 
        /// instance of something needs to be made or if the current instance needs to be returned
        /// </summary>
       public static BaseTableMassService Instance
        {
            get
            {
                if (instance == null)
                {
                    instance = new BaseTableMassService();
                }
                return instance;
            }
        }
        /// <summary>
        /// Name: incrementBaseTableMassPosition
        /// Purpose: currently unknown.  It 
        /// increments the tablePosition by one if
        /// it is less than the number of items in
        /// the table minus 1
        /// or puts it at 0 if it is equal to or greater.  
        /// </summary>
        /// <returns>value of tablePosition</returns>
       public int incrementBaseTableMassPosition()
       {
           if (tableCount == 0)
           {
               return -9;
           }

           else if (tablePosition < TableCount - 1)
           {
               tablePosition++;
           }

           else
           {
               tablePosition = 0;
           }

           return tablePosition;
       }

        /// <summary>
        /// Name: resetBaseTablePosition
        /// Purpose:  This function is a constant setter which sets the
        /// base table position to -1;
        /// </summary>
       public void resetBaseTablePosition()
       {
           tablePosition = -1;
       }
    }
}
