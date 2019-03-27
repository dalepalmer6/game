package tiles;

import mapeditor.Tile;

public class SingleInstanceTile extends Tile {
	protected TileGenerator tg;
	protected TileCollisionMap tcm;
	
	public String getCollisionInfoAtIndex(int x, int y) {
		return tcm.getCollisionData(y*4 + x);
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
		tcm = new TileCollisionMap();
	} 
}
