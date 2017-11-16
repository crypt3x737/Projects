package edu.sdsmt.akshaycheldonleif.csc476project1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 *   Authors: Cheldon Coughlen, Akshay Singh, Leif Torgersen
 *   Mobile Computing
 *   Dr. Lisa Rebenitsch
 *   Project 2
 *   Due Date: Nov 10, 2017
 *
 *   This file contains the code for creating the main
 *   activity or Welcome page to our project. The
 *   welcome page contains a title, and options to login or create a new user.
 */
public class MainActivity extends AppCompatActivity
{
    private static FirebaseUser firebaseUser;
    private FirebaseAuth userAuth = FirebaseAuth.getInstance();
    private static String email="";
    private static String pass="";
    DatabaseReference user=FirebaseDatabase.getInstance().getReference().child("Users");
    DatabaseReference game=FirebaseDatabase.getInstance().getReference().child("Games").child("Game");
    long time=3;
    private Integer playerID=-1;


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

        // Get preferences & set strings
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        email = sharedPreferences.getString(constants.username, "");
        pass = sharedPreferences.getString(constants.password, "");
        if ("".equals(email)) {
            pass = "";
            ((CheckBox)findViewById(R.id.rememberMe)).setChecked(false);
        } else {
            ((CheckBox)findViewById(R.id.rememberMe)).setChecked(true);
        }
        ((EditText)findViewById(R.id.userID)).setText(email);
        ((EditText)findViewById(R.id.passID)).setText(pass);
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
                                String str = firebaseUser.getUid();
                                if (task.isSuccessful() || firebaseUser != null) {
                                    HashMap<String, Object> result = new HashMap<>();
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

	public void signIn(View v) {
        EditText emailtxt= (EditText)findViewById(R.id.userID);
        email=emailtxt.getText().toString();
        EditText passtxt= (EditText)findViewById(R.id.passID);
        pass=passtxt.getText().toString();
        CheckBox checkBox = (CheckBox)findViewById(R.id.rememberMe);
        boolean rememberMe = checkBox.isChecked();

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        if (rememberMe) {
            editor.putString(constants.username, email);
            editor.putString(constants.password, pass);
        } else {
            editor.putString(constants.username, "");
            editor.putString(constants.password, "");
        }

        editor.apply();


        if(isValid()) {
            userAuth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                game.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(final DataSnapshot snapshot) {
                                        if(snapshot.child("Player1").getValue().toString().equals("null@null.com"))
                                        {
                                            setContentView(R.layout.wait);
                                            update_player1();
                                            TextView player1=(TextView)findViewById(R.id.player1text);
                                            player1.setText(String.format(getString(R.string.format_joined), email.substring(0,email.indexOf('@'))));
                                        }
                                        else if(snapshot.child("Player2").getValue().toString().equals("null@null.com"))
                                        {
                                            setContentView(R.layout.wait);
                                            update_player2();
                                            TextView player1=(TextView)findViewById(R.id.player1text);
                                            player1.setText(String.format(getString(R.string.format_joined), snapshot.child("Player1").getValue().toString()));
                                            TextView player2=(TextView)findViewById(R.id.player2text);
                                            player2.setText(String.format(getString(R.string.format_joined), email.substring(0,email.indexOf('@'))));
                                            TextView wait=(TextView)findViewById(R.id.waitView);
                                            wait.setText("");

                                                new CountDownTimer(time * 1000, 1000) {
                                                    TextView wait = (TextView) findViewById(R.id.waitView);

                                                    public void onTick(long millisUntilFinished) {
                                                        time = millisUntilFinished / 1000;
                                                        wait.setText(String.format(getString(R.string.format_game_start), time));
                                                    }

                                                    public void onFinish() {
                                                        wait.setText(R.string.starting_game);
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

    public void update_player1() {
        playerID=0;
        String str=email.substring(0,email.indexOf('@'));
        Integer board[]=new Integer[25];
        for(int i=0;i<25;i++)
            board[i]=-1;
        List<Integer> boardState = Arrays.asList(board);
        HashMap<String, Object> result = new HashMap<>();
        result.put("Timer",10);
        result.put("BoardState",boardState);
        result.put("CurrentPlayer", 0);
        result.put("CurrentRound", 0);
        result.put("MaxRounds", 3);
        result.put("Player1", str);
        result.put("Player2","null@null.com");
        game.setValue(result);
        check();

    }

    public void update_player2() {
        playerID=1;
        String str=email.substring(0,email.indexOf('@'));
        game.child("Player2").setValue(str);
        check();

    }

    public void waitBack(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Continuing with the action will log you out and send you to login screen");
        builder.setPositiveButton("OK",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog,
                                int which) {
                log_out(email.substring(0,email.indexOf('@')));
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

    public void check()
    {
        game.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot snapshot) {
                if(!snapshot.child("Player1").getValue().toString().equals("null@null.com") &&
                        !snapshot.child("Player2").getValue().toString().equals("null@null.com")) {
                    Intent intent2 = new Intent(MainActivity.this, wait.class);
                    Bundle b = new Bundle();
                    b.putInt("playerID", playerID);
                    b.putString("email", email);
                    intent2.putExtras(b);
                    startActivity(intent2);
                    finish();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void log_out(String mail)
    {
        final String player=mail;
        game.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot.child("Player1").getValue().toString().equals(player))
                {
                    game.child("Player1").setValue("null@null.com");
                }
                else if(snapshot.child("Player2").getValue().toString().equals(player))
                {
                    game.child("Player2").setValue("null@null.com");
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
        savedInstanceState.putString("mail",email);
        savedInstanceState.putString("password",pass);
        savedInstanceState.putLong("time",time);
        savedInstanceState.putInt("playerID",playerID);

    }

    public void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        playerID=savedInstanceState.getInt("playerID");
        email=savedInstanceState.getString("mail");
        pass=savedInstanceState.getString("password");
        time=savedInstanceState.getLong("time");
        game.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot snapshot) {
                if(!snapshot.child("Player1").getValue().toString().equals("null@null.com"))
                {
                    setContentView(R.layout.wait);
                    TextView player1=(TextView)findViewById(R.id.player1text);
                    player1.setText(String.format(getString(R.string.format_joined), email.substring(0,email.indexOf('@'))));

                }
                if(!snapshot.child("Player2").getValue().toString().equals("null@null.com"))
                {
                    setContentView(R.layout.wait);
                    TextView player1=(TextView)findViewById(R.id.player1text);
                    player1.setText(String.format(getString(R.string.format_joined), snapshot.child("Player1").getValue().toString()));
                    TextView player2=(TextView)findViewById(R.id.player2text);
                    player2.setText(String.format(getString(R.string.format_joined), email.substring(0,email.indexOf('@'))));
                    TextView wait=(TextView)findViewById(R.id.waitView);
                    wait.setText("");

                        new CountDownTimer(time * 1000, 1000) {
                            TextView wait = (TextView) findViewById(R.id.waitView);

                            public void onTick(long millisUntilFinished) {
                                time = millisUntilFinished / 1000;
                                wait.setText(String.format(getString(R.string.format_game_start), time));
                            }

                            public void onFinish() {
                                wait.setText(R.string.starting_game);
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
                if (!snapshot.child("Player1").getValue().toString().equals("null@null.com") &&
                        !snapshot.child("Player2").getValue().toString().equals("null@null.com")) {
                    Intent intent2 = new Intent(MainActivity.this, wait.class);
                    Bundle b = new Bundle();
                    b.putInt("playerID", playerID);
                    b.putString("email", email);
                    intent2.putExtras(b);
                    startActivity(intent2);
                    finish();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

}
