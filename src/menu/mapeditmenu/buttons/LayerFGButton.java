package menu.mapeditmenu.buttons;

import menu.ButtonMenuItem;
import menu.MenuItem;
import menu.mapeditmenu.MapEditMenu;
import system.SystemState;
import system.map.Map;

public class LayerFGButton extends ButtonMenuItem {

	public LayerFGButton(String t, int x, int y, int w, int h, SystemState m) {
		super("FG", x, y,w,h, m);
		// TODO Auto-generated constructor stub
	}
	
	public void updateAnim() {		
		if (on) {
			createTextWindow("FG *");
		} else {
			createTextWindow("FG");
		}
	}

	public String execute() {
		toggle();
		Map m = ((MapEditMenu) this.state.getMenuStack().peek()).getMap();
		SystemState.out.println("Setting to fg");
		m.setChangeMap("FG");
		return null;
	}
}
