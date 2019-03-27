package mapeditor;

import java.awt.Rectangle;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

import canvas.MainWindow;

public class Tile {
	public int id;
	private float dx = 0;
	private float dy = 0;
	private float dw = 0;
	private float dh = 0;
	private Rectangle tileMapBounds;
	
	public Tile(int id) {
		this.id = id;
	}
	
	public String getCollisionInfoAtIndex(int x, int y) {
		return null;
	}
	
	public Tile(int id, Rectangle tileBounds) {
		this.id = id;
		tileMapBounds = tileBounds;
		getTileCoordinatesInSheet();
	}
	
	public int getDx(int inst_id) {
		return 1;
	}
	public int getDy(int inst_id) {
		return 1;
	}
	public int getDw(int inst_id) {
		return 1;
	}
	public int getDh(int inst_id) {
		return 1;
	}
	
//	public float getDx() {
//		return dx;
//	}
//	
//	public float getDy() {
//		return dy;
//	}
//	
//	public float getDw() {
//		return dw;
//	}
//	
//	public float getDh() {
//		return dh;
//	}
//	
	public int getId() {
		return this.id;
	}
	
	public void getTileCoordinatesInSheet() {
		int tmx = id % (tileMapBounds.width/16);
		int tmy = id / (tileMapBounds.width/16); 
		dx = tmx * 16;
		dy = tmy * 16;
		dw = 16;
		dh = 16;
	}
	
	public static void initDrawTiles(MainWindow m) {
		m.setTexture("img/tiles.png");
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
}
