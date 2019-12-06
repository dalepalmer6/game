package menu;

import system.interfaces.Clickable;

public interface MenuItemInterface extends Clickable {

	public void setText(String s);
	
	public String getText();
	
	public String execute();
}
