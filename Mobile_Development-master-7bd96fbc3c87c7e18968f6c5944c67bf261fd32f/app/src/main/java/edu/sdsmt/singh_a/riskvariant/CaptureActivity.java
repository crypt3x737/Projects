package edu.sdsmt.singh_a.riskvariant;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * This is the CaptureActivity class. It is the activity that allows the player to choose their
 * capture method. This class loads in the values it gets from the main activity and  displays
 * them on the screen. This class calls the game activity when the player clicks start turn.
 */
@SuppressWarnings("DefaultFileTemplate")
public class CaptureActivity extends AppCompatActivity {
    /**
     * list of players
     */
    ArrayList<Integer> players=new ArrayList<>();

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
     * create the capture selection activity and load the values
     * @param savedInstanceState the saved bundle the we load
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // load the extras and make sure the bundle is not null
        Bundle b = getIntent().getExtras();
        if(b != null)
        {
            // get the parameters that are being passed from activity to activity
            player1Name = b.getString("player1Name");
            player2Name = b.getString("player2Name");
            playerTurn = b.getInt("playerTurn");
            maxTurnNumber = b.getInt("maxNumOfTurns");
            currentTurnNumber = b.getInt("turnNumber");
            players=b.getIntegerArrayList("players");
        }
        // set content view to the capture method layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.capturemethod);

        // get the text views for the player name and the round information
        TextView tv1 = (TextView)findViewById(R.id.PlayerTextView);
        TextView tv2 = (TextView)findViewById(R.id.RoundNumTextView);

        // set the text of the text view players name of whose urn it is
        if(playerTurn == 1) {
            tv1.setText(player1Name);
        }
        else {
            tv1.setText(player2Name);
        }

        // load the round info string and set the label
        String turnText = String.format(getResources().getString(R.string.RoundLabelText), currentTurnNumber, maxTurnNumber);
        tv2.setText(turnText);
    }

    /**
     * handle the end game by going back to the welcome page
     */
    private void onEndGame() {
        Intent intent1 = new Intent(this, MainActivity.class);
        startActivity(intent1);
        finish();
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
     * this function handles the start turn click event, which starts the game activity
     * and sends the game activity the player and round info
     * @param view the view
     */
    public void onStartTurnClick(View view){
        // create the intent and the bundle
        Intent intent2 = new Intent(this, GameActivity.class);
        Bundle b = new Bundle();

        // get which radio button is checked
        RadioGroup rg1 = (RadioGroup)findViewById(R.id.CaptureRadioGroup);
        int radioButtonID = rg1.getCheckedRadioButtonId();
        View radioButton = rg1.findViewById(radioButtonID);
        int idx = rg1.indexOfChild(radioButton);

        // set the bundle values
        b.putInt("maxNumOfTurns", maxTurnNumber);
        b.putInt("turnNumber", currentTurnNumber);
        b.putString("player1Name", player1Name);
        b.putString("player2Name", player2Name);
        b.putInt("playerTurn", playerTurn);
        b.putInt("selectionMethod", idx);
        b.putIntegerArrayList("players",players);
        intent2.putExtras(b);

        // start the activity
        startActivity(intent2);
        finish();
    }
}
