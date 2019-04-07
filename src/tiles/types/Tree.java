package tiles.types;

import tiles.MultiInstanceTile;

public class Tree extends MultiInstanceTile{
	public Tree(int id) {
		super(id);
		
		tg.addTileInstance(512,0,32,32);
		tg.addTileInstance(512,0,32,32, new String[] {	"UNDER","UNDER","UNDER","UNDER",
				"UNDER","UNDER","UNDER","UNDER",
				"UNDER","UNDER","UNDER","UNDER",
				"UNDER","UNDER","UNDER","UNDER"});
		tg.addTileInstance(544,0,32,32, new String[] {	"UNDER","UNDER","UNDER","UNDER",
				"UNDER","UNDER","UNDER","UNDER",
				"UNDER","UNDER","UNDER","UNDER",
				"UNDER","UNDER","UNDER","UNDER"});
		tg.addTileInstance(576,0,32,32, new String[] {	"UNDER","UNDER","UNDER","UNDER",
				"UNDER","UNDER","UNDER","UNDER",
				"UNDER","UNDER","UNDER","UNDER",
				"UNDER","UNDER","UNDER","UNDER"});
		
		tg.addTileInstance(512,32,32,32,new String[] {	"UNDER","UNDER","STOP","STOP",
				"UNDER","UNDER","STOP","STOP",
				"UNDER","UNDER","STOP","STOP",
				"UNDER","UNDER","STOP","STOP"});
		tg.addTileInstance(544,32,32,32,new String[] {	"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP"});
		tg.addTileInstance(576,32,32,32,new String[] {	"STOP","STOP","UNDER","UNDER",
				"STOP","STOP","UNDER","UNDER",
				"STOP","STOP","UNDER","UNDER",
				"STOP","STOP","UNDER","UNDER"});
		
		tg.addTileInstance(512,64,32,32,new String[] {	"UNDER","UNDER","UNDER","STOP",
				"UNDER","UNDER","UNDER","STOP",
				"UNDER","UNDER","UNDER","STOP",
				"PASS","PASS","PASS","PASS"});
		tg.addTileInstance(544,64,32,32,new String[] {	"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP",
				"PASS","PASS","PASS","PASS"});
		tg.addTileInstance(576,64,32,32,new String[] {	"STOP","STOP","UNDER","UNDER",
				"STOP","STOP","UNDER","UNDER",
				"STOP","STOP","UNDER","UNDER",
				"PASS","PASS","PASS","PASS"});
		tg.addTileInstance(608,0,32,32,new String[] {	
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","UNDER","UNDER"});
		tg.addTileInstance(608,32,32,32,new String[] {	
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP",
				"UNDER","UNDER","STOP","STOP"});
//		
//		tg.addTileInstance(0,144,16,16);
//		tg.addTileInstance(0,144,16,16, new String[] {	"UNDER","UNDER","UNDER","UNDER",
//				"UNDER","UNDER","UNDER","UNDER",
//				"UNDER","UNDER","UNDER","UNDER",
//				"UNDER","UNDER","UNDER","UNDER"});
//		tg.addTileInstance(16,144,16,16, new String[] {	"UNDER","UNDER","UNDER","UNDER",
//				"UNDER","UNDER","UNDER","UNDER",
//				"UNDER","UNDER","UNDER","UNDER",
//				"UNDER","UNDER","UNDER","UNDER"});
//		tg.addTileInstance(32,144,16,16, new String[] {	"UNDER","UNDER","UNDER","UNDER",
//				"UNDER","UNDER","UNDER","UNDER",
//				"UNDER","UNDER","UNDER","UNDER",
//				"UNDER","UNDER","UNDER","UNDER"});
//		
//		tg.addTileInstance(0,160,16,16,new String[] {	"UNDER","UNDER","STOP","STOP",
//				"UNDER","STOP","STOP","STOP",
//				"UNDER","STOP","STOP","STOP",
//				"UNDER","STOP","STOP","STOP"});
//		tg.addTileInstance(16,160,16,16,new String[] {	"STOP","STOP","STOP","STOP",
//				"STOP","STOP","STOP","STOP",
//				"STOP","STOP","STOP","STOP",
//				"STOP","STOP","STOP","STOP"});
//		tg.addTileInstance(32,160,16,16,new String[] {	"STOP","STOP","UNDER","UNDER",
//				"STOP","STOP","STOP","UNDER",
//				"STOP","STOP","STOP","UNDER",
//				"STOP","STOP","STOP","UNDER"});
//		
//		tg.addTileInstance(0,176,16,16,new String[] {	"UNDER","UNDER","STOP","STOP",
//				"UNDER","STOP","STOP","STOP",
//				"UNDER","STOP","STOP","STOP",
//				"UNDER","UNDER","UNDER","UNDER"});
//		tg.addTileInstance(16,176,16,16,new String[] {	"STOP","STOP","STOP","STOP",
//				"STOP","STOP","STOP","STOP",
//				"STOP","STOP","STOP","STOP",
//				"STOP","STOP","STOP","STOP"});
//		tg.addTileInstance(32,176,16,16,new String[] {	"UNDER","UNDER","UNDER","UNDER",
//				"STOP","STOP","STOP","UNDER",
//				"STOP","STOP","STOP","UNDER",
//				"STOP","STOP","STOP","UNDER"});
	}
}
