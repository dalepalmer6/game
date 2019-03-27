package tiles.types;

import tiles.MultiInstanceTile;

public class Mountain extends MultiInstanceTile{
	public Mountain(int id) {
		super(id);
		tcm.setAllCollisions(new String[] {	"STOP","STOP","STOP","STOP",
								"STOP","STOP","STOP","STOP",
								"STOP","STOP","STOP","STOP",
								"STOP","STOP","STOP","STOP"});
		tg.addTileInstance(48,192,16,16);
		tg.addTileInstance(48,208,16,16);
		tg.addTileInstance(64,208,16,16);
		tg.addTileInstance(80,208,16,16);
		tg.addTileInstance(48,224,16,16);
		tg.addTileInstance(64,224,16,16);
		tg.addTileInstance(80,224,16,16);
		tg.addTileInstance(48,240,16,16);
		tg.addTileInstance(64,240,16,16);
		tg.addTileInstance(80,240,16,16);
	}
}
