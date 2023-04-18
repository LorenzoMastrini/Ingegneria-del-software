import java.awt.FlowLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.BackgroundSubtractorMOG2;
import org.opencv.video.Video;
import org.opencv.videoio.VideoCapture;

public class Main {
	public static MatOfPoint biggestContours = new MatOfPoint();
	
	public static void start(int n_balls) {
		VideoCapture capture = new VideoCapture(0); //capture the webcam input
        if (!capture.isOpened()) {
            System.out.println("Unable to open camera!"); //error in opening the video input
            System.exit(-1);
        }
        
		BackgroundSubtractorMOG2 MOG2 = Video.createBackgroundSubtractorMOG2(); //OpenCV object to remove the background
        
        OpticalFlow optFlow = new OpticalFlow(); //instantiate the OpticalFlow thread (runs during the whole program execution)
        optFlow.start();
        
        /*
        Ball[] balls = new Ball[n_balls];
        for (int i = 0; i < n_balls; i++) {
        	balls[i] = new Ball(100, 100); //creates a new Ball object
        }
        */
        
        //Instantiate JFrame
        JFrame frame = new JFrame();
        frame.setLayout(new FlowLayout());
        JLabel lbl = new JLabel();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        Ball[] balls = new Ball[n_balls];
        for (int i = 0; i < n_balls; i++) {
        	balls[i] = new Ball(100, 100); //creates a new Ball object
        	Thread t = new Thread(balls[i]);
            t.start();
        }
        balls[1].dx = 0;
        balls[2].dx = -5;
        balls[2].dy = -5; 
        
        /*
        Ball ball = new Ball(100, 100); //creates a new Ball object
        Thread t = new Thread(ball);
        t.start();
        */
        int j = 0;
        long time = 0;
        boolean flag = true;
        
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
        	
            optFlow.set(src1, src2); //pass to the optical flow class two consecutive frames to make it work
            
            Mat fg_mask = ImgProcessor.filter(src, MOG2); //makes some technical operation on the image and returns the foreground mask
            
            biggestContours = ImgProcessor.contours(fg_mask); //returns the hand contours used to determine if the ball encounter the hand (in the Ball class)
            
            Imgproc.circle(src, balls[0].position, 20, new Scalar(0, 0, 255), -1); //draws the ball
            if (flag) {
            	time = System.currentTimeMillis();
            	flag = false;
            }
            if (System.currentTimeMillis() - time > 10000) {
            	Imgproc.circle(src, balls[1].position, 20, new Scalar(255, 0, 0), -1); //draws the ball
            	j++;
            	if (System.currentTimeMillis() - time > 20000) {
            		Imgproc.circle(src, balls[2].position, 20, new Scalar(0, 255, 0), -1); //draws the ball
                	j++;
            	}
            }
            /*
            Imgproc.circle(src, balls[0].position, 20, new Scalar(0, 0, 255), -1); //draws the ball
            Imgproc.circle(src, balls[1].position, 20, new Scalar(0, 0, 255), -1); //draws the ball
            Imgproc.circle(src, balls[2].position, 20, new Scalar(0, 0, 255), -1); //draws the ball
            
            Imgproc.circle(src, balls[0].position, 20, new Scalar(0, 0, 255), -1); //draws the ball
            Imgproc.circle(src, balls[1].position, 20, new Scalar(255, 0, 0), -1); //draws the ball
            Imgproc.circle(src, balls[2].position, 20, new Scalar(0, 255, 0), -1); //draws the ball
            */
            lbl.setIcon(new ImageIcon(ImgProcessor.bufferedImage(src)));
            frame.add(lbl);
            frame.pack();
        }
        capture.release();
        return;
	}
	
	
	
	static void tutorial() { //only one ball
		VideoCapture capture = new VideoCapture(0); //capture the webcam input
        if (!capture.isOpened()) {
            System.out.println("Unable to open camera!"); //error in opening the video input
            System.exit(-1);
        }
        
		BackgroundSubtractorMOG2 MOG2 = Video.createBackgroundSubtractorMOG2(); //OpenCV object to remove the background
        
        OpticalFlow optFlow = new OpticalFlow(); //instantiate the OpticalFlow thread (runs during the whole program execution)
        optFlow.start();
        
        /*
        Ball[] balls = new Ball[n_balls];
        for (int i = 0; i < n_balls; i++) {
        	balls[i] = new Ball(100, 100); //creates a new Ball object
        }
        */
        
        //Instantiate JFrame
        JFrame frame = new JFrame();
        frame.setLayout(new FlowLayout());
        JLabel lbl = new JLabel();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        Ball ball = new Ball(100, 100); //creates a new Ball object
        Thread t = new Thread(ball);
        t.start();
        
        int j = 0;
        long time = 0;
        
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
        	
            optFlow.set(src1, src2); //pass to the optical flow class two consecutive frames to make it work
            
            Mat fg_mask = ImgProcessor.filter(src, MOG2); //makes some technical operation on the image and returns the foreground mask
            
            biggestContours = ImgProcessor.contours(fg_mask); //returns the hand contours used to determine if the ball encounter the hand (in the Ball class)
            /*
            if (System.currentTimeMillis() - time > 5000 && j < n_balls) {
            	time = System.currentTimeMillis();
            	balls[j].basicMovement(biggestContours, optFlow.angle);
            	Imgproc.circle(src, balls[j].position, 20, new Scalar(0, 0, 255), -1); //draws the ball
            	j++;
            }
            Imgproc.circle(src, balls[0].position, 20, new Scalar(0, 0, 255), -1); //draws the ball
            Imgproc.circle(src, balls[1].position, 20, new Scalar(0, 0, 255), -1); //draws the ball
            Imgproc.circle(src, balls[2].position, 20, new Scalar(0, 0, 255), -1); //draws the ball
            */
            //balls[0].basicMovement(biggestContours, optFlow.angle);
            Imgproc.circle(src, ball.position, 20, new Scalar(0, 0, 255), -1); //draws the ball
            
            lbl.setIcon(new ImageIcon(ImgProcessor.bufferedImage(src)));
            frame.add(lbl);
            frame.pack();
        }
        capture.release();
        return;
	}
}
