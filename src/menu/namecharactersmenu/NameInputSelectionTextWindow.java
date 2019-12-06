package menu.namecharactersmenu;

import menu.windows.SelectionTextWindow;
import system.SystemState;

public class NameInputSelectionTextWindow extends SelectionTextWindow {

	public NameInputSelectionTextWindow(String orient, int x, int y, int width, int height, SystemState m) {
		super(orient,x, y, width, height, m);
		// TODO Auto-generated constructor stub
	}

	public String execute() {
		update();
		return null;
//		state.getMenuStack().peek().setToRemove(this);
	}
	
}
