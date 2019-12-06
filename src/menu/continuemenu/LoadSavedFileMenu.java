package menu.continuemenu;

import gamestate.GameState;
import menu.Menu;
import menu.MenuItem;
import menu.filemenu.FileSelectionTextWindow;
import menu.windows.SelectionTextWindow;
import system.SystemState;
import system.controller.InputController;

public class LoadSavedFileMenu extends Menu {
	private GameState[] files;
	protected FileSelectionTextWindow stw;
	
	public LoadSavedFileMenu(SystemState m) {
		super(m);
		files = new GameState[3];
		createGameStates();
		createMenuItems();
//		menuItems.add(new SelectionTextWindow(0,0,10,10,state));
		// TODO Auto-generated constructor stub
	}
	
	public void createSTW() {
		stw = new FileSelectionTextWindow(0,0,10,10,state,"load");
	}
	
	public void createMenuItems() {
		createSTW();
		int i = 0;
		for (GameState gs : files) {
			MenuItem mi = new SaveFileMenuItem("File " + (i+1),state,files[i],"file" + (i+1),i);
			stw.add(mi,i);
			i++;
		}
		addMenuItem(stw);
	}

	public void update(InputController in) {
		
		
	}
	
	public void createGameStates() {
		GameState gs = null;
		for (int i = 1; i <= 3; i++) {
			gs = new GameState(state,null);
			gs.loadFromSaveFile("file" + i);
//			gs.loadMapData();
			files[i-1] = gs;
		}
	}
	
}
