package mapeditor;

import menu.ButtonMenuItem;
import menu.MenuItem;
import menu.StartupNew;

public class LayerBaseButton extends ButtonMenuItem {

	public LayerBaseButton(String t, int x, int y, int w, int h, StartupNew m) {
		super("Base", x, y,w,h, m);
		// TODO Auto-generated constructor stub
	}
	
	public String execute() {
		Map m = ((MapEditMenu) this.state.getMenuStack().peek()).getMap();
		System.out.println("Setting to Base");
		m.setChangeMap("BASE");
		return null;
	}

}
