package tiles.types;

import tiles.PremadeTileObject;

public class DeadTree extends PremadeTileObject {

	public DeadTree(int id) {
		super(id);
		// TODO Auto-generated constructor stub
		width = 3;
		height = 3;
		tg.addTileInstance(0,416,32,32,new String[] {	
				"UNDER","UNDER","UNDER","UNDER",
				"UNDER","UNDER","UNDER","UNDER",
				"UNDER","UNDER","STOP","STOP",
				"UNDER","UNDER","STOP","STOP"});
		tg.addTileInstance(32,416,32,32,new String[] {	
				"UNDER","UNDER","UNDER","UNDER",
				"UNDER","UNDER","UNDER","UNDER",
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP"});
		tg.addTileInstance(64,416,32,32,new String[] {	
				"UNDER","UNDER","UNDER","UNDER",
				"UNDER","UNDER","UNDER","UNDER",
				"STOP","STOP","UNDER","UNDER",
				"STOP","STOP","UNDER","UNDER"});
		
		tg.addTileInstance(0,448,32,32,new String[] {	
				"UNDER","UNDER","STOP","STOP",
				"UNDER","UNDER","STOP","STOP",
				"UNDER","UNDER","STOP","STOP",
				"UNDER","UNDER","STOP","STOP"});
		tg.addTileInstance(32,448,32,32,new String[] {	
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP"});
		tg.addTileInstance(64,448,32,32,new String[] {	
				"STOP","STOP","UNDER","UNDER",
				"STOP","STOP","UNDER","UNDER",
				"STOP","STOP","UNDER","UNDER",
				"STOP","STOP","UNDER","UNDER"});
		
		tg.addTileInstance(0,480,32,32,new String[] {	
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP",
				"PASS","PASS","PASS","PASS"});
		tg.addTileInstance(32,480,32,32,new String[] {	
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP",
				"PASS","PASS","PASS","PASS"});
		tg.addTileInstance(64,480,32,32,new String[] {	
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP",
				"PASS","PASS","PASS","PASS"});
		
		
	}

}
