import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.EventListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;



/**
 * The Main JFrame
 *
 */
public class MainFrame extends JFrame implements EventListener{


	private static final long serialVersionUID = 994203769991972863L;
	/**The game panel
	 * 
	 */
	private static JPanel gamePanel;
	/**The top banner over the gamePanel
	 * 
	 */
	private static JPanel topPanel; // Top game banner

	/**
	 * the JPanel for the start menu
	 */
	private static JPanel startMenuPanel;
	/**The container for the start menu 
	 */
	private static Container cont;
	/**A Game attribute associated with the main JFrame
	 * 
	 */
	private static Game game;
	/**The File object currently being used to save files
	 * 
	 */
	private static File currentSaveFile;
	/**Are we still playing the game?
	 * 
	 */
	private static boolean playing;

	/**array for top scores
	 * 
	 */
	private static int[] topScores;

	/**
	 * 
	 * The Constructor for the main frame in the game
	 * @param title - The main frame's title
	 * @param game1 The game object
	 */
	
	public MainFrame(String title, Game game1){
		super(title);
		/*
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); // Set the look and feel of the application to the system default
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		*/
		// CREATION OF THE GAME PANEL
		MainFrame.game = game1;

		//Frame settings
		setSize(300, 350);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(100, 100);
		setVisible(true);

		//Set Layout
		BorderLayout borderLayout = new BorderLayout();
		setLayout(borderLayout);
		//Add a window listener for closing operations
		WindowListener exitListener = new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				if(!Game.isSaved() && playing == true){
					//Changed to YES_NO 
					int confirm = JOptionPane.showOptionDialog(null, "Do you want to save before quitting? Any unsaved progress will be lost.", "Exit Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
					if (confirm == JOptionPane.NO_OPTION) {
						System.exit(0);
					} else if(confirm == JOptionPane.YES_OPTION){
						MainFrame.saveGame(Game.serializableAttributes());
						if(Game.isSaved()){
							System.exit(0);
						}
						return;
					}
				}
			}
		};
		this.addWindowListener(exitListener);

		//Create Swing components 

		//Menu Bar
		JMenuBar menubar = new JMenuBar();
		JMenu file = new JMenu("File");

		JMenuItem eMenuItem0 = new JMenuItem("Save");
		eMenuItem0.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				saveGame(Game.serializableAttributes());
			}
		});

		JMenuItem eMenuItem1 = new JMenuItem("Save As");
		eMenuItem1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				saveAsGame(Game.serializableAttributes());
			}
		});

		JMenuItem eMenuItem3 = new JMenuItem("Exit to Main Menu");
		eMenuItem3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if(!Game.getSaved()){
					int result = askToSave();
					if(result == 0){
						return;
					}
				}
				showStartMenu();
			}
		});

		JMenuItem eMenuItem2 = new JMenuItem("Exit");
		eMenuItem2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				exitGame();
			}
		});

		file.add(eMenuItem0);
		file.add(eMenuItem1);
		file.add(eMenuItem3);
		file.add(eMenuItem2);

		menubar.add(file);

		setJMenuBar(menubar);
		//Panels
		gamePanel = newGamePanel();
		topPanel = newTopPanel();
		playing = false;
		//CREATION OF THE START MENU PANEL
		startMenuPanel = new JPanel();
		startMenuPanel.setVisible(true);
		//Components of start menu
		JLabel menu = new JLabel("Main menu: ");
		JButton newGame = new JButton("New Game");
		JButton loadGame = new JButton("Load Game");
		JButton topScores = new JButton("View Top Scores");
		JButton instructions = new JButton("Instructions");
		JButton exit = new JButton("Exit");

		menu.setAlignmentX(CENTER_ALIGNMENT);
		newGame.setAlignmentX(CENTER_ALIGNMENT);
		loadGame.setAlignmentX(CENTER_ALIGNMENT);
		topScores.setAlignmentX(CENTER_ALIGNMENT);
		exit.setAlignmentX(CENTER_ALIGNMENT);
		instructions.setAlignmentX(CENTER_ALIGNMENT);
		
		newGame.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				MainFrame.startNewGame();
			}

		});
		loadGame.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				MainFrame.loadGame();
			}

		});
		topScores.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				viewTopScores();
			}

		});
		instructions.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFrame instructionsFrame = new JFrame("Instructions");
				JPanel instructionsPanel = new JPanel(new GridBagLayout());
				GridBagConstraints gc = new GridBagConstraints();
				
				JLabel instructions1 = new JLabel
						("Left-click to Open a square");
				JLabel instructions2 = new JLabel
						(" Right-click to Flag a square");
						
				JLabel instructions3 = new JLabel(
						" Middle-click to Probe a square");
				gc.gridy=0;
				instructionsPanel.add(instructions1, gc);
				gc.gridy=1;
				instructionsPanel.add(instructions2, gc);
				gc.gridy = 2;
				instructionsPanel.add(instructions3, gc);
				instructionsFrame.add(instructionsPanel);
				instructionsFrame.setMinimumSize(new Dimension(300, 150));
				instructionsFrame.setVisible(true);
			}
		});
		exit.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				exit();
			}

		});

		Box box = Box.createVerticalBox();

		box.add(Box.createVerticalStrut(25));
		box.add(Box.createHorizontalStrut(250));
		box.add(menu);
		box.add(Box.createVerticalStrut(25));
		box.add(newGame);
		box.add(Box.createVerticalStrut(10));
		box.add(loadGame);
		box.add(Box.createVerticalStrut(10));
		box.add(topScores);
		box.add(Box.createVerticalStrut(10));
		box.add(instructions);
		box.add(Box.createVerticalStrut(10));
		box.add(exit);		
		box.add(Box.createVerticalStrut(25));

		startMenuPanel.add(box, BorderLayout.CENTER);
		//Add swing components to main pane
		cont = getContentPane();
		
		cont.add(gamePanel, BorderLayout.CENTER);
		cont.add(startMenuPanel, BorderLayout.CENTER);
		//load top scores
		loadTopScores();
	}


	/**
	 * @return
	 */
	public static Game getGame() {
		return game;
	}


	/**
	 * @param game
	 */
	public static void setGame(Game game) {
		MainFrame.game = game;
	}


	/**
	 * @return
	 */
	public static JPanel getGamePanel() {
		return gamePanel;
	}


	/**
	 * @param gamePanel
	 */
	public static void setGamePanel(JPanel gamePanel) {
		MainFrame.gamePanel = gamePanel;
	}


	/**
	 * @return
	 */
	public static int[] getTopScores() {
		return topScores;
	}


	/**
	 * @param topScores
	 */
	public static void setTopScores(int[] topScores) {
		MainFrame.topScores = topScores;
	}
	/**
	 * @param index
	 * @param newScore
	 */
	public static void changeTopScores(int index, int newScore) {
		MainFrame.topScores[index] = newScore;
	}


	/**Show the game panel and hide the start menu
	 * 
	 */
	public static void showGamePanel(){
		startMenuPanel.setVisible(false);
		gamePanel.setVisible(true);
		playing = true;
	}
	/**Show the top panel and hide the start menu
	 * 
	 */
	public static void showTopPanel(){
		startMenuPanel.setVisible(false);
		topPanel.setVisible(true);
		playing = true;
	}
	/**show the start menu and hide and gamePanels
	 * 
	 */
	public static void showStartMenu(){
		gamePanel.setVisible(false);
		startMenuPanel.setVisible(true);
		playing = false;
		Minesweeper.resize(new Dimension(300,350));
	}


	/**
	 * Set the topScores attribute to whatever is saved in the "TopScores" File
	 */
	public static void loadTopScores(){
		topScores = new int[10];
		try {
			File aFile = new File("TopScores");
			//check if file exists
			if(aFile.exists()){
				ObjectInputStream input = new ObjectInputStream(new FileInputStream(aFile));
				topScores = (int[]) input.readObject();
				input.close();
				System.out.println("Top scores loaded");
			}
			//Create the file if it doesn't exist
			else {
				aFile.createNewFile();
				ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(aFile));
				for(int i =0; i < topScores.length; i++){
					topScores[i] = 0;
				}
				output.writeObject(topScores);
				output.flush();
				output.close();
			}

		} catch (Exception e){
			e.printStackTrace();
		}
	}

	
	/**Show the top scores (high scores)
	 * 
	 */
	public static void viewTopScores(){
		loadTopScores();
		
		
		JFrame scoreboard = new JFrame("Top Scores - Minesweeper");
		JPanel pScoreboard = new JPanel(new GridBagLayout());
		//BoxLayout bLayout = new BoxLayout(pScoreboard, BoxLayout.Y_AXIS);
		
		JLabel title = new JLabel("Top 10 Scores: ");
		scoreboard.add(title);
		
		GridBagConstraints gc = new GridBagConstraints();
		
		for(int i = 0; i<topScores.length; i++){
			gc.gridy = i +10;
			pScoreboard.add(new JLabel((i+1) + " : " + topScores[i] ), gc);
			
		}
		
		scoreboard.add(pScoreboard);
		scoreboard.setMinimumSize(new Dimension(150,300));
		
		scoreboard.setVisible(true);
		 
	}

	
	/**Creates a new game with the desired height and width in number of squares
	 * @param height
	 * @param width
	 * @return
	 */
	public static Game newGame(int height, int width){
		Game.setLives(3);
		
		//Create board
		Square[][] board = new Square[height][width];
		int count = 1;

		for(int i = 0; i< height;i++){
			for(int j = 0; j<width;j++){
				board[i][j] = new Square("", count);
				count++;
			}
		}
		//Create Rewards
		Reward rewards[] = new Reward[10];

		//Create Game
		Game newGame = new Game(board, rewards);
		return newGame;
	}
	/**Start a new game
	 * 
	 */
	public static void startNewGame(){
		currentSaveFile = null;
		Game.resetAllAttributes();
		Game.setDateStarted(new Date());
		
		
		//Create Game
		// 8x8 for beginner, 16x16 for intermediary, and 16x30 expert
		String[] choices = {"Beginner","Intermediate","Expert"};
		int result = -1;
		result = JOptionPane.showOptionDialog(cont, "Choose your difficulty: ", "Choose Difficulty", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
		Game game2 = null;
		if(result == JOptionPane.YES_OPTION){
			game2 = newGame(10,10);
		}
		if(result == JOptionPane.NO_OPTION){
			game2 = newGame(16,16);
		}
		if(result == JOptionPane.CANCEL_OPTION){
			game2 = newGame(16,30);
		}

		if(result == JOptionPane.CLOSED_OPTION){
			return;
		}
		game = game2;
		Game.setMines(game.getBoard());
		Game.setRewards(game.getBoard());
		Game.setFlags(Game.getNumMines());
		refreshGamePanel(newGamePanel());
		showGamePanel();
		showTopPanel();
	}

	/**Create a new MainFrame
	 * @return
	 */
	public static MainFrame newMainFrame(){
		Game game2 = newGame(8,8);
		//Create mainFrame
		MainFrame frame = new MainFrame("Minesweeper", game2);
		return frame;
	}

	
	/**Creates a new panel to place the board in
	 * @return
	 */
	public static JPanel newGamePanel(){
		//Refreshing
		Square[][] squareArray = game.getBoard();
		//Panel
		JPanel gPanel = new JPanel();
		int result = Game.getBoardSize();
		Dimension size;
		switch(result){
		case 1:
			size = new Dimension(600, 500);
			gPanel.setPreferredSize(size);
			break;
		case 2:
			size = new Dimension(800, 600);
			gPanel.setPreferredSize(size);
			break;
		case 3:
			size = new Dimension(1000, 600);
			gPanel.setPreferredSize(size);
			break;
		}
		gPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		gPanel.setLayout(new GridBagLayout());
		gPanel.setVisible(false);



		//System.out.println(System.getProperty("user.dir") + MainFrame.class.getResource("/resources/mine.ico").toString());


		//GridBagContraints
		//mineLabel.setVisible(true);//seems to be useless
		GridBagConstraints gc = new GridBagConstraints();

		//Creating top panel showing scores, etc.


		//topbanner_gc.anchor = GridBagConstraints.EAST;

		gc.weightx = 0;
		gc.weighty = 0;
		gc.gridx = 0;
		gc.gridy =0;
		//gc.anchor = GridBagConstraints.WEST;



		//gc.fill = GridBagConstraints.BOTH;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.gridx = 0;
		gc.gridy+=35;
		gc.anchor = GridBagConstraints.EAST;
		//Create and Add buttons to the panel
		for(Square[] bArr: squareArray){
			for(Square s: bArr){
				if(s == null){
					break;
				}				
				gPanel.add(s,gc);
				gc.gridx = gc.gridx + 1;
			}
			gc.gridx = 0;
			gc.gridy = gc.gridy + 1;
		}
		return gPanel;
	}
	/**Create a new top panel
	 * @return
	 */
	public static JPanel newTopPanel(){
		JPanel tPanel = new JPanel();
		tPanel.setPreferredSize(new Dimension(35, 35));
		tPanel.setMinimumSize(new Dimension(30, 30));
		if (Math.random() <0.5)
			tPanel.setBorder(BorderFactory.createLineBorder(Color.blue));
		else
			tPanel.setBorder(BorderFactory.createLineBorder(Color.red));
		tPanel.setLayout(new GridBagLayout());
		//mines set
		ImageIcon mineIcon = new ImageIcon("resources/mine.png");		
		JLabel mineLabel = new JLabel(mineIcon);		
		JLabel mineCount = new JLabel(Game.getNumMines() + " mines\t");

		GridBagConstraints gc = new GridBagConstraints();
		gc.ipadx = 20;
		tPanel.add(mineLabel, gc);		
		tPanel.add(mineCount, gc);
		//Lives
		ImageIcon LivesIcon = new ImageIcon("resources/smiley.png");		
		JLabel livesLabel = new JLabel(LivesIcon);		
		JLabel livesCount = new JLabel(Game.getLives() + " Lives\t");

		tPanel.add(livesLabel, gc);		
		tPanel.add(livesCount, gc);
		//probe
		JLabel probeCount = new JLabel(Game.getProbes() + " Probes\t");
		tPanel.add(probeCount, gc);
	
		
		return tPanel;

	}
	
	

	
	/**Changes the gamePanel to the given panel
	 * @param panel
	 */
	public static void refreshGamePanel(JPanel panel){
		cont.remove(gamePanel);
		MainFrame.setGamePanel(panel);
		cont.add(gamePanel, BorderLayout.CENTER);
		cont.add(topPanel, BorderLayout.NORTH);
		Minesweeper.resize(gamePanel.getPreferredSize()); //TODO
	}

	/**Load a saved game
	 * 
	 */
	public static void loadGame(){
		try{
			File saveDir = new File("saves\\");
			File saveFile;
			JFileChooser fc = new JFileChooser();
			fc.setCurrentDirectory(saveDir);
			int returnVal = fc.showOpenDialog(null);
			if(returnVal == JFileChooser.APPROVE_OPTION){
				saveFile = fc.getSelectedFile();
			} else {
				return;
			}

			if(!saveFile.exists()){
				System.out.println("No Save Found!");
			} else {
				ObjectInputStream input = new ObjectInputStream(new FileInputStream(saveFile));
				GameAttributes tempGameAtt = (GameAttributes) input.readObject();
				Game.setAllAttributes(tempGameAtt.getScore(), 
						tempGameAtt.getRewards(), tempGameAtt.getBoard(), 
						tempGameAtt.isSaved(), tempGameAtt.getFlags(), tempGameAtt.getProbes(), 
						tempGameAtt.getNumMines(), tempGameAtt.getNumMinesDetonated(), tempGameAtt.getDateStarted(), 
						tempGameAtt.getDateEnded(), tempGameAtt.getFlaggedSquares());
				//MainFrame.setGame(tempGame);			

				refreshGamePanel(newGamePanel());
				showGamePanel();

				System.out.println("Game Loaded");
				MainFrame.setCurrentSaveFile(saveFile);
				input.close();
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**File->Save
	 * @param game1 A GameAttributes object
	 */
	public static void saveGame(GameAttributes game1){
		try {
			if(currentSaveFile == null){
				saveAsGame(game1);
				return;
			}
			File saveDir = new File("saves\\");
			File saveFile = MainFrame.getCurrentSaveFile();

			if(!saveDir.exists()){
				saveDir.mkdir();
			}
			if(!saveFile.exists()){
				saveFile.createNewFile();
			}
			ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(saveFile));
			output.writeObject(game1);
			output.flush();
			output.close();

			System.out.println("Game Saved");
			Game.setSaved(true);
			MainFrame.setCurrentSaveFile(saveFile);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	//Method to Save the game (SAVE AS)
	/**File->SaveAs
	 * @param game1 A GameAttributes object
	 */
	public static void saveAsGame(GameAttributes game1){
		try {
			File saveDir = new File("saves\\");
			File saveFile;

			JFileChooser fc = new JFileChooser();
			fc.setCurrentDirectory(saveDir);
			int returnVal = fc.showSaveDialog(null);
			if(returnVal == JFileChooser.APPROVE_OPTION){
				saveFile = fc.getSelectedFile();
			} else {
				return;
			}
			//File saveFile = new File("saves\\Save");
			if(!saveDir.exists()){
				saveDir.mkdir();
			}
			if(!saveFile.exists()){
				saveFile.createNewFile();
			}
			ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(saveFile));
			output.writeObject(game1);
			output.flush();
			output.close();

			System.out.println("Game Saved");
			Game.setSaved(true);
			MainFrame.setCurrentSaveFile(saveFile);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**exit the game
	 * 
	 */
	public static void exitGame(){
		int returnVal = 1;
		if(!Game.getSaved()){
			returnVal = askToSave();
		}
		
		if(returnVal != 0){
			System.exit(0);
		}
	}
	/**
	 * 
	 */
	public static void exit(){
		
		System.exit(0);
	}

	/**ask whether or not the user wants to save his progress
	 * @return
	 */
	public static int askToSave(){
		int op = JOptionPane.showConfirmDialog(null, "Do you want to save before quitting? Any unsaved progress will be lost,");

		if(op == JOptionPane.CANCEL_OPTION || op == JOptionPane.CLOSED_OPTION){
			return 0;
		} else if(op == JOptionPane.YES_OPTION){
			saveAsGame(Game.serializableAttributes());
			return 1;
		}
		else if(op == JOptionPane.NO_OPTION){
			return 2;
		}
		return 0;
	}


	
	/**Save the top scores
	 * 
	 */
	public static void saveTopScores(){
		try {
			File saveTopScores = new File("TopScores");
			if(!saveTopScores.exists()){
				saveTopScores.createNewFile();
			}
			ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(saveTopScores));
			output.writeObject(topScores);
			output.flush();
			output.close();
			System.out.println("Top Scores Saved");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @return
	 */
	public static JPanel getTopPanel() {
		return topPanel;
	}
	

	/**
	 * @param topPanel
	 */
	public static void setTopPanel(JPanel topPanel) {
		MainFrame.topPanel = topPanel;
	}
	/**
	 * @return
	 */
	public static File getCurrentSaveFile() {
		return currentSaveFile;
	}


	/**
	 * @param currentSaveFile
	 */
	public static void setCurrentSaveFile(File currentSaveFile) {
		MainFrame.currentSaveFile = currentSaveFile;
	}
}


