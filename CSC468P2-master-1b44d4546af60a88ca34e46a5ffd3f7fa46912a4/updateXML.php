<?php
/******************************************************************************
*	File: updateXML.php
*
*	Author: Kenneth Petry
*
*	Description: This file updates the user's xml file when a change is made to
				the course selection
******************************************************************************/
    $path = "xml/" . $_POST['uname'] . ".xml";
    $xml = simplexml_load_file( $path );
    foreach ( $xml->course as $c )
    {
        if ( $c->prefix == $_POST['prefix'] && $c->number == $_POST['number'] )
        {
            $c->grade = $_POST['grade'];
            $xml->saveXML( $path );
        }
    }

?>
