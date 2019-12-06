package menu.actionmenu;

import org.lwjgl.opengl.GL11;

import menu.MenuItem;
import menu.actionmenu.equipmenu.TextLabel;
import menu.text.Text;
import system.MainWindow;
import system.SystemState;

public class ButtonLabel extends MenuItem {
	private TextLabel textLabel;

	public ButtonLabel(int x, int y, SystemState state) {
		super("",x,y,state);
		textLabel = new TextLabel("Test",x+96,y+16,state);
	}
	
	public void setText(String s) {
		textLabel.setText(s);
	}
	
	public void draw(MainWindow m) {
		m.setTexture("img\\menu.png");
		m.renderTile(x,y,96,88,108,0,16,16);
		m.renderTile(x+96,y,96,88,124,0,16,16);
		m.renderTile(x+192,y,96,88,160,0,16,16);
		
		GL11.glColor3f(0, 0, 0);
		textLabel.draw(m);
		GL11.glColor3f(255,255,255);
	}
}
