package progetto_ingegneriaSW;

import org.opencv.core.CvType;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

public class Ball {
	int dx = 5, dy = 5; //shift values; they represent the speed of the ball
	double x = 100, y = 100; //initial position coordinates
	Point position = new Point(x, y);
	
	void basicMovement(HandDetection hand) {
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
				dx *= -1;
			}
		}
		
		
		return;
	}
}
