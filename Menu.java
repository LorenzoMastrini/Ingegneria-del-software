import java.io.IOException;
import org.opencv.core.Core;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;


public class Menu  {
	
	public static MyFrame appFrame;
	public static MenuPanel menuPanel;
	
	
	static {
		var path = System.getProperty("java.library.path");

	   if (path == null) {
	     throw new RuntimeException("Path isn't set.");
	   }
/*
	   var paths = java.util.List.of(path.split(";"));
	   //paths.forEach(System.out::println);

	   if (!paths.contains("C:/Users/Rollbar/lib")) {
	     throw new RuntimeException("Path to library is missing.");
	   }
*/
	   System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	native void libraryMethod(String arg);
	
	public static void main(String[] args) throws IOException {
		//System.getProperty("java.library.path");
		//System.loadLibrary(Core.NATIVE_LIBRARY_NAME); //load OpenCV library
		
		
		//this is necessary to get the max supported camera sizes to create the JFrame
		VideoCapture capture = new VideoCapture(0); //capture the camera input
        if (!capture.isOpened()) {
            System.out.println("Unable to open camera!"); //error in opening the video input
            System.exit(-1);
        }
        
        //save the maximum values of width and height of the image (in pixels) based on the resolution of the camera
        Level.MAX_WIDTH = (int) capture.get(Videoio.CAP_PROP_FRAME_WIDTH);
    	Level.MAX_HEIGHT = (int) capture.get(Videoio.CAP_PROP_FRAME_HEIGHT);
    	capture.release();
    	
		//creates and sets up the only JFrame of the application
		appFrame = new MyFrame();
		
		//creates and adds the JPanel to the frame
		menuPanel = new MenuPanel();
		appFrame.add(menuPanel);
		appFrame.pack();
		return;
	}
}
