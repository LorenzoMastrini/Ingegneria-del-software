import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MenuPanel extends JPanel implements ActionListener {
	private JButton baseLvButton;
	private JButton intermediateLvButton;
	private JButton advancedLvButton;
	private JButton tutorialLvButton;
	private JButton quitButton;
	
	
	
	public MenuPanel() {		
		//Title
		JLabel label = new JLabel();
		label.setText("CatcheM_CV");
		label.setForeground(Color.black);
		label.setBounds(290, 10, 100, 50);
		this.add(label);
		
		//button for the base level
		baseLvButton = new JButton();
		baseLvButton.addActionListener(this);
		baseLvButton.setBounds(200, 100, 250, 100);
		baseLvButton.setText("Livello base");
		baseLvButton.setFocusable(false);
		this.add(baseLvButton);
		
		//button for the intermediate level
		intermediateLvButton = new JButton();
		intermediateLvButton.addActionListener(this);
		intermediateLvButton.setBounds(200, 200, 250, 100);
		intermediateLvButton.setText("Livello intermedio");
		intermediateLvButton.setFocusable(false);
		this.add(intermediateLvButton);
		
		//button for the advanced level
		advancedLvButton = new JButton();
		advancedLvButton.addActionListener(this);
		advancedLvButton.setBounds(200, 300, 250, 100);
		advancedLvButton.setText("Livello avanzato");
		advancedLvButton.setFocusable(false);
		this.add(advancedLvButton);
		
		//button for the advanced level
		tutorialLvButton = new JButton();
		tutorialLvButton.addActionListener(this);
		tutorialLvButton.setBounds(0, 418, 200, 50);
		tutorialLvButton.setText("Addestramento");
		tutorialLvButton.setFocusable(false);
		this.add(tutorialLvButton);
		
		//button for exiting the game
		quitButton = new JButton();
		quitButton.addActionListener(this);
		quitButton.setBounds(450, 418, 200, 50);
		quitButton.setText("Esci dal gioco");
		quitButton.setFocusable(false);
		this.add(quitButton);
				
		
		
		this.setBackground(Color.white);
		this.setBounds(0, 0, 640, 480);
		this.setLayout(new BorderLayout());
	}

	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		//level 1
		if (e.getSource() == baseLvButton) {
			Level baseLevel = new Level(); //instantiate the level at base difficulty
	        baseLevel.start(1);
		}
		
		//level 2
		if (e.getSource() == intermediateLvButton) {
			Level intermediateLevel = new Level(); //instantiate the level at medium difficulty
			intermediateLevel.start(3);
		}
		
		//level 3
		if (e.getSource() == advancedLvButton) {
			Level advancedLevel = new Level(); //instantiate the level at high difficulty
			advancedLevel.start(5);
		}
		
		//introduction level
		if (e.getSource() == tutorialLvButton) {
			Level tutorialLevel = new Level(); //instantiate the level at medium difficulty
			tutorialLevel.start(1);
		}
		
		//exit the game
		if (e.getSource() == quitButton) {
			System.exit(0);
		}
		
		Menu.appFrame.getContentPane().removeAll(); //remove the menuPanel from the JFrame
		Menu.appFrame.revalidate();
		Menu.appFrame.repaint();
	}
}
