package tiles;

import mapeditor.Tile;

public class MultiInstanceTile extends Tile{
	
	//placeholder for animation coordinates and all that good stuff
	
	
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
	
	public TileInstance getInstance(int inst_id) {
		return tg.getTileInstance(inst_id);
	}
	
	public MultiInstanceTile(int id) {
		super(id);
		tg = new TileGenerator();
	} 
}
