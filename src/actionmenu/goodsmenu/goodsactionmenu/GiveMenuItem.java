package actionmenu.goodsmenu.goodsactionmenu;

import gamestate.elements.items.Item;
import menu.MenuItem;
import menu.StartupNew;

public class GiveMenuItem extends MenuItem {
	private Item item;
	
	public GiveMenuItem(Item item, StartupNew state) {
		super("Give",0,0,state);
		this.item = item;
	}
	
	public String execute() {
		
		return null;
	} 
}
