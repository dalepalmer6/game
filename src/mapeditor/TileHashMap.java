package mapeditor;

import java.util.HashMap;

import tiles.TileInstance;

public class TileHashMap {
	//hash map of all the tiles in the game
	private HashMap<Integer, Tile> tileMap;
	
	public String tilesOutput() {
		String group = "#Group\ngroup\n";
		for (int s : tileMap.keySet()) {
			Tile t = tileMap.get(s);
			TileInstance ti = t.getInstance(0);
			int dx = ti.getDx();
			int dy = ti.getDy();
			int dw = ti.getDw();
			int dh = ti.getDh();
			group += dx + "," + dy + "," + dw + "," + dh + "\n";
			String[] split = ti.getCollisionData().split(",");
			group += split[0] + "," + split[1] + "," + split[2] + "," + split[3] + ",\n";
			group += split[4] + "," + split[5] + "," + split[6] + "," + split[7] + ",\n";
			group += split[8] + "," + split[9] + "," + split[10] + "," + split[11] + ",\n";
			group += split[12] + "," + split[13] + "," + split[14] + "," + split[15] + "\n";
			
		}
		group += "#";
		return group;
	}
	
	public TileHashMap() {
		tileMap = new HashMap<Integer,Tile>();
	}
	
	public HashMap<Integer, Tile> getTileMap() {
		return tileMap;
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
