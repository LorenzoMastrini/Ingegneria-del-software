

import org.opencv.core.Core;


public class Menu {

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME); //load OpenCV library
		
		Main.start(); //main loop
	}
}
