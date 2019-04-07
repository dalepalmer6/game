package tiles;

import java.util.ArrayList;

public class TileGroup {
	protected ArrayList<SingleInstanceTile> groupTiles;
	protected int curId;
	
	public TileGroup(int id) {
		// TODO Auto-generated constructor stub
		curId = id;
		groupTiles = new ArrayList<SingleInstanceTile>();
	}

	public ArrayList<SingleInstanceTile> getGroupTiles() {
		return groupTiles;
	}
	
}
