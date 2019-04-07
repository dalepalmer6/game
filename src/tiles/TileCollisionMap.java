package tiles;

import java.util.ArrayList;

public class TileCollisionMap {
	//collision can be detected down to the 4x4 of the 16x16 tile, meaning there are eight collision points that can be different on a tile.
	//if collision == true, then not passable
	ArrayList<Integer> collisions; 
	
	public void setAllCollisions(int[] s) {
		for (int collision : s) {
			collisions.add(collision);
		}
	}
	
	public void setCollision(int i, int value) {
		collisions.set(i,value);
	}
	
	public int getCollisionData(int i) {
		return collisions.get(i);
	}
	
	public TileCollisionMap() {
		collisions = new ArrayList<Integer>(); 
	}
}
