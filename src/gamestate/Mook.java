package gamestate;

import battlesystem.BattleMenu;
import menu.StartupNew;

public class Mook extends Entity {
	public Mook(int x, int y, int width, int height, StartupNew m) {
		super("mook.png",x,y,width,height,m);
		setSpriteCoordinates(new SpritesheetCoordinates());
		//use an enum 
		getSpriteCoordinates().setPose("idle_down");
		getSpriteCoordinates().addStateToPose("idle_down",0,0,32,64);
		getSpriteCoordinates().addStateToPose("idle_down",32,0,32,64);
	}
	
	public void interact() {
		BattleMenu bm = new BattleMenu(state);
		bm.startBattle();
		state.getMenuStack().push(bm);
	}
	
	public void act() {
		interact();
	}
	
	public String interactText() {
		return this.text;
	}
}
