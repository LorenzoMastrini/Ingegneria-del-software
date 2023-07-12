import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
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

public class MenuPanel extends JPanel implements ActionListener {
	private JButton baseLvButton;
	private JButton intermediateLvButton;
	private JButton advancedLvButton;
	private JButton trainingLvButton;
	private JButton quitButton;
	
	private int MAX_WIDTH = 640;
	private int MAX_HEIGHT = 480;
	
	
	
	public MenuPanel() {
		setPreferredSize(new Dimension(MAX_WIDTH, MAX_HEIGHT));
		//sets up the JPanel properties
		this.setLayout(null);
		/*
		//draw an image to show the result of the game
		Image backgroundImg = new BufferedImage(MAX_WIDTH, MAX_HEIGHT, BufferedImage.TYPE_3BYTE_BGR); 
		JLabel background = new JLabel();
		try {
			backgroundImg = ImageIO.read(new File("./res/menu_background.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		backgroundImg = backgroundImg.getScaledInstance(MAX_WIDTH, MAX_HEIGHT, Image.SCALE_SMOOTH); //resize the image
		ImageIcon icon = new ImageIcon(backgroundImg);
		background.setIcon(icon);
		background.setBounds(0, 0, MAX_WIDTH, MAX_HEIGHT); //same size of the mainMenuButton horizontally
		this.add(background);
		*/
		
		int buttonsWidth = 250;
		int buttonsHeight = 100;
		int secondaryButtonsWidth = 150;
		int secondaryButtonsHeight = 50;
		
		//Title
		JLabel label = new JLabel();
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setText("CatcheM_CV");
		label.setForeground(Color.black);
		label.setBounds(MAX_WIDTH/2 - 50, 10, 100, 50);
		this.add(label);
		
		//button for the base level
		baseLvButton = new JButton();
		baseLvButton.addActionListener(this);
		baseLvButton.setBounds(MAX_WIDTH/2 - buttonsWidth/2, 80, buttonsWidth, buttonsHeight);
		baseLvButton.setText("Livello base");
		baseLvButton.setFocusable(false);
		this.add(baseLvButton);
		
		//button for the intermediate level
		intermediateLvButton = new JButton();
		intermediateLvButton.addActionListener(this);
		intermediateLvButton.setBounds(MAX_WIDTH/2 - buttonsWidth/2, 80 + buttonsHeight, buttonsWidth, buttonsHeight);
		intermediateLvButton.setText("Livello intermedio");
		intermediateLvButton.setFocusable(false);
		this.add(intermediateLvButton);
		
		//button for the advanced level
		advancedLvButton = new JButton();
		advancedLvButton.addActionListener(this);
		advancedLvButton.setBounds(MAX_WIDTH/2 - buttonsWidth/2, 80 + buttonsHeight*2, buttonsWidth, buttonsHeight);
		advancedLvButton.setText("Livello avanzato");
		advancedLvButton.setFocusable(false);
		this.add(advancedLvButton);
		
		//button for the training level
		trainingLvButton = new JButton();
		trainingLvButton.addActionListener(this);
		trainingLvButton.setBounds(0, MAX_HEIGHT - secondaryButtonsHeight, secondaryButtonsWidth, secondaryButtonsHeight);
		trainingLvButton.setText("Addestramento");
		trainingLvButton.setFocusable(false);
		this.add(trainingLvButton);
		
		//button for exiting the game
		quitButton = new JButton();
		quitButton.addActionListener(this);
		quitButton.setBounds(MAX_WIDTH - secondaryButtonsWidth, MAX_HEIGHT - secondaryButtonsHeight, secondaryButtonsWidth, secondaryButtonsHeight);
		quitButton.setText("Esci dal gioco");
		quitButton.setFocusable(false);
		this.add(quitButton);
	}

	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		//level 1
		if (e.getSource() == baseLvButton) {
			Level baseLevel = new Level(1); //instantiate the level at base difficulty
	        baseLevel.start();
		}
		
		//level 2
		if (e.getSource() == intermediateLvButton) {
			Level intermediateLevel = new Level(2); //instantiate the level at medium difficulty
			intermediateLevel.start();
		}
		
		//level 3
		if (e.getSource() == advancedLvButton) {
			Level advancedLevel = new Level(3); //instantiate the level at high difficulty
			advancedLevel.start();
		}
		
		//introduction level
		if (e.getSource() == trainingLvButton) {
			Level tutorialLevel = new Level(0); //instantiate the level at medium difficulty
			tutorialLevel.start();
		}
		
		//exit the game
		if (e.getSource() == quitButton) {
			System.exit(0);
		}
		
		//when a button is pressed we have to change the JPanel on the frame, so we delete the current panel from the frame
		Menu.appFrame.getContentPane().removeAll(); //remove the menuPanel from the JFrame
		Menu.appFrame.revalidate();
		Menu.appFrame.repaint();
	}
}
