import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;



public class GamePanel extends JPanel implements ActionListener {
	private Level level;
	private JLabel counterLabel;
	public JButton pauseButton;
	private Target target; //where the balls should enter
	
	public GamePanel(Level level) {
		this.removeAll();
		this.setBackground(Color.black);
		this.setPreferredSize(new Dimension(Level.MAX_WIDTH, Level.MAX_HEIGHT));
		this.setLayout(new BorderLayout());
		this.level = level;
		this.target = new Target();
		
		//pause button
		this.pauseButton = new JButton();
		this.pauseButton.addActionListener(this);
		this.pauseButton.setFocusable(false);
		ImageIcon imgIcon = new ImageIcon("pause.png");
		Image img = imgIcon.getImage() ;  
		Image newimg = img.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);  
		imgIcon = new ImageIcon(newimg);
		this.pauseButton.setIcon(imgIcon);
		pauseButton.setBorderPainted(false); 
		pauseButton.setContentAreaFilled(false); 
		pauseButton.setFocusPainted(false); 
		pauseButton.setOpaque(false);
		this.add(this.pauseButton, BorderLayout.EAST);
		
		counterLabel = new JLabel(); //label to draw the timer
		counterLabel.setFont(new Font("Arial", Font.PLAIN, 30));
		counterLabel.setHorizontalAlignment(JLabel.CENTER);
		this.add(counterLabel, BorderLayout.NORTH);
	}
	
	
	
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.drawImage(Level.currentFrame, null, 0, 0); //draw the bufferedImage originally read by the camera
				
		counterLabel.setText(level.minute + ":" + level.second); //constantly update the timer
		pauseButton.setBounds(Level.MAX_WIDTH - 50, 0, 50, 50);

		if (!level.isPaused) {
			//draw the target as an ellipse
			g2d.setColor(Color.blue);
			g2d.fillOval(Target.positionX, Target.positionY, Target.width, Target.height);
			
			int indexBallToRemove = -1; //we have to remove the ball once the loop is over, otherwise we get a ConcurrentModificationException
			for (Ball ball : level.ballList) { //iterate through the list
				//draw the ball in a color based on whether the ball is a positive one (green) or a negative one (red)
				if (ball.isGood()) {
					g2d.setColor(Color.GREEN);
				} else {
					g2d.setColor(Color.RED);
				}
				g2d.fillOval((int) ball.position.x, (int) ball.position.y, Ball.DIAMETER, Ball.DIAMETER);
				
				//check if the center of the ball is inside the target
				if (target.contains(ball.getCenter().x, ball.getCenter().y)) {
					level.updatePoints(ball.isGood()); //increase or decrease the points based on the color of the color of the ball
					ball.finished(); //this function sets a local variable in the Ball class to stop the corresponding thread				
					indexBallToRemove = level.ballList.indexOf(ball);
				}
			}
			
			//-1 means that no ball entered the target
			if (indexBallToRemove != -1) {
				level.ballList.remove(level.ballList.remove(indexBallToRemove));
			}
		}
		
		//draws the score of the player
		g2d.setColor(Color.black);
		g2d.setFont(new Font("Arial", Font.PLAIN, 45));
		g2d.drawString(String.valueOf(level.getPoints() + "/10"), 0, Level.MAX_HEIGHT); //paint on the down-left corner
	}



	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == pauseButton) {
			pauseButton.setEnabled(false);
			level.timer.stop();
			level.pause();
			
			PausePanel pausePanel = new PausePanel(level, this);
            this.add(pausePanel, BorderLayout.CENTER);
            this.repaint();
		}
	}
}
