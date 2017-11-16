package edu.sdsmt.akshaycheldonleif.csc476project1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;


/**
 * Custom view class for our game board.
 *
 * Code taken from tutorial 2
 */
public class BoardView extends View {

    public ArrayList<region> regions = new ArrayList<region>();

    static final int numCol = 5;
    static final int numRows = 5;

    float dimensions = 0;
    private final static String DIMENSION = "BoardView.dimensions";
    private final static String LOCATIONS = "region.dimensions";
    private final static String IDS = "region.ids";
    private final static String OWNERS = "region.ownership";
    private final static String COLORS = "region.myColor";
    private final static String CAPTURE_OPTION ="BoardView.captureOption";
    private final static String CAPTURE_X = "BoardView.captureX";
    private final static String CAPTURE_Y = "BoardView.captureY";
    private final static String LINE_ROTATION = "BoardView.lineRotation";
    private final static String BOX_WIDTH = "BoardView.boxWidth";
    private final static String BOX_HEIGHT = "BoardView.boxHeight";

    private int captureOption = -1;
    private static final int dotRadius = 30;

    private float captureX = -1;
    private float captureY = -1;

    private Paint fillPaint;

    private boolean movingCapture = false;

    private int marginX;
    private int marginY;

    private boolean lineRotation = false;
    private final static float lineWidth = 5;
    private final static float lineError = 20;

    private float boxWidth = -1;
    private float boxHeight = -1;

    private int [] location = new int[2];

    Random rand = new Random();

    public BoardView(Context context) {
        super(context);
        init(null, 0);
    }

    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public BoardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    /**
     * Sets the option for capture the player chose.
     * @param option the capture option chosen.
     */
    public void setCaptureOption(int option)
    {
        captureOption = option;
    }


    private void init(AttributeSet attrs, int defStyle) {


        for(int j = 0; j < numRows; j++){
            for (int i = 0; i < numCol; i++) {
                regions.add(new region(numCol * j + i, constants.noPlayer, Color.YELLOW));
            }
        }


        fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int wid = canvas.getWidth();
        int hit = canvas.getHeight();

        // Determine the minimum of the two dimensions
        dimensions = wid < hit ? wid : hit;

        if (captureX < 0 || captureY < 0)
        {
            captureX = wid / 2;
            captureY = hit / 2;
        }



        this.getLocationOnScreen(location);
        marginX = location[0];
        marginY = location[1];

        float regionHeight = dimensions / (float)(numRows);
        float regionWidth = dimensions / (float)(numCol);

        for (int i = 0; i < numRows * numCol; i++) {
            regions.get(i).left = (regionWidth * (i % 5)) + 5;
            regions.get(i).top = (regionHeight * (i / 5)) + 5;
            regions.get(i).right = (regionWidth * ((i % 5) + 1)) - 5;
            regions.get(i).bot = (regionHeight * ((i / 5) + 1)) - 5;
            fillPaint.setColor(regions.get(i).myColor);
            canvas.drawRect(regions.get(i).left, regions.get(i).top, regions.get(i).right, regions.get(i).bot,  fillPaint );
        }

        if (captureOption == constants.dotSelected)
        {
            fillPaint.setColor(Color.BLACK);
            canvas.drawCircle(captureX, captureY, dotRadius, fillPaint);
        }
        else if (captureOption == constants.lineSelected)
        {
            fillPaint.setColor(Color.BLACK);
            fillPaint.setStrokeWidth(lineWidth);
            if (lineRotation)
            {
                canvas.drawLine(0, captureY, wid, captureY, fillPaint);
            }
            else
            {
                canvas.drawLine(captureX, 0, captureX, hit, fillPaint);
            }
        }
        else if (captureOption == constants.boxSelected)
        {
            fillPaint.setColor(Color.BLACK);
            fillPaint.setStrokeWidth(lineWidth);

            if (boxHeight < 0 || boxWidth < 0)
            {
                boxHeight = hit / 4;
                boxWidth = wid / 4;
            }

            canvas.drawRect(captureX - (boxWidth / 2), captureY - (boxHeight / 2), captureX + (boxWidth / 2), captureY + (boxHeight / 2), fillPaint);
        }
    }

    /**
     * Saves the state of the game.  Mainly has the board save its state
     * The rest can be found from the intent again.
     * @param savedInstanceState Bundle to save the state into.
     */
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        float [] locations = new float[regions.size() * 4];
        int [] ids = new int[regions.size()];
        int [] owners = new int[regions.size()];
        int [] colors = new int[regions.size()];

        for(int i=0;  i<regions.size(); i++) {
            region Region = regions.get(i);
            locations[i*4] = Region.left;
            locations[i*4+1] = Region.top;
            locations[i*4+2] = Region.right;
            locations[i*4+3] = Region.bot;
            ids[i] = Region.ID;
            owners[i] = Region.ownership;
            colors[i] = Region.myColor;
        }

        savedInstanceState.putFloat(DIMENSION, dimensions);
        savedInstanceState.putFloatArray(LOCATIONS, locations);
        savedInstanceState.putIntArray(IDS, ids);
        savedInstanceState.putIntArray(OWNERS, owners);
        savedInstanceState.putIntArray(COLORS, colors);

        savedInstanceState.putInt(CAPTURE_OPTION, captureOption);
        savedInstanceState.putFloat(CAPTURE_X, captureX);
        savedInstanceState.putFloat(CAPTURE_Y, captureY);
        savedInstanceState.putBoolean(LINE_ROTATION, lineRotation);
        savedInstanceState.putFloat(BOX_HEIGHT, boxHeight);
        savedInstanceState.putFloat(BOX_WIDTH, boxWidth);
    }

    /**
     * decides what action to take based on type of motion event
     * @param currentPlayer the player whose turn it is.
     * @param event THe motion even to handle
     */
    public boolean onTouchEvent(int currentPlayer, MotionEvent event) {
        switch (event.getActionMasked()) {

            case MotionEvent.ACTION_DOWN:
                touchingCapture(event.getX() - marginX, event.getY() - marginY);
                return true;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                movingCapture = false;
                return true;

            case MotionEvent.ACTION_MOVE:
                onTouched(event.getX() - marginX,event.getY() - marginY, currentPlayer);
                return true;
        }
        return super.onTouchEvent(event);
    }

    /**
     * Loads the state of the game.  Mainly has the board load its state
     * @param savedInstanceState Bundle to load the state from.
     */
    public void loadInstanceState(Bundle savedInstanceState) {

        float [] locations = savedInstanceState.getFloatArray(LOCATIONS);
        int [] ids = savedInstanceState.getIntArray(IDS);
        int [] owners = savedInstanceState.getIntArray(OWNERS);
        int [] colors = savedInstanceState.getIntArray(COLORS);
        dimensions = savedInstanceState.getFloat(DIMENSION);

        for(int i=0;  i<regions.size(); i++) {
            region Region = regions.get(i);
            Region.left = locations[i*4];
            Region.top = locations[i*4+1];
            Region.right = locations[i*4+2];
            Region.bot = locations[i*4+3];
            Region.ID = ids[i];
            Region.ownership = owners[i];
            Region.myColor = colors[i];
        }

        captureOption = savedInstanceState.getInt(CAPTURE_OPTION);
        captureX = savedInstanceState.getFloat(CAPTURE_X);
        captureY = savedInstanceState.getFloat(CAPTURE_Y);
        lineRotation = savedInstanceState.getBoolean(LINE_ROTATION);
        boxHeight = savedInstanceState.getFloat(BOX_HEIGHT);
        boxWidth = savedInstanceState.getFloat(BOX_WIDTH);
    }

    /**
     * Sets the motion event coordinates for capture detection
     * and moving of the capture method.
     * @param x the x position of the motion event.
     * @param y the y position of the motion event.
     * @param currentPlayer the current player.
     */
    private boolean onTouched(float x, float y, int currentPlayer) {


        if (movingCapture)
        {
            if (captureOption == constants.dotSelected)
            {
                captureX = x;
                captureY = y;
            }
            else if (captureOption == constants.lineSelected)
            {
                if (lineRotation)
                {
                    captureY = y;
                }
                else
                {
                    captureX = x;
                }
            }
            else if (captureOption == constants.boxSelected)
            {
                captureX = x;
                captureY = y;
            }

            invalidate();
            return true;
        }

        return false;
    }

    /**
     * Handles the initial touch when a move event is happening.
     * @param x the x position of the event.
     * @param y the y position of the event.
     */
    private boolean touchingCapture(float x, float y)
    {
        if (captureOption == constants.dotSelected)
        {
            double dist = Math.sqrt((y - captureY) * (y-captureY) + (x - captureX) * (x - captureX));

            if ((double) dotRadius >= dist)
            {
                movingCapture = true;
                return true;
            }
        }
        else if (captureOption == constants.lineSelected)
        {
            if (lineRotation)
            {
                if ( (captureY - ((lineWidth + lineError) / 2) < y && (captureY + ((lineWidth + lineError) / 2)) > y))
                {
                    movingCapture = true;
                    return true;
                }
            }
            else
            {
                if ( (captureX - ((lineWidth + lineError) / 2) < x && (captureX + ((lineWidth + lineError) / 2)) > x))
                {
                    movingCapture = true;
                    return true;
                }
            }

        }
        else if (captureOption == constants.boxSelected)
        {
            if (captureX - (boxWidth / 2) < x && captureX + (boxWidth / 2) > x && captureY - (boxHeight / 2) < y && captureY + (boxHeight / 2) > y)
            {
                movingCapture = true;
                return true;
            }
        }

        return false;
    }

    /**
     * rotates the capture line 90 degrees
     */
    public void RotateLine()
    {
        lineRotation = !lineRotation;
        invalidate();
    }

    /**
     * Increases or decreases the x size of the box used for capture.
     * @param val the ammount to change.
     */
    public void ChangeBoxWidth(float val)
    {
        boxWidth += val;
        invalidate();
    }

    /**
     * Increases or decreases the y size of the box used for capture.
     * @param val the ammount to change.
     */
    public void ChangeBoxHeight(float val)
    {
        boxHeight += val;
        invalidate();
    }

    /**
     * Uses the position of the shape used for capture to check if a region is captured.
     * If captured, the region's color and ownership values are changed.
     * @param playerTurn the player whose turn it is.
     * @param capOp the capture type chosen.
     */
    public void checkCapture(int playerTurn, int capOp)
    {
        for(int i = 0; i < regions.size(); i++)
        {
            region temp = regions.get(i);
            if(capOp == 0)
            {
                if(captureX < temp.right && captureX > temp.left)
                {
                    if(captureY < temp.bot && captureY > temp.top)
                    {
                        temp.ownership = playerTurn;
                        if(playerTurn == 1)
                        {
                            temp.myColor = Color.RED;
                        }
                        else
                        {
                            temp.myColor = Color.BLUE;
                        }
                    }
                }
            }
            else if (capOp == 1)
            {
                if(lineRotation)
                {
                    if (captureY < temp.bot && captureY > temp.top)
                    {
                        if(rand.nextInt(100) + 1 > 50)
                        {
                            temp.ownership = playerTurn;

                            if (playerTurn == 1)
                            {
                                temp.myColor = Color.RED;
                            } else
                            {
                                temp.myColor = Color.BLUE;
                            }
                        }
                    }
                }
                else
                {
                    if (captureX < temp.right && captureX > temp.left)
                    {
                        if((rand.nextInt(100) + 1) > 50)
                        {
                            temp.ownership = playerTurn;

                            if (playerTurn == 1)
                            {
                                temp.myColor = Color.RED;
                            } else
                            {
                                temp.myColor = Color.BLUE;
                            }
                        }
                    }
                }
            }
            else
            {
                float boxLeft = captureX - boxWidth/2;
                float boxRight = captureX + boxWidth/2;
                float boxTop = captureX - boxHeight/2;
                float boxBot = captureX + boxHeight/2;

                if((boxLeft < temp.right && boxTop < temp.bot) || (boxRight > temp.left && boxBot > temp.top))
                {
                    if(rand.nextInt(100)+1 > 75)
                    {
                        temp.ownership = playerTurn;
                        if(playerTurn == 1)
                        {
                            temp.myColor = Color.RED;
                        }
                        else
                        {
                            temp.myColor = Color.BLUE;
                        }
                    }
                }
            }
        }
    }

}