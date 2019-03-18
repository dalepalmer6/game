package mapeditor;

import java.util.ArrayList;

public class Map {
	private String mapId;
	private ArrayList<ArrayList<Integer>> tileMap = new ArrayList<ArrayList<Integer>>();
	private TileHashMap tilesGlobalMap;
	
	public Tile getTile(int x, int y) {
		int tId = getTileId(x,y);
		return tilesGlobalMap.getTile(tId);
	}
	
	public int getTileId(int x, int y) {
		return this.tileMap.get(y).get(x);
		//return this.tileMap[y][x];
	}
	
	public void setTile(int tileId, int x, int y) {
		try {
			this.tileMap.get(y).set(x,tileId);
		} catch (IndexOutOfBoundsException e){
			System.err.println("Index out of bounds, cant add tile " + tileId +
					" to (" + x + ", " + y + ")");
		}
//		this.tileMap[y][x] = tileId;
	}
	
	public Map(String mId, int width, int height, TileHashMap tm) {
		tilesGlobalMap = tm;
		this.mapId = mId;
		for (int i = 0; i < height; i++) {
			ArrayList<Integer> list = new ArrayList<Integer>();
			for (int j = 0; j < width; j++) {
				list.add(0);
			}
			tileMap.add(list);
		}
		//tileMap = new int[width][height];
	}
}
