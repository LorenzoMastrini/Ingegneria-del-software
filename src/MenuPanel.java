import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;



public class MenuPanel extends JPanel implements ActionListener {
	private final int SMALLER_BUTTONS_WIDTH = 150;
	private final int SMALLER_BUTTONS_HEIGHT = 40;
	private boolean alreadyClicked = false;
	private JButton easyLvButton;
	private JButton intermediateLvButton;
	private JButton advancedLvButton;
	private JButton trainingLvButton;
	private JButton infoButton;
	private JButton exitButton;
	private JLabel infoLabel;
	private JLabel backgroundLabel;
	private JLabel selectLvLabel;
	
	
	
	public MenuPanel() {
		//sets up the JPanel properties
		setPreferredSize(new Dimension(Level.maxWidth, Level.maxHeight));
		this.setLayout(null); //we manually set the components based on the maximum supported dimensions of the camera frame

		//background (with title)
		ImageIcon backgroundIcon = new ImageIcon(this.getClass().getResource("/res/menu_bg_logo.png")); //read the background image
		backgroundIcon = ImgProcessor.scaleImage(backgroundIcon, backgroundIcon.getIconWidth(), Level.maxHeight); //resize to fit the height
		backgroundLabel = new JLabel(backgroundIcon, JLabel.CENTER);
		backgroundLabel.setBounds(0, 0, Level.maxWidth, Level.maxHeight); //perfectly fits the frame
		this.add(backgroundLabel);
		
		//label 'Seleziona livello'
		selectLvLabel = new JLabel();
		selectLvLabel.setFont(Main.smallerFont);
		selectLvLabel.setText("<html>Seleziona un livello <br/>"
							+ "per giocare:<html>");
		selectLvLabel.setForeground(Color.white);
		selectLvLabel.setBounds(Level.maxWidth/2 - Main.BUTTONS_WIDTH/2, Level.maxHeight/2 - Main.BUTTONS_HEIGHT, Main.BUTTONS_WIDTH, Main.BUTTONS_HEIGHT); //same dimensions of the buttons
		selectLvLabel.setHorizontalAlignment(SwingConstants.LEFT);
		backgroundLabel.add(selectLvLabel, JLabel.CENTER);			
	    
		//read the image that represents each button
		ImageIcon buttonIcon = new ImageIcon(this.getClass().getResource("/res/button.png")); //read the image
		buttonIcon = ImgProcessor.scaleImage(buttonIcon, Main.BUTTONS_WIDTH, Main.BUTTONS_HEIGHT); //resize; this dimensions are used for the main buttons
		
		//button for the easy level
		easyLvButton = new JButton();
		easyLvButton.addActionListener(this);
		easyLvButton.setBounds(Level.maxWidth/2 - Main.BUTTONS_WIDTH/2, Level.maxHeight/2, Main.BUTTONS_WIDTH, Main.BUTTONS_HEIGHT);		 
		this.easyLvButton.setIcon(buttonIcon);
		//these instructions make the background of the button transparent so that we only see the image drawn on it
		easyLvButton.setBorderPainted(false); 
		easyLvButton.setContentAreaFilled(false);
		easyLvButton.setFont(Main.buttonsFont);
		easyLvButton.setHorizontalTextPosition(SwingConstants.CENTER);
		easyLvButton.setForeground(Color.black);
		easyLvButton.setText("Facile");
		backgroundLabel.add(easyLvButton);

		//button for the intermediate level
		intermediateLvButton = new JButton();
		intermediateLvButton.addActionListener(this);
		intermediateLvButton.setBounds(Level.maxWidth/2 - Main.BUTTONS_WIDTH/2, Level.maxHeight/2 + Main.BUTTONS_HEIGHT, Main.BUTTONS_WIDTH, Main.BUTTONS_HEIGHT);
		this.intermediateLvButton.setIcon(buttonIcon);
		//these instructions make the background of the button transparent so that we only see the image drawn on it
		intermediateLvButton.setBorderPainted(false); 
		intermediateLvButton.setContentAreaFilled(false);
		intermediateLvButton.setFont(Main.buttonsFont);
		intermediateLvButton.setHorizontalTextPosition(SwingConstants.CENTER);
		intermediateLvButton.setForeground(Color.black);
		intermediateLvButton.setText("Intermedio");
		backgroundLabel.add(intermediateLvButton);

		//button for the advanced level
		advancedLvButton = new JButton();
		advancedLvButton.addActionListener(this);
		advancedLvButton.setBounds(Level.maxWidth/2 - Main.BUTTONS_WIDTH/2, Level.maxHeight/2 + Main.BUTTONS_HEIGHT*2, Main.BUTTONS_WIDTH, Main.BUTTONS_HEIGHT);
		this.advancedLvButton.setIcon(buttonIcon);
		//these instructions make the background of the button transparent so that we only see the image drawn on it
		advancedLvButton.setBorderPainted(false); 
		advancedLvButton.setContentAreaFilled(false);
		advancedLvButton.setFont(Main.buttonsFont);
		advancedLvButton.setHorizontalTextPosition(SwingConstants.CENTER);
		advancedLvButton.setForeground(Color.black);
		advancedLvButton.setText("Avanzato");
		backgroundLabel.add(advancedLvButton);

		//button for the training level
		trainingLvButton = new JButton();
		trainingLvButton.addActionListener(this);
		trainingLvButton.setBounds(Main.BORDERS_LIMIT, Level.maxHeight - this.SMALLER_BUTTONS_HEIGHT - Main.BORDERS_LIMIT, this.SMALLER_BUTTONS_WIDTH, this.SMALLER_BUTTONS_HEIGHT);
		buttonIcon = ImgProcessor.scaleImage(buttonIcon, this.SMALLER_BUTTONS_WIDTH, this.SMALLER_BUTTONS_HEIGHT); //resize; this dimensions are used for the smaller buttons
		this.trainingLvButton.setIcon(buttonIcon);
		//these instructions make the background of the button transparent so that we only see the image drawn on it
		trainingLvButton.setBorderPainted(false); 
		trainingLvButton.setContentAreaFilled(false);
		trainingLvButton.setFont(Main.smallerFont);
		trainingLvButton.setHorizontalTextPosition(SwingConstants.CENTER);
		trainingLvButton.setForeground(Color.black);
		trainingLvButton.setText("Allenamento");
		backgroundLabel.add(trainingLvButton);

		//button used to exit the game
		exitButton = new JButton();
		exitButton.addActionListener(this);
		exitButton.setBounds(Level.maxWidth - this.SMALLER_BUTTONS_WIDTH - Main.BORDERS_LIMIT, Level.maxHeight - this.SMALLER_BUTTONS_HEIGHT - Main.BORDERS_LIMIT, this.SMALLER_BUTTONS_WIDTH, this.SMALLER_BUTTONS_HEIGHT);
		buttonIcon = ImgProcessor.scaleImage(buttonIcon, this.SMALLER_BUTTONS_WIDTH, this.SMALLER_BUTTONS_HEIGHT); //resize; this dimensions are used for the smaller buttons
		this.exitButton.setIcon(buttonIcon);
		//these instructions make the background of the button transparent so that we only see the image drawn on it
		exitButton.setBorderPainted(false); 
		exitButton.setContentAreaFilled(false);
		exitButton.setFont(Main.smallerFont);
		exitButton.setHorizontalTextPosition(SwingConstants.CENTER);
		exitButton.setForeground(Color.black);
		exitButton.setText("Esci dal gioco");
		backgroundLabel.add(exitButton);
		
		//button for help
		infoButton = new JButton();
		infoButton.addActionListener(this);
		infoButton.setBounds(Main.BORDERS_LIMIT, Main.BORDERS_LIMIT, Main.ROUND_BUTTONS_DIMENSION, Main.ROUND_BUTTONS_DIMENSION); //same height of smaller buttons and equals width because it is a circle
		ImageIcon infoButtonIcon = new ImageIcon(this.getClass().getResource("/res/help_button.png")); //read the image
		infoButtonIcon = ImgProcessor.scaleImage(infoButtonIcon, Main.ROUND_BUTTONS_DIMENSION, Main.ROUND_BUTTONS_DIMENSION); //resize
		this.infoButton.setIcon(infoButtonIcon);
		//these instructions make the background of the button transparent so that we only see the image drawn on it
		infoButton.setBorderPainted(false); 
		infoButton.setContentAreaFilled(false);
		infoButton.setFocusable(false);
		backgroundLabel.add(infoButton);
	}
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		//level 1
		if (e.getSource() == easyLvButton) {
			easyLvButton.setEnabled(false);
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
		
		//training level
		if (e.getSource() == trainingLvButton) {
			Level tutorialLevel = new Level(0); //instantiate the level at medium difficulty
			tutorialLevel.start();
		}
		
		//help
		if(e.getSource() == infoButton && alreadyClicked == false) {
			selectLvLabel.setVisible(false);
			easyLvButton.setVisible(false);
			intermediateLvButton.setVisible(false);
			advancedLvButton.setVisible(false);
			trainingLvButton.setVisible(false);
			exitButton.setVisible(false);
			
			infoLabel = new JLabel();
			infoLabel.setFont(Main.smallerFont);
			infoLabel.setText("<html>Benvenuto in Catchem_CV! Prima di cominciare ecco alcune avvertenze per giocare al meglio:<br/>"
			                  + "1) è necessaria una fotocamera frontale<br/>"
			                  + "2) è preferibile stare in un posto ben illuminato<br/>"
			                  + "3) è preferibile giocare davanti ad uno sfondo neutro<br/><br/>"
			                  + "Durante il gioco appariranno delle palline gialle e il tuo scopo sarà guidarle all'interno del portale per guadagnare punti, ma attenzione! "
			                  + "Se entreranno delle palline rosse nel portale, i tuoi punti scenderanno!<br/>"
			                  + "Il livello 'addestramento' ti da la possibilità di allenarti quanto vuoi con una pallina alla volta.<br/><br/>"
			                  + "Buon divertimento!<html>");
			infoLabel.setForeground(Color.white);
			infoLabel.setBounds(Level.maxWidth/2 - 250, 150, 500, 500);
			infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
			infoLabel.setVerticalAlignment(SwingConstants.TOP);
			backgroundLabel.add(infoLabel, JLabel.CENTER);			
		    infoLabel.setVisible(true);
		    
		    alreadyClicked = true;
		} else if (e.getSource() == infoButton && alreadyClicked == true){
			selectLvLabel.setVisible(true);
			easyLvButton.setVisible(true);
			intermediateLvButton.setVisible(true);
			advancedLvButton.setVisible(true);
			trainingLvButton.setVisible(true);
			exitButton.setVisible(true);
		    infoLabel.setVisible(false);
		    alreadyClicked = false;
		}
		
		//exit the game
		if (e.getSource() == exitButton) {
			System.exit(0);
		}
		
		if (e.getSource() != infoButton) {
			//when a button is pressed we have to change the JPanel on the frame, so we delete this panel from the frame
			Main.appFrame.removePanel(this); //remove the menuPanel from the JFrame
		}
	}
}
