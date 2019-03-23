package mapeditor;

import menu.MenuItem;
import menu.StartupNew;

public class ScrollLeftButton extends MenuItem {
	private TileBar tileBar;
	
	public ScrollLeftButton(String t, int x, int y, int width, int height, StartupNew m, TileBar tb) {
		super(t, x, y, width, height, m);
		// TODO Auto-generated constructor stub
		this.tileBar = tb;
	}

	public String execute() {
		System.out.println("Scrolling left");
		tileBar.updateView("L");
		return null;
	}
}
