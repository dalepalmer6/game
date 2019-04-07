package tiles;

public class GroupableTile extends SingleInstanceTile {

	public GroupableTile(int id,int x, int y, int w, int h) {
		super(id);
		// TODO Auto-generated constructor stub
		tg.addTileInstance(x,y,w,h,new String[] {	"PASS","PASS","PASS","PASS",
				"PASS","PASS","PASS","PASS",
				"PASS","PASS","PASS","PASS",
				"PASS","PASS","PASS","PASS"});
	}
	
	public GroupableTile(int id,int x, int y, int w, int h, String[] colmap) {
		super(id);
		// TODO Auto-generated constructor stub
		tg.addTileInstance(x,y,w,h,colmap);
	}
	
}
