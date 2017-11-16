package edu.sdsmt.singh_a.riskvariant;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;


/**
 * Custom view class for our Game
 */
public class GameView extends View {
    /**
     * The list of player tiles
     */
    ArrayList<Integer> players=new ArrayList<>();

    /**
     * The game activity
     */
    private GameActivity gameActivity;

    /**
     * The game
     */
    private Game game;

    /**
     * The capture method
     */
    private int cap_method=0;

    /**
     * x value
     */
    private float x;

    /**
     * y value
     */
    private float y;

    /**
     * The length
     */
    private float length;

    /**
     * player infot
     */
    private int player_info;

    /**
     * The paint object for drawing
     */
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * a constructor for the game view class
     * @param context the context
     */
    public GameView(Context context) {
        super(context);
        init(null, 0);
    }

    /**
     * a constructor for the game view class
     * @param context the context
     * @param  attrs the attributes
     */
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    /**
     * instance function to set the instance of the game activity
     * @param gameActivity the game activity to be set
     */
    public void instance(GameActivity gameActivity) {
        this.gameActivity=gameActivity;
    }

    /**
     * a constructor for the game view class
     * @param context the context
     * @param attrs the attributes
     * @param defStyle the defStyle
     */
    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    /**
     * set the player information variable
     * @param player_info the player info to be set
     */
    public void setplayer_info(int player_info) {
        this.player_info=player_info;
    }

    /**
     * get the player information
     */
    public int getplayer_info() {
        return player_info;
    }

    /**
     * set the players array list
     * @param players the players array to be set
     */
    public void setPlayers(ArrayList<Integer> players) {
        this.players= new ArrayList<>(players);
    }


    /**
     * the init function to initialize the game class
     * @param attrs the attributes
     * @param defStyle the defStyle
     */
    private void init(AttributeSet attrs, int defStyle) {
        game = new Game(getContext(), this);
    }

    /**
     * the on draw event to draw the game
     * @param canvas the canvas to draw
     */
    public void onDraw(Canvas canvas) {
        game.setPlayers(players);
        super.onDraw(canvas);
        game.draw(canvas);

        switch(cap_method) {
            case 0:
                game.fill(cap_method,player_info);
                gameActivity.setPlayers(players);
                //canvas.drawCircle(x-length/2, y-length/2, length/4,paint);
                break;
            case 1:
                game.fill(cap_method,player_info);
                gameActivity.setPlayers(players);
                //canvas.drawLine(x-length/2, y-length/2,(float)(length*3.5),(float)(length*3.5),paint);
                break;
            case 2:
                game.fill(cap_method,player_info);
                gameActivity.setPlayers(players);
                //canvas.drawRect(x-length/2, y-length/2,x+length/2,y+length/2,paint);
                break;
        }

    }

    /**
     * set the capture method field
     * @param cap the capture method integer
     */
    public void set_cap_method(int cap){
        this.cap_method=cap;
    }

    /**
     * get the capture method
     */
    public double get_cap_method(){
        return this.cap_method;
    }

    /**
     * set the length field
     * @param length the length value to set it to
     */
    public void setLength(float length) {
        this.length=length;
    }

    /**
     * get the length field
     */
    public float getLength() {
        return length;
    }

    /**
     * get the y value
     */
    public float getY() {
        return y;
    }

    /**
     * get the x value
     */
    public float getX() {
        return x;
    }

    /**
     * set the x value
     * @param x the x value to be set
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * set the x value
     * @param y the y value to be set
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * Save the game to a bundle
     * @param bundle The bundle we save to
     */
    public void saveInstanceState(Bundle bundle) {
        game.saveInstanceState(bundle);
    }

    /**
     * Load the game from a bundle
     * @param bundle The bundle we save to
     */
    public void loadInstanceState(Bundle bundle) {
        game.loadInstanceState(bundle);
        gameActivity.setPlayers(players);
    }
    /**
     * get the game field
     */
    public Game getGame() {
        return game;
    }
}