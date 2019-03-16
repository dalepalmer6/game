package mapeditor;

public class Map {
	private String mapId;
	private int[][] tileMap;
	
	public int getTile(int x, int y) {
		return this.tileMap[y][x];
	}
	
	public void setTile(int tileId, int x, int y) {
		this.tileMap[y][x] = tileId;
	}
	
	public Map(String mId, int width, int height) {
		this.mapId = mId;
		tileMap = new int[width][height];
	}
}
