<?php
    include "courseObj.php";
    include "createUserFile.php";
    
    libxml_use_internal_errors(true);
    $xml = simplexml_load_file( 'courses.xml' );
    $course_gpa=array();
    
    //arrays containing credit number
    $freshman = array(
        "CSC 110" =>1,
        "CSC 150/L" =>3,
        "CSC 250" =>4,
        "CSC 251"=>4,
    );
    
    
    $sophomore = array(
         "CSC 300" =>4,
         "CSC 314" =>3,
    );
    
    $junior = array(
         "CSC 317" =>3,
         "CSC 372" =>3,
         "CSC 461" =>4,
         "CSC 468" =>3,
         "CSC 470" =>3,
         "CSC 484" =>3, 
    );
    
     $senior = array(
         "CSC 456"=>4,
         "CSC 464"=>2,
         "CSC 465"=>2,
    );
    
     $electives=array(
         "CSC 410"=>3,
         "CSC 412"=>3,
         "CSC 414"=>3,
         "CSC 415"=>3,
         "CSC 416"=>3,
         "CSC 426"=>3,
         "CSC 433"=>3,
         "CSC 441"=>3,
         "CSC 442"=>3,
         "CSC 445"=>3,
         "CSC 447"=>3,
         "CSC 449"=>3,
         "CSC 454"=>3,
         "CSC 476/476L"=>3,
         "CP 497"=>3,
     );
     //arrays containing the names of each course
       $csc=array(
         "CSC 110"=>"Survey of Computer Science and Mathematics",
         "CSC 150/L"=>"Computer Science 1",
         "CSC 250"=>"Computer Science 2",
         "CSC 251"=>"Finite Structures",
         "CSC 300"=>"Data Structures",
         "CSC 314"=>"Assembly Language",
         "CSC 317"=>"Computer Organization and Architecture",
         "CSC 372"=>"Analysis of Algorithms",
         "CSC 461"=>"Programming Languages",
         "CSC 468"=>"Graphical User Interface Programming",
         "CSC 470"=>"Software Engineering",
         "CSC 484"=>"Database Management Systems", 
         "CSC 456"=>"Operating Systems",
         "CSC 464"=>"Senior Design I",
         "CSC 465"=>"Senior Design II",
         "CSC 410"=>"Parallel Computing",
         "CSC 412"=>"Cryptography",
         "CSC 414"=>"Introduction to Computer Vision",
         "CSC 415"=>"Introduction to Robotics",
         "CSC 416"=>"Advanced Algorithms for Robotics",
         "CSC 426"=>"CyberSecurity",
         "CSC 433"=>"Computer Graphics",
         "CSC 441"=>"Networking and Data Communication",
         "CSC 442"=>"Digital Image Processing",
         "CSC 445"=>"Introduction to Theory of Computation",
         "CSC 447"=>"Artificial Intelligence",
         "CSC 449"=>"Advanced Topics in Artificial Intelligence",
         "CSC 454"=>"Data Mining Theory",
         "CSC 476/476L"=>"Mobile Computing Development",    
         "CP 497"=>" Cooperative Education",
     );
     
     $math_courses=array(
         "MATH 123"=>4,
         "MATH 125"=>4,
         "MATH 225"=>4,
         "MATH 315"=>3,
         "MATH 381"=>3,
     );
     
     $math=array(
     "MATH 123"=>"Calculus 1",
     "MATH 125"=>"Calculus 2",
     "MATH 225"=>"Calculus 3",
     "MATH 315"=>"Linear Algebra",
     "MATH 381"=>"Intro to Probs and Stats",
     
     );
     
     $math_electives_courses=array(
           "MATH 321"=>3,
           "MATH 382"=>3,
           "MATH 353"=>3,
           "MATH 413"=>3,
           "MATH 421"=>3,
           "MATH 423"=>3,
           "MATH 443"=>3,
     );
     
     $math_electives=array(
     "MATH 321"=>"Differential Equations",
     "MATH 382"=>"Probability Theory and Statistics ||",
     "MATH 353"=>"Linear Optimization",
     "MATH 413"=>"Abstract Algebra I",
     "MATH 421"=>"Complex Analysis",
     "MATH 423"=>"Advanced Calculus I",
     "MATH 443"=>"Data Analysis",
     );     

      $engl_courses=array(
         "ENGL 101"=>3,
         "ENGL 279"=>3,
         "ENGL 289"=>3,
     );
     
      $engl=array(
         "ENGL 101"=>"English Composition I",
         "ENGL 279"=>"Technical Communication I",
         "ENGL 289"=>"Technical composition II",
     );
     //arrays for gen ed courses
      $gen_ed_courses=array(
         "ANTH 210"=>3,
         "GEOG 101"=>3,
         "GEOG 210"=>3,
         "GEOG 212"=>3,
         "HIST 151"=>3,
         "HIST 152"=>3,
         "POLS 100"=>3,
         "POLS 165"=>3,
         "POLS 250"=>3,
         "PSYC 101"=>3,
         "SOC 100"=>3,
         "SOC 150"=>3,
         "SOC 250"=>3,
         "ART 111"=>3,
         "ART 112"=>3,
         "ARTH 211"=>3,
         "CHIN 101"=>4,
         "CHIN 102"=>4,
         "ENGL 210"=>3,
         "ENGL 212"=>3,
         "ENGL 221"=>3,
         "ENGL 222"=>3,
         "ENGL 242"=>3,
         "ENGL 250"=>3,
         "GER 101"=>4,
         "GER 102"=>4,
         "HIST 121"=>3,
         "HIST 122"=>3,
         "HUM 100"=>3,
         "HUM 200"=>3,
         "MUS 100"=>3,
         "MUS 117"=>3,
         "PHIL 100"=>3,
         "PHIL 200"=>3,
         "PHIL 220"=>3,
         "PHIL 233"=>3,
         "SPAN 101"=>4,
         "SPAN 102"=>4,
         
         
     );
     
      $gen_ed=array(
         "ANTH 210"=>"Cultural Anthropology",
         "GEOG 101"=>"Intro to Geography",
         "GEOG 210"=>"World Regional Geography",
         "GEOG 212"=>"Geog of North America",
         "HIST 151"=>"US History I",
         "HIST 152"=>"US History II",
         "POLS 100"=>"American Government",
         "POLS 165"=>"Political Ideologies",
         "POLS 250"=>"World Politics",
         "PSYC 101"=>"General Psychology",
         "SOC 100"=>"Intro to Sociology",
         "SOC 150"=>"Social Problems",
         "SOC 250"=>"Courtship & Marriage",
         "ART 111"=>"Drawing I",
         "ART 112"=>"Drawing II",
         "ARTH 211"=>"History of World Art I",
         "CHIN 101"=>"Intro Chinese I",
         "CHIN 102"=>"Intro Chinese II",
         "ENGL 210"=>"Intro to Literature",
         "ENGL 212"=>"World Literature",
         "ENGL 221"=>"British Literature I",
         "ENGL 222"=>"British Literature II",         
         "ENGL 241"=>"American Literature I",
         "ENGL 242"=>"American Literature II",         
         "ENGL 250"=>"Science Fiction",
         "GER 101"=>"Intro German I",
         "GER 102"=>"Intro German II",
         "HIST 121"=>"Western Civilization I",
         "HIST 122"=>"Western Civilization II",
         "HUM 100"=>"Intro to Humanities",
         "HUM 200"=>"Connections",
         "MUS 100"=>"Music Appreciation",
         "MUS 117"=>"Music Performance",
         "PHIL 100"=>"Intro to Philosophy",
         "PHIL 200"=>"Intro to Logic",
         "PHIL 220"=>"Intro to Ethics",
         "PHIL 233"=>"Philosophy and Literature",
         "SPAN 101"=>"Intro to Spanish I",
         "SPAN 102"=>"Intro to Spanish II",
     );
     //arrays containing the science courses
      $science_courses=array(
         "BIOL 151/L"=>4,
         "BIOL 153/L"=>4,
         "CHEM 106/L"=>4,
         "CHEM 108/L"=>5,
         "CHEM 112/L"=>4,
         "CHEM 114/L"=>4,
         "GEOL 201/L"=>4,
         "PHYS 111/L"=>4,
         "PHYS 113/L"=>4,
         "PHYS 211"=>3,
         "PHYS 213/L"=>4,
     );
     
      $science=array(
         "BIOL 151/L"=>"General Biology I",
         "BIOL 153/L"=>"General Biology II",
         "CHEM 106/L"=>"Chemistry Survey",
         "CHEM 108/L"=>"Organic and Biochemistry",
         "CHEM 112/L"=>"General Chemistry I",
         "CHEM 114/L"=>"General Chemistry II",
         "GEOL 201/L"=>"Physical Geology",
         "PHYS 111/L"=>"Introduction to Physics I",
         "PHYS 113/L"=>"Introduction to Physics II",
         "PHYS 211"=>"University Physics I",
         "PHYS 213/L"=>"University Physics II",
     );
     
     $check=array("name_csce0",
     "name_csce1",
     "name_csce2",
     "name_csce3",
     "name_sci1",
     "name_sci2",
     "name_math5",
     "name_gened0",
     "name_gened1",
     "name_gened2",
     "name_gened3",
     "name_gened4",
    );
    
    $jall=(((($csc+$math)+$math_electives)+$engl)+$gen_ed)+$science;
      $all=json_encode($jall);
        	$prereqs = array();
    	$coreqs = array();
    
    //check the course.xml file
    if ( $xml == FALSE )
    {
    	if ( $userxml == FALSE )
    		echo "Could not open " . "xml/" . $_POST['uname'] . ".xml";
        foreach ( libxml_get_errors() as $error )
        {
            echo "\t", $error->message, "<br />";
        }
    }
    else
    {

    	
        foreach ( $xml->course as $c )
        {
            $newCourse = new course($c);
            $courses[] = $newCourse;
			
			$indx = $c->prefix . " " . $c->number;
			
			// get arrays of prereqs for checking
			$prqs = array();
			foreach ( $c->prereq as $pr )
			{
				$arr = explode( ",", $pr );
				$final = "";
				foreach ( $arr as $a )
				{
					$arr2 = explode ( " ", $a );
					if ( count( $arr2 ) > 2 )
					{
                        // permission of instructor
						if ( $arr2[0] == "permission" )
						{
							//$final = $final . "permission,";
						}
						else if ( $arr2[0] == "appropriate" )
						{
							//var_dump($arr2);
						}
						else // C or better
						{
							if ( $arr2[0] == "" )
                            {
                                if(($arr2[1]=="MATH" and ($arr2[2]=="115" or $arr2[2]=="120")) or $arr2[1] == "appropriate" or $arr2[1] == "permission")
                                  {
                                  }
                                  else
                                	$final = $final . $arr2[1] . " " . $arr2[2] . ",";
                            }
                            else
                            {
                                  if($arr2[0]=="MATH" and ($arr2[1]=="115" or $arr2[1]=="120") or $arr2[0] == "appropriate" or $arr2[0] == "permission")
                                  {
                                  }
                                  else
                                	$final = $final . $arr2[0] . " " . $arr2[1] . ",";
                            }
						}
					}
					else
					{
						$final = $final . $a . ",";
					}
				}
				$prqs[] = substr( $final, 0, -1 );
			}
			$prereqs[ $indx ] = $prqs;
            //echo $indx . " -> ";
			//var_dump($prqs);
        	//echo "<br>";
			
            // get coreqs
			$crqs = array();
			foreach ( $c->coreq as $pr )
			{
				$arr = explode( ",", $pr );
				$final = "";
				foreach ( $arr as $a )
				{
                 
                        
					$arr2 = explode ( " ", $a );
					if ( count( $arr2 ) > 2 )
					{
						if ( $arr2[0] == "permission" )
						{
							$final = $final . "permission,";
						}
						else
						{
                            if ( $arr2[0] == "" )
                            {
    							$final = $final . $arr2[1] . " " . $arr2[2] . ",";
                            }
                            else
                            {
                                $final = $final . $arr2[0] . " " . $arr2[1] . ",";
                            }
						}
					}
					else
					{
						$final = $final . $a . ",";
					}
				}
				$crqs[] = substr( $final, 0, -1 );
			}
			$coreqs[ $indx ] = $crqs;
			
            $p = (string) $c->prefix;
            $classes[ $p ][] = $c->number;
            $prefixes[] = $p;
        }
        $prefixes = array_unique( $prefixes );
       
        usort( $courses, function($a, $b)
        {
            $res = strcmp( $a->prefix, $b->prefix );
            if ( $res == 0 )
            {
                if ( $a->number < $b->number )
                    return -1;
                else if ( $a->number > $b->number )
                    return 1;
                else
                    return 0;
            }
            else
                return $res;
        } );
        
        
        if ( !file_exists( "xml/" . $_POST['uname'] . ".xml" ) )
		{
			createUserFile( $_POST['uname'],$courses ,$gen_ed_courses,$science_courses);
		}
		    
		
		//echo "FILE EXISTS: " . file_exists( "xml/" . $_POST['uname'] . ".xml" );
		$userxml = simplexml_load_file( "xml/" . $_POST['uname'] . ".xml" );
		if ( $userxml == FALSE )
		{
			echo "Failed to load the saved file";
			return;
		}

		
		foreach ( $userxml->course as $c )
        {
           $pre = $c->prefix;
           $num = $c->number;
           $temp=$pre." ".$num;
           $course_gpa[$temp]=(int) $c->grade;
        }
        
        }//checks the prereqs for the CSC100 level courses
                $prereqs["MATH 123"]="";
                $jcheck=json_encode($check);
                $jprereqs=json_encode($prereqs);
                $jcoreqs=json_encode($coreqs);
                echo "<table class = 'classTable' id = 'CSC1'>";
                 echo "<tr  class = 'classItem'>";
                echo "<th style = 'text-align: left;' colpan='10'><input type='button' 
						style='border:none;background-color: white;'  id='buttonCSC1'  
						onclick='expand(this.id)' value='-'></input></th>";
                echo "<th style = 'text-align: center;' colspan = '10'>CSC Freshman Courses </th>";	
                echo "</tr>";
                echo "<tr class = 'classItemCSC1'>";
                echo "<td class = 'classItemCSC1' style = 'text-align: center;'>Course #</td>";
                echo "<td class = 'classItemCSC1' style = 'text-align: center;'>Name</td>";
                echo "<td class = 'classItemCSC1' style = 'text-align: center;'>Credits</td>";
                echo "<td class = 'classItemCSC1' style = 'text-align: center;'>Grade</td>";
                $arr=json_encode($csc);
                foreach($freshman as $key=>$value)
                {
                $temp=substr($key,0,(strpos($key," ")))." ".substr($key,(strpos($key," ")+1),3);
                $grade=$course_gpa[$temp];

                echo "<tr class = 'classItemCSC1'>";
                    echo "<td class = 'classItemCSC1'>".substr($key,4,3)."</td>";
                    echo "<td class = 'classItemCSC1'>$csc[$key]</td>";
                    echo "<td class = 'classItemCSC1' style = 'text-align: center;' 
						id = '" . $key . "Credits'>" . $value . "</td>";
                    echo "<td class = 'classItemCSC1'><select id = '" . 
						substr($key,0,3)."_".substr($key,4,3). "' class = 'grade' 
						onchange = 'updateGPA(this.id,$arr, $jcheck,$jprereqs,$jcoreqs,$all)'>";
                    	if ( $grade == 4 )
                    		echo "<option value = '4' selected = 'selected'>A</option>";
                    	else
                        	echo "<option value = '4'>A</option>";
                        	
                        if ( $grade== 3 )
	                        echo "<option value = '3' selected = 'selected'>B</option>";
	                    else
                        	echo "<option value = '3'>B</option>";
                        	
                        if ( $grade == 2 )
                        	echo "<option value = '2' selected = 'selected'>C</option>";
                        else
                        	echo "<option value = '2'>C</option>";
                        
                        if ( $grade == 1 )
                        	echo "<option value = '1' selected = 'selected'>D</option>";
                        else
                        	echo "<option value = '1'>D</option>";
                        
                        if ( $grade == 0 )
	                        echo "<option value = '0' selected = 'selected'>F</option>";
	                    else
	                    	echo "<option value = '0'>F</option>";
	                    
	                    if ( $grade == -1 )
	                    	echo "<option value = '-1' selected = 'selected'>Exempt</option>";
	                    else
	                    	echo "<option value = '-1'>Exempt</option>";
	                    
	                    if ( $grade == -2 )
		                    echo "<option value = '-2' selected = 'selected'>Taking</option>";
		                else
                        	echo "<option value = '-2'>Taking</option>";
                        
                        if ( $grade == -3 )
                        	echo "<option value = '-3' selected = 'selected'>Not Taken</option>";
                        else
                        	echo "<option value = '-3'>Not Taken</option>";
                    echo "< /td>";

                echo "</tr>";
                }
        echo "</table>";
     
     //check the prereqs for the CSC 200 level courses
      echo "<table class = 'classTable' id = 'CSC2'>";
                 echo "<tr  class = 'classItem'>";
                echo "<th style = 'text-align: left;'><input type='button'
					style='border:none;background-color: white;'  id='buttonCSC2'  
					onclick='expand(this.id)' value='-'></input></th>";
                echo "<th style = 'text-align: center;' colspan = '10'>CSC Sophomore Courses </th>";	
                echo "</tr>";
                echo "<tr class = 'classItemCSC2'>";
                echo "<td class = 'classItemCSC2' style = 'text-align: center;'>Course #</td>";
                echo "<td class = 'classItemCSC2' style = 'text-align: center;'>Name</td>";
                echo "<td class = 'classItemCSC2' style = 'text-align: center;'>Credits</td>";
                echo "<td class = 'classItemCSC2' style = 'text-align: center;'>Grade</td>";
                $arr=json_encode($csc);
                foreach($sophomore as $key=>$value)
                {
                 $temp=substr($key,0,(strpos($key," ")))." ".substr($key,(strpos($key," ")+1),3);
                $grade=$course_gpa[$temp];
                echo "<tr class = 'classItemCSC2'>";
                    echo "<td class = 'classItemCSC2'>".substr($key,4,3)."</td>";
                    echo "<td class = 'classItemCSC2'>$csc[$key]</td>";
                    echo "<td class = 'classItemCSC2' style = 'text-align: center;' 
						id = '" . $key . "Credits'>" . $value . "</td>";
                    echo "<td class = 'classItemCSC2'><select id = '" . 
						substr($key,0,3)."_".substr($key,4,3). "' class = 'grade' 
						onchange = 'updateGPA(this.id,$arr, $jcheck,$jprereqs,$jcoreqs,$all)'>";
                    	if ( $grade == 4 )
                    		echo "<option value = '4' selected = 'selected'>A</option>";
                    	else
                        	echo "<option value = '4'>A</option>";
                        	
                        if ( $grade == 3 )
	                        echo "<option value = '3' selected = 'selected'>B</option>";
	                    else
                        	echo "<option value = '3'>B</option>";
                        	
                        if ( $grade == 2 )
                        	echo "<option value = '2' selected = 'selected'>C</option>";
                        else
                        	echo "<option value = '2'>C</option>";
                        
                        if ( $grade == 1 )
                        	echo "<option value = '1' selected = 'selected'>D</option>";
                        else
                        	echo "<option value = '1'>D</option>";
                        
                        if ( $grade == 0 )
	                        echo "<option value = '0' selected = 'selected'>F</option>";
	                    else
	                    	echo "<option value = '0'>F</option>";
	                    
	                    if ( $grade == -1 )
	                    	echo "<option value = '-1' selected = 'selected'>Exempt</option>";
	                    else
	                    	echo "<option value = '-1'>Exempt</option>";
	                    
	                    if ( $grade == -2 )
		                    echo "<option value = '-2' selected = 'selected'>Taking</option>";
		                else
                        	echo "<option value = '-2'>Taking</option>";
                        
                        if ( $grade == -3 )
                        	echo "<option value = '-3' selected = 'selected'>Not Taken</option>";
                        else
                        	echo "<option value = '-3'>Not Taken</option>";
                    echo "</td>";

                echo "</tr>";
                }
        echo "</table>";
     //checks the prereqs for the CSC 300 level courses
      echo "<table class = 'classTable' id = 'CSC3'>";
                 echo "<tr  class = 'classItem'>";
                echo "<th style = 'text-align: left;'><input type='button' 
					style='border:none;background-color: white;'  id='buttonCSC3'  
					onclick='expand(this.id)' value='-'></input></th>";
                echo "<th style = 'text-align: center;' colspan = '10'>CSC Junior Courses </th>";	
                echo "</tr>";
                echo "<tr class = 'classItemCSC3'>";
                echo "<td class = 'classItemCSC3' style = 'text-align: center;'>Course #</td>";
                echo "<td class = 'classItemCSC3' style = 'text-align: center;'>Name</td>";
                echo "<td class = 'classItemCSC3' style = 'text-align: center;'>Credits</td>";
                echo "<td class = 'classItemCSC3' style = 'text-align: center;'>Grade</td>";
                $arr=json_encode($csc);
                foreach($junior as $key=>$value)
                {
                 $temp=substr($key,0,(strpos($key," ")))." ".substr($key,(strpos($key," ")+1),3);
                $grade=$course_gpa[$temp];
                echo "<tr class = 'classItemCSC3'>";
                    echo "<td class = 'classItemCSC3'>".substr($key,4,3)."</td>";
                    echo "<td class = 'classItemCSC3'>$csc[$key]</td>";
                    echo "<td class = 'classItemCSC3' style = 'text-align: center;' 
						id = '" . $key . "Credits'>" . $value . "</td>";
                    echo "<td class = 'classItemCSC3'><select id = '" . 
						substr($key,0,3)."_".substr($key,4,3). "' class = 'grade' 
						onchange = 'updateGPA(this.id,$arr, $jcheck,$jprereqs,$jcoreqs,$all)'>";
                    	if ( $grade == 4 )
                    		echo "<option value = '4' selected = 'selected'>A</option>";
                    	else
                        	echo "<option value = '4'>A</option>";
                        	
                        if ( $grade == 3 )
	                        echo "<option value = '3' selected = 'selected'>B</option>";
	                    else
                        	echo "<option value = '3'>B</option>";
                        	
                        if ( $grade == 2 )
                        	echo "<option value = '2' selected = 'selected'>C</option>";
                        else
                        	echo "<option value = '2'>C</option>";
                        
                        if ( $grade == 1 )
                        	echo "<option value = '1' selected = 'selected'>D</option>";
                        else
                        	echo "<option value = '1'>D</option>";
                        
                        if ( $grade == 0 )
	                        echo "<option value = '0' selected = 'selected'>F</option>";
	                    else
	                    	echo "<option value = '0'>F</option>";
	                    
	                    if ( $grade == -1 )
	                    	echo "<option value = '-1' selected = 'selected'>Exempt</option>";
	                    else
	                    	echo "<option value = '-1'>Exempt</option>";
	                    
	                    if ( $grade == -2 )
		                    echo "<option value = '-2' selected = 'selected'>Taking</option>";
		                else
                        	echo "<option value = '-2'>Taking</option>";
                        
                        if ( $grade == -3 )
                        	echo "<option value = '-3' selected = 'selected'>Not Taken</option>";
                        else
                        	echo "<option value = '-3'>Not Taken</option>";
                    echo "</td>";

                echo "</tr>";
                }
        echo "</table>"; 
        //Checks the prereqs for the CSC 400 level courses
        echo "<table class = 'classTable' id = 'CSC4'>";
                 echo "<tr  class = 'classItem'>";
                echo "<th style = 'text-align: left;'><input type='button' 
					style='border:none;background-color: white;'  id='buttonCSC4'  
					onclick='expand(this.id)' value='-'></input></th>";
                echo "<th style = 'text-align: center;' colspan = '10'>CSC Senior Courses </th>";	
                echo "</tr>";
                echo "<tr class = 'classItemCSC4'>";
                echo "<td class = 'classItemCSC4' style = 'text-align: center;'>Course #</td>";
                echo "<td class = 'classItemCSC4' style = 'text-align: center;'>Name</td>";
                echo "<td class = 'classItemCSC4' style = 'text-align: center;'>Credits</td>";
                echo "<td class = 'classItemCSC4' style = 'text-align: center;'>Grade</td>";
                $arr=json_encode($csc);
                foreach($senior as $key=>$value)
                {
                 $temp=substr($key,0,(strpos($key," ")))." ".substr($key,(strpos($key," ")+1),3);
                $grade=$course_gpa[$temp];
                echo "<tr class = 'classItemCSC4'>";
                    echo "<td class = 'classItemCSC4'>".substr($key,4,3)."</td>";
                    echo "<td class = 'classItemCSC4'>$csc[$key]</td>";
                    echo "<td class = 'classItemCSC4' style = 'text-align: center;' 
						id = '" . $key . "Credits'>" . $value . "</td>";
                    echo "<td class = 'classItemCSC4'><select id = '" . 
						substr($key,0,3)."_".substr($key,4,3). "' class = 'grade' 
						onchange = 'updateGPA(this.id,$arr, $jcheck,$jprereqs,$jcoreqs,$all)'>";
                    	if ( $grade == 4 )
                    		echo "<option value = '4' selected = 'selected'>A</option>";
                    	else
                        	echo "<option value = '4'>A</option>";
                        	
                        if ( $grade == 3 )
	                        echo "<option value = '3' selected = 'selected'>B</option>";
	                    else
                        	echo "<option value = '3'>B</option>";
                        	
                        if ( $grade == 2 )
                        	echo "<option value = '2' selected = 'selected'>C</option>";
                        else
                        	echo "<option value = '2'>C</option>";
                        
                        if ( $grade == 1 )
                        	echo "<option value = '1' selected = 'selected'>D</option>";
                        else
                        	echo "<option value = '1'>D</option>";
                        
                        if ( $grade == 0 )
	                        echo "<option value = '0' selected = 'selected'>F</option>";
	                    else
	                    	echo "<option value = '0'>F</option>";
	                    
	                    if ( $grade == -1 )
	                    	echo "<option value = '-1' selected = 'selected'>Exempt</option>";
	                    else
	                    	echo "<option value = '-1'>Exempt</option>";
	                    
	                    if ( $grade == -2 )
		                    echo "<option value = '-2' selected = 'selected'>Taking</option>";
		                else
                        	echo "<option value = '-2'>Taking</option>";
                        
                        if ( $grade == -3 )
                        	echo "<option value = '-3' selected = 'selected'>Not Taken</option>";
                        else
                        	echo "<option value = '-3'>Not Taken</option>";
                    echo "</td>";

                echo "</tr>";
                }
        echo "</table>";
        //Checks the prereqs for the CSC electives
        echo "<table class = 'classTable' id = 'CSCE'>";
                 echo "<tr  class = 'classItem'>";
                echo "<th style = 'text-align: left;' colpan='10'><input type='button' 
					style='border:none;background-color: white;'  id='buttonCSCE'  
					onclick='expand(this.id)' value='-'></input></th>";
                echo "<th style = 'text-align: center;' colspan = '10'>CSC Upper Electives</th>";	
                echo "</tr>";
                echo "<tr class = 'classItemCSCE'>";
                echo "<td class = 'classItemCSCE' style = 'text-align: center;'>Course #</td>";
                echo "<td class = 'classItemCSCE' style = 'text-align: center;'>Name</td>";
                echo "<td class = 'classItemCSCE' style = 'text-align: center;'>Credits</td>";
                echo "<td class = 'classItemCSCE' style = 'text-align: center;'>Grade</td>";
                $arr=json_encode($csc);
                $arr1=json_encode($electives);
                $electives_copy=$electives;
                $k=0;
                $jtemp="aaa";
                $jgrade=1;
                for($i=0;$i<4;$i++)
                {
                $enter=false;
                echo "<tr class = 'classItemCSCE'>";
                    echo "<td class = 'classItemCSCE' id='num_csce".$i."'></td>";
                   echo "<td class = 'classItemCSCE'><select id='name_csce".$i."' 
						class = 'dropdown' onchange = 'update_course(this.id,$arr,$arr1)'>";
                   echo "<option class='classItemCSCE' value = '".$k."' selected = 'selected'></option>";
                    foreach($electives as $key=>$value)
                    {
                    
                        $temp=substr($key,0,(strpos($key," ")))." ".substr($key,(strpos($key," ")+1),3);
                        $grade=$course_gpa[$temp];
                          $temporary="name_csce".$i;
                          
                        if($grade!=-3 and array_key_exists($key,$electives_copy) and $k==$i)
                           {
                            echo "<option selected='selected' >".$csc[$key]."</option>";
                            unset($electives_copy[$key]);
                             $jtemp=json_encode($temporary);
                             $jgrade=json_encode($grade);
                             $k++;
                             $enter=true;
                            
                           }
                        else 
                           	echo "<option>".$csc[$key]."</option>";
                    	
                    }
                    echo "< /td>";
                    
                    
                    echo "<td class = 'classItemCSCE' id='credits_csce".$i."' 
						style = 'text-align: center;'></td>";
                    echo "<td class = 'classItemCSCE'><select disabled id='grade_csce".$i."' 
						class = 'grade' onchange = 'updateGPA(this.id,$arr, $jcheck,$jprereqs,$jcoreqs,$all)'>";
                    	
                        if ( $grade == 4 )
                    		echo "<option value = '4' selected = 'selected'>A</option>";
                    	else
                        	echo "<option value = '4'>A</option>";
                        	
                        if ( $grade == 3 )
	                        echo "<option value = '3' selected = 'selected'>B</option>";
	                    else
                        	echo "<option value = '3'>B</option>";
                        	
                        if ( $grade == 2 )
                        	echo "<option value = '2' selected = 'selected'>C</option>";
                        else
                        	echo "<option value = '2'>C</option>";
                        
                        if ( $grade == 1 )
                        	echo "<option value = '1' selected = 'selected'>D</option>";
                        else
                        	echo "<option value = '1'>D</option>";
                        
                        if ( $grade == 0 )
	                        echo "<option value = '0' selected = 'selected'>F</option>";
	                    else
	                    	echo "<option value = '0'>F</option>";
	                    
	                    if ( $grade == -1 )
	                    	echo "<option value = '-1' selected = 'selected'>Exempt</option>";
	                    else
	                    	echo "<option value = '-1'>Exempt</option>";
	                    
	                    if ( $grade == -2 )
		                    echo "<option value = '-2' selected = 'selected'>Taking</option>";
		                else
                        	echo "<option value = '-2'>Taking</option>";
                        
                        if ( $grade == -3 )
                        	echo "<option value = '-3' selected = 'selected'>Not Taken</option>";
                        else
                        	echo "<option value = '-3'>Not Taken</option>";
                    echo "< /td>";

                echo "</tr>";
                if($enter==true)
                {
                echo "<script type='text/javascript'>","update_course($jtemp,$arr,$arr1); ","</script>";
                 echo "<script type='text/javascript'>","get_gpa($jgrade,$jtemp); ","</script>";
                }
                }
        echo "</table>";
        
        //Checks the prereqs for the science courses
        echo "<table class = 'classTable' id = 'SCIENCE'>";
                 echo "<tr  class = 'classItem'>";
                echo "<th style = 'text-align: left;' colpan='10'><input type='button' 
					style='border:none;background-color: white;'  id='buttonSCI'  
					onclick='expand(this.id)' value='-'></input></th>";
                echo "<th style = 'text-align: center;' colspan = '10'>Science Electives</th>";	
                echo "</tr>";
                echo "<tr class = 'classItemSCI'>";
                echo "<td class = 'classItemSCI' style = 'text-align: center;'>Course #</td>";
                echo "<td class = 'classItemSCI' style = 'text-align: center;'>Name</td>";
                echo "<td class = 'classItemSCI' style = 'text-align: center;'>Credits</td>";
                echo "<td class = 'classItemSCI' style = 'text-align: center;'>Grade</td>";
                
                 echo "<tr class = 'classItemSCI'>";
                 echo "<td class = 'classItemSCI' >211</td>";
                 echo "<td class = 'classItemSCI' >University Physics I</td>";
                 echo "<td class = 'classItemSCI'  style = 'text-align: center;'>3</td>";
                   $arr=json_encode($science);
               $arr1=json_encode($science_courses);
                     $grade=$course_gpa["PHYS 211"];
                    echo "<td class = 'classItemSCI'><select  id='PHYS_211' class = 'grade' 
						onchange = 'updateGPA(this.id,$arr, $jcheck,$jprereqs,$jcoreqs,$all)'>";
                    	 if ( $grade == 4 )
                    		echo "<option value = '4' selected = 'selected'>A</option>";
                    	else
                        	echo "<option value = '4'>A</option>";
                        	
                        if ( $grade == 3 )
	                        echo "<option value = '3' selected = 'selected'>B</option>";
	                    else
                        	echo "<option value = '3'>B</option>";
                        	
                        if ( $grade == 2 )
                        	echo "<option value = '2' selected = 'selected'>C</option>";
                        else
                        	echo "<option value = '2'>C</option>";
                        
                        if ( $grade == 1 )
                        	echo "<option value = '1' selected = 'selected'>D</option>";
                        else
                        	echo "<option value = '1'>D</option>";
                        
                        if ( $grade == 0 )
	                        echo "<option value = '0' selected = 'selected'>F</option>";
	                    else
	                    	echo "<option value = '0'>F</option>";
	                    
	                    if ( $grade == -1 )
	                    	echo "<option value = '-1' selected = 'selected'>Exempt</option>";
	                    else
	                    	echo "<option value = '-1'>Exempt</option>";
	                    
	                    if ( $grade == -2 )
		                    echo "<option value = '-2' selected = 'selected'>Taking</option>";
		                else
                        	echo "<option value = '-2'>Taking</option>";                      
                        if ( $grade == -3 )
                        	echo "<option value = '-3' selected = 'selected'>Not Taken</option>";
                        else
                        	echo "<option value = '-3'>Not Taken</option>";
                    echo "< /td>";
                echo "</tr>";
               $science_courses_copy=$science_courses;
                $k=1;
                $jtemp="aaa";
                $jgrade=0;
               unset($science_courses_copy["PHYS 211"]);
                for($i=1;$i<=2;$i++)
                {
                $enter=false;
                echo "<tr class = 'classItemSCI'>";
                    echo "<td class = 'classItemSCI' id='num_sci".$i."'></td>";
                   echo "<td class = 'classItemSCI'><select id='name_sci".$i."' 
						class = 'dropdown' onchange='update_course(this.id,$arr,$arr1)'>";
                   echo "<option selected = 'selected'></option>";
                    foreach($science_courses as $key=>$value)
                    {
                        
                      $temp=substr($key,0,(strpos($key," ")))." ".substr($key,(strpos($key," ")+1));
                      $grade=$course_gpa[$temp];
                      $temporary="name_sci".$i;
                          
                     if($grade!=-3 and array_key_exists($key,$science_courses_copy) 
							and $k==$i and $key!="PHYS 211")
                               {
                                echo "<option selected='selected' >".$science[$key]."</option>";
                                unset($science_courses_copy[$key]);
                                 $jtemp=json_encode($temporary);
                                 $jgrade=json_encode($grade);
                                 $k++;
                                 $enter=true;
                               }
                            else if($key!="PHYS 211")
                            	echo "<option>".$science[$key]."</option>";
                    	    
                    	
                    }
                    echo "< /td>";
    
    
                    echo "<td class = 'classItemSCI' id='credits_sci".$i."' 
						style = 'text-align: center;'></td>";
                    echo "<td class = 'classItemSCI'><select disabled id='grade_sci".$i."' 
						class = 'grade' onchange = 'updateGPA(this.id,$arr, $jcheck,$jprereqs,$jcoreqs,$all)'>";
                      if ( $grade == 4 )
                    		echo "<option value = '4' selected = 'selected'>A</option>";
                    	else
                        	echo "<option value = '4'>A</option>";
                        	
                        if ( $grade == 3 )
	                        echo "<option value = '3' selected = 'selected'>B</option>";
	                    else
                        	echo "<option value = '3'>B</option>";
                        	
                        if ( $grade == 2 )
                        	echo "<option value = '2' selected = 'selected'>C</option>";
                        else
                        	echo "<option value = '2'>C</option>";
                        
                        if ( $grade == 1 )
                        	echo "<option value = '1' selected = 'selected'>D</option>";
                        else
                        	echo "<option value = '1'>D</option>";
                        
                        if ( $grade == 0 )
	                        echo "<option value = '0' selected = 'selected'>F</option>";
	                    else
	                    	echo "<option value = '0'>F</option>";
	                    
	                    if ( $grade == -1 )
	                    	echo "<option value = '-1' selected = 'selected'>Exempt</option>";
	                    else
	                    	echo "<option value = '-1'>Exempt</option>";
	                    
	                    if ( $grade == -2 )
		                    echo "<option value = '-2' selected = 'selected'>Taking</option>";
		                else
                        	echo "<option value = '-2'>Taking</option>";
                        
                        if ( $grade == -3 )
                        	echo "<option value = '-3' selected = 'selected'>Not Taken</option>";
                        else
                        	echo "<option value = '-3'>Not Taken</option>";
                    echo "< /td>";
                echo "</tr>";
                if($enter==true)
                {
                echo "<script type='text/javascript'>","update_course($jtemp,$arr,$arr1); ","</script>";
                echo "<script type='text/javascript'>","get_gpa($jgrade,$jtemp); ","</script>";
                }
                }
        echo "</table>";
        
         
     //Checks the prereqs for the english classes
     echo "<table class = 'classTable' id = 'ENGL'>";
                 echo "<tr  class = 'classItem'>";
                echo "<th style = 'text-align: left;'><input type='button' 
					style='border:none;background-color: white;'  id='buttonEngl'  
					onclick='expand(this.id)' value='-'></input></th>";
                echo "<th style = 'text-align: center;' colspan = '10'>English </th>";	
                echo "</tr>";
                echo "<tr class = 'classItemEngl'>";
                echo "<td class = 'classItemEngl' style = 'text-align: center;'>Course #</td>";
                echo "<td class = 'classItemEngl' style = 'text-align: center;'>Name</td>";
                echo "<td class = 'classItemEngl' style = 'text-align: center;'>Credits</td>";
                echo "<td class = 'classItemEngl' style = 'text-align: center;'>Grade</td>";
                $arr=json_encode($engl);
                foreach($engl_courses as $key=>$value)
                {
                 $temp=substr($key,0,(strpos($key," ")))." ".substr($key,(strpos($key," ")+1),3);
                $grade=$course_gpa[$temp];
                echo "<tr class = 'classItemEngl'>";
                    echo "<td class = 'classItemEngl'>".substr($key,5,3)."</td>";
                    echo "<td class = 'classItemEngl'>$engl[$key]</td>";
                    echo "<td class = 'classItemEngl' style = 'text-align: center;' 
						id = '" . $key . "Credits'>" . $value . "</td>";
                    echo "<td class = 'classItemEngl'><select id = '" . 
						substr($key,0,4)."_".substr($key,5,3). "' class = 'grade' 
						onchange = 'updateGPA(this.id,$arr, $jcheck,$jprereqs,$jcoreqs,$all)'>";
                    	if ( $grade == 4 )
                    		echo "<option value = '4' selected = 'selected'>A</option>";
                    	else
                        	echo "<option value = '4'>A</option>";
                        	
                        if ( $grade == 3 )
	                        echo "<option value = '3' selected = 'selected'>B</option>";
	                    else
                        	echo "<option value = '3'>B</option>";
                        	
                        if ( $grade == 2 )
                        	echo "<option value = '2' selected = 'selected'>C</option>";
                        else
                        	echo "<option value = '2'>C</option>";
                        
                        if ( $grade == 1 )
                        	echo "<option value = '1' selected = 'selected'>D</option>";
                        else
                        	echo "<option value = '1'>D</option>";
                        
                        if ( $grade == 0 )
	                        echo "<option value = '0' selected = 'selected'>F</option>";
	                    else
	                    	echo "<option value = '0'>F</option>";
	                    
	                    if ( $grade == -1 )
	                    	echo "<option value = '-1' selected = 'selected'>Exempt</option>";
	                    else
	                    	echo "<option value = '-1'>Exempt</option>";
	                    
	                    if ( $grade == -2 )
		                    echo "<option value = '-2' selected = 'selected'>Taking</option>";
		                else
                        	echo "<option value = '-2'>Taking</option>";
                        
                        if ( $grade == -3 )
                        	echo "<option value = '-3' selected = 'selected'>Not Taken</option>";
                        else
                        	echo "<option value = '-3'>Not Taken</option>";
                    echo "</td>";

                echo "</tr>";
                }
        echo "</table>";
        //checks the preqs for the math courses
        echo "<table class = 'classTable' id = 'MATH'>";
                 echo "<tr  class = 'classItem'>";
                echo "<th style = 'text-align: left;'><input type='button' 
					style='border:none;background-color: white;'  id='buttonMath'  
					onclick='expand(this.id)' value='-'></input></th>";
                echo "<th style = 'text-align: center;' colspan = '10'>Mathematics </th>";	
                echo "</tr>";
                echo "<tr class = 'classItemMath'>";
                echo "<td class = 'classItemMath' style = 'text-align: center;'>Course #</td>";
                echo "<td class = 'classItemMath' style = 'text-align: center;'>Name</td>";
                echo "<td class = 'classItemMath' style = 'text-align: center;'>Credits</td>";
                echo "<td class = 'classItemMath' style = 'text-align: center;'>Grade</td>";
                $arr=json_encode($math_electives);
                $arr1=json_encode($math_electives_courses);
                foreach($math_courses as $key=>$value)
                {
                 $temp=substr($key,0,(strpos($key," ")))." ".substr($key,(strpos($key," ")+1),3);
                $grade=$course_gpa[$temp];
                echo "<tr class = 'classItemMath'>";
                    echo "<td class = 'classItemMath' >".substr($key,5,3)."</td>";
                    echo "<td class = 'classItemMath'>$math[$key]</td>";
                    echo "<td class = 'classItemMath' style = 'text-align: center;' 
						id = '" . $key . "Credits'>" . $value . "</td>";
                    echo "<td class = 'classItemMath'><select id = '" . 
						substr($key,0,4)."_".substr($key,5,3). "' class = 'grade' 
						onchange = 'updateGPA(this.id,$arr, $jcheck,$jprereqs,$jcoreqs,$all)'>";
                    	if ( $grade == 4 )
                    		echo "<option value = '4' selected = 'selected'>A</option>";
                    	else
                        	echo "<option value = '4'>A</option>";
                        	
                        if ( $grade == 3 )
	                        echo "<option value = '3' selected = 'selected'>B</option>";
	                    else
                        	echo "<option value = '3'>B</option>";
                        	
                        if ( $grade == 2 )
                        	echo "<option value = '2' selected = 'selected'>C</option>";
                        else
                        	echo "<option value = '2'>C</option>";
                        
                        if ( $grade == 1 )
                        	echo "<option value = '1' selected = 'selected'>D</option>";
                        else
                        	echo "<option value = '1'>D</option>";
                        
                        if ( $grade == 0 )
	                        echo "<option value = '0' selected = 'selected'>F</option>";
	                    else
	                    	echo "<option value = '0'>F</option>";
	                    
	                    if ( $grade == -1 )
	                    	echo "<option value = '-1' selected = 'selected'>Exempt</option>";
	                    else
	                    	echo "<option value = '-1'>Exempt</option>";
	                    
	                    if ( $grade == -2 )
		                    echo "<option value = '-2' selected = 'selected'>Taking</option>";
		                else
                        	echo "<option value = '-2'>Taking</option>";
                        
                        if ( $grade == -3 )
                        	echo "<option value = '-3' selected = 'selected'>Not Taken</option>";
                        else
                        	echo "<option value = '-3'>Not Taken</option>";
                    echo "</td>";

                echo "</tr>";
                }
                 $math_electives_courses_copy=$math_electives_courses;
                $arr=json_encode($math_electives);
                $arr1=json_encode($math_electives_courses);
                $k=0;
                $jtemp="aaa";
                $jgrade=1;
                $enter=false;
                 echo "<tr class = 'classItemMath'>";
                    echo "<td class = 'classItemMath' id='num_math5'></td>";
                   echo "<td class = 'classItemMath'><select id='name_math5' class = 'dropdown' 
						onchange = ' update_course(this.id,$arr,$arr1)' >";
                   echo "<option  selected = 'selected'></option>";
                    foreach($math_electives_courses as $key=>$value)
                    {
                      
                         $temp=substr($key,0,(strpos($key," ")))." ".substr($key,(strpos($key," ")+1),3);
                        $grade=$course_gpa[$temp];
                          $temporary="name_math5";
                          
                        if($grade!=-3 and array_key_exists($key,$math_electives_courses_copy))
                           {
                            echo "<option selected='selected' >".$math_electives[$key]."</option>";
                            unset($math_electives_courses_copy[$key]);
                             $jtemp=json_encode($temporary);
                             $jgrade=json_encode($grade);
                             $enter=true;
                           }
                        else
                        	echo "<option>".$math_electives[$key]."</option>";                    	
                    }
                    echo "< /td>";
                    echo "<td class = 'classItemMath' id='credits_math5' style = 'text-align: center;'></td>";
                    echo "<td class = 'classItemMath'><select disabled id='grade_math5' 
						class = 'grade' onchange = 'updateGPA(this.id,$arr, $jcheck,$jprereqs,$jcoreqs,$all)'>";
                    	if ( $grade == 4 )
                    		echo "<option value = '4' selected = 'selected'>A</option>";
                    	else
                        	echo "<option value = '4'>A</option>";
                        	
                        if ( $grade == 3 )
	                        echo "<option value = '3' selected = 'selected'>B</option>";
	                    else
                        	echo "<option value = '3'>B</option>";
                        	
                        if ( $grade == 2 )
                        	echo "<option value = '2' selected = 'selected'>C</option>";
                        else
                        	echo "<option value = '2'>C</option>";
                        
                        if ( $grade == 1 )
                        	echo "<option value = '1' selected = 'selected'>D</option>";
                        else
                        	echo "<option value = '1'>D</option>";
                        
                        if ( $grade == 0 )
	                        echo "<option value = '0' selected = 'selected'>F</option>";
	                    else
	                    	echo "<option value = '0'>F</option>";
	                    
	                    if ( $grade == -1 )
	                    	echo "<option value = '-1' selected = 'selected'>Exempt</option>";
	                    else
	                    	echo "<option value = '-1'>Exempt</option>";
	                    
	                    if ( $grade == -2 )
		                    echo "<option value = '-2' selected = 'selected'>Taking</option>";
		                else
                        	echo "<option value = '-2'>Taking</option>";
                        
                        if ( $grade == -3 )
                        	echo "<option value = '-3' selected = 'selected'>Not Taken</option>";
                        else
                        	echo "<option value = '-3'>Not Taken</option>";
                    echo "< /td>";
                echo "</tr>";
                if($enter==true)
                {
                echo "<script type='text/javascript'>","update_course($jtemp,$arr,$arr1); ","</script>";
                 echo "<script type='text/javascript'>","get_gpa($jgrade,$jtemp); ","</script>";
                 }
        echo "</table>"; 
        
        //checks the prereqs for the gened courses
         echo "<table class = 'classTable' id = 'GENED'>";
                 echo "<tr  class = 'classItem'>";
                echo "<th style = 'text-align: left;' colpan='10'><input type='button' 
					style='border:none;background-color: white;'  id='buttonGENED'  
					onclick='expand(this.id)' value='-'></input></th>";
                echo "<th style = 'text-align: center;' colspan = '10'>General Education Electives</th>";	
                echo "</tr>";
                echo "<tr class = 'classItemGENED'>";
                echo "<td class = 'classItemGENED' style = 'text-align: center;'>Course #</td>";
                echo "<td class = 'classItemGENED' style = 'text-align: center;'>Name</td>";
                echo "<td class = 'classItemGENED' style = 'text-align: center;'>Credits</td>";
                echo "<td class = 'classItemGENED' style = 'text-align: center;'>Grade</td>";
                $gen_ed_courses_copy=$gen_ed_courses;
                $arr=json_encode($gen_ed);
                $arr1=json_encode($gen_ed_courses);
                $k=0;
                $jtemp="aaa";
                $jgrade=1;
               
                for($i=0;$i<5;$i++)
                {
                 $enter=false;
                echo "<tr class = 'classItemGENED'>";
                    echo "<td class = 'classItemGENED' id='num_gened".$i."'></td>";
                   echo "<td class = 'classItemGENED'><select id='name_gened".$i."' 
						class = 'dropdown' onchange = 'update_course(this.id,$arr,$arr1)' >";
                  
                   echo "<option selected = 'selected'></option>";
                    foreach($gen_ed_courses as $key=>$value)
                    {
                        $temp=substr($key,0,(strpos($key," ")))." ".substr($key,(strpos($key," ")+1),3);
                        $grade=$course_gpa[$temp];
                          $temporary="name_gened".$i;
                          
                        if($grade!=-3 and array_key_exists($key,$gen_ed_courses_copy) and $k==$i)
                           {
                            echo "<option selected='selected' >".$gen_ed[$key]."</option>";
                            unset($gen_ed_courses_copy[$key]);
                             $jtemp=json_encode($temporary);
                             $jgrade=json_encode($grade);
                             $k++;
                                $enter=true;
                           }
                        else
                        	echo "<option>".$gen_ed[$key]."</option>";
                    	
                    }
                    echo "< /td>";
                    echo "<td class = 'classItemGENED' id='credits_gened".$i."' 
						style = 'text-align: center;'></td>";
                    echo "<td class = 'classItemGENED'><select disabled='false' id='grade_gened".$i."' 
						class = 'grade' onchange = 'updateGPA(this.id,$arr, $jcheck,$jprereqs,$jcoreqs,$all)'>";
                    	  if ( $grade == 4 )
                    		echo "<option value = '4' selected = 'selected'>A</option>";
                    	else
                        	echo "<option value = '4'>A</option>";
                        	
                        if ( $grade == 3 )
	                        echo "<option value = '3' selected = 'selected'>B</option>";
	                    else
                        	echo "<option value = '3'>B</option>";
                        	
                        if ( $grade == 2 )
                        	echo "<option value = '2' selected = 'selected'>C</option>";
                        else
                        	echo "<option value = '2'>C</option>";
                        
                        if ( $grade == 1 )
                        	echo "<option value = '1' selected = 'selected'>D</option>";
                        else
                        	echo "<option value = '1'>D</option>";
                        
                        if ( $grade == 0 )
	                        echo "<option value = '0' selected = 'selected'>F</option>";
	                    else
	                    	echo "<option value = '0'>F</option>";
	                    
	                    if ( $grade == -1 )
	                    	echo "<option value = '-1' selected = 'selected'>Exempt</option>";
	                    else
	                    	echo "<option value = '-1'>Exempt</option>";
	                    
	                    if ( $grade == -2 )
		                    echo "<option value = '-2' selected = 'selected'>Taking</option>";
		                else
                        	echo "<option value = '-2'>Taking</option>";
                        
                        if ( $grade == -3 )
                        	echo "<option value = '-3' selected = 'selected'>Not Taken</option>";
                        else
                        	echo "<option value = '-3'>Not Taken</option>";
                    echo "< /td>";
                echo "</tr>";
                if($enter==true)
                {
                echo "<script type='text/javascript'>","update_course($jtemp,$arr,$arr1); ","</script>";
                 echo "<script type='text/javascript'>","get_gpa($jgrade,$jtemp); ","</script>";
                }
                }
        echo "</table>";
        
    echo "<br />";
    $arr=json_encode($csc);
      echo "<script type='text/javascript'>","calculateGPA( $jcheck, $jprereqs,$all ); ","</script>";
?>
