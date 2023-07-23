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
	private Target target; //where the balls should enter
	private Image targetImg;
	private Image yellowBallImg;
	private Image redBallImg;
	private Font labelsFont;
	public JButton pauseButton;
	
	
	public GamePanel(Level level) {
		this.removeAll();
		this.setBackground(Color.black);
		this.setPreferredSize(new Dimension(Level.maxWidth, Level.maxHeight));
		this.setLayout(null);
		this.level = level;
		this.target = new Target();
		this.labelsFont = Main.buttonsFont.deriveFont(30f); //with this dimensions the font is used only in this class
		
		//pause button
		pauseButton = new JButton();
		pauseButton.addActionListener(this);
		pauseButton.setBounds(Level.maxWidth - Main.ROUND_BUTTONS_DIMENSION - Main.BORDERS_LIMIT, Main.BORDERS_LIMIT, Main.ROUND_BUTTONS_DIMENSION, Main.ROUND_BUTTONS_DIMENSION);
		ImageIcon pauseButtonIcon = new ImageIcon(this.getClass().getResource("/res/pause_button.png")); //read the image
		pauseButtonIcon = ImgProcessor.scaleImage(pauseButtonIcon, Main.ROUND_BUTTONS_DIMENSION, Main.ROUND_BUTTONS_DIMENSION); //resize
		this.pauseButton.setIcon(pauseButtonIcon);
		//these instructions make the background of the button transparent so that we only see the image drawn on it
		pauseButton.setBorderPainted(false);
		pauseButton.setContentAreaFilled(false);
		pauseButton.setFocusable(false);
		this.add(pauseButton);
		
		//timer label
		counterLabel = new JLabel(); //label to draw the timer
		counterLabel.setFont(labelsFont);
		counterLabel.setHorizontalAlignment(JLabel.CENTER);
		counterLabel.setVerticalAlignment(JLabel.TOP);
		counterLabel.setBounds(Level.maxWidth/2 - Main.BUTTONS_WIDTH/2, Main.BORDERS_LIMIT - 5, Main.BUTTONS_WIDTH, Main.BUTTONS_HEIGHT);
		this.add(counterLabel);
		
		//read the images to be drawn on screen from the res folder (target, green ball, red ball)
		//target
		ImageIcon targetIcon = new ImageIcon(this.getClass().getResource("/res/portal.png")); //read the background image
		targetImg = targetIcon.getImage();
		targetImg = targetImg.getScaledInstance((int) target.getWidth(), (int) target.getHeight(), Image.SCALE_SMOOTH);
		
		//positive ball
		ImageIcon yellowBallIcon = new ImageIcon(this.getClass().getResource("/res/yellow_ball.png")); //read the background image
		yellowBallIcon = ImgProcessor.scaleImage(yellowBallIcon, Ball.DIAMETER, Ball.DIAMETER); //resize to fit the height
		yellowBallImg = yellowBallIcon.getImage();
		
		//negative ball
		ImageIcon redBallIcon = new ImageIcon(this.getClass().getResource("/res/red_ball.png")); //read the background image
		redBallIcon = ImgProcessor.scaleImage(redBallIcon, Ball.DIAMETER, Ball.DIAMETER); //resize to fit the height
		redBallImg = redBallIcon.getImage();
	}
	
	
	
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.drawImage(level.currentFrame, null, 0, 0); //draw each frame originally read by the camera
		
		if (!(Level.chosenDifficulty == 0)) {
			counterLabel.setForeground(Color.black);
			counterLabel.setText(level.minute + ":" + level.second); //constantly update the timer
		}

		g2d.drawImage(targetImg, (int) target.getX(), (int) target.getY(), null); //draw the portal image that represents the target
		
		if (!level.isPaused) {
			//we have to remove the ball once the loop is over (otherwise we get a ConcurrentModificationException), so we save the index of the ball to be removed
			int indexBallToRemove = -1; 
			
			for (Ball ball : level.ballList) { //iterate through the list of balls
				//draw the ball in a color based on whether the ball is a positive one (green) or a negative one (red)
				if (ball.isGood()) {
					g2d.drawImage(yellowBallImg, (int) ball.position.x, (int) ball.position.y, null); //draw the green ball image
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
		g2d.setFont(labelsFont);
		if (!(Level.chosenDifficulty == 0)) {
			g2d.drawString(String.valueOf(level.getPoints() + "/10"), Main.BORDERS_LIMIT, Level.maxHeight - Main.BORDERS_LIMIT); //paint on the bottom-left corner
		} else {
			g2d.drawString(String.valueOf(level.getPoints()), Main.BORDERS_LIMIT, Level.maxHeight - Main.BORDERS_LIMIT); //paint on the bottom-left corner
		}
		
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
