package prova_collisione;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import javax.swing.Timer;

public class Game {
	Panel game_panel;
	private int points;
	private boolean end;
	Timer timer;
	int second = 10, minute = 0;
	String ddSecond, ddMinute;
	DecimalFormat dFormat = new DecimalFormat("00");
	
	
	
	public Game(Frame frame) {
		game_panel = new Panel(this);
		frame.gm = this;
		frame.add(game_panel);
		
		this.points = 0;
		end = false;
	}
	
	
	
	public void start() {
		timer();
		timer.start();
		while (!end) {
			game_panel.repaint();
		}
		return;
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
	
	
	
	public void timer() {
		timer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				second--;
				ddSecond = dFormat.format(second);
				ddMinute = dFormat.format(minute);
				
				if(second == -1) {
					if (minute == 0) {
						second = 0;
						timer.stop(); //time's up
					} else {
						second = 59;
						minute--;
					}
					
					ddSecond = dFormat.format(second);
					ddMinute = dFormat.format(minute);
				}
			}
		});
	}
	
	
	
	public void closeGame() {
		timer.stop(); //time's up
		end = true;
	}
}
