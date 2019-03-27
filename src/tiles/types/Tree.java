package tiles.types;

import tiles.MultiInstanceTile;

public class Tree extends MultiInstanceTile{
	public Tree(int id) {
		super(id);
		tcm.setAllCollisions(new String[] {	"STOP","STOP","STOP","STOP",
								"STOP","STOP","STOP","STOP",
								"STOP","STOP","STOP","STOP",
								"STOP","STOP","STOP","STOP"});
		tg.addTileInstance(0,144,16,16);
		tg.addTileInstance(0,144,16,16);
		tg.addTileInstance(16,144,16,16);
		tg.addTileInstance(32,144,16,16);
		tg.addTileInstance(0,160,16,16);
		tg.addTileInstance(16,160,16,16);
		tg.addTileInstance(32,160,16,16);
		tg.addTileInstance(0,176,16,16);
		tg.addTileInstance(16,176,16,16);
		tg.addTileInstance(32,176,16,16);
	}
}
