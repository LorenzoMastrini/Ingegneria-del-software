import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;



public class EndGamePanel extends JPanel implements ActionListener {
	private JButton mainMenuButton;
	private JButton restartButton;
	private JButton quitButton;
	
	
	
	public EndGamePanel(Level level) {
		//sets up the JPanel properties
		setPreferredSize(new Dimension(Level.maxWidth, Level.maxHeight));
		this.setLayout(null); //we manually set the components based on the maximum supported dimensions of the camera frame
		
		//the background also contains an image to show the result of the game
		ImageIcon backgroundIcon;
		if (level.victory == true) {
			backgroundIcon = new ImageIcon(this.getClass().getResource("/res/win_bg.png")); //read the image
		} else {
			backgroundIcon = new ImageIcon(this.getClass().getResource("/res/lose_bg.png")); //read the image
		}
		backgroundIcon = ImgProcessor.scaleImage(backgroundIcon, backgroundIcon.getIconWidth(), Level.maxHeight); //resize to fit the height
		JLabel backgroundLabel = new JLabel(backgroundIcon, JLabel.CENTER);
		backgroundLabel.setBounds(0, 0, Level.maxWidth, Level.maxHeight); //perfectly fits the frame
		backgroundLabel.setIcon(backgroundIcon);
		this.add(backgroundLabel);
		
		//button to restart the game at the same difficulty
		restartButton = new JButton();
		restartButton.addActionListener(this);
		restartButton.setBounds(Level.maxWidth/2 - Main.BUTTONS_WIDTH/2, Level.maxHeight/2, Main.BUTTONS_WIDTH, Main.BUTTONS_HEIGHT);		
		ImageIcon restartButtonIcon = new ImageIcon(this.getClass().getResource("/res/button.png")); //read the image
		restartButtonIcon = ImgProcessor.scaleImage(restartButtonIcon, Main.BUTTONS_WIDTH, Main.BUTTONS_HEIGHT); //resize
		this.restartButton.setIcon(restartButtonIcon);
		//these instructions make the background of the button transparent so that we only see the image drawn on it
		restartButton.setBorderPainted(false);
		restartButton.setContentAreaFilled(false);
		//set text
		restartButton.setFont(Main.buttonsFont);
		restartButton.setHorizontalTextPosition(SwingConstants.CENTER);
		restartButton.setForeground(Color.black);
		restartButton.setText("Ricomincia");
		backgroundLabel.add(restartButton);
		
		//button to return to the main menu
		mainMenuButton = new JButton();
		mainMenuButton.addActionListener(this);
		mainMenuButton.setBounds(Level.maxWidth/2 - Main.BUTTONS_WIDTH/2, Level.maxHeight/2 + Main.BUTTONS_HEIGHT, Main.BUTTONS_WIDTH, Main.BUTTONS_HEIGHT);		
		ImageIcon mainMenuButtonIcon = new ImageIcon(this.getClass().getResource("/res/button.png")); //read the image
		mainMenuButtonIcon = ImgProcessor.scaleImage(mainMenuButtonIcon, Main.BUTTONS_WIDTH, Main.BUTTONS_HEIGHT); //resize
		this.mainMenuButton.setIcon(mainMenuButtonIcon);
		//these instructions make the background of the button transparent so that we only see the image drawn on it
		mainMenuButton.setBorderPainted(false); 
		mainMenuButton.setContentAreaFilled(false);
		//set text
		mainMenuButton.setFont(Main.buttonsFont);
		mainMenuButton.setHorizontalTextPosition(SwingConstants.CENTER);
		mainMenuButton.setForeground(Color.black);
		mainMenuButton.setText("Torna al menu");
		backgroundLabel.add(mainMenuButton);

		Main.appFrame.revalidate();
		Main.appFrame.repaint();
	}

	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		//go back to the main menu
		if (e.getSource() == mainMenuButton) {
			//"go back" to the main menu, adding to the frame the corresponding panel
			MenuPanel menuPanel = new MenuPanel();
			Main.appFrame.addPanel(menuPanel);
		}
		
		//restart the level at same difficulty
		if (e.getSource() == restartButton) {
			//recreate the level at the previously chosen difficulty
			switch (Level.chosenDifficulty) {
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
		
		//exit the game
		if (e.getSource() == quitButton) {
			System.exit(0);
		}
		
		//delete the current panel from the frame, no matter which button is pressed
		Main.appFrame.removePanel(this); //remove the endGamePanel from the JFrame
	}
}