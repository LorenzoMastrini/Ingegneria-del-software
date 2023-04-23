package prova_collisione;

import java.util.Scanner;

public class Main_bozza_gioco {
	public static void main(String[] args) {
		Frame f = new Frame();
	    f.setVisible(true);
		
		Scanner sc = new Scanner(System.in);    //System.in is a standard input stream  
		System.out.print("Enter the level number (1-3): ");  
		int input = sc.nextInt(); 
		switch(input) {
			case 1:
				level(f);
				break;
			default:
				break;
		}

		System.out.println("uscita dal gioco");
	}
	
	
	public static void level(Frame frame) {
		Game game = new Game(frame);
		game.start();
		return;
	}
}
