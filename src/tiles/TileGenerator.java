package tiles;

import java.util.ArrayList;

public class TileGenerator {
	private ArrayList<TileInstance> tiles;
	
	public void addTileInstance(int x, int y, int w, int h) {
		addTileInstance(x,y,w,h,new int[] {	1,1,1,1,
				1,1,1,1,
				1,1,1,1,
				1,1,1,1});
	} 
	
	public void addTileInstance(int x, int y, int w, int h, int[] colMap) {
			tiles.add(new TileInstance(Integer.toString(tiles.size()),x,y,w,h,colMap));
	}
	
	public TileInstance getTileInstance(int i) {
		return tiles.get(i);
	}
	
	public int getInstanceCount() {
		return tiles.size();
	}
	
	
	public TileGenerator() {
		tiles = new ArrayList<TileInstance>();
	}
	
}