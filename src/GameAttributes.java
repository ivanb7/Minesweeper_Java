import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Class used to save the Game attributes to a file.
 * We can'y directly save an object of Game because it's attributes are static, so we transfer it's attributes to this class and save this class instead
 * The attributes are the same as those in Game so i will not comment them
 * 
 *
 */
public class GameAttributes implements Serializable {
	/**Constructor 
	 * @param lives
	 * @param score
	 * @param rewards
	 * @param board
	 * @param saved
	 * @param flags
	 * @param probes
	 * @param numMines
	 * @param numMinesDetonated
	 * @param dateStarted
	 * @param dateEnded
	 * @param flaggedSquares
	 */
	public GameAttributes(int lives, int score, Reward[] rewards,
			Square[][] board, boolean saved, int flags, int probes,
			int numMines, int numMinesDetonated, Date dateStarted,
			Date dateEnded, ArrayList<Square> flaggedSquares) {
		super();
		this.lives = lives;
		this.score = score;
		this.rewards = rewards;
		this.board = board;
		this.saved = saved;
		this.flags = flags;
		this.probes = probes;
		this.numMines = numMines;
		this.numMinesDetonated = numMinesDetonated;
		this.dateStarted = dateStarted;
		this.dateEnded = dateEnded;
		this.flaggedSquares = flaggedSquares;
	}

	private int lives;
	
	/**
	 * Score will be calculated like this:
	 * during the game:
	 * -15 for each mine detonated
	 * 
	 * at the end of the game:
	 * +15 for each mine avoided
	 * +15 for each life left
	 * -1.875 score per minute taken
	 */

	private int score;
	
	private Reward[] rewards;
	
	private Square[][] board; //CHANGED TO NOV 30, 6:12 AM
	
	private boolean saved;

	private int flags;					//Number of flags the user has in a game
	
	private int probes;					//Number of probe rewards the user has in a game
	
	private int numMines;				//The number of undetonated mines at any given time
	

	private int numMinesDetonated; //The number of mines detonated by the user

	
	private Date dateStarted;

	private Date dateEnded;
	
	
	
	
	private ArrayList<Square> flaggedSquares = new ArrayList<>(); // Array containing all flagged square


	public int getLives() {
		return lives;
	}


	public void setLives(int lives) {
		this.lives = lives;
	}

	/**
	 * @return
	 */
	public int getScore() {
		return score;
	}

	/**
	 * @param score
	 */
	public void setScore(int score) {
		this.score = score;
	}

	/**
	 * @return
	 */
	public Reward[] getRewards() {
		return rewards;
	}

	/**
	 * @param rewards
	 */
	public void setRewards(Reward[] rewards) {
		this.rewards = rewards;
	}

	/**
	 * @return
	 */
	public Square[][] getBoard() {
		return board;
	}

	/**
	 * @param board
	 */
	public void setBoard(Square[][] board) {
		this.board = board;
	}

	/**
	 * @return
	 */
	public boolean isSaved() {
		return saved;
	}

	/**
	 * @param saved
	 */
	public void setSaved(boolean saved) {
		this.saved = saved;
	}

	/**
	 * @return
	 */
	public int getFlags() {
		return flags;
	}

	/**
	 * @param flags
	 */
	public void setFlags(int flags) {
		this.flags = flags;
	}

	/**
	 * @return
	 */
	public int getProbes() {
		return probes;
	}

	/**
	 * @param probes
	 */
	public void setProbes(int probes) {
		this.probes = probes;
	}

	/**
	 * @return
	 */
	public int getNumMines() {
		return numMines;
	}

	/**
	 * @param numMines
	 */
	public void setNumMines(int numMines) {
		this.numMines = numMines;
	}

	/**
	 * @return
	 */
	public int getNumMinesDetonated() {
		return numMinesDetonated;
	}

	/**
	 * @param numMinesDetonated
	 */
	public void setNumMinesDetonated(int numMinesDetonated) {
		this.numMinesDetonated = numMinesDetonated;
	}

	/**
	 * @return
	 */
	public Date getDateStarted() {
		return dateStarted;
	}

	/**
	 * @param dateStarted
	 */
	public void setDateStarted(Date dateStarted) {
		this.dateStarted = dateStarted;
	}

	/**
	 * @return
	 */
	public Date getDateEnded() {
		return dateEnded;
	}

	/**
	 * @param dateEnded
	 */
	public void setDateEnded(Date dateEnded) {
		this.dateEnded = dateEnded;
	}

	/**
	 * @return
	 */
	public ArrayList<Square> getFlaggedSquares() {
		return flaggedSquares;
	}

	/**
	 * @param flaggedSquares
	 */
	public void setFlaggedSquares(ArrayList<Square> flaggedSquares) {
		this.flaggedSquares = flaggedSquares;
	}
}
