package mapeditor;

import menu.MenuItem;
import menu.StartupNew;

public class LoadMapButton extends MenuItem {

	public LoadMapButton(String t, int x, int y, StartupNew m) {
		super(t, x, y, m);
		// TODO Auto-generated constructor stub
	}
	
	public void execute() {
		System.out.println("Loading Map");
	}

}
