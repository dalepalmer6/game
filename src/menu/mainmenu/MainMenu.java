package menu.mainmenu;

import menu.Menu;

public class MainMenu extends Menu {
	
	public MainMenu() {
		super();
		addMenuItem(new NewGameMenuItem(10,10));
		addMenuItem(new ContinueMenuItem(25,110));
		addMenuItem(new OptionsMenuItem(10,210));
		addMenuItem(new MapPreviewTestButton(25,310));
	}
	
}
