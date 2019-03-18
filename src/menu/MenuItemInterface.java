package menu;

import canvas.Clickable;
import global.InputController;

public interface MenuItemInterface extends Clickable {

	public void setText(String s);
	
	public String getText();
	
	public void execute();
}
