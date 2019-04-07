package tiles.types;

import tiles.PremadeTileObject;

public class Gravestone extends PremadeTileObject {

	public Gravestone(int id) {
		super(id);
		// TODO Auto-generated constructor stub
		width = 2;
		height = 1;
		tg.addTileInstance(96,416,32,32,new String[] {	
				"PASS","PASS","PASS","PASS",
				"PASS","PASS","PASS","PASS",
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP"});
		tg.addTileInstance(128,416,32,32,new String[] {	
				"UNDER","UNDER","UNDER","UNDER",
				"UNDER","UNDER","UNDER","UNDER",
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP"});
	}

}
