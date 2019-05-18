package mapeditor;

import menu.ButtonMenuItem;
import menu.MenuItem;
import menu.StartupNew;

public class GridToggleButton extends ButtonMenuItem {
	public GridToggleButton(String t, int x, int y, StartupNew m) {
		super(t, x, y,64,16, m);
		// TODO Auto-generated constructor stub
	}
	
	
	public String execute() {
		System.out.println("Toggle Grid");
		((MapEditMenu) this.state.getMenuStack().peek()).getMapPreview().toggleDrawGrid();
		return null;
	}
}
