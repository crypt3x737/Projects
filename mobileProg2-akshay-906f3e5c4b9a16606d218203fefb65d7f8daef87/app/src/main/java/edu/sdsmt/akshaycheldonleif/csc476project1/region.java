package edu.sdsmt.akshaycheldonleif.csc476project1;

import android.graphics.Color;

/**
 * Holds data for the regions in the game board
 */

public class region {


    float left = 0;
    float top = 0;
    float bot = 0;
    float right = 0;

    int ownership = 0;
    int ID = 0;

    int myColor = Color.YELLOW;


    public region(int id, int owner, int startColor){

        ID = id;
        ownership = owner;
        myColor = startColor;
    }

}
