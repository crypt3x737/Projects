package edu.sdsmt.singh_a.riskvariant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

import static edu.sdsmt.singh_a.riskvariant.R.id.player1_name;

/**
 * This is the MainActivity class. It is the starting activity for the application.
 * This class handles the welcome screen, so it has an onCreate function and a function
 * to handle the start game button click
 */
public class MainActivity extends AppCompatActivity {
    /**
     * list of players
     */
    ArrayList<Integer> players = new ArrayList<>();

    /**
     * handle the create main activity
     * @param savedInstanceState the saved instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        player1();
        player2();
        rounds();
        for(int i=0;i<100;i++)
            players.add(i,0);
    }

    /**
     * handle the user pressing the back button
     */
    @Override
    public void onBackPressed() {
        finish();
    }

    /**
     * add a listener to the player 1 edit text
     */
    private void player1() {
        final EditText player = (EditText) findViewById(R.id.player1_name);

        player.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
               if(player.getText().toString().equalsIgnoreCase("Player1"))
                   player.setText("");

            }

        });

    }

    /**
     * add a listener to the player 2 edit text
     */
    private void player2() {
        final EditText player = (EditText) findViewById(R.id.player2_name);

        player.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(player.getText().toString().equalsIgnoreCase("Player2"))
                    player.setText("");

            }

        });

    }

    /**
     * add a listener to the round number edit text
     */
    private void rounds() {
        final EditText round = (EditText) findViewById(R.id.rounds_num);

        round.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(round.getText().toString().equalsIgnoreCase("Rounds"))
                    round.setText("");

            }

        });

    }

    /**
     * handle on start game click event
     * @param view the view
     */
    public void onStartGame(View view){
        // get the values in the player 1 name, player 2 name, and round number edit view
        EditText edt;
        String player1, player2;
        int rounds;
        edt = (EditText)findViewById(player1_name);
        player1 = edt.getText().toString();
        // if player 1 name is empty default it to: Player1
        if(player1.equals(""))
            player1 = "Player1";
        edt = (EditText)findViewById(R.id.player2_name);
        player2 = edt.getText().toString();
        // if player 2 name is empty default it to: Player2
        if(player2.equals(""))
            player1 = "Player2";
        edt = (EditText)findViewById(R.id.rounds_num);

        // check if the rounds is empty, if so default to 1
        if(edt.getText().toString().equals(""))
            rounds = 1;
        else {
            // if there is something in the rounds edit text get it and make sure its a integer
            try {
                rounds = Integer.parseInt(edt.getText().toString());
            } catch (Exception ex) {
                rounds = 1;
            }
        }

        // create the capture activity intent and put the game info into the extras area
        Intent intent1 = new Intent(this, CaptureActivity.class);
        intent1.putExtra("player1Name",player1);
        intent1.putExtra("player2Name",player2);
        intent1.putExtra("maxNumOfTurns",rounds);
        intent1.putExtra("turnNumber",1);
        intent1.putExtra("playerTurn",1);
        intent1.putIntegerArrayListExtra("players",players);
        startActivity(intent1);
        finish();
    }
}
