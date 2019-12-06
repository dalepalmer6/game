package menu.continuemenu;

import menu.filemenu.FileSelectionTextWindow;
import system.SystemState;

public class SelectSaveFileMenu extends LoadSavedFileMenu {

	public SelectSaveFileMenu(SystemState m) {
		super(m);
		// TODO Auto-generated constructor stub
	}
	
	public void createSTW() {
		stw = new FileSelectionTextWindow(0,0,10,10,state,"save");
	}

}
