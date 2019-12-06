package menu;

import system.MainWindow;
import system.interfaces.Drawable;

public class DrawableObject implements Drawable {
	protected double x;
	protected double y;
	protected int width;
	protected int height;
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	
	@Override
	public void draw(MainWindow m) {

	}

}
