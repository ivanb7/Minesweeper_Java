import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JOptionPane;



/**
 * Encapsulates the data related to the Game currently being played
 *
 */
public class Game implements Serializable{
	
	private static final long serialVersionUID = 2647162638767810264L;
	/**The number of lives the player has
	 * 
	 */
	private static int lives;
	
	/**The player's score, set at the end of the game
	 * Score will be calculated like this:
	 * during the game:
	 * -15 for each mine detonated
	 * 
	 * at the end of the game:
	 * +15 for each mine avoided
	 * +15 for each life left
	 * -1.875 score per minute taken
	 */	
	private static int score;
	/**An array of rewards
	 * 
	 */
	private static Reward[] rewards;
	/**Nested array of Squares representing the Minesweeper board	*/
	private Square[][] board; //CHANGED TO STATIC NOV 30, 6:12 AM
	/**Is the game saved?	 */
	private static boolean saved = false;	
	private static int flags;					//Number of flags the user has in a game
	
	private static int probes;					//Number of probe rewards the user has in a game
	
	private static int numMines;				//The number of undetonated mines at any given time	
	
	private static int numMinesDetonated; //The number of mines detonated by the user

	/**The date at which the game was started	 */
	private static Date dateStarted;
	/**The Date at which the game was won	 */
	private static Date dateEnded;	
	/**An ArrayList containing all of the Square objects that have been flagged (right-clicked) by the user	 */
	private static ArrayList<Square> flaggedSquares = new ArrayList<>(); // Array containing all flagged square

	/**Constructor
	 * @param board
	 * @param rewards
	 */
	public Game(Square[][] board, Reward[] rewards) {
		
		Game.rewards = rewards;
		this.board = board;
		Game.lives = 3;
		Game.score = 0;
	}
	/**Constructor
	 * @param lives
	 * @param score
	 * @param rewards
	 * @param board
	 */
	public Game(Integer lives, Integer score, Reward[] rewards, Square[][] board) {
		
		Game.lives = lives;
		Game.score = score;
		Game.rewards = rewards;
		this.board = board;
	}
	/**
	 * Reset all Game attributes*/	
	public static void resetAllAttributes(){
		score =0;
		rewards = null;
		MainFrame.getGame().setBoard(null);
		saved = false;
		flags = 0;
		probes = 0;
		numMines = 0;
		numMinesDetonated = 0;
		dateStarted = null;
		dateEnded = null;
		flaggedSquares = new ArrayList<>();
	}
	
	/**set all the game attributes: SPAIRINGLY USED
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
	public static void setAllAttributes(int score, Reward[] rewards,Square[][] board, boolean saved, int flags, int probes, int numMines, int numMinesDetonated, Date dateStarted, Date dateEnded, ArrayList<Square> flaggedSquares){
		Game.score =score;
		Game.rewards = rewards;
		MainFrame.getGame().setBoard(board);
		Game.saved = saved;
		Game.flags = flags;
		Game.probes = probes;
		Game.numMines = numMines;
		Game.numMinesDetonated = numMinesDetonated;
		Game.dateStarted = dateStarted;
		Game.dateEnded = dateEnded;
		Game.flaggedSquares = flaggedSquares;
		
	}
	/**	
	 * @return a data structure we can use to save the attributes of the game
	 */	
	public static GameAttributes serializableAttributes(){
		return(new GameAttributes(lives, score, rewards, MainFrame.getGame().getBoard(),saved, flags, probes, numMines, numMinesDetonated, dateStarted, dateEnded, flaggedSquares));
	}
	
	/**
	 * @return
	 */
	public static int getLives() {
		return lives;
	}
	/**
	 * @param lives
	 */
	public static void setLives(Integer lives) {
		Game.lives = lives;
	}
	/**
	 * @return
	 */
	public static int getScore() {
		return score;
	}
	/**
	 * @param score
	 */
	public static void setScore(Integer score) {
		Game.score = score;
	}
	/**
	 * @return
	 */
	public static Reward[] getRewards() {
		return rewards;
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
	public static boolean isSaved() {
		return saved;
	}
	/**
	 * @param saved1
	 */
	public static void setSaved(boolean saved1) {
		saved = saved1;
	}
	/**
	 * @return
	 */
	public static boolean getSaved() {
		return saved;
	}
	/**
	 * When looking at a particular Square object (board[i][j]), this enum-type
	 * encapsulates (Relative to that square) the integer positions of the squares to the left, right, bottom right etc.	 * 	
	 * 
	 */
	private enum RelativePosition{Left(0,-1),Right(0,1),TopLeft(-1,-1),TopRight(-1,1),Top(-1,0),BottomLeft(1,-1),BottomRight(1,1),Bottom(1,0);
	private int relHeight;//Height relative to the square in question
	private int relWidth;//width relative to the square in question
	private RelativePosition(int relHeight, int relWidth){
		this.setRelHeight(relHeight);
		this.setRelWidth(relWidth);
	}
	public int getRelHeight() {
		return relHeight;
	}
	public void setRelHeight(int relHeight) {
		this.relHeight = relHeight;
	}
	public int getRelWidth() {
		return relWidth;
	}
	public void setRelWidth(int relWidth) {
		this.relWidth = relWidth;
	}

	} 
	
	/**Returns the number of mines adjacent to the square with the given grid number
	 * @param gridNumber
	 * @return
	 */
	public static int calcAdjacentMines(int gridNumber){
		Square[][] board = MainFrame.getGame().getBoard();
		int bheight = board.length; 		//finds the height of the board
		int bwidth = board[0].length;		//finds the width of the board
		int sheight;						//the height the square
		int swidth; 						//the width of the square
		int cheight;						//height of the square to check
		int cwidth;							//width of the square to check

		int adjacentMines = 0;
		//finds the height and the width of the square given the grid number
		sheight = (gridNumber-1)/bwidth;
		swidth = (gridNumber-1)%bwidth;


		//Check all relative positions
		for (RelativePosition p: RelativePosition.values()){
			cheight = sheight + p.getRelHeight();
			cwidth = swidth + p.getRelWidth();

			if(cheight >= 0 && cheight <= (bheight-1) && cwidth >= 0 && cwidth <= (bwidth-1)){
				Square square = board[cheight][cwidth];// The relative(i.e. adjacent) square
				if(square.isMine()){
					adjacentMines += square.getMineCount();
				}
			}
		}
		//System.out.println("Adjacent mines: " + adjacentMines);
		return adjacentMines;
		

	}

	
	/**Randomnly set mines on the board
	 * @param board
	 */
	public static void setMines(Square[][] board){
		int numMinesSet = 0;
		int maxMines = 0;
		int height = board.length;
		int width = board[0].length;
		double prob = 0; //Probability of finding 1 mine on a square
		double prob2 = 0; //Probability of finding 2 mines on a square
		double prob3 = 0; //Probability of finding 3 mines on a square
		switch(getBoardSize()){
		case 1:
			maxMines = 15;
			prob = 0.02;
			prob2 = 0.001;
			prob3 = 0.0005;
			break;
		case 2:
			maxMines = 40;
			prob = 0.02;
			prob2 = 0.001;
			prob3 = 0.0005;
			break;
		case 3:
			maxMines = 99;
			prob = 0.02;
			prob2 = 0.001;
			prob3 = 0.0005;
			break;
		}
		flags = maxMines;
		while(numMinesSet < maxMines){
			for(int i = 0; i<height;i++){
				for(int j = 0;j<width;j++){
					if(!board[i][j].isReward()){
						if(numMinesSet < maxMines){
							if(Math.random() <= prob3){
								board[i][j].setMine(true);
								numMinesSet = numMinesSet + 3;
								board[i][j].setMineCount(3);
							}
							else if(Math.random() <= prob2){
								board[i][j].setMine(true);
								numMinesSet = numMinesSet + 2;
								board[i][j].setMineCount(2);
							}
							else if(Math.random() <= prob){
								board[i][j].setMine(true);
								numMinesSet = numMinesSet + 1;
								board[i][j].setMineCount(1);
							}
						}
					}
				}
			}
		}
		System.out.println(numMinesSet + " Mines have been set");
		setNumMines(numMinesSet);
	}

	/**Randomnly set rewards onto the board
	 * @param board
	 */
	public static void setRewards(Square[][] board){
		int numRewardsSet = 0;
		int maxRewards = 0;
		int height = board.length;
		int width = board[0].length;
		double prob = 0; //Probability of finding 1 reward on a square (excluding immortal)		
		double probIMM = 0.00001; //Probability of finding the IMMORTAL reward	
		switch(getBoardSize()){
		case 1:
			maxRewards = 15;
			prob = 0.015;
			
			break;
		case 2:
			maxRewards = 40;
			prob = 0.008;
			
			break;
		case 3:
			maxRewards = 99;
			prob = 0.005;
			
			break;
		}
		
		while(numRewardsSet < maxRewards){
			for(int i = 0; i<height;i++){
				for(int j = 0;j<width;j++){
					if(!board[i][j].isMine()){ //Can't put a reward on a mine				
							if (Math.random() <= probIMM){
								//put immortal reward
								board[i][j].setReward(new Reward(Reward.Types.IMMORTAL));
								//System.out.println("Set immortal (OK)");
								numRewardsSet += 1;
							}
							else if(Math.random() <= prob){
								int rewardIndex = (int) (3*(Math.random()));
								Reward.Types[] arrayOfPossibleRewards = Reward.Types.values();
								Reward.Types rewardType = arrayOfPossibleRewards[rewardIndex];
								
								board[i][j].setReward(new Reward(rewardType));
								
								//System.out.println("Set: (IMMORTAL NOT OK)" + rewardType);
								numRewardsSet += 1;
								
						}
					}
				}
			}		
	}
	}
	
	/**Show the mines at the end of the game (after a loss)
	 *
	 */
	public static void showMines(){
		Square[][] board = MainFrame.getGame().getBoard();		
		int height = board.length; 		//finds the height of the board
		int width = board[0].length;	// finds the width of the board
		for(int i = 0; i<height;i++){
			for(int j = 0;j<width;j++){
				Square square = board[i][j];
				if(square.isMine() && !square.isOpened() && !square.isFlag()){
					square.setText("M" + square.getMineCount());
					square.setBackground(Color.GRAY.brighter());
					square.setOpened(true); //Not sure if useful
				}
			}
		}
	}
	
	/**Show the rewards at the end of the game (after a loss)
	 * 
	 */
	public static void showRewards(){
		Square[][] board = MainFrame.getGame().getBoard();
		int height = board.length; 		//finds the height of the board
		int width = board[0].length;	// finds the width of the board
		for(int i = 0; i<height;i++){
			for(int j = 0;j<width;j++){
				Square square = board[i][j];
				if(square.getReward() != null && !square.isOpened() && !square.isFlag()){
					square.setText(String.valueOf(square.getReward().getType().toString().charAt(0)));//Displays the first letter of the Reward type. I for Immortal, etc.
					square.setBackground(Color.GREEN.brighter());
					square.setOpened(true); //Not sure if useful
				}
			}
		}
	}
	
	/**End the game, Sets all the squares as "open" so that the user can't play anymore
	 * 
	 */
	public static void endGame(){
		dateEnded = new Date();
		Square[][] board = MainFrame.getGame().getBoard();
		int height = board.length; 		//finds the height of the board
		int width = board[0].length;	// finds the width of the board
		for(int i = 0; i<height;i++){
			for(int j = 0;j<width;j++){
				board[i][j].setOpened(true);

			}
		}
	}
	/**Gets the board size
	 * returns 1 for 8x8
	 * returns 2 for 16x16
	 * returns 3 for 16x30
	 */
	
	public static int getBoardSize(){
		Square[][] board = MainFrame.getGame().getBoard();
		int bheight = board.length; 		//finds the height of the board
		int bwidth = board[0].length;		//finds the width of the board

		if(bheight == 10 && bwidth == 10){
			return 1;
		}	else 
			if(bheight == 16 && bwidth == 16){
				return 2;
			}	else if(bheight == 16 && bwidth == 30){
				return 3;
			}
		return 0;
	}
	/**Clear any empty squares near the gridNumber index
	 * @param gridNumber
	 */
		public static void clearEmpty(int gridNumber){
			Square[][] board = MainFrame.getGame().getBoard();
			int bheight = board.length; 		//finds the height of the board
			int bwidth = board[0].length;		//finds the width of the board
			int sheight;						//the height the square
			int swidth; 						//the width of the square
			int cheight;						//height of the square to check
			int cwidth;							//width of the square to check
			
			
			//finds the height and the width of the square given the grid number
			sheight = (gridNumber-1)/bwidth;
			swidth = (gridNumber-1)%bwidth;
			
			//check above-left
					cheight = sheight - 1;
					cwidth = swidth - 1;
					
					if(cheight >= 0 && cheight <= (bheight-1) && cwidth >= 0 && cwidth <= (bwidth-1)){
						board[cheight][cwidth].setAdjacentMines(calcAdjacentMines(board[cheight][cwidth].getNumber()));
						if(board[cheight][cwidth].isEmpty() && !(board[cheight][cwidth].isOpened())){
							board[cheight][cwidth].setOpened(true);
							//Changes the background color to mark open squares
							board[cheight][cwidth].setBackground(Color.getColor("",board[cheight][cwidth].getBackground()).brighter());
							Game.setSaved(false);
							
							clearEmpty(board[cheight][cwidth].getNumber());
						}
						if((!board[cheight][cwidth].isEmpty()) && !(board[cheight][cwidth].isOpened()) && board[cheight][cwidth].getAdjacentMines() > 0){
							board[cheight][cwidth].setText("" + board[cheight][cwidth].getAdjacentMines());
							board[cheight][cwidth].setOpened(true);
							if(board[cheight][cwidth].getReward() != null){
								board[cheight][cwidth].setBackground(Color.getColor("",board[cheight][cwidth].getBackground()).brighter());
							} else {
								board[cheight][cwidth].setBackground(Color.getColor("",board[cheight][cwidth].getBackground()).brighter());
							}
							Game.setSaved(false);
						}
					}
					//check above center
					cheight = sheight - 1;
					cwidth = swidth;
					
					if(cheight >= 0 && cheight <= (bheight-1) && cwidth >= 0 && cwidth <= (bwidth-1)){
						board[cheight][cwidth].setAdjacentMines(calcAdjacentMines(board[cheight][cwidth].getNumber()));
						if(board[cheight][cwidth].isEmpty() && !(board[cheight][cwidth].isOpened())){
							board[cheight][cwidth].setOpened(true);
							//Changes the background color to mark open squares
							board[cheight][cwidth].setBackground(Color.getColor("",board[cheight][cwidth].getBackground()).brighter());
							Game.setSaved(false);
							
							clearEmpty(board[cheight][cwidth].getNumber());
						}
						if((!board[cheight][cwidth].isEmpty()) && !(board[cheight][cwidth].isOpened()) && board[cheight][cwidth].getAdjacentMines() > 0){
							board[cheight][cwidth].setText("" + board[cheight][cwidth].getAdjacentMines());
							board[cheight][cwidth].setOpened(true);
							if(board[cheight][cwidth].getReward() != null){
								board[cheight][cwidth].setBackground(Color.getColor("",board[cheight][cwidth].getBackground()).brighter());
							} else {
								board[cheight][cwidth].setBackground(Color.getColor("",board[cheight][cwidth].getBackground()).brighter());
							}
							Game.setSaved(false);
						}
					}
					//check above right
					cheight = sheight - 1;
					cwidth = swidth + 1;
					
					if(cheight >= 0 && cheight <= (bheight-1) && cwidth >= 0 && cwidth <= (bwidth-1)){
						board[cheight][cwidth].setAdjacentMines(calcAdjacentMines(board[cheight][cwidth].getNumber()));
						if(board[cheight][cwidth].isEmpty() && !(board[cheight][cwidth].isOpened())){
							board[cheight][cwidth].setOpened(true);
							//Changes the background color to mark open squares
							board[cheight][cwidth].setBackground(Color.getColor("",board[cheight][cwidth].getBackground()).brighter());
							Game.setSaved(false);
							
							clearEmpty(board[cheight][cwidth].getNumber());
						}
						if((!board[cheight][cwidth].isEmpty()) && !(board[cheight][cwidth].isOpened()) && board[cheight][cwidth].getAdjacentMines() > 0){
							board[cheight][cwidth].setText("" + board[cheight][cwidth].getAdjacentMines());
							board[cheight][cwidth].setOpened(true);
							if(board[cheight][cwidth].getReward() != null){
								board[cheight][cwidth].setBackground(Color.getColor("",board[cheight][cwidth].getBackground()).brighter());
							} else {
								board[cheight][cwidth].setBackground(Color.getColor("",board[cheight][cwidth].getBackground()).brighter());
							}
							Game.setSaved(false);
						}
					}
					//check center left
					cheight = sheight;
					cwidth = swidth - 1;
					
					if(cheight >= 0 && cheight <= (bheight-1) && cwidth >= 0 && cwidth <= (bwidth-1)){
						board[cheight][cwidth].setAdjacentMines(calcAdjacentMines(board[cheight][cwidth].getNumber()));
						if(board[cheight][cwidth].isEmpty() && !(board[cheight][cwidth].isOpened())){
							board[cheight][cwidth].setOpened(true);
							//Changes the background color to mark open squares
							board[cheight][cwidth].setBackground(Color.getColor("",board[cheight][cwidth].getBackground()).brighter());
							Game.setSaved(false);
							
							clearEmpty(board[cheight][cwidth].getNumber());
						}
						if((!board[cheight][cwidth].isEmpty()) && !(board[cheight][cwidth].isOpened()) && board[cheight][cwidth].getAdjacentMines() > 0){
							board[cheight][cwidth].setText("" + board[cheight][cwidth].getAdjacentMines());
							board[cheight][cwidth].setOpened(true);
							if(board[cheight][cwidth].getReward() != null){
								board[cheight][cwidth].setBackground(Color.getColor("",board[cheight][cwidth].getBackground()).brighter());
							} else {
								board[cheight][cwidth].setBackground(Color.getColor("",board[cheight][cwidth].getBackground()).brighter());
							}
							Game.setSaved(false);
						}
					}
					//check center right
					cheight = sheight;
					cwidth = swidth + 1;
					
					if(cheight >= 0 && cheight <= (bheight-1) && cwidth >= 0 && cwidth <= (bwidth-1)){
						board[cheight][cwidth].setAdjacentMines(calcAdjacentMines(board[cheight][cwidth].getNumber()));
						if(board[cheight][cwidth].isEmpty() && !(board[cheight][cwidth].isOpened())){
							board[cheight][cwidth].setOpened(true);
							//Changes the background color to mark open squares
							board[cheight][cwidth].setBackground(Color.getColor("",board[cheight][cwidth].getBackground()).brighter());
							Game.setSaved(false);
							
							clearEmpty(board[cheight][cwidth].getNumber());
						}
						if((!board[cheight][cwidth].isEmpty()) && !(board[cheight][cwidth].isOpened()) && board[cheight][cwidth].getAdjacentMines() > 0){
							board[cheight][cwidth].setText("" + board[cheight][cwidth].getAdjacentMines());
							board[cheight][cwidth].setOpened(true);
							if(board[cheight][cwidth].getReward() != null){
								board[cheight][cwidth].setBackground(Color.getColor("",board[cheight][cwidth].getBackground()).brighter());
							} else {
								board[cheight][cwidth].setBackground(Color.getColor("",board[cheight][cwidth].getBackground()).brighter());
							}
							Game.setSaved(false);
						}
					}
					//check below left
					cheight = sheight + 1;
					cwidth = swidth - 1;
					
					if(cheight >= 0 && cheight <= (bheight-1) && cwidth >= 0 && cwidth <= (bwidth-1)){
						board[cheight][cwidth].setAdjacentMines(calcAdjacentMines(board[cheight][cwidth].getNumber()));
						if(board[cheight][cwidth].isEmpty() && !(board[cheight][cwidth].isOpened())){
							board[cheight][cwidth].setOpened(true);
							//Changes the background color to mark open squares
							board[cheight][cwidth].setBackground(Color.getColor("",board[cheight][cwidth].getBackground()).brighter());
							Game.setSaved(false);
							
							clearEmpty(board[cheight][cwidth].getNumber());
						}
						if((!board[cheight][cwidth].isEmpty()) && !(board[cheight][cwidth].isOpened()) && board[cheight][cwidth].getAdjacentMines() > 0){
							board[cheight][cwidth].setText("" + board[cheight][cwidth].getAdjacentMines());
							board[cheight][cwidth].setOpened(true);
							if(board[cheight][cwidth].getReward() != null){
								board[cheight][cwidth].setBackground(Color.getColor("",board[cheight][cwidth].getBackground()).brighter());
							} else {
								board[cheight][cwidth].setBackground(Color.getColor("",board[cheight][cwidth].getBackground()).brighter());
							}
							Game.setSaved(false);
						}
					}
					//check below center
					cheight = sheight + 1;
					cwidth = swidth;
					
					if(cheight >= 0 && cheight <= (bheight-1) && cwidth >= 0 && cwidth <= (bwidth-1)){
						board[cheight][cwidth].setAdjacentMines(calcAdjacentMines(board[cheight][cwidth].getNumber()));
						if(board[cheight][cwidth].isEmpty() && !(board[cheight][cwidth].isOpened())){
							board[cheight][cwidth].setOpened(true);
							//Changes the background color to mark open squares
							board[cheight][cwidth].setBackground(Color.getColor("",board[cheight][cwidth].getBackground()).brighter());
							Game.setSaved(false);
							
							clearEmpty(board[cheight][cwidth].getNumber());
						}
						if((!board[cheight][cwidth].isEmpty()) && !(board[cheight][cwidth].isOpened()) && board[cheight][cwidth].getAdjacentMines() > 0){
							board[cheight][cwidth].setText("" + board[cheight][cwidth].getAdjacentMines());
							board[cheight][cwidth].setOpened(true);
							if(board[cheight][cwidth].getReward() != null){
								board[cheight][cwidth].setBackground(Color.getColor("",board[cheight][cwidth].getBackground()).brighter());
							} else {
								board[cheight][cwidth].setBackground(Color.getColor("",board[cheight][cwidth].getBackground()).brighter());
							}
							Game.setSaved(false);
						}
					}
					//check below right
					cheight = sheight + 1;
					cwidth = swidth + 1;
					
					if(cheight >= 0 && cheight <= (bheight-1) && cwidth >= 0 && cwidth <= (bwidth-1)){
						board[cheight][cwidth].setAdjacentMines(calcAdjacentMines(board[cheight][cwidth].getNumber()));
						if(board[cheight][cwidth].isEmpty() && !(board[cheight][cwidth].isOpened())){
							//TODO clickit
							board[cheight][cwidth].setOpened(true);
							//Changes the background color to mark open squares
							board[cheight][cwidth].setBackground(Color.getColor("",board[cheight][cwidth].getBackground()).brighter());
							Game.setSaved(false);
							
							clearEmpty(board[cheight][cwidth].getNumber());
						}
						if((!board[cheight][cwidth].isEmpty()) && !(board[cheight][cwidth].isOpened()) && board[cheight][cwidth].getAdjacentMines() > 0){
							board[cheight][cwidth].setText("" + board[cheight][cwidth].getAdjacentMines());
							board[cheight][cwidth].setOpened(true);
							if(board[cheight][cwidth].getReward() != null){
								board[cheight][cwidth].setBackground(Color.getColor("",board[cheight][cwidth].getBackground()).brighter());
							} else {
								board[cheight][cwidth].setBackground(Color.getColor("",board[cheight][cwidth].getBackground()).brighter());
							}
							Game.setSaved(false);
						}
					}
			
			
		}

	
	/**Checks if the player has solved the puzzle succesfully
	 * 
	 */
	public static void checkForWin() {
		
		Square[][] board = MainFrame.getGame().getBoard();
		int height = board.length; 		//finds the height of the board
		int width = board[0].length;	// finds the width of the board
		boolean win = true;
		for(int i = 0; i<height;i++){
			for(int j = 0; j<width; j++){
				
				if(board[i][j].isMine() && !board[i][j].isOpened() && !board[i][j].isFlag()){
					win = false;
					break;
				}

			}
		}
		if (win)
			win();
		
		/*
		if (numMines == 0){
			
			Game.win();
		}
		else if (!flaggedSquares.isEmpty() && numMines == getFlags()){
			boolean win = true;
			for(Square s: flaggedSquares){
				if(!s.isMine())
					win = false;
			}
			
			if (win)
				win();
	}*/
	}
	/**Ends the game and tells the user he has won 
	 * 
	 */
	public static void win() {
		
		
		Game.endGame();
		int score =calculateScore();
		JOptionPane.showMessageDialog(null, "You won!\n Score: " + getScore());
		
		Game.checkHighScore(score);
		Game.setSaved(true);
	}
	/**
	 * Calculates the score and returns it.
	 * Also sets the Game.score attribute to the score.
	 * To be used in conjunction with the win() method at the end of the game
	 * @return score The score */	
	private static int calculateScore(){
		int numMine = Game.getNumMines(); // Get the number of mines left, (undetonated)
		Date start = dateStarted;
		Date end = dateEnded;
		int timeTaken = end.compareTo(start); // How much time did it take to complete the puzzle? In ms
		int secondsTaken = timeTaken / 1000;
		
		score += (int) (numMine*15 + numMinesDetonated*(-15) + -0.03125*secondsTaken);
		return score;
		//Game.setNumMines(0);
	}
	
	/**Compares the current score to the top scores and adds the score to the top score list if it is high enough
	 * @param score
	 */
	public static void checkHighScore(int score) {
		for(int i = 0; i < MainFrame.getTopScores().length; i++){
			if (score > MainFrame.getTopScores()[i]){
				MainFrame.changeTopScores(i, score);
				break;
			}
		}
		saveHighScores();
	}
	
	/**Save the current High scores (useful when the high scores change)
	 * 
	 */
	public static void saveHighScores(){
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("TopScores"));
			oos.writeObject(MainFrame.getTopScores());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return
	 */
	public static int getFlags() {
		return flags;
	}
	/**
	 * @param flags
	 */
	public static void setFlags(int flags) {
		Game.flags = flags;
	}
	/**
	 * @return
	 */
	public static int getProbes() {
		return probes;
	}
	/**
	 * @param probes
	 */
	public static void setProbes(int probes) {
		Game.probes = probes;
	}
	/**
	 * @return
	 */
	public static int getNumMines() {
		return numMines;
	}
	/**
	 * @param numMines
	 */
	public static void setNumMines(Integer numMines) {
		Game.numMines = numMines;
	}
	/**
	 * @return
	 */
	public static Date getDateStarted() {
		return dateStarted;
	}
	/**
	 * @param dateStarted
	 */
	public static void setDateStarted(Date dateStarted) {
		Game.dateStarted = dateStarted;
	}
	/**
	 * @return
	 */
	public static Date getDateEnded() {
		return dateEnded;
	}
	/**
	 * @param dateEnded
	 */
	public static void setDateEnded(Date dateEnded) {
		Game.dateEnded = dateEnded;
	}

	
	/**
	 * @return
	 */
	public static int getNumMinesDetonated() {
		return numMinesDetonated;
	}
	/**
	 * @param numMinesDetonated
	 */
	public static void setNumMinesDetonated(int numMinesDetonated) {
		Game.numMinesDetonated = numMinesDetonated;
	}
	/**
	 * @return
	 */
	public static ArrayList<Square> getFlaggedSquares() {
		return flaggedSquares;
	}
	/**
	 * @param flaggedSquares
	 */
	public static void setFlaggedSquares(ArrayList<Square> flaggedSquares) {
		Game.flaggedSquares = flaggedSquares;
	}

}
