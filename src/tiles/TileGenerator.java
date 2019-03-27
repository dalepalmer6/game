package tiles;

import java.util.ArrayList;

public class TileGenerator {
	private ArrayList<TileInstance> tiles;
	
	public void addTileInstance(int x, int y, int w, int h) {
		tiles.add(new TileInstance(Integer.toString(tiles.size()),x,y,w,h));
	} 
	
	public TileInstance getTileInstance(int i) {
		return tiles.get(i);
	}
	
	public TileGenerator() {
		tiles = new ArrayList<TileInstance>();
	}
	
}
