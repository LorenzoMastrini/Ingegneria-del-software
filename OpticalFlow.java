package progetto_ingegneriaSW;

import java.util.ArrayList;
import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.BackgroundSubtractorMOG2;
import org.opencv.video.Video;
import org.opencv.videoio.VideoCapture;

class OpticalFlow {
    public int run(VideoCapture capture, double x, double y) {    	
    	Mat frame1 = new Mat(), prvs = new Mat();
        capture.read(frame1);
        Core.flip(frame1, frame1, 1); //makes the image mirrored
        
        BackgroundSubtractorMOG2 MOG2 = Video.createBackgroundSubtractorMOG2(); //OpenCV object to remove the background
        
        
        Imgproc.cvtColor(frame1, prvs, Imgproc.COLOR_BGR2GRAY);
        
        /*
        MOG2.apply(prvs, prvs); // update the background model
        HighGui.imshow("MOG2", prvs);
		HandDetection.filterImage(prvs); //do filtering operations
        HighGui.imshow("img filtrata", prvs);
        Imgproc.GaussianBlur(prvs, prvs, new Size(3,3), 0);
        //HighGui.imshow("fgMask", prvs);
         */
        
        Mat frame2 = new Mat(), next = new Mat();
        capture.read(frame2);
        Core.flip(frame2, frame2, 1); //makes the image mirrored
        if (frame2.empty()) {
            return 0;
        }
        
        Imgproc.cvtColor(frame2, next, Imgproc.COLOR_BGR2GRAY);
        
        //MOG2.apply(next, next); // update the background model
        
        Mat flow = new Mat(prvs.size(), CvType.CV_32FC2);
        Video.calcOpticalFlowFarneback(prvs, next, flow, 0.5, 3, 15, 3, 5, 1.2, 0);
        
        // visualization
        ArrayList<Mat> flow_parts = new ArrayList<>(2);
        Core.split(flow, flow_parts);
        Mat magnitude = new Mat(), angle = new Mat(), magn_norm = new Mat();
        Core.cartToPolar(flow_parts.get(0), flow_parts.get(1), magnitude, angle, true);
        
        //System.out.println("angle: " + angle.get(240, 320)[0]);
        Core.normalize(magnitude, magn_norm, 0.0, 1.0, Core.NORM_MINMAX);
        float factor = (float) ((1.0/360.0)*(180.0/255.0));
        Mat new_angle = new Mat();
        Core.multiply(angle, new Scalar(factor), new_angle);

        int elements = 0;
        double angle_sum = 0;
        int angle_mean = 0;
        for (int i = (int) x - 2; i < (int) x + 2; i++) {
        	for (int j = (int) y - 2; j < (int) y + 2; j++) {
    			if (x > 0 && x < angle.rows() && y > 0 && y < angle.cols()) {
    				angle_sum += angle.get(i, j)[0];
        			elements++;
    			}
        	}
        }
		//System.out.println(elements);
        angle_mean = (int) (Math.round(angle_sum / elements) % 360);
        System.out.println("Media angoli: " + angle_mean);
        //https://stackoverflow.com/questions/37849313/java-opencv-detecting-roi-creating-submat-and-copy-to-original-mat

        //build hsv image
        ArrayList<Mat> _hsv = new ArrayList<>() ;
        Mat hsv = new Mat(), hsv8 = new Mat(), bgr = new Mat();
        _hsv.add(new_angle);
        _hsv.add(Mat.ones(angle.size(), CvType.CV_32F));
        _hsv.add(magn_norm);
        Core.merge(_hsv, hsv);
        hsv.convertTo(hsv8, CvType.CV_8U, 255.0);
        Imgproc.cvtColor(hsv8, bgr, Imgproc.COLOR_HSV2BGR);
        HighGui.imshow("frame2", bgr);

        prvs = next;
        
        return angle_mean;
    }
}