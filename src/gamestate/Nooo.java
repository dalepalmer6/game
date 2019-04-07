package gamestate;

import menu.StartupNew;

public class Nooo extends Entity {
	public Nooo(int x, int y, int width, int height, StartupNew m) {
		super("blanket.png",x,y,width,height,m);
		setSpriteCoordinates(new SpritesheetCoordinates());
		//use an enum 
		getSpriteCoordinates().setPose("idle_down");
		getSpriteCoordinates().addStateToPose("idle_down",0,0,100,64);
		getSpriteCoordinates().addStateToPose("idle_down",109,0,100,64);
	}
	
	public String interactText() {
		return "FINAL FANTASY 7 WAS SUPPOSED TO BE RELEASED ON THE FAMICOM DISK SYSTEM.[NEWLINE]ISN'T THAT GREAT?[NEWLINE]I'M GOING TO GO VISIT THE FLAPSHACK TOMORROW AFTERNOON. WOULD YOU LIKE TO COME?[NEWLINE][NEWLINE]TOOOOOOO BAD.";
	}
}
