package gamestate;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import canvas.MainWindow;
import mapeditor.Tile;
import menu.StartupNew;
import tiles.TileInstance;

public class RedrawObject {
	double x;
	double y;
	int width;
	int height;
	int dx;
	int dy;
	int dw;
	int dh;
	TileInstance t;
	Entity e;
	private StartupNew state;
	
	public void draw(MainWindow m, MapRenderer mr) {
		if (e == null) {
			mr.initDrawTiles(m);
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, state.tilesetTexture.getTextureID());
			m.renderTiles(x,y,width,height,dx,dy,dw,dh,false);
		}
		else {
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, state.getTextureAtlas().getTexture().getTextureID());
			e.draw(m);
		}
	}
	
	
	public RedrawObject(double x, double y, int width, int height, int dx, int dy, int dw, int dh,StartupNew state) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.dx = dx;
		this.dy = dy;
		this.dw = dw;
		this.dh = dw;
		this.state = state;
	}
	
	public RedrawObject(Entity e, StartupNew state) {
		this.e = e;
		this.state =state;
		this.y = e.getYOnScreen() + e.getHeight();
		this.height = e.getHeight();
	}
	
	public double getY() {
		return y;
	}
	
	public int getHeight() {
		return height;
	}
}
