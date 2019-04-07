package tiles.types;

import tiles.MultiInstanceTile;

public class Mountain extends MultiInstanceTile{
	public Mountain(int id) {
		super(id);
//		tcm.setAllCollisions(new String[] {	"STOP","STOP","STOP","STOP",
//								"STOP","STOP","STOP","STOP",
//								"STOP","STOP","STOP","STOP",
//								"STOP","STOP","STOP","STOP"});
		tg.addTileInstance(512,160,32,32,new String[] {	
				"PASS","PASS","PASS","PASS",
				"PASS","PASS","PASS","STOP",
				"PASS","PASS","STOP","STOP",
				"STOP","STOP","STOP","STOP"});
		tg.addTileInstance(512,160,32,32,new String[] {	
				"PASS","PASS","PASS","PASS",
				"PASS","PASS","PASS","STOP",
				"PASS","PASS","STOP","STOP",
				"STOP","STOP","STOP","STOP"});
		tg.addTileInstance(544,160,32,32,new String[] {	
				"PASS","PASS","PASS","PASS",
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP"});
		tg.addTileInstance(576,160,32,32,new String[] {	
				"PASS","PASS","PASS","PASS",
				"STOP","PASS","PASS","PASS",
				"STOP","STOP","PASS","PASS",
				"STOP","STOP","STOP","STOP"});
		tg.addTileInstance(512,192,32,32,new String[] {	
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP"});
		tg.addTileInstance(544,192,32,32,new String[] {	
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP"});
		tg.addTileInstance(576,192,32,32,new String[] {	
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP"});
		tg.addTileInstance(512,224,32,32,new String[] {	
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP",
				"PASS","PASS","STOP","STOP",
				"PASS","PASS","PASS","PASS"});
		tg.addTileInstance(544,224,32,32,new String[] {	
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP",
				"PASS","PASS","PASS","PASS"});
		tg.addTileInstance(576,224,32,32,new String[] {	
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","PASS","PASS",
				"PASS","PASS","PASS","PASS"});
	}
}
