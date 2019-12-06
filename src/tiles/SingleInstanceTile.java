package tiles;

import system.map.Tile;

public class SingleInstanceTile extends Tile {
	
//	public void addTileInstance(int x, int y, int width, int height, String[] collision) {
//		tg.addTileInstance(x,y,width,height,collision);
//	}
	
	public TileInstance getInstance(int inst_id) {
		return tg.getTileInstance(inst_id);
	}
	
	public int getDx(int inst_id) {
		return tg.getTileInstance(inst_id).getDx();
	}
	public int getDy(int inst_id) {
		return tg.getTileInstance(inst_id).getDy();
	}
	public int getDw(int inst_id) {
		return tg.getTileInstance(inst_id).getDw();
	}
	public int getDh(int inst_id) {
		return tg.getTileInstance(inst_id).getDh();
	}
	
	public SingleInstanceTile(int id) {
		super(id);
		tg = new TileGenerator();
	} 
}
