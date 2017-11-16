package edu.sdsmt.singh_a.riskvariant;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Katherine MacMillan on 9/20/2017.
 */

@SuppressWarnings("DefaultFileTemplate")
/**
 * the game class which handles the game and the drawing of the game
 */
class Game {
    private int counter=0;
    /**
     * Percentage of the display screen width
     * or height that is taken up by the game
     * board
     */
    private final float SCALE_IN_VIEW = 0.9f;


    /**
     * Paint filling the area the puzzle occupies
     */
    private Paint fillPaint;

    /**
     * Paint for the outline of the puzzle area
     */
    private Paint outlinePaint;

    /**
     * The size of the game board area
     */
    private int gameBoardSize;

    /**
     * Left margin in pixels
     */
    private int marginX;

    /**
     * Top margin in pixels
     */
    private int marginY;

    /**
     * the canvas
     */
    private Canvas canvas1;

    /**
     * the length
     */
    private float length;

    /**
     * A reference to the game view
     */
    private GameView gameView;

    /**
     * list of tiles
     */
    private ArrayList<GameTile> tiles = new ArrayList<>();

    /**
     * list of players
     */
    ArrayList<Integer> players = new ArrayList<>();

    /**
     * the game constructor
     *
     * @param context  the context
     * @param gameView the game view
     */
    public Game(Context context, GameView gameView) {
        this.gameView = gameView;

        // Create paint for filling the game board area
        fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setColor(0xff1A2C56);

        outlinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        outlinePaint.setStrokeWidth(5);
        outlinePaint.setColor(0xffD1A683);
        int intervalX = marginX / 2;
        int intervalY = marginY / 2;

    }


    public void setCanvas1(Canvas canvas) {
        this.canvas1 = canvas;
    }

    /**
     * the draw function
     *
     * @param canvas the canvas to draw
     */
    public void draw(Canvas canvas) {
        canvas1 = canvas;
        int wid = canvas.getWidth();
        int hit = canvas.getHeight();

        // Determine the smaller of the two dimensions
        int minDim = wid < hit ? wid : hit;

        gameBoardSize = (int) (minDim * SCALE_IN_VIEW);

        // Compute the margins for tile centering
        marginX = (minDim - gameBoardSize) / 2;
        marginY = (minDim - gameBoardSize) / 2;

        // Draw game board outline and area
        //canvas.drawRect(marginX-2, marginY-2, marginX + gameBoardSize+5, marginY + gameBoardSize+5, outlinePaint);
        canvas.drawRect(marginX, marginY, marginX + gameBoardSize, marginY + gameBoardSize, fillPaint);
        length = (gameBoardSize) / 10;
        gameView.setLength(length);
        gameView.setX(marginX + (length / 2) + (5 * length));
        gameView.setY(marginY + (length / 2) + (5 * length));

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (players.get((i * 10) + j).intValue() == 0) {
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setStrokeWidth(5);
                    paint.setColor(0xffD1A683);
                } else if (players.get((i * 10) + j).intValue() == 1) {
                    paint.setColor(0xffD1A683);
                    paint.setStyle(Paint.Style.FILL);
                } else if (players.get((i * 10) + j).intValue() == 2) {
                    paint.setColor(Color.RED);
                    paint.setStyle(Paint.Style.FILL);
                }
                tiles.add(new GameTile(marginX + (length / 2) + (i * length), marginY + (length / 2) + (j * length), length));
                tiles.get((i * 10) + j).onDraw(canvas, paint);
            }
        }
    }

    /**
     * the draw function
     *
     * @param players the integer array to hold tiles
     */
    public void setPlayers(ArrayList<Integer> players) {
        this.players = new ArrayList<>(players);
    }

    /**
     * fill a tile with the players color
     *
     * @param cap_method  the capture method
     * @param player_info the player info
     */
    public void fill(int cap_method, int player_info) {
        try {
            counter++;
            if (counter == 1) {
                Random rand = new Random();

                // check which capture method it is
                if (cap_method == 0) {
                    int k = rand.nextInt(99);
                    while (player_info == players.get(k).intValue()) {
                        k = rand.nextInt();
                    }
                    players.set(k, player_info);
                    gameView.setPlayers(players);
                    draw(canvas1);

                } else if (cap_method == 1) {
                    int k = rand.nextInt(99);
                    int t = rand.nextInt(7);
                    int c = rand.nextInt(10);
                    if (c >= 6) {
                        if (t == 0) {
                            while (k - 11 < 0)
                                k = rand.nextInt();

                            players.set(k, player_info);
                            players.set(k - 11, player_info);


                        } else if (t == 1) {
                            while (k - 10 < 0)
                                k = rand.nextInt();

                            players.set(k, player_info);
                            players.set(k - 10, player_info);

                        } else if (t == 2) {
                            while (k - 9 < 0)
                                k = rand.nextInt();

                            players.set(k, player_info);
                            players.set(k - 9, player_info);

                        } else if (t == 3) {
                            while (k - 1 < 0)
                                k = rand.nextInt();

                            players.set(k, player_info);
                            players.set(k - 1, player_info);

                        } else if (t == 4) {
                            while (k + 1 > 97)
                                k = rand.nextInt();

                            players.set(k, player_info);
                            players.set(k + 1, player_info);

                        } else if (t == 5) {
                            while (k + 9 > 99)
                                k = rand.nextInt();

                            players.set(k, player_info);
                            players.set(k + 9, player_info);

                        } else if (t == 6) {
                            while (k + 10 > 99)
                                k = rand.nextInt();

                            players.set(k, player_info);
                            players.set(k + 10, player_info);

                        } else if (t == 7) {
                            while (k + 11 > 99)
                                k = rand.nextInt();

                            players.set(k, player_info);
                            players.set(k + 11, player_info);

                        }
                    }
                    gameView.setPlayers(players);
                    draw(canvas1);

                } else if (cap_method == 2) {
                    int l = rand.nextInt(10) + 1;
                    int b = rand.nextInt(10) + 1;
                    int power = (int) (Math.log(l * b) / Math.log(2));
                    int x = rand.nextInt((int) (Math.pow(2, power)) + 1);
                    if (x <= 2) {
                        int start, end;
                        if (l != 10)
                            start = rand.nextInt(9 - l);
                        else
                            start = 0;
                        if (b != 0)
                            end = rand.nextInt(9 - b);
                        else
                            end = 0;
                        for (int i = start; i < start + l; i++) {
                            for (int j = end; j < end + b; j++) {
                                players.set(i + (j * 10), player_info);
                            }
                        }
                    }
                    gameView.setPlayers(players);
                    draw(canvas1);
                }
            }
        }
        catch(Exception ex)
        {

        }
    }


    /**
     * Save the game to a bundle
     *
     * @param bundle The bundle we save to
     */
    public void saveInstanceState(Bundle bundle) {
        bundle.putIntegerArrayList("players", players);
        bundle.putInt("marginX", marginX);
        bundle.putInt("marginY", marginY);
        bundle.putFloat("length", length);
  //      bundle.putParcelable("canvas", (Parcelable) canvas1);

    }

    /**
     * Read the game from a bundle
     *
     * @param bundle The bundle we save to
     */
    public void loadInstanceState(Bundle bundle) {
        counter=5;
        players = bundle.getIntegerArrayList("players");
        marginX = bundle.getInt("marginX");
        marginY = bundle.getInt("marginY");
        length = bundle.getFloat("length");
        gameView.setPlayers(players);
        gameView.invalidate();
    }
}
