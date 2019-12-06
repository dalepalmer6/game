package menu.mapeditmenu.buttons;

import menu.ButtonMenuItem;
import menu.MenuItem;
import menu.mapeditmenu.MapEditMenu;
import system.SystemState;

public class GridToggleButton extends ButtonMenuItem {
	public GridToggleButton(String t, int x, int y, SystemState m) {
		super(t, x, y,64,16, m);
		// TODO Auto-generated constructor ssstub
	}
	
	
	public String execute() {
//		SystemState.out.println("Toggle Grid");
		((MapEditMenu) this.state.getMenuStack().peek()).getMapPreview().toggleDrawGrid();
		return null;
	}
}
