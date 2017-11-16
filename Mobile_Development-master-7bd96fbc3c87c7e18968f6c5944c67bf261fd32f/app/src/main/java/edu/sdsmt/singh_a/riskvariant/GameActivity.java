package edu.sdsmt.singh_a.riskvariant;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * This is the GameActivity class. It is the activity that displays the game. This
 * activity has a create function where the activity is loaded and loads the game info
 */
public class GameActivity extends AppCompatActivity {
    /**
     * list of players tiles
     */
    ArrayList<Integer> players = new ArrayList<>();

    /**
     * player 1 name
     */
    private String player1Name;

    /**
     * player 2 name
     */
    private String player2Name;

    /**
     * current round number
     */
    private int currentTurnNumber;

    /**
     * max number of rounds
     */
    private int maxTurnNumber;

    /**
     * which players turn it is
     */
    private int playerTurn;

    /**
     * The game view in this activity's view
     */
    private GameView gameView;

    /**
     * capture method
     */
    private int cap_method;

    /**
     * create the game activity and load in game info
     * @param bundle the saved bundle the we load
     */
    @Override
    protected void onCreate(Bundle bundle) {
        // get the intents bundle to get the extras
        Bundle b = getIntent().getExtras();
        int selectionMethod = 10;
        // if the bundle is not null load the values in
        if(b != null)
        {
            selectionMethod = b.getInt("selectionMethod");
            player1Name = b.getString("player1Name");
            player2Name = b.getString("player2Name");
            currentTurnNumber = b.getInt("turnNumber");
            maxTurnNumber = b.getInt("maxNumOfTurns");
            playerTurn = b.getInt("playerTurn");
            players=b.getIntegerArrayList("players");
        }

        // set content view
        super.onCreate(bundle);
        setContentView(R.layout.activity_game);

        // get the text views
        TextView tv1 = (TextView)findViewById(R.id.CaptureMethodType);
        TextView tv2 = (TextView)findViewById(R.id.GamePlayerName);
        TextView tv3 = (TextView)findViewById(R.id.RoundNum);
        // set the players name in the label for whose turn it is
        if(playerTurn == 1)
        {
            tv2.setText(player1Name);
        }
        else
        {
            tv2.setText(player2Name);
        }

        // load the round info string and set the label
        String turnText = String.format(getResources().getString(R.string.RoundLabelText), currentTurnNumber, maxTurnNumber);
        tv3.setText(turnText);
        // set the text for the capture method
        switch(selectionMethod){
            case 0: tv1.setText(R.string.GuaranteedCollect);
                    break;
            case 1: tv1.setText(R.string.Line);
                    break;
            case 2: tv1.setText(R.string.Rectangle);
                    break;
            default: tv1.setText(R.string.Error);
                     break;
        }
        // get the game view and set info
        gameView = (GameView)this.findViewById(R.id.gameView);
        gameView.set_cap_method(selectionMethod);
        gameView.setplayer_info(playerTurn);
        gameView.setPlayers(players);
        gameView.instance(this);
        if(bundle != null) {
            // We have saved state
            gameView.loadInstanceState(bundle);
        }
        cap_method=selectionMethod;
    }

    /**
     * Save the instance state into a bundle
     * @param bundle the bundle to save into
     */
    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        gameView.saveInstanceState(bundle);
    }

    /*
     * start the main activity cause the game is over
     */
    private void onEndGame() {
        Intent intent1 = new Intent(this, MainActivity.class);
        startActivity(intent1);
        finish();
    }

    /*
     * set the players array list
     */
    public void setPlayers(ArrayList<Integer> players) {
        this.players= new ArrayList<>(players);
    }

    /**
     * handle the user pressing the back button
     */
    @Override
    public void onBackPressed() {
        //End Game
        // Instantiate a dialog box builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Parameterize the builder
        builder.setTitle(R.string.BackButtonEndGameTitle);
        builder.setMessage(R.string.BackButtonEndGameContent);
        builder.setPositiveButton(R.string.DialogOkButton, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                onEndGame();
            }
        });
        builder.setNegativeButton(R.string.DialogCancelButton, null);

        // Create the dialog box and show it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * this function handles the capture click event, which ends the players turn
     * @param view the view
     */
    public void onCaptureButtonClick(View view) {
        if(playerTurn == 2 && currentTurnNumber == maxTurnNumber){
            int player1Count = 0;
            int player2Count = 0;
            for(int p = 0; p < players.size(); p++){
                if(players.get(p).intValue() == 1){
                    player1Count = player1Count + 1;
                }
                else if (players.get(p).intValue() == 2) {
                    player2Count = player2Count + 1;
                }
            }
            //End Game
            // Instantiate a dialog box builder
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

            // Parameterize the builder
            builder.setTitle(R.string.GameComplete);
            String message = "";
            if(player1Count == player2Count){
                message = getResources().getString(R.string.GameEndTie);
            } else if (player1Count > player2Count) {
                message = String.format(getResources().getString(R.string.GameEndWon), player1Name);
            } else if (player2Count > player1Count){
                message = String.format(getResources().getString(R.string.GameEndWon), player2Name);
            }
            builder.setMessage(message);
            builder.setPositiveButton(R.string.DialogOkButton, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    onEndGame();
                }
            });

            // Create the dialog box and show it
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        else{
            // create intent and bundle
            Intent intent2 = new Intent(this, CaptureActivity.class);
            Bundle b = new Bundle();
            // if its player 2 increase the round number
            if(playerTurn == 2)
                b.putInt("turnNumber", currentTurnNumber + 1);
            else
                b.putInt("turnNumber", currentTurnNumber);
            // save the value to the bundle
            b.putInt("maxNumOfTurns", maxTurnNumber);
            b.putString("player1Name", player1Name);
            b.putString("player2Name", player2Name);
            if(playerTurn == 1)
                b.putInt("playerTurn", 2);
            else
                b.putInt("playerTurn", 1);
            b.putInt("selectionMethod", cap_method);
            b.putIntegerArrayList("players",players);
            intent2.putExtras(b);
            // start activity
            startActivity(intent2);
            finish();
        }
    }
}
