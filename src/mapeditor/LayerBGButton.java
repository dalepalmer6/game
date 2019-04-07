package mapeditor;

import menu.MenuItem;
import menu.StartupNew;

public class LayerBGButton extends MenuItem {

	public LayerBGButton(String t, int x, int y, int w, int h, StartupNew m) {
		super(t, x, y,w,h, m);
		// TODO Auto-generated constructor stub
	}

	public String execute() {
		Map m = ((MapEditMenu) this.state.getMenuStack().peek()).getMap();
		System.out.println("Setting to bg");
		m.setChangeMap("BG");
		return null;
	}
	
}
