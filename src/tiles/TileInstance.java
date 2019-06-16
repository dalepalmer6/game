package tiles;

public class TileInstance {
	private int id;
	private int dx;
	private int dy;
	private int dw;
	private int dh;
	private TileCollisionMap tileCollisionMap;
	
	public int getId() {
		return id;
	}
	
	public int getCollisionInfoAtIndex(int x, int y) {
		return tileCollisionMap.getCollisionData(y*4 + x);
	}
	
	public String getCollisionData() {
		return tileCollisionMap.getCollisionData();
	}
	
	public void modifyCollisionInfoAtIndex(int x, int y, int newValue) {
		tileCollisionMap.setCollision(x+  y*4,newValue);
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
	
	public TileInstance(int id, int dx, int dy, int dw, int dh, int[] colmap) {
		this.id = id;
		this.dx = dx;
		this.dy = dy;
		this.dw = dw;
		this.dh = dh;
		tileCollisionMap = new TileCollisionMap();
		tileCollisionMap.setAllCollisions(colmap);
	}
}
