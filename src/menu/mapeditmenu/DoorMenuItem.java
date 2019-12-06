package menu.mapeditmenu;

import gamestate.entities.DoorEntity;
import menu.MenuItem;
import menu.mapeditmenu.tools.DoorTool;
import system.SystemState;

public class DoorMenuItem extends MenuItem {
	private DoorEntity door;
	
	public DoorMenuItem(DoorEntity door, String text,SystemState state) {
		super(text,0,0,state);
		this.door = door;
		this.text = text;
	}
	
	public String execute() {
		state.getMenuStack().pop();
		MapEditMenu mem = (MapEditMenu) state.getMenuStack().peek();
		mem.getMapPreview().setTool(new DoorTool(door,state));
		return null;
	}
}
