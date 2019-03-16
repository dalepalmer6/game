package menu.mainmenu;

import menu.MenuItem;

public class OptionsMenuItem extends MenuItem {
	private static String text = "Options";
	
	public OptionsMenuItem(int x, int y) {
		super(text,x,y);
	}
	
	public void execute() {
		System.out.println("Loading the options menu.");
	}
	
}
