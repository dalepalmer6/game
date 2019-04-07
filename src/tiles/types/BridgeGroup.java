package tiles.types;

import tiles.GroupableTile;
import tiles.TileGroup;

public class BridgeGroup extends TileGroup {

	public BridgeGroup(int id) {
		super(id);
		groupTiles.add(new GroupableTile(id++,256,384,32,32,
				new String[] {	
				"PASS","PASS","PASS","PASS",
				"PASS","PASS","PASS","PASS",
				"PASS","PASS","PASS","PASS",
				"PASS","PASS","PASS","PASS"}));
		groupTiles.add(new GroupableTile(id++,288,384,32,32,new String[] {	
				"PASS","PASS","PASS","PASS",
				"PASS","PASS","PASS","PASS",
				"PASS","PASS","PASS","PASS",
				"PASS","PASS","PASS","PASS"}));
		groupTiles.add(new GroupableTile(id++,320,384,32,32,new String[] {	
				"PASS","PASS","PASS","PASS",
				"PASS","PASS","PASS","PASS",
				"PASS","PASS","PASS","PASS",
				"PASS","PASS","PASS","PASS"}));
	}
	
}
