package edu.sdsmt.akshaycheldonleif.csc476project1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Class for the board activity. Handles options for the capture options and has a boardView for
 * displaying and interacting with the board.
 */
public class Board extends AppCompatActivity
{
    private Board board;

    /**
     * The puzzle view in this activity's view
     */
    private BoardView boardView;

    /**
     * TextView for the name of the current player
     */
    private TextView playerNameText;

    /**
     * TextView to display the number of rounds left
     */
    private TextView roundsLeftText;

    /**
     * Button for rotating the line
     */
    private Button lineRotate;

    /**
     * Button to increase the length of the box
     */
    private Button boxLengthPlus;

    /**
     * Button to decrease the length of the box
     */
    private Button boxLengthMinus;

    /**
     * Button to increase the width of the box
     */
    private Button boxWidthPlus;

    /**
     * Button to decrease the width of the box
     */
    private Button boxWidthMinus;

    /**
     * Value for the current player
     * Values defined in the constants class
     */
    private int currentPlayer;

    private int currentRound;


    /**
     * Number of rounds left in the game
     */
    private int roundsLeft;

    /**
     * String to store the number of rounds left
     */
    private String roundsLeftString;

    /**
     * Name of player 1
     */
    private String player1Name;

    /**
     * Name of player 2
     */
    private String player2Name;

    /**
     * Option for capture specified by the user
     * Option values defined in the constants class
     */
    private int captureOption;

    private static int playerID;

    private static final float boxIncrement = 10;

    DatabaseReference game= FirebaseDatabase.getInstance().getReference().child("Games").child("Game");

    /**
     * Sets all the values needed by the class.  Gets view references, extracts data from the intent,
     * an reloads the board if needed.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_board);

        playerID=this.getIntent().getIntExtra("playerID",-1);
        currentPlayer = this.getIntent().getIntExtra(constants.currentPlayer, -1);
        currentRound= this.getIntent().getIntExtra("currentRound", -1);
        roundsLeft =3-currentRound/2;

        if (player1Name == null)
            player1Name = this.getIntent().getStringExtra(constants.player1Name);

        if (player2Name == null)
            player2Name = this.getIntent().getStringExtra(constants.player2Name);

        if (playerNameText == null)
            playerNameText = (TextView) this.findViewById(R.id.playerName);

        if (roundsLeftText == null)
            roundsLeftText = (TextView) this.findViewById(R.id.roundsLeft);

        if (roundsLeftString == null)
            roundsLeftString = Integer.toString(roundsLeft);

        roundsLeftText.setText(roundsLeftString);

        if (currentPlayer == constants.player1)
        {
            playerNameText.setText(player1Name);
        }
        else
        {
            playerNameText.setText(player2Name);
        }

        if (boardView == null)
        {
            boardView = (BoardView) this.findViewById(R.id.boardView);
        }

        boardView.setRegions(getIntent().getIntegerArrayListExtra("board"));

        if(savedInstanceState != null) {
            // We have saved state
            boardView.loadInstanceState(savedInstanceState);
        }

        captureOption = this.getIntent().getIntExtra(constants.captureOption, -1);
        if (captureOption == constants.dotSelected)
        {
            DisableBoxButtons();
            DisableLineButtons();
            boardView.setCaptureOption(constants.dotSelected);
        }
        else if (captureOption == constants.lineSelected)
        {
            DisableBoxButtons();
            boardView.setCaptureOption(constants.lineSelected);
        }
        else if (captureOption == constants.boxSelected)
        {
            DisableLineButtons();
            boardView.setCaptureOption(constants.boxSelected);
        }




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.exit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuExit) {
            constants.clearFireBase();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            return true;
        }
        return false;
    }

    /**
     * Gets the views for the buttons to control how the box resizing works.
     * Saves the references into class variables
     */
    private void GetBoxButtons()
    {
        if (boxLengthMinus == null)
        {
            boxLengthMinus = (Button) this.findViewById(R.id.decreaseBoxLength);
        }

        if (boxLengthPlus == null)
        {
            boxLengthPlus = (Button) this.findViewById(R.id.increaseBoxLength);
        }

        if (boxWidthMinus == null)
        {
            boxWidthMinus = (Button) this.findViewById(R.id.decreaseBoxWidth);
        }

        if (boxWidthPlus == null)
        {
            boxWidthPlus = (Button) this.findViewById(R.id.increaseBoxWidth);
        }
    }

    /**
     * Disables the buttons that control the box size
     */
    private void DisableBoxButtons()
    {
        GetBoxButtons();

        boxLengthMinus.setEnabled(false);
        boxLengthPlus.setEnabled(false);
        boxWidthPlus.setEnabled(false);
        boxWidthMinus.setEnabled(false);
    }

    /**
     *  Gets the button to rotate the lien
     */
    private void GetLineButtons()
    {
        if (lineRotate == null)
        {
            lineRotate = (Button) this.findViewById(R.id.rotateLine);
        }
    }

    /**
     *  Disables the button to rotate the line
     */
    private void DisableLineButtons()
    {
        GetLineButtons();

        lineRotate.setEnabled(false);
    }


    /**
     * Performs the rotation of the line
     * @param view needed for callback
     */
    public void rotateLine(View view)
    {
        boardView.RotateLine();
    }

    /**
     * Increases the width of the box
     * @param view needed for callback
     */
    public void increaseBoxWidth(View view)
    {
        boardView.ChangeBoxWidth(boxIncrement);
    }

    /**
     * Decreases the width of the box
     * @param view needed for callback
     */
    public void decreaseBoxWidth(View view)
    {
        boardView.ChangeBoxWidth(-1 * boxIncrement);
    }

    /**
     * Increases the length of the box
     * @param view needed for callback
     */
    public void increaseBoxLength(View view)
    {
        boardView.ChangeBoxHeight(boxIncrement);
    }

    /**
     * Decreases the length of the box
     * @param view needed for callback
     */
    public void decreaseBoxLength(View view)
    {
        boardView.ChangeBoxHeight(-1 * boxIncrement);
    }

    /**
     * Called when the user clicks the OK button to finish their turn.
     * Switches the current player when sending the information to the next activity.
     * Decreases the number of rounds left if the next current player is player 1.
     * @param view needed for callback
     */
    public void completeTurn(View view)
    {

        if (currentPlayer == constants.player1)
        {
            currentPlayer = constants.player2;
            boardView.checkCapture(constants.player1, captureOption);
        }
        else if (currentPlayer == constants.player2)
        {
            currentPlayer = constants.player1;
            roundsLeft--;
            boardView.checkCapture(constants.player2, captureOption);
        }

        //finish();
        game.child("CurrentPlayer").setValue(Integer.toString(currentPlayer));
        game.child("CurrentRound").setValue(Integer.toString(currentRound+1));



        if (roundsLeft <= 0)
        {
            //****** GET WINNING PLAYER NUMBER
            int p1Count = 0;
            int p2Count = 0;
            for (int i = 0; i < boardView.regions.size(); i++) {
                if (boardView.regions.get(i).ownership == constants.player1) {
                    p1Count++;
                } else if (boardView.regions.get(i).ownership == constants.player2) {
                    p2Count++;
                }
            }

            if (p1Count > p2Count) {
                onFinal(view, constants.player1);
            } else if (p1Count < p2Count) {
                onFinal(view, constants.player2);
            } else {
                onFinal(view, constants.noPlayer);
            }
        }
        else
        {
            Intent i = new Intent(this, wait.class);
            i.putExtra("player1", player1Name);
            i.putExtra("player2", player2Name);
            i.putExtra("currentround", Integer.toString(currentRound+1));
            i.putExtra("currentplayer", Integer.toString(currentPlayer));
            startActivity(i);
        }
    }

    /**
     *  Clicking the next button after the final round will
     *  lead into the Final Score Activity.
     *
     *  Before starting up the final activity, values
     *  are carried over and/or updated, including
     *  the player names and the winning player.
     *
     * @param view needed for callback
     * @param winningPlayer Player number indicating who won the game
     */
    public void onFinal(View view, int winningPlayer)
    {
        Intent intent = new Intent(this, FinalScore.class);
        intent.putExtra(constants.player1Name, player1Name);
        intent.putExtra(constants.player2Name, player2Name);
        intent.putExtra(constants.currentPlayer, winningPlayer);
        startActivity(intent);
    }

    /**
     *  This function overrides the back button,
     *  Pressing the back button will return to
     *  the Setup Activity
     */
    @Override
    public void onBackPressed()
    {
        finish();
        Intent intent = new Intent(this, RoundOptions.class);

        startActivity(intent);
    }

    /**
     * Saves the state of the game.  Mainly has the board save its state
     * The rest can be found from the intent again.
     * @param savedInstanceState Bundle to save the state into.
     */
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState)
    {

        if (boardView == null)
        {
            boardView = (BoardView) this.findViewById(R.id.boardView);
        }

        boardView.onSaveInstanceState(savedInstanceState);
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * Handles motion events
     * @param event Motion event to handle.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return boardView.onTouchEvent(currentPlayer, event);
    }


}
