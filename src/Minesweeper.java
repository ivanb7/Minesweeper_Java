import java.awt.Dimension;


import javax.swing.JFrame;

/**Driver class
 * 
 * 
 *
 */

public class Minesweeper {
	/**
	 * The JFrame for the game
	 */
	private static JFrame gameJFrame;
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args){
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				startGame();
			}
		});



	}
	/**
	 * Starts a Minesweeper game
	 */

	public static void startGame(){
		gameJFrame = MainFrame.newMainFrame();
		gameJFrame.getName(); //Used to remove local variable not used warning.. purely esthetic 
	}
	/**Resize gameJFrame
	 * @param size
	 */
	public static void resize(Dimension size){
		gameJFrame.setPreferredSize(size);
		gameJFrame.pack();
	}
	
	/**
	 * @return
	 */
	public static JFrame getGameJFrame() {
		return gameJFrame;
	}
	/**
	 * @param startGame
	 */
	public static void setGameJFrame(JFrame startGame) {
		Minesweeper.gameJFrame = startGame;
	}
}
