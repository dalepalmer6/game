package mapeditor;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import canvas.MainWindow;

public class Tile {
	public int id;
	private float dx = 0;
	private float dy = 0;
	private float dw = 0;
	private float dh = 0;
	Texture texture;
	
	public Tile(int id, Texture t) {
		this.id = id;
		this.texture = t;
		getTileCoordinatesInSheet();
	}
	
	public float getDx() {
		return dx;
	}
	
	public float getDy() {
		return dy;
	}
	
	public float getDw() {
		return dw;
	}
	
	public float getDh() {
		return dh;
	}
	
	public int getId() {
		return this.id;
	}
	
	public void getTileCoordinatesInSheet() {
		int tmx = id % (texture.getImageWidth()/16);
		int tmy = id / (texture.getImageWidth()/16); 
		dx = tmx * 16;
		dy = tmy * 16;
		dw = 16;
		dh = 16;
	}
	
	public static void initDrawTiles(MainWindow m) {
		m.setTexture("img/tiles.png");
		m.bindTexture();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
}
