import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;



public class AppFrame extends JFrame {
	
	public AppFrame() {
		this.setTitle("CatcheM_CV");
		
		//set the icon of the app, read from file
		ImageIcon logoIcon = new ImageIcon(this.getClass().getResource("/res/yellow_ball.png")); //read the image
		this.setIconImage(logoIcon.getImage());
		
		this.setResizable(false);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
	
	public void addPanel(JPanel panel) {
		Main.appFrame.add(panel);
		Main.appFrame.revalidate();
		Main.appFrame.repaint();
	}
	
	
	
	public void removePanel(JPanel panel) {
		Main.appFrame.remove(panel);
		Main.appFrame.revalidate();
		Main.appFrame.repaint();
	}
}
