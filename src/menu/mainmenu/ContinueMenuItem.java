package menu.mainmenu;

import global.MenuStack;
import menu.MenuItem;
import menu.StartupNew;

public class ContinueMenuItem extends MenuItem {
	private static String text = "Continue";
	
	public ContinueMenuItem(int x, int y, StartupNew m) {
		super(text,x,y,m);
	}
	
	public String execute() {
		System.out.println("Loading a previous game.");
		return null;
	}
	
}
