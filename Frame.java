package prova_collisione;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Frame extends JFrame implements KeyListener{
	Panel panel = new Panel();
	
	Frame(){
		this.setSize(800, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.add(panel);
		this.addKeyListener(this);
	}
	
	public void callComponents() {
		panel.components();
	}
	
	public void updatePanel() {
		panel.repaint();
	}
	
	/*public void collision() {
		if(panel.p.intersects(panel.pozzo)) {
			panel.increase = true;
			panel.g
		}
		else {
			panel.increase = false;
		}
		
		if(panel.e.intersects(panel.pozzo)) {
			panel.decrease = true;
		}
		else {
			panel.decrease = false;
		}
	}*/
	
	@Override
	public void keyPressed(KeyEvent e) {
		if(panel.gm.getPoints() < 10 && (panel.second > 0 || panel.minute > 0)) {
			panel.p.keyPressed(e);
			panel.e.keyPressedLetter(e);
			
			panel.collision();
			
			this.updatePanel();
		}
		else if(panel.gm.getPoints() == 10 || (panel.second == 0 && panel.minute == 0)) {
			panel.timer.stop();
			this.updatePanel();
			panel.gm.closeGame(e);
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub	
	}	
}
