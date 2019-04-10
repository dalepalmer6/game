package mapeditor;

import gamestate.DoorEntity;
import mapeditor.tools.DoorTool;
import menu.MenuItem;
import menu.StartupNew;

public class DoorMenuItem extends MenuItem {
	private DoorEntity door;
	
	public DoorMenuItem(DoorEntity door, String text,StartupNew state) {
		super("",0,0,0,0,state);
		this.door = door;
		this.text = text;
	}
	
	public String execute() {
		state.getMenuStack().pop();
		MapEditMenu mem = (MapEditMenu) state.getMenuStack().peek();
		mem.getMapPreview().setTool(new DoorTool(door));
		return null;
	}
}
