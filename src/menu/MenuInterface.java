package menu;

import java.util.List;

import canvas.Drawable;
import global.MenuStack;

public interface MenuInterface {
	public void addMenuItem(MenuItem m);
	
	public List<MenuItem> getMenuItems();
	
	public String getId();
	
	public void setId(String s);

	public String getTitle();
	
	public void setTitle(String s);

}
