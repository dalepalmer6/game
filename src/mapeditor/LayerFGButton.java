package mapeditor;

import menu.MenuItem;
import menu.StartupNew;

public class LayerFGButton extends MenuItem {

	public LayerFGButton(String t, int x, int y, int w, int h, StartupNew m) {
		super(t, x, y,w,h, m);
		// TODO Auto-generated constructor stub
	}

	public String execute() {
		Map m = ((MapEditMenu) this.state.getMenuStack().peek()).getMap();
		System.out.println("Setting to fg");
		m.setChangeMap("FG");
		return null;
	}
}
