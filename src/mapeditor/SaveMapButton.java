package mapeditor;

import menu.MenuItem;
import menu.StartupNew;

public class SaveMapButton extends MenuItem {

	public SaveMapButton(String t, int x, int y, StartupNew m) {
		super(t, x, y, m);
		// TODO Auto-generated constructor stub
	}
	
	public void execute() {
		System.out.println("Saving Map");
	}
}
