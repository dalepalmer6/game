package mapeditor;

import java.util.ArrayList;
import java.util.List;

import font.TextWindow;
import menu.ButtonMenuItem;
import menu.Menu;
import menu.MenuItem;
import menu.StartupNew;

public class LayerBaseButton extends ButtonMenuItem {

	public LayerBaseButton(String t, int x, int y, int w, int h, StartupNew m) {
		super("Base", x, y,w,h, m);
		// TODO Auto-generated constructor stub
	}
	
	public void updateAnim() {		
		if (on) {
			createTextWindow("Base *");
		} else {
			createTextWindow("Base");
		}
	}
	
	public String execute() {
		Map m = ((MapEditMenu) this.state.getMenuStack().peek()).getMap();
		
		toggle();
		
		System.out.println("Setting to Base");
		m.setChangeMap("BASE");
		return null;
	}

}
