package mapeditor;

import canvas.MainWindow;
import menu.LeftClickableItem;
import menu.MenuItem;
import menu.StartupNew;

public class TileBarMoveButton extends MenuItem {
	private String direction;
	private TileBar tb;
	
	public TileBarMoveButton(String t, double x, double y, StartupNew st, TileBar m) {
		super(t,x,y,st);
		this.tb = m;
		this.width = 64;
		this.height = 64;
		// TODO Auto-generated constructor stub
	}
	
	public void setAction(String s) {
		direction = s;
	}
	
	public String execute() {
		tb.updateView(direction);
		return null;
	}
	
	public void draw(MainWindow m) {
		m.setTexture("img\\button.png");
		m.renderTile(x,y,width,height,0,0,490,234);
	}

}
