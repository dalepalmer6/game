package gamestate;

import menu.StartupNew;

public class Mook extends Entity {
	public Mook(int x, int y, int width, int height, StartupNew m) {
		super("mook.png",x,y,width,height,m);
		spriteCoordinates = new SpritesheetCoordinates();
		//use an enum 
		spriteCoordinates.setPose("idle_down");
		spriteCoordinates.addStateToPose("idle_down",0,0,32,64);
		spriteCoordinates.addStateToPose("idle_down",32,0,32,64);
	}
	
	public String interactText() {
		return "@HI MY NAME IS MOOK WHAT'S YOURS?";
	}
}
