package font;

import org.newdawn.slick.opengl.Texture;

import system.map.Tile;

public class CharacterData{
	private char character;
	private int dx;
	private int dy;
	private int dw;
	private int dh;
	
	public CharacterData(char c,int dx, int dy, int dw, int dh) {
		character = c;
		this.dx = dx;
		this.dy = dy;
		this.dw = dw;
		this.dh = dh;
	}
	
	public float getDx() {
		return dx;
	}
	
	public float getDy() {
		return dy;
	}
	
	public float getDw() {
		return dw;
	}
	
	public float getDh() {
		return dh;
	}
}
