package edu.sdsmt.akshaycheldonleif.csc476project1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Class for when a user is waiting for the other play to take their turn.
 */


public class wait extends AppCompatActivity {

    DatabaseReference game= FirebaseDatabase.getInstance().getReference().child("Games").child("Game");
    private static int playerID;
    private static String email;
    long time=60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waiting);

        Bundle b = getIntent().getExtras();

        if(b!=null)
        {
            playerID=b.getInt("playerID");
            email=b.getString("email");
        }

        game.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot snapshot) {
                if(playerID==Integer.parseInt(snapshot.child("CurrentPlayer").getValue().toString()))
                {
                    Intent intent = new Intent(wait.this, RoundOptions.class);
                    intent.putExtra("playerID", playerID);
                    intent.putExtra("email",email);
                    startActivity(intent);
                    finish();

                }


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
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
    public void waitBack(android.view.View view) {
    }
}
