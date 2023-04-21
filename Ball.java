import org.opencv.core.CvType;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

public class Ball implements Runnable {
	public double dx, dy; //shift values; they represent the speed and the direction of the ball
	public Point position;
	private long time;
	//static int counter = 0;
	
	public Ball (int x, int y) {
		//x and y are the initial position coordinates
		this.position = new Point(x, y);
		this.dx = 5;
		this.dy = 5;
		this.time = 0;
		//counter++;
	}


	@Override
	public void run() {
		while(true) {
			double angle = OpticalFlow.getAngle(position);
			position.x += dx;
	        position.y += dy;
	        
	        //make the ball bounce in other directions when it gets to the borders of the screen
	        if (position.y + 20 >= 480) { //down border
				dy *= -1;
			} else if (position.y - 20 <= 0) { //up border
				dy *= -1;
			}
			if (position.x + 20 >= 640) { //right border
				dx *= -1;
			} else if (position.x - 20 <= 0) { //left border
				dx *= -1;
			}
			
			try {
				Thread.sleep(70);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			MatOfPoint2f hand = new MatOfPoint2f(); //this object contains the points that represent the contours of the hand
			if (!Main.biggestContours.empty()) {
				Main.biggestContours.convertTo(hand, CvType.CV_32FC2); //this step is necessary to use the function pointPolygonTest
				if (!hand.empty()) {
					if (Imgproc.pointPolygonTest(hand, position, false) >= 0 && System.currentTimeMillis() - time > 100) {
						time = System.currentTimeMillis();
						if (position.x + 20 <= 620 && position.x - 20 >= 20 && position.y + 20 <= 460 && position.y - 20 >= 20) {
				        	if (angle >= 337 || angle < 23 && angle != 0) { //0
				        		System.out.println("Destra");
				        		dx = 5;
				        		dy = 0;
				        	} else if (angle >= 23 && angle < 67) { //45
				        		System.out.println("Basso-destra");
				        		dx = 5;
				        		dy = 5;
				        	} else if (angle >= 67 && angle < 112) { //90
				        		System.out.println("Basso");
				        		dx = 0;
				        		dy = 5;
				        	} else if (angle >= 112 && angle < 157) { //135
				        		System.out.println("Basso-sinistra");
				        		dx = -5;
				        		dy = 5;
				        	} else if (angle >= 157 && angle < 202) { //180
				        		System.out.println("Sinistra");
				        		dx = -5;
				        		dy = 0;
				        	} else if (angle >= 202 && angle < 247) { //225
				        		System.out.println("Alto-sinistra");
				        		dx = -5;
				        		dy = -5;
				        	} else if (angle >= 247 && angle < 292) { //270
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
}
