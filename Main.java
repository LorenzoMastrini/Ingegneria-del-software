package progetto_ingegneriaSW;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.BackgroundSubtractorMOG2;
import org.opencv.video.Video;
import org.opencv.videoio.VideoCapture;

public class Main {

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME); //load OpenCV library
		
		VideoCapture capture = new VideoCapture(0); //capture the webcam input
        if (!capture.isOpened()) {
            //error in opening the video input
            System.out.println("Unable to open camera!");
            System.exit(-1);
        }
        
        BackgroundSubtractorMOG2 MOG2 = Video.createBackgroundSubtractorMOG2(); //OpeCV object to remove the background
        
        Ball ball = new Ball();
        
        while (true) {
            Mat src = new Mat();
            capture.read(src); //read frame from webcam
            if (src.empty()) {
                break;
            }
            Core.flip(src, src, 1); //makes the image mirrored
            
            /*
            //(optional) copy for results
            Mat srcCopy = new Mat();
            src.copyTo(srcCopy);
            */
            
            HandDetection hand = new HandDetection(src); //does all the technical operations to detect the hand
            hand.grayscaleImg(); //convert image to grayscale
            MOG2.apply(hand.src, hand.fgMask); // update the background model
            //HighGui.imshow("fgMask", hand.fgMask);
            hand.imgProcessing();
            
            ball.basicMovement(hand);
            Imgproc.circle(src, ball.position, 20, new Scalar(0, 0, 255), -1);
			
            HighGui.imshow("src", src);
            int keyboard = HighGui.waitKey(30);
            if (keyboard == 'q' || keyboard == 27) {
                break;
            }
        }
	
	}

	
	
}
