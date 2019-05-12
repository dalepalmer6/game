package mapeditor;

import gamestate.Entity;
import menu.ButtonMenuItem;
import menu.StartupNew;

public class NewEntityButton extends ButtonMenuItem {

	public NewEntityButton(int x, int y, int width, int height, StartupNew m) {
		super("New Entity", x, y, width, height, m);
		// TODO Auto-generated constructor stub
	}
	
	public String execute() {
		state.setHoldable(false);
		Map m = ((MapEditMenu) this.state.getMenuStack().peek()).getMap();
		Entity e = state.allEntities.get("ninten").createCopy(0,0,24,32,"NewEntityName");
		e.setText("New Entity");
		m.getEntities().add(e);
		return null;
	}

}
