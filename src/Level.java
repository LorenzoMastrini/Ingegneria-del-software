import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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



public class Level implements Runnable {
	public static final int MAX_UPS = 20;
	public static final int MAX_POINTS = 10; //points to win the game
	//maximum values of width and height of the image (in pixels) based on the resolution of the camera
	public static int maxWidth;
	public static int maxHeight;
	public static MatOfPoint biggestContours;
	public static int chosenDifficulty;
	public BufferedImage currentFrame; //used in the GamePanel class to draw the images read by the camera
	public List<Ball> ballList;
	public Timer timer;
	public int second = 3, minute = 0; //initial available time
	public boolean isPaused;
	public boolean victory;
	private boolean end; //flag that indicates if the game is over, to stop all the threads
	private boolean interrupted; //indicates whether the game has stopped earlier or has ended regularly
	private int points; //points during the game
	private int counterNegativeBalls;
	private Thread t;
	private VideoCapture capture;
	private GamePanel gamePanel;
	private OpticalFlow optFlow;
	private DecimalFormat dFormat;
	
	
	public Level(int difficulty) {
		capture = new VideoCapture(0); //capture the camera input
        if (!capture.isOpened()) {
            System.out.println("Unable to open camera!"); //error in opening the video input
            System.exit(-1);
        }
                
		//set up the JPanel to be added to the JFrame (Menu.appFrame)
		gamePanel = new GamePanel(this);
		
		this.counterNegativeBalls = 1;
		this.ballList = new ArrayList<Ball>();
		this.points = 0;
		this.end = false;
		this.isPaused = false;
		this.interrupted = false;
		this.victory = false;
		this.dFormat = new DecimalFormat("00");
		chosenDifficulty = difficulty;
		biggestContours = new MatOfPoint();
	}

	
	
	@Override
	public void run() {
		BackgroundSubtractorMOG2 MOG2 = Video.createBackgroundSubtractorMOG2(); //OpenCV object to remove the background
        
        this.optFlow = new OpticalFlow(); //instantiate the OpticalFlow thread (runs during the whole Level thread execution)
        optFlow.start();
        
        timer(); //start the timer for the game
		
		Main.appFrame.addPanel(gamePanel);
		
		//matrixes read by camera
		Mat src = new Mat(); //src is used to make all image processing operations, and it is the one that is displayed
		//src1 e src2 are the matrixes passed to the optical flow algorithm and they are used only for this purpose
		Mat src1 = new Mat();
		Mat src2 = new Mat();
		Mat fg_mask; //foreground mask, used to find hand contours
		
        //main loop
        while (!end) {
            capture.read(src); //read frame from webcam
            if (src.empty()) {
                break;
            }
            Core.flip(src, src, 1); //makes the image mirrored
            
            capture.read(src1); //read frame from webcam
            if (src1.empty()) {
                break;
            }
            Core.flip(src1, src1, 1); //makes the image mirrored
            
            capture.read(src2); //read frame from webcam
            if (src2.empty()) {
                break;
            }
            Core.flip(src2, src2, 1); //makes the image mirrored
        	
            optFlow.set(src1, src2); //pass to the optical flow class two consecutive frames to make it work
            
            fg_mask = ImgProcessor.filter(src, MOG2); //processing operations on the image and returns the foreground mask
            
            biggestContours = ImgProcessor.contours(fg_mask); //returns the hand contours used to determine if the ball encounter the hand (in the Ball class)
            
            currentFrame = ImgProcessor.bufferedImage(src); //buffered image used in the method paintComponent() in GamePanel class to draw the source image into the game panel
            
            //if the game has been stopped do not update the current situation (points, timer, spawned balls, ...)
            if(!this.isPaused) {
            	this.updateBalls(this); //checks if we have to update the game in terms of how many balls are on screen, and eventually adds new ones
            	
    			Main.appFrame.revalidate();
    			Main.appFrame.repaint();
            	gamePanel.repaint(); //draw the image and every graphic component of the game (balls, target, timer, ...)
            }
            
            if (this.points == MAX_POINTS && chosenDifficulty != 0) {
            	victory = true;
            	endGame(false);
            } else if (this.second == 0 && this.minute == 0 && chosenDifficulty != 0) {
            	victory = false;
            	endGame(false);
            }
        }
		
		if (!interrupted) { //if the game is ended regularly (victory or defeat)
			//create the end game menu
			EndGamePanel endGamePanel = new EndGamePanel(this);
			Main.appFrame.addPanel(endGamePanel);
		}
	}

	
	
	public void start() {
		if (t == null) {
	         t = new Thread(this);
	         t.start();
	      }
	}
	
	
	
	public void timer() {
		this.timer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				second--;
				dFormat.format(second);
				dFormat.format(minute);
				
				if(second == -1) {
					if (minute == 0) {
						second = 0;
						timer.stop(); //time's up
					} else {
						second = 59;
						minute--;
					}
					
					dFormat.format(second);
					dFormat.format(minute);
				}
			}
		});
		
		timer.start();
		return;
	}
	
	
	
	//method responsible for checking if we have to generate new balls
	public void updateBalls(Level level) {
		if (chosenDifficulty == 0) { //in level 0 we have only one positive ball
			if (ballList.isEmpty()) {
				Ball newBall; //create a new ball
				newBall = new Ball(true); //we pass 'true' as argument because the spawned ball is positive
				ballList.add(newBall);
				Thread t = new Thread(newBall);
	            t.start();
			}
		} else if (chosenDifficulty == 2 || chosenDifficulty == 3) { //in levels 2 and 3 we introduce negative balls
			if (Ball.isReadyToSpawn(level)) {
				Ball newBall; //create a new ball
				if (counterNegativeBalls % 4 == 0) { //each three positive ball we spawn a negative one
					newBall = new Ball(false); //false means that the ball is a negative one
				} else {
					newBall = new Ball(true); //true means that the ball is a positive one
				}
				
				ballList.add(newBall);
				Thread t = new Thread(newBall);
	            t.start();
	            counterNegativeBalls++;
			}
		} else { //we don't have negative balls
			if (Ball.isReadyToSpawn(level)) {
				Ball newBall; //create a new ball
				newBall = new Ball(true); //balls are always positive, so we pass 'true' as argument
				ballList.add(newBall);
				Thread t = new Thread(newBall);
	            t.start();
			}
		}
	}
	
	
	
	public void updatePoints(boolean increase) { //increase or decrease the points whether the ball was positive or negative
		if (increase) {
			this.points++;
		} else if (chosenDifficulty == 2) {
			this.points--; //at intermediate level negative balls decrement the score by 1 point
		} else if (chosenDifficulty == 3) {
			this.points = this.points - 3; //at advanced level negative balls decrement the score by 3 points
		}
		
		//the minimum score is always 0
		if (this.points < 0) {
			this.points = 0;
		}
	}
	
	
	
	public void pause() {
		for (Ball ball : ballList) { //iterate through the list of balls
			ball.pause();
		}
		this.isPaused = true;
		
		this.timer.stop();
	}
	
	
	
	public void resume() {
		this.isPaused = false;
        gamePanel.pauseButton.setEnabled(true);
        
        for (Ball ball : ballList) { //iterate through the list of balls
			ball.resume();
		}
		
        if (chosenDifficulty != 0) {
        	this.timer.start();
        }
	}
	
	
	
	//cause the stopping of every running thread in the game and remove the gamePanel
	public void endGame(boolean interrupted) {
		timer.stop(); //time's up
		this.interrupted = interrupted;
		capture.release(); //first release the camera
		optFlow.stop(true); //stop the opticalFlow thread
		end = true; //set the variable 'end' to true so that the level thread stops too
		
		for (Ball ball : ballList) { //iterate through the list
			ball.finished(); //stop the threads of all the remaining balls
		}
		ballList.clear(); //delete all the balls from the list
		
		//remove the gamePanel from the JFrame
		Main.appFrame.removePanel(gamePanel);
	}



	public int getPoints() {
		return this.points;
	}
}