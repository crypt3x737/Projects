package edu.sdsmt.singh_a.riskvariant;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Katherine MacMillan on 9/20/2017.
 */

/**
 * This is the game tile class. Each instance of this class represents one tile in the game
 */
@SuppressWarnings("DefaultFileTemplate")
class GameTile {
    /**
     * the id
     */
    private int id;

    /**
     * x value
     */
    private float x;

    /**
     * y value
     */
    private float y;

    /**
     * length value
     */
    private float length;


    /**
     * Paint for the outline of the tile area
     */
    private Paint outlinePaint;

    /**
     * getter for the id
     */
    public int getId() {
        return id;
    }

    /**
     * setter for the length
     * @param length the length value to be set
     */
    public void setLength(float length) {
        this.length=length;
    }

    /**
     * getter for the length
     */
    public float getLength() {
        return length;
    }

    /**
     * getter for the y value
     */
    public float getY() {
        return y;
    }

    /**
     * getter for the x value
     */
    public float getX() {
        return x;
    }

    /**
     * setter for the x value
     * @param x the x value to be set
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * setter for the y value
     * @param y the y value to be set
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * game tile class constructor
     * @param x the x value
     * @param y the y value
     * @param length the length
     * @param val the value
     */
    GameTile(float x, float y,float length)
    {
        this.setX(x);
        this.setY(y);
        this.setLength(length);
    }

    /**
     * game tile class draw method
     * @param canvas the canvas
     * @param paint the paint object
     */
    public void onDraw(Canvas canvas, Paint paint)
    {
        canvas.drawRect(x-(length/2),y-(length/2),x+(length/2),y+(length/2),paint);
    }
}
