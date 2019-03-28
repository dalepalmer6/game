package tiles;

public class TileInstance {
	private String id;
	private int dx;
	private int dy;
	private int dw;
	private int dh;
	private TileCollisionMap tileCollisionMap;
	
	public String getCollisionInfoAtIndex(int x, int y) {
		return tileCollisionMap.getCollisionData(y*4 + x);
	}
	
	public int getDx() {
		return dx;
	}
	
	public int getDy() {
		return dy;
	}
	
	public int getDh() {
		return dh;
	}
	
	public int getDw() {
		return dw;
	}
	
	public TileInstance(String id, int dx, int dy, int dw, int dh, String[] colmap) {
		this.id = id;
		this.dx = dx;
		this.dy = dy;
		this.dw = dw;
		this.dh = dh;
		tileCollisionMap = new TileCollisionMap();
		tileCollisionMap.setAllCollisions(colmap);
	}
}
