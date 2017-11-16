var currentGPA;
var all={};
/******************************************************************************
 *	Function: calculateGPA
 *
 *	Description: This function is used to calculate the gpa from the grades
 *		entered by the user. It also changes course color with grade
 * ***************************************************************************/
function calculateGPA(jcheck, jprereqs,all )
{
    var courses = document.getElementsByClassName( 'classTable' );
    var i, j;
    var totalCredits = 0;
    var gpa_credits=0;
    var earnedCredits = 0;
    var progressCredits = 0;
    var passedCredits = 0;
    
	//loop through the courses to get credits
    for ( i = 0; i < courses.length; i++ )
    {
        var c = courses[i];
        
	for ( j = 2; j < c.rows.length; j++ )
        { 
	    //collect grade entered and credits earned for each class
            var grade = c.rows[j].cells[3].firstChild.value - 0;
            var credits = c.rows[j].cells[2].innerHTML - 0;
            
            var course_name;
            var temp;
            
	    course_name =  c.rows[j].cells[1].innerHTML
            
	    if ( course_name.search( "<select" ) != -1 )
            {
        	course_name = c.rows[j].cells[1].firstChild.value;
            }
        		
	    temp = getKeyByValue( all, course_name );
        	//if they have a grade entered	
            if( grade >= -1)
            {
                totalCredits += credits;
                
		if(grade >= 0)
                {
                    earnedCredits += grade * credits;
                    gpa_credits+=credits; 
                }
                
                if ( grade > 0 || grade==-1)
                {
                	passedCredits += credits;
                }
            }
            // check to see if the class has been taken
            else if ( grade == -2 )
            {
                progressCredits += credits;
            }

            // apply color to rows
            var color = "white";
            if ( grade > 0 || grade == -1 ) // greater than f or exempt
            {
                color = "lightgreen";
            }
            if ( grade == 0 )   // grade f
            {
                color = "orange";
            }
            if ( grade == 1 )
            {
            	color = "yellow"; // grade d
            }
            if ( grade == -2 ) // taking
            {
                color = "#ABCDEF";
            }
     	 	
            if (temp != undefined && !prereq_check( temp, jcheck, jprereqs, all ) && grade != -1 )
            {
            	color = "#A0A0A0";
            }
            
            c.rows[j].style.backgroundColor = color;
            
        }
    }

    var gpa = (earnedCredits - 0) / (gpa_credits - 0);
    
    if ( isNaN(gpa) )
    {
    	document.getElementById("gpa").value = "";
    }
    else
    {
    	document.getElementById("gpa").value = (earnedCredits - 0) / (gpa_credits - 0);
    }
    //set the top bar information
    document.getElementById("completeCredits").value = passedCredits;
    document.getElementById("pbar").style.width = (passedCredits / 120.0) * 100 + "%";
    document.getElementById("inProgress").value = progressCredits;
}
/******************************************************************************
 *	Function: updateGPA
 *
 *	Description: This function handles the pre req checking when the user
 *		selects the course
 * ***************************************************************************/
function updateGPA( id, array, jcheck, jprereqs, jcoreqs,all )
{
   var enter=true;

   if(id.substring(0,5)=="grade")
   {   
        var g = document.getElementById(id);
        var n="name"+id.substring(5);
        var a=document.getElementById(n);
        var name = a.options[a.selectedIndex].text;
        var temp=(getKeyByValue(array,name));
        var course_name=temp.substring(0,temp.indexOf(" "))+"_"+temp.substring(temp.indexOf(" ")+1);
        var http = new XMLHttpRequest(); 
       
        http.open("POST", "updateXML.php", true );
        http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
       
        if ( this.readyState == 4 && this.status == 200 )
        {
            //console.log(this.responseText);
        }

        console.log( "ARRAY: " + array );

        var uname = document.getElementById('uname').value;
        var grade = document.getElementById(id).value;
        var res = course_name.split("_");
        var prefix = res[0];
        var number = res[1];
       
        if(g.options[g.selectedIndex].value!=-3 && g.options[g.selectedIndex].value!=-1)
        {
            if(jprereqs[temp]!=null  && jprereqs[temp].length!=0)
            {
                 //loop through and check prereqs
		 for(var i=0;i<jprereqs[temp].length;i++)
            	 {
                     ans= jprereqs[temp][i];
                     res=ans.split(",");
                    
                     for(var j=0;j<res.length;j++)
                     {
                 	if(res[j]!="")
                        {
                     
		   	     enter=is_taken(res[j],jcheck,array);
                     
		    	     if(enter==false)
                             {
                                 window.alert("Please make sure you have completed\nthe following prerequisites  - "+jprereqs[temp][i]);
                                 g.value=-3;
                                 return;
                             }
                        }
                     }
 
                     if (enter==true)
                         break;
                 }
            }
             
            if(jcoreqs[temp]!=null && jcoreqs[temp].length!=0)
            {
           	for(var i=0;i<jcoreqs[temp].length;i++)
                {
                    ans= jcoreqs[temp][i];
                    res=ans.split(",");
                 
		    for(var j=0;j<res.length;j++)
                    {
                        if(res[j]!="")
                        {
                            enter=is_taking(res[j],jcheck,array);
                            
			    if(enter==false)
                            {
                                window.alert("Please make sure you have completed\nthe following corequisites  - "+jcoreqs[temp][i]);
                         	g.value=-3;
                        	return;
                            }
                        }
                    }
                 }
             }
             
         }
         if(enter==true)
         {
	        var str = "uname=" + uname + "&grade=" + grade + "&prefix="+ 
				prefix + "&number=" + number ;

            http.send("uname=" + uname + "&grade=" + grade + "&prefix="+ prefix + "&number=" + number );
            calculateGPA(  jcheck, jprereqs,all );
         }
   }
   else
   {
        var grade = document.getElementById(id);
        if(grade.options[grade.selectedIndex].value==-3 || 
				grade.options[grade.selectedIndex].value==-1)
        {
             updateXML( id );
                  calculateGPA(  jcheck, jprereqs,all );
                  return;
        }
        else
        {
            var index=id.substring(0,id.indexOf("_"))+" "+id.substring(id.indexOf("_")+1);
            
	    if(jprereqs[index].length!=0 && jprereqs[index]!=null)
            {
                for(var i=0;i<jprereqs[index].length;i++)
                {
                    ans= jprereqs[index][i];
                    var res=ans.split(",");
                 
                    for(var j=0;j<res.length;j++)
                    {
                        if(res[j]!="")
                        {
                            enter=is_taken(res[j],jcheck,array);
                         
			    if(enter==false)
                            {
                                window.alert("Please make sure you have completed\nthe following prerequisites  - "+jprereqs[index][i]);
                                grade.value=-3;
                                return;
                            }
                        }
                    }
                  
		    if(enter==true)
                        break;
                }
           }
           if(jcoreqs[index].length!=0 && jcoreqs[index]!=null)
           {
               for(var i=0;i<jcoreqs[index].length;i++)
               {
                   ans= jcoreqs[index][i];
                   res=ans.split(",");
                 
		   for(var j=0;j<res.length;j++)
                   {
                       if(res[j]!="")
                       {
                           enter=is_taking(res[j],jcheck,array);
                       
			   if(enter==false)
                           {
                               window.alert("Please make sure you have completed\nthe following corequisites  - "+jcoreqs[index][i]);
                               grade.value=-3;
                               return;
                           }
                       }
                   }
               }     
            }
            
	    if(enter==true)
            {
                updateXML( id );
                calculateGPA(  jcheck, jprereqs,all );
                return;
            }
        }
    }
}

/******************************************************************************
 *	Function: prereq_check
 *
 *	Description: This function does the actual prereq checking based on the 
 *		generated arrays keeping track of the pre and co req
 * ***************************************************************************/
function prereq_check(course_name,jcheck,jprereqs,array)
{
    var temp=course_name.substring(0,course_name.indexOf(" "))+"_"+
			course_name.substring(course_name.indexOf(" ")+1);
    var elem=document.getElementById(temp);
    var enter=true;

    if(elem==null)
    {
        for(var i=0;i<jcheck.length;i++)
        {
            var name=document.getElementById(jcheck[i]);
            var t=name.options[name.selectedIndex].text;
            
	    if(getKeyByValue(array,t)==course_name)
            {
                if(jprereqs[course_name]!=null  && jprereqs[course_name].length!=0)
                {
                    for(var i=0;i<jprereqs[course_name].length;i++)
                    {
                        var ans= jprereqs[course_name][i];
                        var res=ans.split(",");
    
                        for(var j=0;j<res.length;j++)
                        {
                            if(res[j]!="")
                            {
	                         enter=is_taken(res[j],jcheck,array);
                                 if(enter==false)
                                 {
                                     return enter;
                                 }
                             }
                        }
                        if(enter==true)
                             return true;
                    }
                }
             }
        }
        return enter;
    }
    else
    {
    if(jprereqs[course_name]!=null  && jprereqs[course_name].length!=0)
        {
            for(var i=0;i<jprereqs[course_name].length;i++)
            {
                var ans= jprereqs[course_name][i];
                var res=ans.split(",");
              
	        for(var j=0;j<res.length;j++)
                {
                     if(res[j]!="")
                     {
                          var enter=is_taken(res[j],jcheck,array);
                          if(enter==false)
                          {
                             return false;
                          }
                    }
                }
                if(enter==true)
                  return true;
            }
        }
        return true;
    }

}

/******************************************************************************
 *	Function: is_taking
 *
 *	Description: This function returns true if the user is taking the class
 *		in question and false if not
 * ***************************************************************************/

function is_taking(str,jcheck,array)
{
    var index=str.substring(0,str.indexOf(" "))+"_"+str.substring(str.indexOf(" ")+1);
    var elem=document.getElementById(index);
    if(elem==null)
    {
        for(var i=0;i<jcheck.length;i++)
        {
            var name=document.getElementById(jcheck[i]);
            var t=name.options[name.selectedIndex].text;
           
	    if(getKeyByValue(array,t)==str)
            {
                var inp="grade_"+str.substring(jcheck[i].indexOf("_")+1);
                var grade_elem=document.getElementById(inp);
           
                if(grade_elem.options[grade_elem.selectedIndex].value>1 || 
						grade_elem.options[grade_elem.selectedIndex].value==-1 || 
						grade_elem.options[grade_elem.selectedIndex].value==-2)
                      return true;
            }
        }
        return false;
    }
    else
    {
        if(elem.options[elem.selectedIndex].value>1 || 
			elem.options[elem.selectedIndex].value==-1 || 
			elem.options[elem.selectedIndex].value==-2)
        return true;
        else
        return false;
    }
}
/******************************************************************************
 *	Function: is_taken
 *
 *	Description: This function is used by the pre req checked to make sure
 *		the required courses are taken.
 * ***************************************************************************/
function is_taken(str,jcheck,array)
{
    var index=str.substring(0,str.indexOf(" "))+"_"+str.substring(str.indexOf(" ")+1);
    var elem=document.getElementById(index);
    if(elem==null)
    {
        for(var i=0;i<jcheck.length;i++)
        {
            var name=document.getElementById(jcheck[i]);
            var t=name.options[name.selectedIndex].text;
           
	    if(getKeyByValue(array,t)==str)
            {
                var inp="grade_"+str.substring(jcheck[i].indexOf("_")+1);
                var grade_elem=document.getElementById(inp);
          
                if(grade_elem.options[grade_elem.selectedIndex].value>1 || 
						grade_elem.options[grade_elem.selectedIndex].value==-1)
                        
		    return true;
            }
        }
        return false;
    }
    else
    {
        if(elem.options[elem.selectedIndex].value>1 || 
			elem.options[elem.selectedIndex].value==-1)
        return true;
        else
        return false;
    }
}

function getKeyByValue(object, value) 
{
  return Object.keys(object).find(key => object[key] === value);
}
/******************************************************************************
 *	Function: get_gpa
 *
 *	Description: This function returns the calculated gpa
 * ***************************************************************************/
function get_gpa(grade,course)
{
    var a="grade";
    var str=(a.concat(course.substring(4)));
    var s=document.getElementById(str);
    s.value=parseInt(grade);
}

/******************************************************************************
 *	Function: updateXML
 *
 *	Description: This function updates the user's xml file
 * ***************************************************************************/
function updateXML( id )
{
    var http = new XMLHttpRequest(); 
    http.open("POST", "updateXML.php", true );
    http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");

    if ( this.readyState == 4 && this.status == 200 )
    {
        //console.log(this.responseText);
    }

    var uname = document.getElementById('uname').value;
    var grade = document.getElementById(id).value;
    var res = id.split("_");
    var prefix = res[0];
    var number = res[1];
	var str = "uname=" + uname + "&grade=" + grade + "&prefix="+ prefix + "&number=" + number ;
	//console.log(str);
    http.send("uname=" + uname + "&grade=" + grade + "&prefix="+ prefix + "&number=" + number );
}
