<!--
	File: classDisplay.php

	Author: Kenneth Petry

	Description: This file contains the html code to draw the top section of the
				class display page. This includes the gpa, credits, and progress.
				positionTables.js is then called to draw the tables.
-->
<!DOCTYPE html>

<html lang = "en">
	<head>
		<title>View Classes</title>
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
		<link rel = "stylesheet" type = "text/css" href = "styles.css" />		
        <script type = "text/javascript" src = "calculateGPA.js"></script>
        <script type = "text/javascript" src = "expand.js"></script>
        <script type = "text/javascript" src = "update_course.js"></script>

        <style>
            #progressTotal
            {
                width: 100%;
                background-color: #ddd;
            }

            #pbar
            {
                width: 10%;
                height: 30px;
                background-color: green;
                text-align: center;
                color: white;
            }
        </style>
	</head>

	<body>
		
		<p class = "header">
			<br />
			Courses for
			<?php echo ( $_POST['uname'] );	?>
            <br />
			<br />
		</p>
        <form action = "classDisplay.php" method = "post">
            <?php echo "<input type = 'hidden' id = 'uname' name = 'uname' value = ". $_POST['uname'] . ">"; ?>
            <input type = "hidden" name = "saveFile" value = "true">
            
		    <p class = "headerInfo">
			    <br />
			    <label class = "headerInfoItem"> GPA: <input type = "text" id = "gpa" disabled = "disabled"></label>			                   
                <label class = "headerInfoItem"> In Progress: <input type = "text" id = "inProgress" disabled = "disabled"></label>              
                <br /> <br /> 
                <label class = "headerInfoItem"> Completed Credits: <input type = "text" id = "completeCredits" disabled = "disabled"></label>
		    </p>
		    <!--div style = "text-align: center;" class = "saveButton">
		        <br />
		    </div-->

		    <p class = "headerInfo">
			    <br />
	    	</p>
		
		    <br /> 
            <p>Overall Progress (out of 120 credits)</p>
            <div id = "progressTotal">
                <div id = "pbar"></div>
            </div>
        <!-- Read in and display the tables of data -->
            <div name = "tableSection">
                <?php include 'parseXML.php'; ?>                            
                
                <script type = "text/javascript" src = "positionTables.js"></script>
                <script type = "text/javascript">calculateGPA()</script>
            </div>
        	
        </form>
		<!-- <p class = "footer">
			<br />
			<br />
		</p> -->
	
	</body>
</html>
