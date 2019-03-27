package tiles;

public class TileInstance {
	private String id;
	private int dx;
	private int dy;
	private int dw;
	private int dh;
	
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
	
	public TileInstance(String id, int dx, int dy, int dw, int dh) {
		this.id = id;
		this.dx = dx;
		this.dy = dy;
		this.dw = dw;
		this.dh = dh;
	}
}
