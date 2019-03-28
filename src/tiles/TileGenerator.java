package tiles;

import java.util.ArrayList;

public class TileGenerator {
	private ArrayList<TileInstance> tiles;
	
	public void addTileInstance(int x, int y, int w, int h) {
		addTileInstance(x,y,w,h,new String[] {	"PASS","PASS","PASS","PASS",
				"PASS","PASS","PASS","PASS",
				"PASS","PASS","PASS","PASS",
				"PASS","PASS","PASS","PASS"});
	} 
	
	public void addTileInstance(int x, int y, int w, int h, String[] colMap) {
			tiles.add(new TileInstance(Integer.toString(tiles.size()),x,y,w,h,colMap));
	}
	
	public TileInstance getTileInstance(int i) {
		return tiles.get(i);
	}
	
	public TileGenerator() {
		tiles = new ArrayList<TileInstance>();
	}
	
}
