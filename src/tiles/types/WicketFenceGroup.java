package tiles.types;

import tiles.GroupableTile;
import tiles.TileGroup;

public class WicketFenceGroup extends TileGroup {

	public WicketFenceGroup(int id) {
		super(id);
		groupTiles.add(new GroupableTile(id++,384,256,32,32,new String[] {	
				"PASS","PASS","PASS","UNDER",
				"PASS","PASS","STOP","UNDER",
				"PASS","UNDER","UNDER","STOP",
				"STOP","STOP","STOP","PASS"}));
		groupTiles.add(new GroupableTile(id++,416,256,32,32,new String[] {	
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP",
				"PASS","PASS","PASS","PASS",
				"PASS","PASS","PASS","PASS"}));
		groupTiles.add(new GroupableTile(id++,480,256,32,32,new String[] {	
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP",
				"PASS","PASS","STOP","STOP",
				"PASS","UNDER","STOP","STOP"}));
		groupTiles.add(new GroupableTile(id++,352,288,32,32,new String[] {	
				"PASS","PASS","PASS","UNDER",
				"PASS","PASS","UNDER","STOP",
				"PASS","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP"}));
		groupTiles.add(new GroupableTile(id++,384,288,32,32,new String[] {	
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP",
				"UNDER","UNDER","STOP","STOP"}));
		groupTiles.add(new GroupableTile(id++,448,288,32,32,new String[] {	
				"PASS","PASS","PASS","UNDER",
				"PASS","PASS","UNDER","STOP",
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP"}));
		groupTiles.add(new GroupableTile(id++,448,288,32,32,new String[] {	
				"PASS","PASS","PASS","UNDER",
				"PASS","PASS","UNDER","STOP",
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP"}));
		groupTiles.add(new GroupableTile(id++,416,288,32,32,new String[] {	
				"PASS","PASS","PASS","PASS",
				"PASS","PASS","PASS","PASS",
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP"}));
		groupTiles.add(new GroupableTile(id++,484,288,32,32,new String[] {	
				"STOP","PASS","PASS","PASS",
				"PASS","PASS","PASS","PASS",
				"PASS","PASS","PASS","PASS",
				"PASS","PASS","PASS","PASS"}));
	}

}
