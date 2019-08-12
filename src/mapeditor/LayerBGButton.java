package mapeditor;

import menu.ButtonMenuItem;
import menu.MenuItem;
import menu.StartupNew;

public class LayerBGButton extends ButtonMenuItem {

	public LayerBGButton(String t, int x, int y, int w, int h, StartupNew m) {
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
		System.out.println("Setting to bg");
		m.setChangeMap("BG");
		return null;
	}
	
}
