import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


/**
 * This class encapsulates one "square" on the board
 *
 */
public class Square extends JButton {
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 2427895813330684416L;
	/**
	 * Number representing the position of the square on the board
	 */
	private int gridNumber; 
	
	/**Does this square contain a mine?
	 * 
	 */
	private boolean mine; 		
	/**How many mines are under this square
	 * 
	 */
	private int mineCount; 
	/**Is this square opened?
	 * 
	 */
	private boolean opened; 
	/**How many mines are adjacent to this square?
	 * 
	 */
	private int adjacentMines; 
	/**The Reward contained in this square (null == no reward)
	 * 
	 */
	private Reward reward;
	/**Is this square flagged?
	 * 
	 */
	private boolean flag;

	/**Constructor
	 * @param title
	 * @param number
	 */
	public Square(String title, int number){
		super(title);
		this.setNumber(number);
		setPreferredSize(new Dimension(30,30));
		setMinimumSize(new Dimension(30, 30));
		setMargin(new Insets(0,0,0,0));

		addMouseListener(new ActionListenerS(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 5713679916742991182L;

			@Override
			public void mouseClicked(MouseEvent e) {
				//We must make the sure number of flags never exceeds the number of undetonated mines
				
				
				//Left Click
				if(e.getButton() == MouseEvent.BUTTON1 && !isFlag()){
					//TODO Change square clicks actions!
					//TODO ThIS IS WHERE THE Majority of the game code will be
					//TODO ADD REWARDS Actions
					boolean dead = open(false);

					Game.setSaved(false);
					setBackground(Color.getColor("",getBackground()).brighter());
					setOpened(true);
					if (!dead) Game.checkForWin();
				}
				//THE RIGHT CLICK
				if(e.getButton() == MouseEvent.BUTTON3){
					if(!isOpened() && !isFlag()){
						Icon flagIcon = new ImageIcon("mine_flag.png");
						if(Game.getFlags() > 0){
							setFlag(true);
							
								setIcon(flagIcon);
								registerFlaggedSquare();
								Game.setFlags(Game.getFlags() - 1);
							
							Game.setSaved(false);
						}
					}
					else if(!isOpened() && isFlag()){	
						
						//TODO
						System.out.println("removing flag icon");
							setFlag(false);							
								setIcon(null);
								Game.setFlags(Game.getFlags() + 1);
								removeFlaggedSquare();
							
						
					}
					Game.setSaved(false);
					Game.checkForWin();
					//setBackground(Color.getColor("",getBackground()).brighter());
					

				}
				//MIDDLE CLICK, FOR PROBES
				if (e.getButton() == MouseEvent.BUTTON2){
					if (Game.getProbes() > 0){
						Game.setProbes(Game.getProbes() -1);
						System.out.println("probes left: " + Game.getProbes());
						open(true);
						Game.setSaved(false);
						setBackground(Color.getColor("",getBackground()).brighter());
						setOpened(true);
						Game.checkForWin();

					}

				}


			}
		});
	}
	
	/**open this square
	 * @param probe are you opening square with a probe?
	 * @return true if the player has died, false otherwise
	 */
	private boolean open(boolean probe){
		if(!isOpened() && !isFlag()){
			//if there is a mine
			if(isMine()){
				setText("M" + getMineCount());
				setBackground(Color.red);
				Game.setNumMines(Game.getNumMines()-getMineCount());
				Game.setNumMinesDetonated(Game.getNumMinesDetonated() + getMineCount() );
				//take off a life
				if (!probe)	Game.setLives(Game.getLives() - getMineCount());
				//check if dead
				if(Game.getLives() <= 0){
					//TODO GAME OVER
					//ADD a Game Over Message..
					System.out.println("Game Over");
					Object[] options = {"Back to Main Menu", "Hide"};

					Game.setSaved(true);
					Game.showMines();
					Game.showRewards();
					Game.endGame();



					int choice = JOptionPane.showOptionDialog(null, "You lost!","Title", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null,options, options[0]);
					if(choice == JOptionPane.YES_OPTION){
						MainFrame.showStartMenu();
						return true;
					}
					
				}
			} else if(reward != null){

				Reward.Types rewardType = reward.getType();
				setText(String.valueOf(rewardType.toString().charAt(0))); // TODO replace with png ? THis currently displays the first letter of the reward
				setBackground(Color.green);

				//Actions to perform depending on the rewardType
				if(rewardType == Reward.Types.BONUS){
					Game.setScore(Game.getScore() + 30);
				}
				else if (rewardType == Reward.Types.PROBE){
					Game.setProbes(Game.getProbes() + 1);
				}
				else if (rewardType == Reward.Types.IMMORTAL){
					Game.setLives(999999);
				}
				else if (rewardType == Reward.Types.SHIELD){
					Game.setLives(Game.getLives() + 3);
				}
			} else	{
				adjacentMines = Game.calcAdjacentMines(gridNumber);
				if(adjacentMines > 0){
					setText("" + adjacentMines);
				}
				else if(adjacentMines <= 0){
					Game.clearEmpty(gridNumber);
				}


			}
		}
		updateTopPanel();
		return false;

	}
	/**Update the top panel after ever click
	 * 
	 */
	public void updateTopPanel(){
		// This was really hard en tabarnac
		if(SwingUtilities.isEventDispatchThread()){
			System.out.println("updating top panel");
			System.out.println("num mines: " + Game.getNumMines());
			//Minesweeper.getGameJFrame().remove(MainFrame.getTopPanel());
			JPanel jpan = MainFrame.getTopPanel(); // Get the JPanel topPanel attribute

			Minesweeper.getGameJFrame().remove(jpan); // remove old JPanel from the JFrame

			jpan = MainFrame.newTopPanel(); // Fetch the new JPanel

			MainFrame.setTopPanel(jpan); // Set the topPanel attribute to the new jpan

			Minesweeper.getGameJFrame().add(jpan,BorderLayout.NORTH); // add the new jpanel to the JFrame


			MainFrame.getTopPanel().repaint(); // repaint jpan
		}



	}
	
	/**Register that this square object has been flagged
	 * 
	 */
	private void registerFlaggedSquare(){
		Game.getFlaggedSquares().add(this);
	}
	/**unregister that this square object has been flagged
	 * 
	 */
	private void removeFlaggedSquare(){
		Game.getFlaggedSquares().remove(this);
	}
	/**
	 * @return
	 */
	public int getNumber() {
		return gridNumber;
	}
	/**
	 * @param number
	 */
	public void setNumber(int number) {
		this.gridNumber = number;
	}
	/**
	 * @return
	 */
	public boolean isMine() {
		return mine;
	}
	/**
	 * @param mine
	 */
	public void setMine(boolean mine) {
		this.mine = mine;
	}
	/**
	 * @return
	 */
	public int getAdjacentMines() {
		return adjacentMines;
	}
	/**
	 * @param adjacentMines
	 */
	public void setAdjacentMines(int adjacentMines) {
		this.adjacentMines = adjacentMines;
	}
	/**
	 * @return
	 */
	public boolean isOpened() {
		return opened;
	}
	/**
	 * @param opened
	 */
	public void setOpened(boolean opened) {
		this.opened = opened;
	}


	/**
	 * @return
	 */
	public Reward getReward() {
		return reward;
	}
	/**
	 * @param reward
	 */
	public void setReward(Reward reward) {
		this.reward = reward;
	}
	/**
	 * @return
	 */
	public int getMineCount() {
		return mineCount;
	}
	/**
	 * @return
	 */
	public boolean isReward(){
		return (reward != null);
	}
	/**
	 * @param mineNumber
	 */
	public void setMineCount(int mineNumber) {
		this.mineCount = mineNumber;
	}
	/**Is this square empty?
	 * @return
	 */
	public boolean isEmpty(){
		if(!mine && reward == null && getAdjacentMines() <= 0){
			return true;
		} else {
			return false;
		}
	}
	/**
	 * @return
	 */
	public boolean isFlag() {
		return flag;
	}
	/**
	 * @param flag
	 */
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
}

