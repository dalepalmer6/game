package tiles.types;

import tiles.PremadeTileObject;

public class HouseLarge extends PremadeTileObject {

	public HouseLarge(int id) {
		super(id);
		width = 3;
		height = 3;
		tg.addTileInstance(96,128,32,32,new String[] {	
				"UNDER","UNDER","UNDER","UNDER",
				"UNDER","UNDER","UNDER","UNDER",
				"UNDER","UNDER","UNDER","UNDER",
				"UNDER","UNDER","UNDER","UNDER"});
		tg.addTileInstance(128,128,32,32,new String[] {	
				"UNDER","UNDER","UNDER","UNDER",
				"UNDER","UNDER","UNDER","UNDER",
				"UNDER","UNDER","UNDER","UNDER",
				"UNDER","UNDER","UNDER","UNDER"});
		tg.addTileInstance(160,128,32,32,new String[] {	
				"UNDER","UNDER","UNDER","UNDER",
				"UNDER","UNDER","UNDER","UNDER",
				"UNDER","UNDER","UNDER","UNDER",
				"UNDER","UNDER","UNDER","UNDER"});
		tg.addTileInstance(96,160,32,32,new String[] {	
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP"});
		tg.addTileInstance(128,160,32,32,new String[] {	
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP"});
		tg.addTileInstance(160,160,32,32,new String[] {	
				"STOP","STOP","PASS","PASS",
				"STOP","STOP","PASS","PASS",
				"STOP","STOP","PASS","PASS",
				"STOP","STOP","PASS","PASS"});
		tg.addTileInstance(96,192,32,32,new String[] {	
				"STOP","STOP","STOP","STOP",
				"STOP","PASS","PASS","PASS",
				"PASS","PASS","PASS","PASS",
				"PASS","PASS","PASS","PASS"});
		tg.addTileInstance(128,192,32,32,new String[] {	
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP",
				"PASS","PASS","PASS","PASS",
				"PASS","PASS","PASS","PASS"});
		tg.addTileInstance(160,192,32,32,new String[] {	
				"STOP","PASS","PASS","PASS",
				"PASS","PASS","PASS","PASS",
				"PASS","PASS","PASS","PASS",
				"PASS","PASS","PASS","PASS"});
	}

}
