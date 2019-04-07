package gamestate;

import canvas.MainWindow;
import tiles.TileInstance;

public class RedrawObject {
	int x;
	int y;
	TileInstance t;
	Entity e;
	
	public void  draw(MainWindow m, MapRenderer mr) {
		e.draw(m);
	}
	
	public RedrawObject(Entity e) {
		this.e = e;
	}
}
