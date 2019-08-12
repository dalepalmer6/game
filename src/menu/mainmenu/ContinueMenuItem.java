package menu.mainmenu;

import gamestate.GameState;
import global.MenuStack;
import menu.Animation;
import menu.AnimationFadeToBlack;
import menu.AnimationMenu;
import menu.Menu;
import menu.MenuItem;
import menu.StartupNew;
import menu.continuemenu.LoadSavedFileMenu;
import menu.namecharactersmenu.CharacterNamingMenu;

public class ContinueMenuItem extends MenuItem {
	private static String text = "Continue";
	
	public ContinueMenuItem(int x, int y, StartupNew m) {
		super(text,x,y,m);
	}
	
	public String execute() {
//		System.out.println("Loading a previous game.");
//		AnimationMenu an = new AnimationMenu(state);
//		an.createAnimMenu();
//		state.getMenuStack().push(an);
//		state.getMenuStack().pop();
		Menu m = new LoadSavedFileMenu(state);
		state.getMenuStack().push(m);
		return null;
	}
	
}
