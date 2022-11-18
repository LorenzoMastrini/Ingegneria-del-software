package camera;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

//import org.javaopencvbook.utils.ImageProcessor;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

public class App {
	static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }
	
	private JFrame frame;
	private JLabel imageLabel;
	
	public static void main(String[] args) {
		App app = new App();
		app.initGUI();
		app.runMainLoop(args);
	}
	
	private void initGUI() {
		frame = new JFrame("Camera Input Example");  
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
		frame.setSize(400,400);  
		imageLabel = new JLabel();
		frame.add(imageLabel);
		frame.setVisible(true);       
	}

	private void runMainLoop(String[] args) {
		ImageProcessor imageProcessor = new ImageProcessor();
		Mat webcamMatImage = new Mat();  
		Image tempImage;  
		VideoCapture capture = new VideoCapture(0);   //VideoCapture capture = new VideoCapture(0, Videoio.CAP_ANY);
		capture.set(Videoio.CAP_PROP_FRAME_WIDTH, 1080);
		capture.set(Videoio.CAP_PROP_FRAME_HEIGHT, 720);
		capture.set(Videoio.CAP_PROP_FPS, 50.0);
		
		if( capture.isOpened()){  
			while (true){
				System.out.println(capture.get(Videoio.CAP_PROP_FPS));
				//capture.set(Videoio.CAP_PROP_FRAME_WIDTH,frame.getWidth());
				//capture.set(Videoio.CAP_PROP_FRAME_HEIGHT,frame.getHeight());
				capture.read(webcamMatImage);  
				if( !webcamMatImage.empty() ){
					Core.flip(webcamMatImage, webcamMatImage, 1);   //serve a rendere specchiata l'immagine
					tempImage= imageProcessor.toBufferedImage(webcamMatImage);
					ImageIcon imageIcon = new ImageIcon(tempImage, "Captured video");
					imageLabel.setIcon(imageIcon);
					frame.pack();  //this will resize the window to fit the image
				}  
				else{  
					System.out.println(" -- Frame not captured -- Break!"); 
					break;  
				}
			}  
		}
		else{
			System.out.println("Couldn't open capture.");
		}
		
	}
}