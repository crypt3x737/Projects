<?php
/******************************************************************************
*	File: createUserFile.php
*
*	Author: Jack Westlund
*	Modified by: Akshay Singh
*
*	Functions Included: createUserFile($userID, $courses, $gen_ed_courses, 
*											$science_courses)
*	Description: This file generates a blank xml file for new users. It also uses
*					chmod to give everyone access to the file once created.
******************************************************************************/
	function createUserFile( $userID, $courses,$gen_ed_courses,$science_courses )
	{
		$xmlDoc = new DOMDocument();
		$xmlDoc->formatOutput = true;
		//create root node
		$root = $xmlDoc->createElement('usr');
		$root = $xmlDoc->appendChild($root);
		foreach ($courses as $cor)
		{	//add course node
			$course = $xmlDoc->createElement('course');
			$course = $root->appendChild($course);
			//add prefix
			$prefix = $xmlDoc->createElement('prefix');
			$prefix = $course->appendChild($prefix);
			$text = $xmlDoc->createTextNode($cor->prefix);
			$text = $prefix->appendChild($text);
			//add course number
			$num = $xmlDoc->createElement('number');
			$num = $course->appendChild($num);
			$text = $xmlDoc->createTextNode($cor->number);
			$text = $num->appendChild($text);
			//add grade
			$grade = $xmlDoc->createElement('grade');
			$grade = $course->appendChild($grade);
			$text = $xmlDoc->createTextNode($cor->grade);
			$text = $grade->appendChild($text);
			
		}
		
        $course = $xmlDoc->createElement('course');
			$course = $root->appendChild($course);

			$prefix = $xmlDoc->createElement('prefix');
			$prefix = $course->appendChild($prefix);
			$text = $xmlDoc->createTextNode('CP');
			$text = $prefix->appendChild($text);

			$num = $xmlDoc->createElement('number');
			$num = $course->appendChild($num);
			$text = $xmlDoc->createTextNode(497);
			$text = $num->appendChild($text);

			$grade = $xmlDoc->createElement('grade');
			$grade = $course->appendChild($grade);
			$text = $xmlDoc->createTextNode(-3);
			$text = $grade->appendChild($text);
		
		foreach ($gen_ed_courses as $key=>$value)
		{
			$course = $xmlDoc->createElement('course');
			$course = $root->appendChild($course);

			$prefix = $xmlDoc->createElement('prefix');
			$prefix = $course->appendChild($prefix);
			$text = $xmlDoc->createTextNode(substr($key,0,strpos($key," ")));
			$text = $prefix->appendChild($text);

			$num = $xmlDoc->createElement('number');
			$num = $course->appendChild($num);
			$text = $xmlDoc->createTextNode(substr($key,strpos($key," ")+1,3));
			$text = $num->appendChild($text);

			$grade = $xmlDoc->createElement('grade');
			$grade = $course->appendChild($grade);
			$text = $xmlDoc->createTextNode(-3);
			$text = $grade->appendChild($text);
			
		}
		
		foreach ($science_courses as $key=>$value)
		{
			$course = $xmlDoc->createElement('course');
			$course = $root->appendChild($course);

			$prefix = $xmlDoc->createElement('prefix');
			$prefix = $course->appendChild($prefix);
			$text = $xmlDoc->createTextNode(substr($key,0,strpos($key," ")));
			$text = $prefix->appendChild($text);

			$num = $xmlDoc->createElement('number');
			$num = $course->appendChild($num);
			$text = $xmlDoc->createTextNode(substr($key,strpos($key," ")
									+1,strlen($key)-strpos($key," ")));
			$text = $num->appendChild($text);

			$grade = $xmlDoc->createElement('grade');
			$grade = $course->appendChild($grade);
			$text = $xmlDoc->createTextNode(-3);
			$text = $grade->appendChild($text);
			
	    }

        chmod ("xml/", 0777); //doesnt work
		$xmlDoc->save("xml/" . $userID . ".xml");
        chmod ( "xml/" . $userID . ".xml", 0777 );

		return ;
	}
?>
