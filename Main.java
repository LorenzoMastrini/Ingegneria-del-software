import java.awt.FlowLayout;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
	static void start() throws IOException {
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
        
        //Instantiate JFrame
        JFrame frame = new JFrame();
        frame.setLayout(new FlowLayout());
        JLabel lbl = new JLabel();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
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
            
            Mat fg_mask = ImgProcessor.filter(src, MOG2); //makes some technical operation on the image and returns the foreground mask
            
            MatOfPoint biggestContours = ImgProcessor.contours(fg_mask); //returns the hand contours used to determine if the ball encounter the hand (in the Ball class)
            
            ball.basicMovement(biggestContours, optFlow.angle);
            Imgproc.circle(src, ball.position, 20, new Scalar(0, 0, 255), -1); //draws the ball
            
            lbl.setIcon(new ImageIcon(ImgProcessor.bufferedImage(src)));
            frame.add(lbl);
            frame.pack();
/*
            HighGui.imshow("src", src); //display the image
            int keyboard = HighGui.waitKey(1);
            System.out.println(keyboard);
            if (keyboard == 'q' || keyboard == 81) {
            	System.out.println("prova");
                break;
            }
*/
        }
        capture.release();
        HighGui.destroyAllWindows();
        System.out.println("destroy All Windows");
        return;
	}

	
	
	
}
