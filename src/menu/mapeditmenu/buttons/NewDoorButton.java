package menu.mapeditmenu.buttons;

import gamestate.entities.DoorEntity;
import gamestate.entities.Entity;
import menu.ButtonMenuItem;
import menu.mapeditmenu.MapEditMenu;
import system.SystemState;
import system.map.Map;

public class NewDoorButton extends ButtonMenuItem {

	public NewDoorButton(int x, int y, int width, int height, SystemState m) {
		super("New Door", x, y, width, height, m);
		// TODO Auto-generated constructor stub
	}
	
	public String execute() {
		state.setHoldable(false);
		Map m = ((MapEditMenu) this.state.getMenuStack().peek()).getMap();
		DoorEntity e = new DoorEntity("Description",0,0,32,32,state,0,0,"podunk","Text");
		m.getEntities().add(e);
		return null;
	}

}
