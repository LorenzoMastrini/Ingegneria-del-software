import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

public class EndGamePanel extends JPanel implements ActionListener {
	private JButton mainMenuButton;
	private JButton restartButton;
	private JButton quitButton;
	
	
	
	public EndGamePanel() {
		//button to return to the main menu
		mainMenuButton = new JButton();
		mainMenuButton.addActionListener(this);
		mainMenuButton.setBounds(200, 100, 300, 100);
		mainMenuButton.setText("Torna al menu principale");
		mainMenuButton.setFocusable(false);
		this.add(mainMenuButton);
		
		//button to restart the game at the same difficulty
		restartButton = new JButton();
		restartButton.addActionListener(this);
		restartButton.setBounds(0, 418, 200, 50);
		restartButton.setText("Ricomincia");
		restartButton.setFocusable(false);
		this.add(restartButton);
		
		//button for exiting the game
		quitButton = new JButton();
		quitButton.addActionListener(this);
		quitButton.setBounds(450, 418, 200, 50);
		quitButton.setText("Esci dal gioco");
		quitButton.setFocusable(false);
		this.add(quitButton);
		
		//sets up the JPanel properties
		this.setBackground(Color.white);
		this.setBounds(0, 0, Level.MAX_WIDTH, Level.MAX_HEIGHT);
		this.setLayout(new BorderLayout());
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
			Menu.appFrame.add(menuPanel);
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
