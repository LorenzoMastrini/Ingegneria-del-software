package prova_collisione;

import java.awt.*;

import javax.swing.*;

public class Panel extends JPanel {
	Image image;
	Graphics g;
	Box p;
	Box pozzo;
	Box e;
	boolean increase;
	boolean decrease;
	JLabel counterLabel;
	Game gm;
	
		   
	
	public Panel(Game gm) {
		p = new Box(100, 300, 50, 50, Color.green);
		pozzo = new Box(400, 300, 50, 50, Color.blue);
		e = new Box(200, 300, 50, 50, Color.red);
		counterLabel = new JLabel("");
		increase = false;
		decrease = false;
		this.setSize(640, 480);
		this.setVisible(true);
		this.gm = gm;
	}
	
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.add(counterLabel);
		counterLabel.setBounds(this.getWidth()/ 2 - 50,  0, 100, 50);
		counterLabel.setFont(new Font("Arial", Font.PLAIN, 30));
		counterLabel.setText(gm.minute + ":" + gm.second);

		image = createImage(this.getWidth(), this.getHeight());
		this.g = image.getGraphics();
		g.drawImage(image, 0, 0, this);
		
		//paint objects only if they still exist (they haven't intersected the can yet)
		if(p != null) {
			p.draw(g);
		}
		if(e != null) {
			e.draw(g);
		}

		pozzo.draw(g); //always on screen
		
		if(increase) {
			gm.increasePoints();
			increase = false;
		}
		else if(decrease) {
			gm.decreasePoints();
			decrease = false;
		}
		
		g.setColor(Color.BLUE);
		g.setFont(new Font("Arial", Font.PLAIN, 45));
		g.drawString(String.valueOf(gm.getPoints() + "/10"), 50, 100); //paint the score of the player
		
		if(gm.getPoints() == 10) {
			g.drawString("Victory!", 50, 450);
			gm.closeGame();
		}
		if(gm.second == 0 && gm.minute == 0) {
			g.drawString("Lose!", 50, 450);
			gm.closeGame();
		}
	}
	
	public void collision() {
		if(p != null && p.intersects(pozzo)) {
			increase = true;
			p = null; //delete the box
		}
		else {
			increase = false;
		}
		
		if(e != null && e.intersects(pozzo)) {
			decrease = true;
			e = null; //delete the box
		}
		else {
			decrease = false;
		}
	}
}
