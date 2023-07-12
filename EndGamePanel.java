import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Image;

public class EndGamePanel extends JPanel implements ActionListener {
	private JButton mainMenuButton;
	private JButton restartButton;
	private JButton quitButton;
	
	private int MAX_WIDTH = 640;
	private int MAX_HEIGHT = 480;
	
	
	
	public EndGamePanel(Level level) {
		int buttonWidth = 250;
		int buttonHeight = 100;
		int secondaryButtonsWidth = 150;
		int secondaryButtonsHeight = 50;
		Image resultImg; //this image will contain the win or lose image, based on the result of the game
		
		//read the images to be drawn on screen from the res folder (target, green ball, red ball)
		resultImg = new BufferedImage(Target.width, Target.height, BufferedImage.TYPE_3BYTE_BGR);
		
		//draw an image to show the result of the game
		JLabel label = new JLabel();
		label.setHorizontalAlignment(SwingConstants.CENTER);
		if (level.victory == true) {
			try {
				resultImg = ImageIO.read(new File("res/win_img.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				resultImg = ImageIO.read(new File("res/lose_img.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		resultImg = resultImg.getScaledInstance(buttonWidth, buttonWidth, Image.SCALE_SMOOTH); //resize the image
		ImageIcon icon = new ImageIcon(resultImg);
		label.setIcon(icon);
		label.setBounds(MAX_WIDTH/2 - buttonWidth/2, 10, buttonWidth, buttonWidth); //same size of the mainMenuButton horizontally
		this.add(label);
		
		//button to return to the main menu
		mainMenuButton = new JButton();
		mainMenuButton.addActionListener(this);
		mainMenuButton.setBounds(MAX_WIDTH/2 - buttonWidth/2, MAX_HEIGHT/2 + 50, buttonWidth, buttonHeight);
		mainMenuButton.setText("Torna al menu principale");
		mainMenuButton.setFocusable(false);
		this.add(mainMenuButton);
		
		//button to restart the game at the same difficulty
		restartButton = new JButton();
		restartButton.addActionListener(this);
		restartButton.setBounds(0, MAX_HEIGHT - secondaryButtonsHeight, secondaryButtonsWidth, secondaryButtonsHeight);
		restartButton.setText("Ricomincia");
		restartButton.setFocusable(false);
		this.add(restartButton);
		
		//button for exiting the game
		quitButton = new JButton();
		quitButton.addActionListener(this);
		quitButton.setBounds(MAX_WIDTH - secondaryButtonsWidth, MAX_HEIGHT - secondaryButtonsHeight, secondaryButtonsWidth, secondaryButtonsHeight);
		quitButton.setText("Esci dal gioco");
		quitButton.setFocusable(false);
		this.add(quitButton);
		
		//sets up the JPanel properties
		//this.setBackground(Color.white);
		this.setBounds(0, 0, MAX_WIDTH, MAX_HEIGHT);
		this.setLayout(new BorderLayout());
		
		Menu.appFrame.revalidate();
		Menu.appFrame.repaint();
	}

	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		//go back to the main menu
		if (e.getSource() == mainMenuButton) {
			//delete the current panel from the frame
			Menu.appFrame.getContentPane().removeAll(); //remove the endGamePanel from the JFrame
			Menu.appFrame.revalidate();
			Menu.appFrame.repaint();
			
			//"go back" to the main menu, adding to the frame the corresponding panel
			MenuPanel menuPanel = new MenuPanel();
			Menu.appFrame.getContentPane().add(menuPanel);
			Menu.appFrame.revalidate();
			Menu.appFrame.repaint();
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
			
			//in this case we have to delete the current panel from the frame, because the constructor of the Level class will add the game panel
			Menu.appFrame.getContentPane().removeAll(); //remove the menuPanel from the JFrame
			Menu.appFrame.revalidate();
			Menu.appFrame.repaint();
		}
		
		//exit the game
		if (e.getSource() == quitButton) {
			System.exit(0);
		}
	}
}