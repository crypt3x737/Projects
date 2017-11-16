/********************************************************************************
 *  File: expand.js
 *
 *  Author: Akshay Singh
 *
 *  Description: Allows the tables with the classes to expand and contract on a 
 *      button click.  It will keep the automatic positioning used when loading
 *      the tables.
 *
 * ******************************************************************************/

function expand(clicked_id )
{
    if(clicked_id=="buttonCSC1")
   {
      $(".classItemCSC1").toggle();
   }
   
   else if(clicked_id=="buttonCSC2")
   {
      $(".classItemCSC2").toggle();
   }
   
    else if(clicked_id=="buttonCSC3")
    {
      $(".classItemCSC3").toggle();
    }
   
    else if(clicked_id=="buttonCSC4")
    {
      $(".classItemCSC4").toggle();
    }
   
    else if(clicked_id=="buttonCSCE")
    {
      $(".classItemCSCE").toggle();
    }
   
    else if(clicked_id=="buttonSCI")
    {
     $(".classItemSCI").toggle();
    }
   
    else if(clicked_id=="buttonEngl")
    {
       $(".classItemEngl").toggle();
    }
   
    else if(clicked_id=="buttonMath")
    {
      $(".classItemMath").toggle();
    }
   
    else if(clicked_id=="buttonGENED")
    {
      $(".classItemGENED").toggle();
    }
    
    if( document.getElementById(clicked_id).value=="+")
       document.getElementById(clicked_id).value="-"; 
       else
       document.getElementById(clicked_id).value="+"; 
    positionTables();
} 

