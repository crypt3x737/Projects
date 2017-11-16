package edu.sdsmt.akshaycheldonleif.csc476project1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 *   Authors: Kenneth Petry, Leif Torgersen, and Samantha Kranstz
 *   Mobile Computing
 *   Dr. Lisa Rebenitsch
 *   Project 1
 *   Due Date: October 5, 2017
 *
 *   This file contains the code for creating the main
 *   activity or Welcome page to our project. The
 *   welcome page contains a title, our logo, and a begin
 *   button that leads to the Setup activity.
 */
public class MainActivity extends AppCompatActivity
{
    public static final String MAIL = "mail";
    public static final String PASSWORD = "password";
    public static final String TIME = "time";
    public static final String NULL_AT_NULL = "null@null.com";

    private static FirebaseUser firebaseUser;
    private FirebaseAuth userAuth = FirebaseAuth.getInstance();
    private static String email="";
    private static String pass="";
    DatabaseReference user=FirebaseDatabase.getInstance().getReference().child("Users");
    DatabaseReference game=FirebaseDatabase.getInstance().getReference().child("Games").child("Game");
    long time=3;
    RoundOptions obj=new RoundOptions();


    @Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        final Button button = (Button)findViewById(R.id.loginID);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                signIn(v);
                }
        });

    }

	public void onClick(View v) {
        setContentView(R.layout.create_user);
	}

	public void onCreateClick(View v) {
        String repass;
        EditText emailtxt= (EditText)findViewById(R.id.createuserID);
        email=emailtxt.getText().toString();
        EditText passtxt= (EditText)findViewById(R.id.enterPassID);
        pass=passtxt.getText().toString();
        EditText repasstxt= (EditText)findViewById(R.id.reenterPassID);
        repass=repasstxt.getText().toString();
        if(repass.equals(pass)) {
            if (isValid() == true) {
                userAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                if (task.isSuccessful() || firebaseUser != null) {
                                    HashMap<String, Object> result = new HashMap<>();
                                    String str = firebaseUser.getUid();
                                    result.put("id", str);
                                    result.put("password", pass);
                                    result.put("email", email);
                                    user.child(str).setValue(result);
                                    setContentView(R.layout.activity_main);
                                } else {

                                }
                            }

                        });

            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Please make sure the entered data is valid");
                builder.show();
            }
        }
        else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Sorry, the passwords don't match.");
            builder.show();
        }

	}

	public void signIn(View v)
    {
        EditText emailtxt= (EditText)findViewById(R.id.userID);
        email=emailtxt.getText().toString();
        EditText passtxt= (EditText)findViewById(R.id.passID);
        pass=passtxt.getText().toString();
        if(isValid()==true) {
            userAuth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                game.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(final DataSnapshot snapshot) {
                                        if(NULL_AT_NULL.equals(snapshot.child("Player1").getValue().toString()))
                                        {
                                           update_player1();
                                           setContentView(R.layout.wait);
                                            TextView player1=(TextView)findViewById(R.id.player1text);
                                            player1.setText(email.substring(0,email.indexOf('@'))+" joined");

                                        }
                                        else if(NULL_AT_NULL.equals(snapshot.child("Player2").getValue().toString()))
                                        {
                                            update_player2();
                                            setContentView(R.layout.wait);
                                            TextView player1=(TextView)findViewById(R.id.player1text);
                                            player1.setText(snapshot.child("Player1").getValue().toString()+" joined");
                                            TextView player2=(TextView)findViewById(R.id.player2text);
                                            player2.setText(email.substring(0,email.indexOf('@'))+" joined");
                                            TextView wait=(TextView)findViewById(R.id.waitView);
                                            wait.setText("");

                                                new CountDownTimer(time * 1000, 1000) {
                                                    TextView wait = (TextView) findViewById(R.id.waitView);

                                                    public void onTick(long millisUntilFinished) {
                                                        time = millisUntilFinished / 1000;
                                                        wait.setText("Game will start in " + time + " seconds");
                                                    }

                                                    public void onFinish() {
                                                        wait.setText("Starting Game");
                                                        //setContentView(R.layout.activity_round_options);
                                                        onStartTurnClick(snapshot);

                                                    }

                                                }.start();
                                        }
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });


                            } else {
                            }
                        }
                    });
        }
        else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please make sure the entered data is valid");
            builder.show();
        }
        }

    public void update_player1()
    {
        String str=email.substring(0,email.indexOf('@'));
        Integer board[]=new Integer[100];
        for(int i=0;i<100;i++)
            board[i]=0;
        List<Integer> boardState = Arrays.asList(board);
        HashMap<String, Object> result = new HashMap<>();
        result.put("BoardState",boardState);
        result.put("CurrentPlayer", 1);
        result.put("CurrentRound", 1);
        result.put("MaxRounds", 3);
        result.put("Player1", str);
        result.put("Player2",NULL_AT_NULL);
        game.setValue(result);
    }

    public void update_player2()
    {
        String str=email.substring(0,email.indexOf('@'));
        game.child("Player2").setValue(str);
    }

    public void waitBack(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Continuing with the action will log you out and send you to login screen");
        builder.setPositiveButton("OK",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog,
                                int which) {
                log_out(email.substring(0,email.indexOf('@')));
                finish();
                setContentView(R.layout.activity_main);

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

    public void log_out(String mail)
    {
        final String player=mail;
        game.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot.child("Player1").getValue().toString().equals(player))
                {
                    game.child("Player1").setValue(NULL_AT_NULL);
                }
                else if(snapshot.child("Player2").getValue().toString().equals(player))
                {
                    game.child("Player2").setValue(NULL_AT_NULL);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public Boolean isValid()
    {
        Boolean valid=true;
        if(pass.length()<6)
            valid=false;
        if(email==null || email.isEmpty() || pass==null || pass.isEmpty())
            valid=false;
        return valid;
    }

    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(MAIL,email);
        savedInstanceState.putString(PASSWORD,pass);
        savedInstanceState.putLong(TIME,time);


    }

    public void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        email=savedInstanceState.getString(MAIL);
        pass=savedInstanceState.getString(PASSWORD);
        time=savedInstanceState.getLong(TIME);
        game.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot snapshot) {
                if(!NULL_AT_NULL.equals(snapshot.child("Player1").getValue().toString()))
                {
                    setContentView(R.layout.wait);
                    TextView player1=(TextView)findViewById(R.id.player1text);
                    player1.setText(email.substring(0,email.indexOf('@'))+" joined");

                }
                if(!NULL_AT_NULL.equals(snapshot.child("Player2").getValue().toString()))
                {
                    setContentView(R.layout.wait);
                    TextView player1=(TextView)findViewById(R.id.player1text);
                    player1.setText(snapshot.child("Player1").getValue().toString()+" joined");
                    TextView player2=(TextView)findViewById(R.id.player2text);
                    player2.setText(email.substring(0,email.indexOf('@'))+" joined");
                    TextView wait=(TextView)findViewById(R.id.waitView);
                    wait.setText("");

                        new CountDownTimer(time * 1000, 1000) {
                            TextView wait = (TextView) findViewById(R.id.waitView);

                            public void onTick(long millisUntilFinished) {
                                time = millisUntilFinished / 1000;
                                wait.setText("Game will start in " + time + " seconds");
                            }

                            public void onFinish() {
                                wait.setText("Starting Game");
                                onStartTurnClick(snapshot);
                                //setContentView(R.layout.activity_round_options);


                            }

                        }.start();

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }

    public void onStartTurnClick(DataSnapshot snapshot){
        game.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot snapshot) {
                Intent intent2 = new Intent(MainActivity.this, RoundOptions.class);
                Bundle b=new Bundle();
                b.putString(constants.player1Name, snapshot.child("Player1").getValue().toString());
                b.putString(constants.player2Name, snapshot.child("Player2").getValue().toString());
                b.putInt(constants.roundsLeft, Integer.parseInt(snapshot.child("CurrentRound").getValue().toString()));
                b.putInt(constants.currentPlayer, Integer.parseInt(snapshot.child("CurrentPlayer").getValue().toString()));
                b.putIntegerArrayList(constants.board,(ArrayList<Integer>)snapshot.child("BoardState").getValue());
                b.putString(constants.email,email);
                intent2.putExtras(b);
                startActivity(intent2);
                finish();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

}
