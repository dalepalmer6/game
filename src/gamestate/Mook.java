package gamestate;

import menu.StartupNew;

public class Mook extends Entity {
	public Mook(String texture, int x, int y, int width, int height, int dw, int dh, StartupNew m) {
		super(texture,x,y,width,height,dw,dh,m);
		this.dw = 32;
		this.dh = 64;
	}
	
	public String interactText() {
		return "HI MY NAME IS MOOK WHAT'S YOURS?";
	}
}
