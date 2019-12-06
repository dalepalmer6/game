package menu.mainmenu;

import gamestate.GameState;
import gamestate.entities.Player;
import menu.BackButton;
import menu.Menu;
import menu.MenuItem;
import menu.namecharactersmenu.CharacterNamingMenu;
import menu.windows.SelectionTextWindow;
import system.SystemState;
import system.interfaces.Drawable;

public class NewGameMenuItem extends MenuItem{
	private static String text = "New Game";
	
	public NewGameMenuItem(int x, int y, SystemState m) {
		super(text,x,y,m);
	}
	
	public String execute() {
		SystemState.out.println("New Game Selected.");
		//push a new menu onto the stack
		Menu m = new CharacterNamingMenu(state);
		state.getMenuStack().push(m);
		return null;
	}

}
