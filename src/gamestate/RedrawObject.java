package gamestate;

import canvas.MainWindow;
import mapeditor.Tile;
import tiles.TileInstance;

public class RedrawObject {
	int x;
	int y;
	int width;
	int height;
	int dx;
	int dy;
	int dw;
	int dh;
	TileInstance t;
	Entity e;
	
	public void draw(MainWindow m, MapRenderer mr) {
		mr.initDrawTiles(m);
		m.renderTiles(x,y,width,height,dx,dy,dw,dh,false);
	}
	
	
	public RedrawObject(int x, int y, int width, int height, int dx, int dy, int dw, int dh) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.dx = dx;
		this.dy = dy;
		this.dw = dw;
		this.dh = dw;
	}
	
	public RedrawObject(Entity e) {
		this.e = e;
	}
}
