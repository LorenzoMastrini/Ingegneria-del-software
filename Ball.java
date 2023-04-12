package progetto_ingegneriaSW;

import org.opencv.core.CvType;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

public class Ball {
	double dx = 5, dy = 5; //shift values; they represent the speed and the direction of the ball
	double x = 100, y = 100; //initial position coordinates
	Point position = new Point(x, y);
	
	void basicMovement(HandDetection hand, int angle) {
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
		
		MatOfPoint2f prova = new MatOfPoint2f();
		hand.biggestContours.convertTo(prova, CvType.CV_32FC2);
		if (!prova.empty()) {
			if (Imgproc.pointPolygonTest(prova, position, false) >= 0) {
	        	if (angle >= 337 || angle < 23) { //0
	        		System.out.println("Destra");
	        		dx = 10;
	        		dy = 0;
	        	} else if (angle >= 23 && angle < 67) { //45
	        		System.out.println("Basso-destra");
	        		dx = 5;
	        		dy = 5;
	        	} else if (angle >= 67 && angle < 112) { //90
	        		System.out.println("Basso");
	        		dx = 0;
	        		dy = 10;
	        	} else if (angle >= 112 && angle < 157) { //135
	        		System.out.println("Basso-sinistra");
	        		dx = 5;
	        		dy = -5;
	        	} else if (angle >= 157 && angle < 202) { //180
	        		System.out.println("Sinistra");
	        		dx = -10;
	        		dy = 0;
	        	} else if (angle >= 202 && angle < 247) { //225
	        		System.out.println("Alto-sinistra");
	        		dx = -5;
	        		dy = -5;
	        	} else if (angle >= 247 && angle < 292) { //270
	        		System.out.println("Alto");
	        		dx = 0;
	        		dy = -10;
	        	} else if (angle >= 292 && angle < 337) { //315
	        		System.out.println("Alto-destra");
	        		dx = 5;
	        		dy = -5;
	        	}
			}
		}
		
		return;
	}
}
