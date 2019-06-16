package menu.mainmenu;

import font.SelectionTextWindow;
import font.Text;
import font.TextWindow;
import global.InputController;
import global.MenuStack;
import menu.Menu;
import menu.StartupNew;
import menu.TexturedMenuItem;

public class MainMenu extends Menu {
	private TexturedMenuItem logo;
	
	public MainMenu(StartupNew m) {
		super(m);
		logo = new TexturedMenuItem("Mother",state.getMainWindow().getScreenWidth()/2 - 96*4,96,192*4,64*4,state,"motherlogo.png",0,0,192,64);
		addMenuItem(logo);
	}
	
	public void update(InputController input) {
		
	}
	
}
