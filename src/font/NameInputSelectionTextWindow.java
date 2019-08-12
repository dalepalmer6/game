package font;

import menu.MenuItem;
import menu.StartupNew;

public class NameInputSelectionTextWindow extends SelectionTextWindow {

	public NameInputSelectionTextWindow(String orient, int x, int y, int width, int height, StartupNew m) {
		super(orient,x, y, width, height, m);
		// TODO Auto-generated constructor stub
	}

	public String execute() {
		update();
		return null;
//		state.getMenuStack().peek().setToRemove(this);
	}
	
}
