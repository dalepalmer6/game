package menu.mainmenu;

import global.MenuStack;
import menu.Menu;
import menu.StartupNew;

public class MainMenu extends Menu {
	
	public MainMenu(StartupNew m) {
		super(m);
		addMenuItem(new NewGameMenuItem(10,10,m));
		addMenuItem(new ContinueMenuItem(25,110,m));
		addMenuItem(new OptionsMenuItem(10,210,m));
		addMenuItem(new MapPreviewTestButton(25,310,m));
	}
	
}
