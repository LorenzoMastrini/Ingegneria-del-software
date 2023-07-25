import java.util.ArrayList;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.Video;
//only used to visualize the optical flow (for debugging)
//import org.opencv.highgui.HighGui;

class OpticalFlow implements Runnable {
	private static Mat angleMat = new Mat(); //it indicates the general direction of the movement; used in the Ball class
	private Thread t;
	private Mat frame1;
	private Mat frame2;
	private boolean end;
	
	
	
	public OpticalFlow() {
		this.frame1 = new Mat();
		this.frame2 = new Mat();
		this.end = false;
	}
	
	
	
	@Override
	public void run() {
		while(!end) {
			if (!this.frame1.empty() || !this.frame2.empty()) { //starts only if there are two frames to be analyzed
				Mat prvs = new Mat();
		        Imgproc.cvtColor(frame1, prvs, Imgproc.COLOR_BGR2GRAY);

		        Mat next = new Mat();
		        Imgproc.cvtColor(frame2, next, Imgproc.COLOR_BGR2GRAY);

		        Mat flow = new Mat(prvs.size(), CvType.CV_32FC2);
		        Video.calcOpticalFlowFarneback(prvs, next, flow, 0.5, 1, 35, 3, 7, 1.5, Video.OPTFLOW_FARNEBACK_GAUSSIAN); //standard parameters: (prvs, next, flow,0.5,3,15,3,5,1.2,0)
		        
		        // visualization
		        ArrayList<Mat> flow_parts = new ArrayList<>(2);
		        Core.split(flow, flow_parts);
		        Mat magnitude = new Mat(), angle = new Mat(), magn_norm = new Mat();
		        Core.cartToPolar(flow_parts.get(0), flow_parts.get(1), magnitude, angle, true);
		        Core.normalize(magnitude, magn_norm, 0.0, 1.0, Core.NORM_MINMAX);
		        double factor = (double) ((1.0/360.0)*(180.0/255.0));
		        Mat new_angle = new Mat();
		        Core.multiply(angle, new Scalar(factor), new_angle);
		        angleMat = angle.clone();
/*		        
				//only used to visualize the optical flow (for debugging)
		        //build hsv image
		        ArrayList<Mat> _hsv = new ArrayList<>() ;
		        Mat hsv = new Mat(), hsv8 = new Mat(), bgr = new Mat();
		        _hsv.add(new_angle);
		        _hsv.add(Mat.ones(angle.size(), CvType.CV_32F));
		        _hsv.add(magn_norm);
		        Core.merge(_hsv, hsv);
		        hsv.convertTo(hsv8, CvType.CV_8U, 255.0);
		        Imgproc.cvtColor(hsv8, bgr, Imgproc.COLOR_HSV2BGR);
		        if (!bgr.empty()) {
		        	HighGui.imshow("optical flow", bgr);
		            int keyboard = HighGui.waitKey(1);
		            if (keyboard == 'q' || keyboard == 27) {
		                break;
		            }
		        }
*/
		        prvs = next;
			}
		}
    }

	

	public void start() {
		if (t == null) {
	         t = new Thread(this);
	         t.start();
	      }
	}
	
	
	
	//method used in the Level class to end the execution of this thread
	public void stop(boolean interrupted) {
		this.end = true;
	}
	
	
	
	public void set(Mat frame1, Mat frame2) {
		this.frame1 = frame1;
		this.frame2 = frame2;
	}
	
	
	
	public static int getAngle(Point center) {
        //this code is for limiting the zone of calculation of optical flow, based on the center of the ball
        int elements = 0; //number of sub-matrix pixels considered in the calculation of the mean of the angles
        double angle_sum = 0;
        
        //this "builds" a square around the ball to detect only the movements of the pixels inside it
        for (int j = (int) center.x - 1; j < (int) center.x + 1; j++) {
        	for (int i = (int) center.y - 1; i < (int) center.y + 1; i++) {
    			if (center.x - 20 >= 0 && center.x + 20 <= Level.maxWidth && center.y - 20 >= 0 && center.y <= Level.maxHeight) { //this condition avoids problems in the corners of the image
					if (!angleMat.empty()) {
						angle_sum += angleMat.get(i, j)[0];
	        			elements++;
					}
    			}
        	}
        }
        int angle_mean = 0;
        angle_mean = (int) (angle_sum / elements) % 360; //all the angles are between 0 degrees and 360 degrees

        return angle_mean;
	}
}