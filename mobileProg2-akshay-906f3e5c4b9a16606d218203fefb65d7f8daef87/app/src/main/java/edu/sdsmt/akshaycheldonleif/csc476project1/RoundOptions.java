package edu.sdsmt.akshaycheldonleif.csc476project1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *  This file gives the user the capture options for
 *  the following round. The options are dot capture,
 *  line capture, and rectangle capture. This activity
 *  also provides a help button that describes each
 *  capture options and their probabilities in capturing.
 *  The next button on this activity leads to the board
 *  activity.
 */
public class RoundOptions extends AppCompatActivity
{
    /**
     *  Holds the first player's name
     */
    private String player1Name;

    /**
     *  Holds the second player's name
     */
    private String player2Name;

    /**
     *   Contains the current player
     *   Or which player has the turn
     */
    private int currentPlayer;

    /**
     *  Holds the remaining number of rounds
     */
    private int roundsLeft;

    private int currentRound;


    /**
     *  Text box displaying the current player
     */
    private TextView player;

    private int playerID;

    private Integer board[]=new Integer[25];

    /**
     *  Text box displaying the remaining number of rounds
     */
    private TextView rounds;

    private String email;
    int opt;

    DatabaseReference game= FirebaseDatabase.getInstance().getReference().child("Games").child("Game");


    /**
     *  Create Activity
     *  Check to see if text boxes are null
     *  If so, then set them to the corresponding
     *  text boxes' ids in the activity
     *  then set the correct values to be displayed
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round_options);

        if (player == null)
            player = (TextView) this.findViewById(R.id.playerName);

        if (rounds == null)
            rounds = (TextView) this.findViewById(R.id.roundsLeft);

        game.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot snapshot) {
                for (int i = 0; i <= 24; i++)
                    board[i] = Integer.parseInt(snapshot.child("BoardState").child(Integer.toString(i)).getValue().toString());
                player1Name= snapshot.child("Player1").getValue().toString();
                player2Name= snapshot.child("Player2").getValue().toString();
                currentPlayer = Integer.parseInt(snapshot.child("CurrentPlayer").getValue().toString());
                currentRound = Integer.parseInt(snapshot.child("CurrentRound").getValue().toString());
                roundsLeft=3-currentRound/2;
                if (currentPlayer == 0) {
                    player.setText(player1Name);
                } else {
                    player.setText(player2Name);
                }
                rounds.setText(String.valueOf(roundsLeft));

            }
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        Bundle b = getIntent().getExtras();

        if(b != null)
        {
            playerID=b.getInt("playerID");
            email=b.getString("email");
        }



        if(currentPlayer == 0)
        {
            player.setText(player1Name);
        }
        else if (currentPlayer == 1)
        {
            player.setText(player2Name);
        }

            rounds.setText(String.valueOf(roundsLeft));

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
     *  Clicking the next button will move to the Board Activity
     *  Before moving on, carry over and/or update the values of
     *  the players names, the current player, the remaining rounds,
     *  and which capture option was selected.
     *
     * @param view
     */
    public void onBoard(View view)
    {
        Intent intent = new Intent(this, Board.class);

        intent.putExtra(constants.player1Name, player1Name);
        intent.putExtra(constants.player2Name, player2Name);
        intent.putExtra(constants.currentPlayer, currentPlayer);
        intent.putExtra("currentRound",currentRound);
        intent.putExtra("email",email);
        intent.putIntegerArrayListExtra("board", new ArrayList<>(Arrays.asList(board)) );

        RadioGroup options = (RadioGroup) this.findViewById(R.id.gameOptions);
        int optionId = options.getCheckedRadioButtonId();

        if (optionId == R.id.dotCapture)
        {
            opt = constants.dotSelected;
        }
        else if (optionId == R.id.boxCapture)
        {
            opt = constants.boxSelected;
        }
        else
        {
            opt = constants.lineSelected;
        }

        intent.putExtra(constants.captureOption, opt);
        intent.putExtra("playerID",playerID);
        startActivity(intent);
        finish();
    }

    /**
     *  Clicking the help button pops up an alert box
     *  The dialog in the alert box describes the different
     *  capture options and gives their probabilities
     *
     * @param view
     */
    public void onHelp(View view)
    {
        // Instantiate a dialog box builder
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

        // Parameterize the builder
        builder.setTitle(R.string.helpTitle);
        builder.setMessage(R.string.helpMessage);
        builder.setPositiveButton(android.R.string.ok, null);

        // Create the dialog box and show it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     *  This function overrides the back button,
     *  Pressing the back button will return to
     *  the Setup Activity
     */
    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Continuing with the action will log you out and send you to login screen");
        builder.setPositiveButton("OK",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog,
                                int which) {
                MainActivity act=new MainActivity();
                act.log_out(email);
                setContentView(R.layout.activity_main);
                finish();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog,
                                int which) {
                dialog.dismiss();

            }
        });
        builder.show();
    }

    public  void onSaveInstanceState(Bundle savedInstanceState)
    {
        final Bundle save=savedInstanceState;
        super.onSaveInstanceState(save);
        savedInstanceState.putInt("currentplayer",currentPlayer);
        savedInstanceState.putInt("roundsleft",roundsLeft);
        savedInstanceState.putInt("currentRound",currentRound);
        savedInstanceState.putInt("selected",opt);

    }

    public void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        currentPlayer=savedInstanceState.getInt("currentplayer");
        roundsLeft=3-savedInstanceState.getInt("roundsleft");
        currentRound=savedInstanceState.getInt("currentRound");
        int selected=savedInstanceState.getInt("selected");
        RadioButton btn;
        if(selected==0) {
            btn = (RadioButton) findViewById(R.id.dotCapture);
            btn.setSelected(true);
        }
        else if(selected==0) {
            btn = (RadioButton) findViewById(R.id.lineCapture);
            btn.setSelected(true);
        }
        else
        {
            btn = (RadioButton) findViewById(R.id.boxCapture);
            btn.setSelected(true);
        }


    }
}
