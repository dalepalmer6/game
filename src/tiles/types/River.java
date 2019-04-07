package tiles.types;

import java.awt.Rectangle;

import mapeditor.Tile;
import tiles.MultiInstanceTile;

public class River extends MultiInstanceTile {
	
	
	public River(int id) {
		super(id);
//		tcm.setAllCollisions(new String[] {	"PASS","PASS","PASS","PASS",
//				"PASS","PASS","PASS","PASS",
//				"PASS","PASS","PASS","PASS",
//				"PASS","PASS","PASS","PASS"});
		tg.addTileInstance(224,96,32,32,new String[] {	
				"PASS","PASS","PASS","PASS",
				"PASS","PASS","PASS","PASS",
				"PASS","PASS","STOP","STOP",
				"PASS","PASS","STOP","STOP"});
		tg.addTileInstance(192,96,32,32,new String[] {	
				"PASS","PASS","PASS","PASS",
				"PASS","PASS","PASS","PASS",
				"PASS","PASS","STOP","STOP",
				"PASS","PASS","STOP","STOP"});
		tg.addTileInstance(224,96,32,32,new String[] {	
				"PASS","PASS","PASS","PASS",
				"PASS","PASS","PASS","PASS",
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP"});
		tg.addTileInstance(256,96,32,32,new String[] {	
				"PASS","PASS","PASS","PASS",
				"PASS","PASS","PASS","PASS",
				"STOP","STOP","PASS","PASS",
				"STOP","STOP","PASS","PASS"});
		tg.addTileInstance(192,128,32,32,new String[] {	
				"PASS","PASS","STOP","STOP",
				"PASS","PASS","STOP","STOP",
				"PASS","PASS","STOP","STOP",
				"PASS","PASS","STOP","STOP"});
		tg.addTileInstance(224,128,32,32,new String[] {	
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP"});
		tg.addTileInstance(256,128,32,32,new String[] {	
				"STOP","STOP","PASS","PASS",
				"STOP","STOP","PASS","PASS",
				"STOP","STOP","PASS","PASS",
				"STOP","STOP","PASS","PASS"});
		tg.addTileInstance(192,160,32,32,new String[] {	
				"PASS","PASS","STOP","STOP",
				"PASS","PASS","STOP","STOP",
				"PASS","PASS","PASS","PASS",
				"PASS","PASS","PASS","PASS"});
		tg.addTileInstance(224,160,32,32,new String[] {	
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP",
				"PASS","PASS","PASS","PASS",
				"PASS","PASS","PASS","PASS"});
		tg.addTileInstance(256,160,32,32,new String[] {	
				"STOP","STOP","PASS","PASS",
				"STOP","STOP","PASS","PASS",
				"PASS","PASS","PASS","PASS",
				"PASS","PASS","PASS","PASS"});
		tg.addTileInstance(112,0,32,32,new String[] {	
				"STOP","STOP","PASS","PASS",
				"STOP","STOP","PASS","PASS",
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP"});
		tg.addTileInstance(112,32,32,32,new String[] {	
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP",
				"PASS","PASS","STOP","STOP",
				"PASS","PASS","STOP","STOP"});
		tg.addTileInstance(144,0,32,32,new String[] {	
				"PASS","PASS","STOP","STOP",
				"PASS","PASS","STOP","STOP",
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP"});
		tg.addTileInstance(144,32,32,32,new String[] {	
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","PASS","PASS",
				"STOP","STOP","PASS","PASS"});
	}

}
