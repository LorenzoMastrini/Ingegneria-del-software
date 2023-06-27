import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class Target extends Ellipse2D {
	public static final int width = 50;
	public static final int height = 150;
	public static final int positionX = Level.MAX_WIDTH - width;
	public static final int positionY = Level.MAX_HEIGHT/2 - height/2;
	
	

	@Override
	public Rectangle2D getBounds2D() {
		return null;
	}

	@Override
	public double getX() {
		return positionX;
	}

	@Override
	public double getY() {
		return positionY;
	}

	@Override
	public double getWidth() {
		return width;
	}

	@Override
	public double getHeight() {
		return height;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public void setFrame(double x, double y, double w, double h) {
		
	}

}
