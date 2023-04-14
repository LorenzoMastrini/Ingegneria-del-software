import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.BackgroundSubtractorMOG2;
import org.opencv.video.Video;
import org.opencv.videoio.VideoCapture;

public class Main {
	static void start() {
		VideoCapture capture = new VideoCapture(0); //capture the webcam input
        if (!capture.isOpened()) {
            //error in opening the video input
            System.out.println("Unable to open camera!");
            System.exit(-1);
        }
        
		BackgroundSubtractorMOG2 MOG2 = Video.createBackgroundSubtractorMOG2(); //OpenCV object to remove the background
        
        OpticalFlow optFlow = new OpticalFlow(); //instantiate the OpticalFlow thread
        optFlow.start();
        
        Ball ball = new Ball(100, 100); //creates a new Ball object
        
        //main loop
        while (true) {
        	//src is used to make all image processing operations, and it is the one the is displayed
        	Mat src = new Mat();
            capture.read(src); //read frame from webcam
            if (src.empty()) {
                break;
            }
            Core.flip(src, src, 1); //makes the image mirrored
            
            //src1 e src2 are the matrixes passed to the optical flow algorithm and they are used only for this purpose
            Mat src1 = new Mat();
            capture.read(src1); //read frame from webcam
            if (src1.empty()) {
                break;
            }
            Core.flip(src1, src1, 1); //makes the image mirrored
            Mat src2 = new Mat();
            capture.read(src2); //read frame from webcam
            if (src2.empty()) {
                break;
            }
            Core.flip(src2, src2, 1); //makes the image mirrored
        	
            optFlow.set(src1, src2, ball.position); //sets optical flow parameters to make it work
            
            Mat fg_mask = Main.imgProcessing(src, MOG2); //makes some technical operation on the image and returns the foreground mask
            
            MatOfPoint biggestContours = Main.contours(fg_mask); //returns the hand contours used to determine if the ball encounter the hand (in the Ball class)
            
            ball.basicMovement(biggestContours, optFlow.angle);
            Imgproc.circle(src, ball.position, 20, new Scalar(0, 0, 255), -1); //draws the ball
            
            HighGui.imshow("src", src); //display the image
            int keyboard = HighGui.waitKey(1);
            if (keyboard == 'q' || keyboard == 27) {
                break;
            }
        }
        capture.release();
        HighGui.destroyAllWindows();
	}

	
	
	public static Mat imgProcessing(Mat input, BackgroundSubtractorMOG2 background_subtractor) {
		Mat mask = new Mat();
		      
		background_subtractor.apply(input, mask); //Get the foreground mask (it is gray scale)
        //Convert the gray scale to black and white with a threshold
        //the 250 threshold can be modified fitting the webcam and needs, setting it lower will make it more sensitive (also to noise)
        Imgproc.threshold(mask, mask, 250, 255, Imgproc.THRESH_BINARY);
                
        return mask;
	}
	
	
	
	public static MatOfPoint contours(Mat mask) {
		Mat binary = mask;
		
        //detect all contours
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat(); //not used
        Imgproc.findContours(binary, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        
        //find biggest contours
        //int biggestContourIndex = 0; //used only for drawing purposes
        int maxContourArea = -1000;
        MatOfPoint biggestContours = new MatOfPoint();
        for (int i = 0; i < contours.size(); i++) {       
            if (Imgproc.contourArea(contours.get(i)) > maxContourArea) {
                maxContourArea = (int) Imgproc.contourArea(contours.get(i));
                //biggestContourIndex = i;
                biggestContours = contours.get(i); //extract from all contour list only biggest contours (hand)
            }
        }
/*
        // show biggest contour
        Mat imgBiggestContour = new Mat(binary.rows(), binary.cols(), CvType.CV_8UC1, new Scalar(0, 0, 0));
        Imgproc.drawContours(imgBiggestContour, contours, biggestContourIndex, new Scalar(255, 255, 255), 2, 8, new Mat(), 0, new Point());
        HighGui.imshow("maxContour", imgBiggestContour);
*/
        return biggestContours;
	}
}
