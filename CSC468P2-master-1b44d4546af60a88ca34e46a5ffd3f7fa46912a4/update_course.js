/******************************************************************************
*	File: update_course.js
*
*	Author: Akshay
*
*	Functions included:
*			update_course(id,array,array1)
*			getKeyByValue(object, value)
*
*	Description:
*	    Called when the user uses one of the dropdown boxes to select an 
*	        elective course.  It updates the course number, credits, and 
*	        the grade for the elective course. 
******************************************************************************/
function update_course(id,array,array1 )
{

        var a=document.getElementById(id);
        var name = a.options[a.selectedIndex].text;
        var grade= ("grade"+id.substring(4));
       	document.getElementById(grade).value = -3;
        
        // enable or disable the grade dropdown
        if(name!="")
        {
            document.getElementById(grade).disabled=false;
        }
        else
        {
            document.getElementById(grade).disabled=true;
        }

        var temp=(String) (getKeyByValue(array,name));
        var course_name=temp.substring(0,temp.indexOf(" "))+"_"+temp.substring(temp.indexOf(" ")+1);
        var num="num"+id.substring(4);
        var credit="credits"+id.substring(4);
        
        // set the course number for a class        
        var s=document.getElementById(num);
        if ( name != "" )
        {
            s.innerHTML=temp.substring(temp.indexOf(" ")+1);
        }
        else
        {
            s.innerHTML = "";
        }

        // set  the number of credits
        var s=document.getElementById(credit);
        if ( name != "" )
        {
            s.innerHTML=array1[temp]; 
        }
        else
        {
            s.innerHTML = "";
        }
         
}

/*****************************************************************************
 *
 *  Author: Akshay Singh
 *
 *  Description:
 *      Retreives the key from a key-value pair from the value.
 *
 * **************************************************************************/
function getKeyByValue(object, value) 
{
  return Object.keys(object).find(key => object[key] === value);
}

