package mapeditor;

import gamestate.DoorEntity;
import gamestate.Entity;
import menu.ButtonMenuItem;
import menu.StartupNew;

public class NewDoorButton extends ButtonMenuItem {

	public NewDoorButton(int x, int y, int width, int height, StartupNew m) {
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
