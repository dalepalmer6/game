package tiles.types;

import java.awt.Rectangle;

import mapeditor.Tile;
import tiles.MultiInstanceTile;

public class Path extends MultiInstanceTile {
	
	
	public Path(int id) {
		super(id);
//		tcm.setAllCollisions(new String[] {	"PASS","PASS","PASS","PASS",
//				"PASS","PASS","PASS","PASS",
//				"PASS","PASS","PASS","PASS",
//				"PASS","PASS","PASS","PASS"});
		tg.addTileInstance(0,160,32,32);
		tg.addTileInstance(0,160,32,32);
		tg.addTileInstance(32,160,32,32);
		tg.addTileInstance(64,160,32,32);
		tg.addTileInstance(0,192,32,32);
		tg.addTileInstance(32,192,32,32);
		tg.addTileInstance(64,192,32,32);
		tg.addTileInstance(0,224,32,32);
		tg.addTileInstance(32,224,32,32);
		tg.addTileInstance(64,224,32,32);
		tg.addTileInstance(0,96,32,32);
		tg.addTileInstance(0,128,32,32);
		tg.addTileInstance(32,96,32,32);
		tg.addTileInstance(32,128,32,32);
	}

}
