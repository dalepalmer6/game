package menu.mapeditmenu.buttons;

import menu.ButtonMenuItem;
import menu.MenuItem;
import menu.mapeditmenu.MapEditMenu;
import system.SystemState;
import system.map.Map;

public class LayerBGButton extends ButtonMenuItem {

	public LayerBGButton(String t, int x, int y, int w, int h, SystemState m) {
		super("BG", x, y,w,h, m);
		// TODO Auto-generated constructor stub
	}
	
	public void updateAnim() {		
		if (on) {
			createTextWindow("BG *");
		} else {
			createTextWindow("BG");
		}
	}

	public String execute() {
		toggle();
		Map m = ((MapEditMenu) this.state.getMenuStack().peek()).getMap();
		SystemState.out.println("Setting to bg");
		m.setChangeMap("BG");
		return null;
	}
	
}
