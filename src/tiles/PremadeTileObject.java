package tiles;

import mapeditor.Tile;

public class PremadeTileObject extends MultiInstanceTile {
	protected int width;
	protected int height;
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public PremadeTileObject(int id,int width, int height) {
		super(id);
//		tg = new TileGenerator();
		this.width = width;
		this.height = height;
	}
	
}
