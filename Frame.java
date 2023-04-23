package prova_collisione;

import java.awt.event.*;
import javax.swing.*;

public class Frame extends JFrame implements KeyListener {
	public Game gm;
	Panel menu_panel;
	


	public Frame() {
		this.setSize(640, 480);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.addKeyListener(this);
	}

	   
	
	@Override
	public void keyPressed(KeyEvent e) {
		if (gm == null) { //the game itself is not started yet, so we are in the initial game menu
			
		} else {
			if(gm.getPoints() < 10 && (gm.second > 0 || gm.minute > 0)) {
				if (gm.game_panel.p != null) {
					gm.game_panel.p.keyPressed(e);
				}
				if (gm.game_panel.e != null) {
					gm.game_panel.e.keyPressedLetter(e);
				}
				
				gm.game_panel.collision();
				
			} else if(gm.getPoints() == 10 || (gm.second == 0 && gm.minute == 0)) {
				System.out.println("fine gioco");
			}
		}
		
	}
	
	
	
	@Override
	public void keyReleased(KeyEvent e) {
		//we have to override this method because the class implements the KeyListener interface
	}
	
	
	
	@Override
	public void keyTyped(KeyEvent e) {
		//we have to override this method because the class implements the KeyListener interface	
	}	
}
