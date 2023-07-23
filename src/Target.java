import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;



public class Target extends Ellipse2D {
	private final int WIDTH = 60;
	private final int HEIGHT = 200;
	private final int POSITION_X = Level.maxWidth - WIDTH - Main.BORDERS_LIMIT;
	private final int POSITION_Y = Level.maxHeight/2 - HEIGHT/2;
	
	
	
	@Override
	public Rectangle2D getBounds2D() {
		return new Rectangle(POSITION_X, POSITION_Y, WIDTH, HEIGHT);
	}

	@Override
	public double getX() {
		return POSITION_X;
	}

	@Override
	public double getY() {
		return POSITION_Y;
	}

	@Override
	public double getWidth() {
		return WIDTH;
	}

	@Override
	public double getHeight() {
		return HEIGHT;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public void setFrame(double x, double y, double w, double h) {
		
	}

}
