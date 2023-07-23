import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;



public class PausePanel extends JPanel implements ActionListener {
	private int panelHeight = Main.BUTTONS_HEIGHT * 3; //we have 3 buttons so the height of the panel is BUTTONS_HEIGHT * 3
	private Level level;
	private JButton resumeButton;
	private JButton mainMenuButton;
	private JButton restartButton;
	private JButton exitButton;
	private GamePanel gamePanel;
	
	
	
	public PausePanel(Level level, GamePanel gamePanel) {
		this.level = level;
		this.gamePanel = gamePanel;
		
		//set up the JPanel properties
		this.setLayout(null);
		this.setBackground(new Color(0, 0, 0, 0));
		this.setBounds(Level.maxWidth/2 - Main.BUTTONS_WIDTH/2, Level.maxHeight/2 - this.panelHeight/2, Main.BUTTONS_WIDTH, this.panelHeight); //same width of the buttons inside
		this.setOpaque(false);
		
		//button to resume the game
		resumeButton = new JButton();
		resumeButton.addActionListener(this);
		resumeButton.setBounds(0, 0, Main.BUTTONS_WIDTH, Main.BUTTONS_HEIGHT);		
		ImageIcon resumeButtonIcon = new ImageIcon(this.getClass().getResource("/res/button.png")); //read the image
		resumeButtonIcon = ImgProcessor.scaleImage(resumeButtonIcon, Main.BUTTONS_WIDTH, Main.BUTTONS_HEIGHT); //resize
		//these instructions make the background of the button transparent so that we only see the image drawn on it
		resumeButton.setBackground(this.getBackground());
		resumeButton.setOpaque(false); // Must add
		resumeButton.setContentAreaFilled(false); // No fill
		resumeButton.setBorderPainted(true); // I'd like to enable it.
		resumeButton.setBorder(null); // Border (No border for now)
		resumeButton.setIcon(resumeButtonIcon);
		resumeButton.setFont(Main.buttonsFont);
		resumeButton.setHorizontalTextPosition(SwingConstants.CENTER);
		resumeButton.setForeground(Color.black);
		resumeButton.setText("Riprendi");
		this.add(resumeButton);
		
		//button to restart the game at the same difficulty
		restartButton = new JButton();
		restartButton.addActionListener(this);
		restartButton.setBounds(0, Main.BUTTONS_HEIGHT, Main.BUTTONS_WIDTH, Main.BUTTONS_HEIGHT);		
		ImageIcon restartButtonIcon = new ImageIcon(this.getClass().getResource("/res/button.png")); //read the image
		restartButtonIcon = ImgProcessor.scaleImage(restartButtonIcon, Main.BUTTONS_WIDTH, Main.BUTTONS_HEIGHT); //resize
		//these instructions make the background of the button transparent so that we only see the image drawn on it
		restartButton.setBackground(this.getBackground());
		restartButton.setOpaque(false); // Must add
		restartButton.setContentAreaFilled(false); // No fill
		restartButton.setBorderPainted(true); // I'd like to enable it.
		restartButton.setBorder(null); // Border (No border for now)
		restartButton.setIcon(restartButtonIcon);
		restartButton.setFont(Main.buttonsFont);
		restartButton.setHorizontalTextPosition(SwingConstants.CENTER);
		restartButton.setForeground(Color.black);
		restartButton.setText("Ricomincia");
		this.add(restartButton);
		
		//button to return to the main menu
		mainMenuButton = new JButton();
		mainMenuButton.addActionListener(this);
		mainMenuButton.setBounds(0, Main.BUTTONS_HEIGHT*2, Main.BUTTONS_WIDTH, Main.BUTTONS_HEIGHT);
		ImageIcon mainMenuButtonIcon = new ImageIcon(this.getClass().getResource("/res/button.png")); //read the image
		mainMenuButtonIcon = ImgProcessor.scaleImage(mainMenuButtonIcon, Main.BUTTONS_WIDTH, Main.BUTTONS_HEIGHT); //resize
		//these instructions make the background of the button transparent so that we only see the image drawn on it
		mainMenuButton.setBackground(this.getBackground());
		mainMenuButton.setOpaque(false); // Must add
		mainMenuButton.setContentAreaFilled(false); // No fill
		mainMenuButton.setBorderPainted(true); // I'd like to enable it.
		mainMenuButton.setBorder(null); // Border (No border for now)
		mainMenuButton.setIcon(mainMenuButtonIcon);
		mainMenuButton.setFont(Main.buttonsFont);
		mainMenuButton.setHorizontalTextPosition(SwingConstants.CENTER);
		mainMenuButton.setForeground(Color.black);
		mainMenuButton.setText("Torna al menu");
		this.add(mainMenuButton);
	}

	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		//resume the game
		if (e.getSource() == resumeButton) {
			level.resume();
			
			gamePanel.remove(this); //remove the pausePanel from the JFrame
			gamePanel.revalidate();
			gamePanel.repaint();
		}
		
		//restart the level at same difficulty
		if (e.getSource() == restartButton) {
			int difficulty_tmp = Level.chosenDifficulty;
			level.endGame(true); //delete all game-related info and remove the gamePanel from the JFrame
			
			Main.appFrame.removePanel(this); //remove the pausePanel from the JFrame
			
			//recreate the level at the previously chosen difficulty
			switch (difficulty_tmp) {
				case 0:
					Level trainingLevel = new Level(0); //instantiate the training level
					trainingLevel.start();
					break;
				case 1:
					Level baseLevel = new Level(1); //instantiate the level at medium difficulty
					baseLevel.start();
					break;
				case 2:
					Level intermediateLevel = new Level(2); //instantiate the level at medium difficulty
					intermediateLevel.start();
					break;
				case 3:
					Level advancedLevel = new Level(3); //instantiate the level at medium difficulty
					advancedLevel.start();
					break;
			}
		}
		
		//go back to the main menu
		if (e.getSource() == mainMenuButton) {			
			level.endGame(true); //delete all game-related info and remove the gamePanel from the JFrame
			
			//"go back" to the main menu, adding to the frame the corresponding panel
			MenuPanel menuPanel = new MenuPanel();
			Main.appFrame.addPanel(menuPanel);
		}
		
		//exit the game
		if (e.getSource() == exitButton) {
			System.exit(0);
		}
	}
}