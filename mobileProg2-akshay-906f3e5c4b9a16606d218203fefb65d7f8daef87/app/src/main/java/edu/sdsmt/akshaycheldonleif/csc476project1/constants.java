package edu.sdsmt.akshaycheldonleif.csc476project1;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * A class to keep constant key values for passing data between activities.
 */

public final class constants
{
	/**
	 * Key value for passing the player 1 name between activities
	 */
	final static String player1Name = "player1";

    /**
     * Key value for username in preferences
     */
	final static String username = "username";

    /**
     * Key value for password in preferences
     */
	final static String password = "password";

	/**
	 * Key value for passing the player 2 name between activities
	 */
	final static String player2Name = "player2";

	/**
	 * Key value for passing the current player number between activities
	 */
	final static String currentPlayer = "currentPlayer";

	/**
	 * Key value for passing the number of rounds left between activities
	 */
	final static String roundsLeft = "roundsLeft";

	/**
	 * Key value for passing the chosen capture option between activities
	 */
	final static String captureOption = "captureOption";

	/**
	 * Key value for passing email between activities
	 */
	final static String email = "email";

	/**
	 * Key value for passing board between activities
	 */
	final static String board = "board";

	/**
	 * Enum like value for the player choosing the dot capture option
	 */
	final static int dotSelected = 0;

	/**
	 * Enum like value for the player choosing the line capture option
	 */
	final static int lineSelected = 1;

	/**
	 * Enum like value for the player choosing the box capture option
	 */
	final static int boxSelected = 2;

	/**
	 * Enum like value for indicating player 1
	 */
	final static int player1 = 0;

    final static int playerID = -1;


    /**
	 * Enum like value for indicating player 2
	 */
	final static int player2 = 1;

	/**
	 * Enum like value for indicating a lack of a player
	 */
	final static int noPlayer = -1;

	private static DatabaseReference game= FirebaseDatabase.getInstance().getReference().child("Games").child("Game");

    /**
     * Allows any activity to clear the firebase when exiting
     */
	public static void clearFireBase() {
		Integer board[]=new Integer[25];
		for(int i=0;i<25;i++)
			board[i]=-1;
		List<Integer> boardState = Arrays.asList(board);
		HashMap<String, Object> result = new HashMap<>();
		result.put("Timer", 10);
		result.put("BoardState",boardState);
		result.put("CurrentPlayer", 0);
		result.put("CurrentRound", 0);
		result.put("MaxRounds", 3);
		result.put("Player1", "null@null.com");
		result.put("Player2", "null@null.com");
		game.setValue(result);
	}
}
