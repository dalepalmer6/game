package menu.continuemenu;

import font.FileSelectionTextWindow;
import menu.StartupNew;

public class SelectSaveFileMenu extends LoadSavedFileMenu {

	public SelectSaveFileMenu(StartupNew m) {
		super(m);
		// TODO Auto-generated constructor stub
	}
	
	public void createSTW() {
		stw = new FileSelectionTextWindow(0,0,10,10,state,"save");
	}

}
