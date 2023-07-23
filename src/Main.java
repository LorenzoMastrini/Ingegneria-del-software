import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;
import org.opencv.core.Core;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;



public class Main  {
	//series of constants and objects used in other classes
	public final static int BORDERS_LIMIT = 10;
	public final static int BUTTONS_WIDTH = 200;
	public final static int BUTTONS_HEIGHT = 50;
	public final static int ROUND_BUTTONS_DIMENSION = 30;
	public static AppFrame appFrame;
	public static Font buttonsFont; //font used for the text inside the biggest buttons
	public static Font smallerFont; //font used for the text outside the buttons and inside the smallest ones
	
	
	
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME); //load OpenCV library
		
		//this is necessary to get the max supported camera sizes to create the JFrame
		VideoCapture capture = new VideoCapture(0); //momentarily capture the camera input
        if (!capture.isOpened()) {
            System.out.println("Unable to open camera!"); //error in opening the video input
            System.exit(-1);
        }
        
        //import the font in res folder
        InputStream is = Main.class.getResourceAsStream("/res/UnscreenMK.ttf"); //InputStream to load the used font
	    try {
	      Font pixelFont = Font.createFont(Font.TRUETYPE_FONT, is); //create the new font in a variable
	      buttonsFont = pixelFont.deriveFont(22f); //resize the font
	      smallerFont = pixelFont.deriveFont(15f); //resize the font
	    } catch (FontFormatException e) {
	      e.printStackTrace();
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	    
        //save the maximum values of width and height of the image (in pixels) based on the resolution of the camera
        Level.maxWidth = (int) capture.get(Videoio.CAP_PROP_FRAME_WIDTH);
    	Level.maxHeight = (int) capture.get(Videoio.CAP_PROP_FRAME_HEIGHT);
    	capture.release(); //close camera
    	
		//creates and sets up the (only) JFrame of the application
		appFrame = new AppFrame();

		//creates and adds the JPanel to the frame
		MenuPanel menuPanel = new MenuPanel();
		appFrame.addPanel(menuPanel);
		appFrame.pack();
		
		return;
	}
}
