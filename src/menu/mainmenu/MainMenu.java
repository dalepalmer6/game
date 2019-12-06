package menu.mainmenu;

import menu.Menu;
import menu.TexturedMenuItem;
import menu.text.Text;
import menu.windows.SelectionTextWindow;
import menu.windows.TextWindow;
import system.MenuStack;
import system.SystemState;
import system.controller.InputController;

public class MainMenu extends Menu {
	private TexturedMenuItem logo;
	
	public MainMenu(SystemState m) {
		super(m);
		canRemove = false;
		logo = new TexturedMenuItem("Mother",state.getMainWindow().getScreenWidth()/2 - 96*4,96,192*4,64*4,state,"motherlogo.png",0,0,192,64);
		addMenuItem(logo);
	}
	
	public void update(InputController input) {
		
	}
	
}
