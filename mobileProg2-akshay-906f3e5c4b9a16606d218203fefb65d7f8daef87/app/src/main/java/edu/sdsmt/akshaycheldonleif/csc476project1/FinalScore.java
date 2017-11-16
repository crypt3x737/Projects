package edu.sdsmt.akshaycheldonleif.csc476project1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

/**
 *   This file contains the Final Score Activity
 *   This activity displays which player won the game.
 *   The return button in this activity goes back to the
 *   Setup activity, to begin a new game.
 */
public class FinalScore extends AppCompatActivity
{

    /**
     *  The text box that contains who won the game
     */
    private TextView winnerText;

    /**
     *  Contains the player that won
     */
    private int winner;

    /**
     *  Create activity
     *  Check if variable is null
     *  If null set it to the corresponding
     *  text box id from the activity
     *  Set values to display the player that won
     *
     * @param savedInstanceState bundle to load data from
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_score);

        if (winnerText == null)
        {
            winnerText = (TextView) this.findViewById(R.id.roundOptionsLabel);
        }

        winner = this.getIntent().getIntExtra(constants.currentPlayer, -1);


        String winResult;
        if (winner == constants.player1)
        {
            winResult = this.getIntent().getStringExtra(constants.player1Name);
        }
        else if (winner == constants.player2)
        {
            winResult = this.getIntent().getStringExtra(constants.player2Name);
        }
        else
        {
            winResult = "Tie";
        }

        if (winnerText != null)
        {
            winnerText.setText(winResult);
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
     *  Clicking the return button returns the game back
     *  to the setup activity, to give the option to play
     *  another set of rounds.
     *
     * @param view
     */
    public void onReturn(View view)
    {
        Intent intent = new Intent(this, Setup.class);
        finish();
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
        Intent intent = new Intent(this, Setup.class);
        startActivity(intent);
    }
}
