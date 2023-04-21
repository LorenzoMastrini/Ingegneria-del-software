import java.awt.FlowLayout;
import java.util.Random;

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
import org.opencv.videoio.Videoio;

public class Main {
	public static MatOfPoint biggestContours = new MatOfPoint();
	
	public static void start(int n_balls) {
		VideoCapture capture = new VideoCapture(0); //capture the webcam input
        if (!capture.isOpened()) {
            System.out.println("Unable to open camera!"); //error in opening the video input
            System.exit(-1);
        }
        int max_resolution_width = (int) capture.get(Videoio.CAP_PROP_FRAME_WIDTH);
        int max_resolution_height = (int) capture.get(Videoio.CAP_PROP_FRAME_HEIGHT);
        
		BackgroundSubtractorMOG2 MOG2 = Video.createBackgroundSubtractorMOG2(); //OpenCV object to remove the background
        
        OpticalFlow optFlow = new OpticalFlow(); //instantiate the OpticalFlow thread (runs during the whole program execution)
        optFlow.start();
        
        //Instantiate JFrame
        JFrame frame = new JFrame();
        frame.setLayout(new FlowLayout());
        JLabel lbl = new JLabel();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        Random rand = new Random();
        Ball[] balls = new Ball[n_balls]; //array of balls, with every ball that will be spawned during the game
        //creates a new Ball object (and starts the corresponding thread) each iteration
        for (int i = 0; i < n_balls; i++) {
        	//create the balls in a random initial position
        	balls[i] = new Ball(rand.nextInt((max_resolution_width - 20 - 20) + 1) + 20, rand.nextInt((max_resolution_height - 20 - 20) + 1) + 20); //the 20s are the radius of the balls
        	Thread t = new Thread(balls[i]);
            t.start();
        }
        
        //these three variables are used to draw a new ball each 10 seconds (in the while loop)
        long time = 0;
        boolean flag_first_ball = true;
        int current_balls = 0;
        
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
            
            if (flag_first_ball) { //entered only for the first iteration of the cicle, to spawn the first ball
            	time = System.currentTimeMillis(); 
            	flag_first_ball = false;
            	current_balls = 1;
            }
            if (System.currentTimeMillis() - time > 10000 && current_balls < n_balls) { //each 10 seconds this adds a new ball
            	time = System.currentTimeMillis();
            	current_balls++;
            }
            for (int i = 0; i < current_balls; i++) {
            	Imgproc.circle(src, balls[i].position, 20, new Scalar(0, 0, 255), -1); //draws the ball(s)
            }
            
            lbl.setIcon(new ImageIcon(ImgProcessor.bufferedImage(src)));
            frame.add(lbl);
            frame.pack();
        }
        capture.release();
        return;
	}
}
