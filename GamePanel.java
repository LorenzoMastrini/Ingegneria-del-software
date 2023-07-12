import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;



public class GamePanel extends JPanel implements ActionListener {
	private Level level;
	private JLabel counterLabel;
	private Target target; //where the balls should enter
	private Image targetImg;
	private Image greenBallImg;
	private Image redBallImg;
	public JButton pauseButton;
	
	private int MAX_WIDTH = 640;
	private int MAX_HEIGHT = 480;
	
	
	
	public GamePanel(Level level) {
		this.removeAll();
		this.setBackground(Color.black);
		this.setPreferredSize(new Dimension(MAX_WIDTH, MAX_HEIGHT));
		this.setLayout(null);
		this.level = level;
		this.target = new Target();
		
		//pause button
		this.pauseButton = new JButton();
		this.pauseButton.addActionListener(this);
		this.pauseButton.setFocusable(false);
		this.pauseButton.setBounds(MAX_WIDTH - 50, 0, 50, 50);
		ImageIcon pauseImgIcon = new ImageIcon("res/pause_button.png"); //read the image
		Image pauseImg = pauseImgIcon.getImage();  
		pauseImg = pauseImg.getScaledInstance(50, 50, Image.SCALE_SMOOTH); //resize the image to fit the button's dimensions
		pauseImgIcon = new ImageIcon(pauseImg);
		this.pauseButton.setIcon(pauseImgIcon);
		//these instructions make the background of the button transparent so that we only see the image drawn on it
		pauseButton.setBorderPainted(false); 
		pauseButton.setContentAreaFilled(false);
		//pauseButton.setFocusPainted(false); //non so se serve
		//pauseButton.setOpaque(false); //non so se serve
		this.add(this.pauseButton);
		
		//timer
		counterLabel = new JLabel(); //label to draw the timer
		counterLabel.setFont(new Font("Arial", Font.PLAIN, 30));
		counterLabel.setHorizontalAlignment(JLabel.CENTER);
		counterLabel.setVerticalAlignment(JLabel.TOP);
		counterLabel.setBounds(MAX_WIDTH/2 - 100, 0, 200, 30);
		this.add(counterLabel);
		
		//read the images to be drawn on screen from the res folder (target, green ball, red ball)
		try {
			//target
			targetImg = ImageIO.read(new File("res/blue_portal.png"));
			targetImg = targetImg.getScaledInstance(Target.width, Target.height, Image.SCALE_SMOOTH); //resize the image
			
			//green ball
			greenBallImg = ImageIO.read(new File("res/green_ball.png"));
			greenBallImg = greenBallImg.getScaledInstance(Ball.DIAMETER, Ball.DIAMETER, Image.SCALE_SMOOTH); //resize the image 
			
			//target
			redBallImg = ImageIO.read(new File("res/red_ball.png"));
			redBallImg = redBallImg.getScaledInstance(Ball.DIAMETER, Ball.DIAMETER, Image.SCALE_SMOOTH); //resize the image 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.drawImage(Level.currentFrame, null, 0, 0); //draw each frame originally read by the camera
		
		counterLabel.setText(level.minute + ":" + level.second); //constantly update the timer

		g2d.drawImage(targetImg, Target.positionX, Target.positionY, null); //draw the portal image that represents the target
		
		if (!level.isPaused) {
			//we have to remove the ball once the loop is over (otherwise we get a ConcurrentModificationException), so we save the index of the ball to be removed
			int indexBallToRemove = -1; 
			
			for (Ball ball : level.ballList) { //iterate through the list of balls
				//draw the ball in a color based on whether the ball is a positive one (green) or a negative one (red)
				if (ball.isGood()) {
					g2d.drawImage(greenBallImg, (int) ball.position.x, (int) ball.position.y, null); //draw the green ball image
				} else {
					g2d.drawImage(redBallImg, (int) ball.position.x, (int) ball.position.y, null); //draw the green ball image
				}
				
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
		g2d.drawString(String.valueOf(level.getPoints() + "/10"), 0, MAX_HEIGHT); //paint on the bottom-left corner
	}



	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == pauseButton) {
			pauseButton.setEnabled(false);
			level.timer.stop();
			level.pause();

			PausePanel pausePanel = new PausePanel(level, this);
            this.add(pausePanel);
            this.repaint();
		}
	}
}
