package edu.sdsmt.akshaycheldonleif.csc476project1;

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

	/**
	 * Enum like value for indicating player 2
	 */
	final static int player2 = 1;

	/**
	 * Enum like value for indicating a lack of a player
	 */
	final static int noPlayer = -1;
}
