package tiles.types;

import tiles.GroupableTile;
import tiles.TileGroup;

public class RoadGroup extends TileGroup{

	public RoadGroup(int id) {
		super(id);
		// TODO Auto-generated constructor stub
		groupTiles.add(new GroupableTile(id++,352,320,32,32));
		groupTiles.add(new GroupableTile(id++,384,320,32,32));
		groupTiles.add(new GroupableTile(id++,416,320,32,32));
		groupTiles.add(new GroupableTile(id++,448,320,32,32));
		groupTiles.add(new GroupableTile(id++,480,320,32,32));
		groupTiles.add(new GroupableTile(id++,512,320,32,32));
		groupTiles.add(new GroupableTile(id++,544,320,32,32));
		
		groupTiles.add(new GroupableTile(id++,352,352,32,32));
		groupTiles.add(new GroupableTile(id++,384,352,32,32));
		groupTiles.add(new GroupableTile(id++,416,352,32,32));
		groupTiles.add(new GroupableTile(id++,448,352,32,32));
		groupTiles.add(new GroupableTile(id++,480,352,32,32));
		groupTiles.add(new GroupableTile(id++,512,352,32,32));
		groupTiles.add(new GroupableTile(id++,544,352,32,32));
		
		groupTiles.add(new GroupableTile(id++,352,384,32,32));
		groupTiles.add(new GroupableTile(id++,384,384,32,32));
		groupTiles.add(new GroupableTile(id++,416,384,32,32));
		groupTiles.add(new GroupableTile(id++,448,384,32,32));
		groupTiles.add(new GroupableTile(id++,480,384,32,32));
		groupTiles.add(new GroupableTile(id++,512,384,32,32));
		
	}

}
