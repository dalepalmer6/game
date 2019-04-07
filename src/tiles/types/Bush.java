package tiles.types;

import tiles.SingleInstanceTile;

public class Bush extends SingleInstanceTile {

	public Bush(int id) {
		super(id);
		// TODO Auto-generated constructor stub
		tg.addTileInstance(352,256,32,32,new String[] {	
				"PASS","STOP","STOP","PASS",
				"PASS","STOP","STOP","STOP",
				"STOP","STOP","STOP","PASS",
				"STOP","STOP","STOP","PASS"});
	}

}
