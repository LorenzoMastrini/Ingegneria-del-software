import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;



public class GamePanel extends JPanel {
	private Level level;
	private Target target; //where the balls should enter
	
	public GamePanel(Level level) {
		this.setBackground(Color.black);
		this.setBounds(0, 0, 654, 518);
		this.level = level;
		target = new Target();
	}
	
	
	
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.drawImage(Level.currentFrame, null, 0, 0); //draw the bufferedImage originally read by the camera
		
		//draw the target as an ellipse
		g2d.setColor(Color.blue);
		g2d.fillOval(Target.positionX, Target.positionY, Target.width, Target.height);
		
		int indexBallToRemove = -1; //we have to remove the ball once the loop is over, otherwise we get a ConcurrentModificationException
		for (Ball ball : level.ballList) { //iterate through the list
			//draw the ball
			g2d.setColor(Color.RED);
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
}
