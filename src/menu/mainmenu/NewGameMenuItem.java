package menu.mainmenu;

import canvas.Drawable;
import font.SelectionTextWindow;
import gamestate.GameState;
import gamestate.Player;
import menu.BackButton;
import menu.Menu;
import menu.MenuItem;
import menu.StartupNew;
import menu.namecharactersmenu.CharacterNamingMenu;

public class NewGameMenuItem extends MenuItem{
	private static String text = "New Game";
	
	public NewGameMenuItem(int x, int y, StartupNew m) {
		super(text,x,y,m);
	}
	
	public String execute() {
		System.out.println("New Game Selected.");
		//push a new menu onto the stack
		Menu m = new CharacterNamingMenu(state);
		state.getMenuStack().push(m);
		return null;
	}

}
