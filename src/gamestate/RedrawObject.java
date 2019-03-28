package gamestate;

import canvas.MainWindow;
import tiles.TileInstance;

public class RedrawObject {
	int x;
	int y;
	TileInstance t;
	
	public void  draw(MainWindow m, MapRenderer mr) {
		mr.redrawTile(m,x,y,t);
	}
	
	public RedrawObject(int x, int y, TileInstance t) {
		this.x = x;
		this.y = y;
		this.t = t;
	}
}
