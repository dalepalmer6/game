package menu.mainmenu;

import menu.MenuItem;
import menu.StartupNew;
import menu.namecharactersmenu.CharacterNamingMenu;

public class NewGameMenuItem extends MenuItem{
	private static String text = "New Game";
	
	public NewGameMenuItem(int x, int y, StartupNew m) {
		super(text,x,y,m);
	}
	
	public void execute() {
		System.out.println("New Game Selected.");
		//push a new menu onto the stack
		state.getMenuStack().push(new CharacterNamingMenu(state));
	}

}
