package menu.mainmenu;

import gamestate.GameState;
import menu.Menu;
import menu.MenuItem;
import menu.animation.Animation;
import menu.animation.AnimationFadeToBlack;
import menu.animation.AnimationMenu;
import menu.continuemenu.LoadSavedFileMenu;
import menu.namecharactersmenu.CharacterNamingMenu;
import system.MenuStack;
import system.SystemState;

public class ContinueMenuItem extends MenuItem {
	private static String text = "Continue";
	
	public ContinueMenuItem(int x, int y, SystemState m) {
		super(text,x,y,m);
	}
	
	public String execute() {
//		SystemState.out.println("Loading a previous game.");
//		AnimationMenu an = new AnimationMenu(state);
//		an.createAnimMenu();
//		state.getMenuStack().push(an);
//		state.getMenuStack().pop();
		Menu m = new LoadSavedFileMenu(state);
		state.getMenuStack().push(m);
		return null;
	}
	
}
