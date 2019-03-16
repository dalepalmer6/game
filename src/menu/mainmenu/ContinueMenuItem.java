package menu.mainmenu;

import menu.MenuItem;

public class ContinueMenuItem extends MenuItem {
	private static String text = "Continue";
	
	public ContinueMenuItem(int x, int y) {
		super(text,x,y);
	}
	
	public void execute() {
		System.out.println("Loading a previous game.");
	}
	
}
