import java.io.IOException;
import org.opencv.core.Core;


public class Menu  {
	public static MyFrame appFrame;
	public static MenuPanel menuPanel;
	
	
	
	public static void main(String[] args) throws IOException {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME); //load OpenCV library
		
		//creates and sets up the only JFrame of the application
		appFrame = new MyFrame();
		appFrame.setBounds(0, 0, 654, 518);
		
		//creates and adds the JPanel to the frame
		MenuPanel menuPanel = new MenuPanel();
		appFrame.add(menuPanel);
		
		return;
	}
}
