package system.sprites;

import java.util.HashMap;

public class TileMetadata {
	private int x;
	private int y;
	private int width;
	private int height;
	private boolean flipped;
	
	public TileMetadata(int x, int y, int width, int height, boolean flipped) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.flipped = flipped;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}

	public boolean getFlipState() {
		// TODO Auto-generated method stub
		return flipped;
	}
	
}
