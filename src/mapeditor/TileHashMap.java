package mapeditor;

import java.util.HashMap;

public class TileHashMap {
	//hash map of all the tiles in the game
	private HashMap<Integer, Tile> tileMap;
	
	public TileHashMap() {
		tileMap = new HashMap<Integer,Tile>();
	}
	
	public void addTile(Tile value) {
		this.tileMap.put(value.getId(),value);
	}
	
	public Tile getTile(int key) {
		return this.tileMap.get(key);
	}
	
	public int size() {
		return this.tileMap.size();
	}
}
