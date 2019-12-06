package menu;

import java.util.List;

import system.MenuStack;
import system.interfaces.Drawable;

public interface MenuInterface {
	public void addMenuItem(MenuItem m);
	
	public List<MenuItem> getMenuItems();
	
	public String getId();
	
	public void setId(String s);

	public String getTitle();
	
	public void setTitle(String s);

}
