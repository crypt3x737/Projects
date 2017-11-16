/******************************************************************************
*	File: positionTables.js
*
*	Author: Kenneth Petry
*
*	Description: This file displays the course tables formatting them for the
				number of classes in the section. 
******************************************************************************/
window.addEventListener( "resize", positionTables);
positionTables();

function positionTables()
{
	var tbls = document.getElementsByClassName( 'classTable' );
	var i;
	var tblsPerRow = 4;

	var rowHeight = -1;
	var heightOffset = 300;
	var baseOffset = 10;
	var leftOffset = baseOffset;

    // for all the tables
	for ( i = 0; i < tbls.length; i++ )
	{	
        // determine if the table needs to go to the next row
		if ( leftOffset + (tbls[i].offsetWidth + baseOffset) >= window.innerWidth )
		{
			heightOffset += rowHeight + 100;
			leftOffset = baseOffset;
			rowHeight = -1;
		}
	    
        // get the hight of the longest table in the row
		if ( rowHeight < tbls[i].offsetHeight )
		{
			rowHeight = tbls[i].offsetHeight;
		}
	    
        // position tables
		tbls[i].style.position = "absolute";
		tbls[i].style.top = heightOffset + "px";
		tbls[i].style.left = leftOffset + "px";
	
        // move the pointer over for the next table
		leftOffset += (tbls[i].offsetWidth + baseOffset);
	}
}
