package menu;

import org.lwjgl.opengl.GL11;

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
//		// TODO Auto-generated method stub
//		m.setTexture("img/button.png");
//		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
//		GL11.glEnable(GL11.GL_TEXTURE_2D);
//		m.renderTile(x,y,width,height);
	}

}
