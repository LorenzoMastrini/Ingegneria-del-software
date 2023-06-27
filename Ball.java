import org.opencv.core.CvType;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

public class Ball implements Runnable {
	public static final int DIAMETER = 40; //diameter is fixed and it is the same for all the balls
	private static final double SPAWNS_PER_MINUTE = 20;
	private static final double SPAWNS_PER_SECOND = SPAWNS_PER_MINUTE / 60.0;
	private static final double UPDATES_PER_SPAWN = Level.MAX_UPS / SPAWNS_PER_SECOND;
	private static double updatesUntilNextSpawn = UPDATES_PER_SPAWN;
	private boolean end;
	private boolean isGood; //whether the ball increase or decrease the points if it gets to the target
	public double dx, dy; //shift values; they represent the speed and the direction of the ball
	public Point position;
	
	private long time;

	

	public Ball() {
		//the arguments of Point() function (position.x, position.y) are the top-left coordinates of the bounding box of the circle
		this.position = new Point((int) (Math.random() * (Level.MAX_WIDTH - Ball.DIAMETER)),(int) (Math.random() * (Level.MAX_HEIGHT - Ball.DIAMETER)));
		this.end = false;
		this.isGood = true;
		//initial speed and direction of the new created ball
		this.dx = 5;
		this.dy = 5;
		this.time = 0;
	}


	@Override
	public void run() {
		while(!end) {
			double angle = OpticalFlow.getAngle(position); //estimated angle of the movement of the hand when there's a collision with the ball
			position.x += dx;
	        position.y += dy;
	        
	        //make the ball bounce in other directions when it gets to the edges of the screen
	        if (position.y + DIAMETER >= Level.MAX_HEIGHT) { //down edge
				dy *= -1; //invert vertical direction
			} else if (position.y <= 0) { //up edge
				dy *= -1; //invert vertical direction
			}
			if (position.x + DIAMETER >= Level.MAX_WIDTH) { //right edge
				dx *= -1; //invert horizontal direction
			} else if (position.x <= 0) { //left edge
				dx *= -1; //invert horizontal direction
			}
			
			//short delay
			try {
				Thread.sleep(70);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			MatOfPoint2f hand = new MatOfPoint2f(); //this object contains the points that represent the contours of the hand
			if (!Level.biggestContours.empty()) {
				Level.biggestContours.convertTo(hand, CvType.CV_32FC2); //this step is necessary to use the function pointPolygonTest()
				if (!hand.empty()) {
					if (Imgproc.pointPolygonTest(hand, new Point(position.x + DIAMETER/2, position.y + DIAMETER/2), false) >= 0 && System.currentTimeMillis() - time > 400) { //adds a short delay
						time = System.currentTimeMillis();
						
						//we have 8 possible directions in which the ball can go
						if (position.x + DIAMETER <= Level.MAX_WIDTH && position.x >= 0 && position.y + DIAMETER <= Level.MAX_HEIGHT && position.y >= 0) { //the ball needs to be inside the image
				        	if (angle >= 337 || angle < 23 && angle != 0) { //0 degrees
				        		System.out.println("Destra");
				        		dx = 5;
				        		dy = 0;
				        	} else if (angle >= 23 && angle < 67) { //45 degrees
				        		System.out.println("Basso-destra");
				        		dx = 5;
				        		dy = 5;
				        	} else if (angle >= 67 && angle < 112) { //90 degrees
				        		System.out.println("Basso");
				        		dx = 0;
				        		dy = 5;
				        	} else if (angle >= 112 && angle < 157) { //135 degrees
				        		System.out.println("Basso-sinistra");
				        		dx = -5;
				        		dy = 5;
				        	} else if (angle >= 157 && angle < 202) { //180 degrees
				        		System.out.println("Sinistra");
				        		dx = -5;
				        		dy = 0;
				        	} else if (angle >= 202 && angle < 247) { //225 degrees
				        		System.out.println("Alto-sinistra");
				        		dx = -5;
				        		dy = -5;
				        	} else if (angle >= 247 && angle < 292) { //270 degrees
				        		System.out.println("Alto");
				        		dx = 0;
				        		dy = -5;
				        	} else if (angle >= 292 && angle < 337) { //315
				        		System.out.println("Alto-destra");
				        		dx = 5;
				        		dy = -5;
				        	}
						}
					}
				}
			}
		}
	}

	
	
	//checks if a new ball should spawn according to a decided number of spawns per minute
	public static boolean isReadyToSpawn(Level level) {
		if(updatesUntilNextSpawn <= 0 && level.ballList.size() <= 4) {
			updatesUntilNextSpawn += UPDATES_PER_SPAWN;
			return true;
		} else {
			updatesUntilNextSpawn--;
			return false;
		}
	}
	
	
	
	public Point getCenter() {
		int x = (int) position.x + DIAMETER/2;
		int y = (int) position.y + DIAMETER/2;
		
		return new Point(x, y);
	}

	

	public void finished() {
		this.end = true;
	}
	
	
	
	public boolean isGood() {
		return this.isGood;
	}
}