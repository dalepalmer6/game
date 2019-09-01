package gamestate;

import menu.StartupNew;
import menu.TexturedMenuItem;

public enum StatusIcons {
	NORMAL(0,0,0,0),
	COLD(112,32,16,16),
	POISON(64,32,16,16),
	ASTHMA(160,32,16,16),
	STONE(176,32,16,16),
	DEAD(0,0,0,0),
	AMNESIA(144,32,16,16),
	CRYING(128,32,16,16),
	UNKNOWN(0,0,0,0),
	SLEEP(96,32,16,16),
	PARALYZE(80,32,16,16);
	
	private int x;
	private int y;
	private int w;
	private int h;
	private String texture = "battlehud.png";
	
	private StatusIcons(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	public int getDx() {
		return x;
	}
	
	public int getDy() {
		return y;
	}
	
	public int getDh() {
		return h;
	}
	
	public int getDw() {
		return w;
	}
	
	/*
	 * When this method is called, we need to set the (StartupNew) state variable before we can draw it
	 * */
	public TexturedMenuItem getMenuItem(double x, double y) {
		return new TexturedMenuItem(x,y,w*4,h*4,null,texture,this.x,this.y,w,h);
	}
}
