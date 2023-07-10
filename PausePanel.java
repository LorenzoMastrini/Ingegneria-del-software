import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;


public class PausePanel extends JPanel implements ActionListener {
	private int buttonsHeight = 50;
	private int width = 200; //same width for the panel and each button
	private int height = buttonsHeight * 4; //we have 4 buttons so the height of the panel is buttonsHeight * 4
	private Level level;
	private JButton resumeButton;
	private JButton mainMenuButton;
	private JButton restartButton;
	private JButton quitButton;
	private GamePanel gamePanel;
	
	
	
	public PausePanel(Level level, GamePanel gamePanel) {
		this.level = level;
		this.gamePanel = gamePanel;
		
		//button to resume the game
		resumeButton = new JButton();
		resumeButton.addActionListener(this);
		resumeButton.setBounds(0, 0, width, buttonsHeight);
		resumeButton.setText("Riprendi");
		resumeButton.setFocusable(false);
		this.add(resumeButton);
		
		//button to restart the game at the same difficulty
		restartButton = new JButton();
		restartButton.addActionListener(this);
		restartButton.setBounds(0, buttonsHeight, width, buttonsHeight);
		restartButton.setText("Ricomincia");
		restartButton.setFocusable(false);
		this.add(restartButton);
		
		//button to return to the main menu
		mainMenuButton = new JButton();
		mainMenuButton.addActionListener(this);
		mainMenuButton.setBounds(0, buttonsHeight*2, width, buttonsHeight);
		mainMenuButton.setText("Torna al menu principale");
		mainMenuButton.setFocusable(false);
		this.add(mainMenuButton);
		
		//button for exiting the game
		quitButton = new JButton();
		quitButton.addActionListener(this);
		quitButton.setBounds(0, buttonsHeight*3, width, buttonsHeight);
		quitButton.setText("Esci dal gioco");
		quitButton.setFocusable(false);
		this.add(quitButton);
		
		//set up the JPanel properties
		this.setBackground(Color.white);
		this.setBounds(Level.MAX_WIDTH/2 - width/2, Level.MAX_HEIGHT/2 - height/2, this.width, this.height);
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
			
			Menu.appFrame.getContentPane().remove(this); //remove the pausePanel from the JFrame
			Menu.appFrame.revalidate();
			Menu.appFrame.repaint();
			
			//recreate the level at the previously chosen difficulty
			switch (difficulty_tmp) {
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
			//delete the current panel from the frame
			Menu.appFrame.getContentPane().remove(this); //remove the pausePanel from the JFrame
			
			level.endGame(true); //delete all game-related info and remove the gamePanel from the JFrame
			
			//"go back" to the main menu, adding to the frame the corresponding panel
			MenuPanel menuPanel = new MenuPanel();
			Menu.appFrame.add(menuPanel);
			Menu.appFrame.revalidate();
			Menu.appFrame.repaint();
		}
		
		//exit the game
		if (e.getSource() == quitButton) {
			System.exit(0);
		}
	}
}
