package tiles.types;

import mapeditor.Tile;
import tiles.SingleInstanceTile;

public class Plain extends SingleInstanceTile {

	public Plain(int id) {
		super(id);
		tg.addTileInstance(96,0,16,16);
	}

}