import java.io.IOException;
import org.opencv.core.Core;


public class Menu {

	public static void main(String[] args) throws IOException{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME); //load OpenCV library
		
		Main.start(5); //main loop (the parameter is the number of balls we want to spawn)
		
		return;
	}
}
