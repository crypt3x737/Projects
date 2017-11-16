package edu.sdsmt.akshaycheldonleif.csc476project1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

/**
 *  This file creates the setup activity.
 *  The setup activity consists of three edit text
 *  boxes and a next button. The text boxes are for
 *  entering two player names and the number of rounds.
 *  The next button goes to Round Options Activity.
 *  When the next button is clicked, this activity checks
 *  to see if the user provided the required information
 *  and if not, sets default values.
 *
 */
public class  Setup extends AppCompatActivity
{
    /**
     *  Text box for the first player's name
     */
    private EditText player1Name;

    /**
     *  Text box for the second player's name
     */
    private EditText player2Name;

    /**
     *  Text box for the number of rounds to be played
     */
    private EditText rounds;

    /**
     *  Default number of rounds if not given
     */
    private static final int default_rounds = 1;

    /**
     *  Default string for player 1's name if not given
     */
    private static final String default_player1Name = "Player 1";

    /**
     *  Default string for player 2's name if not given
     */
    private static final String default_player2Name = "Player 2";

    /**
     *  Create the activity
     *  Check if edittext variables are not set
     *  Set them to their corresponding edittext ids in the xml files
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        if (player1Name == null)
        {
             player1Name = (EditText) this.findViewById(R.id.player1Name);
        }

        if (player2Name == null)
        {
            player2Name = (EditText) this.findViewById(R.id.player2Name);
        }

        if (rounds == null)
        {
            rounds = (EditText) this.findViewById(R.id.rounds);
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
     *
     * Clicking on the next button will lead to
     * the round options activity
     * Before moving on to the next activity,
     * This function checks to see if the players have
     * entered values into the names and round text boxes
     * If there are no values, defaults are set
     * The defaults are Player 1, Player 2, and 1 round
     *
     * @param view
     */
    public void onRoundOptions( View view )
    {
        Intent intent = new Intent(this, RoundOptions.class);

        if( player1Name.getText().toString().equals("")){

            player1Name.setText(default_player1Name);
        }

        if( player2Name.getText().toString().equals("")){

            player2Name.setText(default_player2Name);
        }

        intent.putExtra(constants.player1Name, player1Name.getText().toString());
        intent.putExtra(constants.player2Name, player2Name.getText().toString());

        intent.putExtra(constants.currentPlayer, constants.player1);

        try{
            intent.putExtra(constants.roundsLeft, Integer.parseInt(rounds.getText().toString()));
        }
        catch (Exception e){
            intent.putExtra(constants.roundsLeft, default_rounds);
        }
        startActivity(intent);
        finish();

    }

    /**
     *  This function overrides the back button,
     *  Pressing the back button will return to
     *  the Setup Activity
     */
    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, Setup.class);
        startActivity(intent);
        finish();
    }

}
