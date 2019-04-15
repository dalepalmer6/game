package actionmenu.goodsmenu.goodsactionmenu;

import gamestate.elements.items.Item;
import menu.MenuItem;
import menu.StartupNew;

public class HelpMenuItem extends MenuItem {
	private Item item;
	
	public HelpMenuItem(Item item, StartupNew state) {
		super("Help",0,0,state);
		this.item = item;
	}
	
	public String execute() {
		
		return null;
	} 
}
