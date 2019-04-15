package actionmenu.goodsmenu.goodsactionmenu;

import gamestate.elements.items.Item;
import menu.MenuItem;
import menu.StartupNew;

public class DropMenuItem extends MenuItem {
	private Item item;
	
	public DropMenuItem(Item item, StartupNew state) {
		super("Drop",0,0,state);
		this.item = item;
	}
	
	public String execute() {
		
		return null;
	} 
}
