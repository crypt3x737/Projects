<?php
/******************************************************************************
*	File: courseObj.php
*
*	Author: Kenneth Petry
*
*	Description: This file contains the class definition for a course.  It was
*       used mainly in early versions of the program, but is still somewhat
*       present in the final version.
*
******************************************************************************/
class course
{
    public $isactive;
    public $prefix;
    public $number;
    public $name;
    public $credits;
    public $offered;
    public $description;
    public $prereq;
    public $coreq;
    public $notes;
	public $grade;

    // constructor for the class that takez a course from the simple xml parser 
    //  and makes it into a class object	    
    function __construct( $c )
    {
        $this->isactive = $c['isactive'];
        $this->prefix = (string) $c->prefix;
        $this->number = (int) $c->number;
        $this->name = (string) $c->name;
        $this->credits = (int) $c->credits;
        $this->offered = $c->offered;
        $this->description = $c->description;
        $this->prereq = $c->prereq;
        $this->coreq = $c->coreq;
        $this->notes = $c->notes;
        $this->grade = -3;
    }

}
?>
