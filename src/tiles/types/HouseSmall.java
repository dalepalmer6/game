package tiles.types;

import tiles.MultiInstanceTile;
import tiles.PremadeTileObject;

public class HouseSmall extends PremadeTileObject {
	
	
	public HouseSmall(int id) {
		super(id);
		width = 2;
		height = 2;
		tg.addTileInstance(96,64,32,32,new String[] {	
				"PASS","PASS","PASS","UNDER",
				"PASS","PASS","PASS","UNDER",
				"PASS","PASS","UNDER","UNDER",
				"PASS","UNDER","UNDER","UNDER"});
		tg.addTileInstance(128,64,32,32,new String[] {	
				"UNDER","UNDER","UNDER","UNDER",
				"UNDER","UNDER","UNDER","UNDER",
				"UNDER","UNDER","UNDER","UNDER",
				"UNDER","UNDER","UNDER","UNDER"});
		tg.addTileInstance(96,96,32,32,new String[] {	
				"UNDER","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","PASS","PASS"});
		tg.addTileInstance(128,96,32,32,new String[] {	
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","PASS",
				"STOP","STOP","PASS","PASS"});
	}

	public void editTool(int x, int y) {
		//put instance 1 at x,y
		//put instance 2
	}
}
