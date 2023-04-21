package prova_collisione;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import javax.swing.*;

public class Panel extends JPanel {
	Image image;
	Graphics g;
	Box p;
	Box pozzo;
	Box e;
	Game gm;
	boolean draw = true;
	boolean increase;
	boolean decrease;
	int points = 0;
	JLabel counterLabel;
	Timer timer;
	int second = 30, minute = 0;
	String ddSecond, ddMinute;
	DecimalFormat dFormat = new DecimalFormat("00");
	
	Panel(){
		p = new Box(100, 300, 50, 50, Color.green);
		pozzo = new Box(400, 300, 50, 50, Color.blue);
		e = new Box(200, 300, 50, 50, Color.red);
		gm = new Game(points);
		counterLabel = new JLabel("");
		increase = false;
		decrease = false;
		this.setSize(600, 600);
		this.setVisible(true);
	}
	
	public void components() {
		this.add(counterLabel);
		counterLabel.setBounds(300, 230, 200, 100);
		counterLabel.setFont(new Font("Arial", Font.PLAIN, 30));
		counterLabel.setText("00:00");
		
		this.timer();
		timer.start();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		//draw = true;
		
		image = createImage(this.getWidth(), this.getHeight());
		this.g = image.getGraphics();
		g.drawImage(image,0,0, this);
		
		if(draw) {
			p.draw(g);
		}
		
		e.draw(g);
		
		pozzo.draw(g);
		
		if(increase) {
			gm.increasePoints();
		}
		else if(decrease) {
			gm.decreasePoints();
		}
		
		g.setColor(Color.BLUE);
		g.setFont(new Font("Arial", Font.PLAIN, 45));
		g.drawString(String.valueOf(gm.getPoints()+"/10"), 50, 100);	
		System.out.println(gm.getPoints());
		
		if(gm.getPoints() == 10) {
			g.drawString("Victory!", 50, 450);
		}
		if(second == 0 && minute == 0) {
			g.drawString("Lose!", 50, 450);
		}	
	}
	
	public void collision() {
		if(p.intersects(pozzo)) {
			increase = true;
			draw = false;
		}
		else {
			increase = false;
		}
		
		if(e.intersects(pozzo)) {
			decrease = true;
		}
		else {
			decrease = false;
		}
	}
	
	public void timer() {
		timer = new Timer(1000, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				second--;
				ddSecond = dFormat.format(second);
				ddMinute = dFormat.format(minute);
				counterLabel.setText(ddMinute+":"+ddSecond);
				
				if(second == -1) {
					second = 59;
					minute--;
					ddSecond = dFormat.format(second);
					ddMinute = dFormat.format(minute);
					counterLabel.setText(ddMinute+":"+ddSecond);				
				}
			}
		});
	}
		
	/*
	@Override
	public void keyPressed(KeyEvent e) {
		p.keyPressed(e);
		this.e.keyPressedLetter(e);
		collision();
		if(gm.getPoints() < 10) {
			repaint();
		}
		else if(gm.getPoints() == 10) {
			gm.closeGame(e);
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub	
	}*/	
}
