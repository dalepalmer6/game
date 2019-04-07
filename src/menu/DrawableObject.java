package menu;

import org.lwjgl.opengl.GL11;

import canvas.Drawable;
import canvas.MainWindow;

public class DrawableObject implements Drawable {
	protected int x;
	protected int y;
	protected int width;
	protected int height;
	
	public int getX() {
		return x;
	}
	
	public int getY() {
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
