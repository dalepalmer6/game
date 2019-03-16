package menu.mainmenu;

import global.GlobalVars;
import menu.MenuItem;
import menu.namecharactersmenu.CharacterNamingMenu;

public class NewGameMenuItem extends MenuItem{
	private static String text = "New Game";
	
	public NewGameMenuItem(int x, int y) {
		super(text,x,y);
	}
	
	public void execute() {
		System.out.println("New Game Selected.");
		//push a new menu onto the stack
		GlobalVars.menuStack.push(new CharacterNamingMenu());
	}

}
