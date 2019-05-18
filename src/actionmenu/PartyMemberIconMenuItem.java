package actionmenu;

import canvas.MainWindow;
import menu.MenuItem;
import menu.StartupNew;
import menu.TexturedMenuItem;

public class PartyMemberIconMenuItem extends TexturedMenuItem {
	private int[] backdrop = {88,0,18,16};
	private int[] selectedBack = {0,32,22,21};
	
	public PartyMemberIconMenuItem(String text, int x, int y, int width, int height,  StartupNew state, String texture, int dx, int dy, int dw, int dh) {
		super(text, x, y, width,height,state,texture,dx,dy,dw,dh);
		// TODO Auto-generated constructor stub
	}
	
	public void draw(MainWindow m) {
		m.setTexture("img\\" + texture);
		if (hovered) {
			m.renderTile(x,y,selectedBack[2]*4,selectedBack[3]*4,selectedBack[0],selectedBack[1],selectedBack[2],selectedBack[3]);
		}
		m.renderTile(x,y,backdrop[2]*4,backdrop[3]*4,backdrop[0],backdrop[1],backdrop[2],backdrop[3]);
		m.renderTile(x,y,dw*4,dh*4,dx,dy,dw,dh);
	}

}
