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
		tg.addTileInstance(0,192,16,16);
		tg.addTileInstance(0,208,16,16);
		tg.addTileInstance(16,208,16,16);
		tg.addTileInstance(32,208,16,16);
		tg.addTileInstance(0,224,16,16);
		tg.addTileInstance(16,224,16,16);
		tg.addTileInstance(32,224,16,16);
		tg.addTileInstance(0,240,16,16);
		tg.addTileInstance(16,240,16,16);
		tg.addTileInstance(32,240,16,16);
		tg.addTileInstance(48,208,16,16);
		tg.addTileInstance(48,224,16,16);
	}

}
