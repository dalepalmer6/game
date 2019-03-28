package tiles.types;

import tiles.MultiInstanceTile;

public class Tree extends MultiInstanceTile{
	public Tree(int id) {
		super(id);
		
		tg.addTileInstance(512,0,28,28);
		tg.addTileInstance(512,0,28,28, new String[] {	"UNDER","UNDER","UNDER","UNDER",
				"UNDER","UNDER","UNDER","UNDER",
				"UNDER","UNDER","UNDER","UNDER",
				"UNDER","UNDER","UNDER","UNDER"});
		tg.addTileInstance(540,0,28,28, new String[] {	"UNDER","UNDER","UNDER","UNDER",
				"UNDER","UNDER","UNDER","UNDER",
				"UNDER","UNDER","UNDER","UNDER",
				"UNDER","UNDER","UNDER","UNDER"});
		tg.addTileInstance(568,0,28,28, new String[] {	"UNDER","UNDER","UNDER","UNDER",
				"UNDER","UNDER","UNDER","UNDER",
				"UNDER","UNDER","UNDER","UNDER",
				"UNDER","UNDER","UNDER","UNDER"});
		
		tg.addTileInstance(512,28,28,28,new String[] {	"UNDER","UNDER","STOP","STOP",
				"UNDER","STOP","STOP","STOP",
				"UNDER","STOP","STOP","STOP",
				"UNDER","STOP","STOP","STOP"});
		tg.addTileInstance(540,28,28,28,new String[] {	"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP"});
		tg.addTileInstance(568,28,28,28,new String[] {	"STOP","STOP","UNDER","UNDER",
				"STOP","STOP","STOP","UNDER",
				"STOP","STOP","STOP","UNDER",
				"STOP","STOP","STOP","UNDER"});
		
		tg.addTileInstance(512,56,28,28,new String[] {	"UNDER","UNDER","STOP","STOP",
				"UNDER","STOP","STOP","STOP",
				"UNDER","STOP","STOP","STOP",
				"UNDER","UNDER","UNDER","UNDER"});
		tg.addTileInstance(540,56,28,28,new String[] {	"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP",
				"STOP","STOP","STOP","STOP"});
		tg.addTileInstance(568,56,28,28,new String[] {	"UNDER","UNDER","UNDER","UNDER",
				"STOP","STOP","STOP","UNDER",
				"STOP","STOP","STOP","UNDER",
				"STOP","STOP","STOP","UNDER"});
		tg.addTileInstance(596,0,28,28);
		tg.addTileInstance(596,28,28,28);
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
