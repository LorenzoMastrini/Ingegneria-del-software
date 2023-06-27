import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Timer;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.video.BackgroundSubtractorMOG2;
import org.opencv.video.Video;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;



public class Level implements Runnable {
	public static final double MAX_UPS = 10;
	public static int MAX_WIDTH;
	public static int MAX_HEIGHT;
	public List<Ball> ballList;
	public static MatOfPoint biggestContours;
	public static BufferedImage currentFrame; //used in the GamePanel class to draw the images read by the camera
	private boolean end; //flag that indicates if the game is over, to stop all the threads
	private Thread t;
	private VideoCapture capture;
	private GamePanel gamePanel;
	private int points;
	Timer timer;
	int second = 10, minute = 0;
	String ddSecond, ddMinute;
	DecimalFormat dFormat = new DecimalFormat("00");
	private int counter;
	
	
	
	public Level() {
		capture = new VideoCapture(0); //capture the camera input
        if (!capture.isOpened()) {
            System.out.println("Unable to open camera!"); //error in opening the video input
            System.exit(-1);
        }
        //save the maximum values of width and height of the image (in pixels) based on the resolution of the camera
        MAX_WIDTH = (int) capture.get(Videoio.CAP_PROP_FRAME_WIDTH);
    	MAX_HEIGHT = (int) capture.get(Videoio.CAP_PROP_FRAME_HEIGHT);
        
		//set up the JPanel to be added to the JFrame (Menu.appFrame)
		gamePanel = new GamePanel(this);
		
		counter = 0;
		ballList = new ArrayList<Ball>();
		this.points = 0;
		end = false;
		biggestContours = new MatOfPoint();
	}

	
	
	@Override
	public void run() {
		BackgroundSubtractorMOG2 MOG2 = Video.createBackgroundSubtractorMOG2(); //OpenCV object to remove the background
        
        OpticalFlow optFlow = new OpticalFlow(); //instantiate the OpticalFlow thread (runs during the whole Level thread execution)
        optFlow.start();
                
        //(PROVVISORIO) these variable is used to increment the points each 10 seconds (in the while loop)
        long time = System.currentTimeMillis();;
        
        //main loop
        while (!end) {
        	//src is used to make all image processing operations, and it is the one that is displayed
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
            
            if (System.currentTimeMillis() - time > 10000) { //each 10 seconds increment the points
            	time = System.currentTimeMillis();
            	this.counter++;
            }
            
            this.update(this); //checks if we have to update the game in terms of how many balls are on screen, and eventually adds new ones
            
            currentFrame = ImgProcessor.bufferedImage(src); //buffered image used in the method paintComponent() in GamePanel class to draw the source image into the game panel
            gamePanel.repaint(); //draw the image and every graphic component of the game (balls, target, timer, ...)
            
            Menu.appFrame.add(gamePanel);
            Menu.appFrame.revalidate();
            Menu.appFrame.repaint();
            
            //if the game is over stop the opticalFlow thread and set the variable 'end' to true so that the level thread stops too
            if (counter == 4) {
            	end = true;
            	optFlow.stop(true);
            }
        }

        capture.release();
        endGame();
	}

	
	
	public void start() {
		if (t == null) {
	         t = new Thread(this);
	         t.start();
	      }
	}
	
	
	
	public void update(Level level) {
		if (Ball.isReadyToSpawn(level)) {
			Ball newBall = new Ball();
			ballList.add(newBall);
			Thread t = new Thread(newBall);
            t.start();
		}
	}
	
	
	
	public void updatePoints(boolean increase) { //increase or decrease the points whether the ball was red or black
		if (increase) {
			this.points++;
			System.out.println("Nuovo punteggio: " + points);
		} else {
			this.points--;
		}
	}
	
	
	
	public void endGame() {
		for (Ball ball : ballList) { //iterate through the list
			ball.finished();
		}
		ballList.clear(); //delete all the balls from the list
		
		Menu.appFrame.remove(gamePanel); //remove the gamePanel from the JFrame
		
		//"go back" to the main menu
		MenuPanel menuPanel = new MenuPanel();
		Menu.appFrame.add(menuPanel);
		Menu.appFrame.revalidate();
		Menu.appFrame.repaint();
	}
}
