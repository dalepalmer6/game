package tiles;

import java.util.ArrayList;

public class TileCollisionMap {
	//collision can be detected down to the 4x4 of the 16x16 tile, meaning there are eight collision points that can be different on a tile.
	//if collision == true, then not passable
	ArrayList<String> collisions; 
	
	public void setAllCollisions(String[] s) {
		for (String collision : s) {
			collisions.add(collision);
		}
	}
	
	public void setCollision(int i, String value) {
		collisions.set(i,value);
	}
	
	public String getCollisionData(int i) {
		return collisions.get(i);
	}
	
	public TileCollisionMap() {
		collisions = new ArrayList<String>(8); 
	}
}
