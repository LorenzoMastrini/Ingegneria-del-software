import java.io.IOException;
import org.opencv.core.Core;


public class Menu {

	public static void main(String[] args) throws IOException{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME); //load OpenCV library
		
		Main.start(3); //main loop
		//Main.tutorial(); //main loop with one single ball
		
		return;
	}
}
