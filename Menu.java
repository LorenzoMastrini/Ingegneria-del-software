import java.io.IOException;
import org.opencv.core.Core;


public class Menu  {
	public static MyFrame appFrame;
	public static MenuPanel menuPanel;
	
	
	public static void main(String[] args) throws IOException {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME); //load OpenCV library
		
		appFrame = new MyFrame();
		appFrame.setBounds(0, 0, 664, 528);
		appFrame.setResizable(false);
		
		MenuPanel menuPanel = new MenuPanel();
		appFrame.add(menuPanel);
		
		return;
	}
}
