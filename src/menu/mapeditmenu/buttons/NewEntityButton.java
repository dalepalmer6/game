package menu.mapeditmenu.buttons;

import gamestate.entities.Entity;
import menu.ButtonMenuItem;
import menu.mapeditmenu.MapEditMenu;
import system.SystemState;
import system.map.Map;

public class NewEntityButton extends ButtonMenuItem {

	public NewEntityButton(int x, int y, int width, int height, SystemState m) {
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
