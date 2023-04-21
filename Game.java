package prova_collisione;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Game {
	private int points = 0;
	
	Game(int points){
		this.points = points;
	}
	
	public void increasePoints() {
		points++;
	}
	public void decreasePoints() {
		points--;
	}
	public int getPoints() {
		return points;
	}
	
	public void closeGame(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_V) {
			System.exit(0);
		}
	}
}
