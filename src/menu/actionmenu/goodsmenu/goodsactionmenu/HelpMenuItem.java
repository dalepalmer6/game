package menu.actionmenu.goodsmenu.goodsactionmenu;

import gamestate.elements.items.Item;
import menu.MenuItem;
import system.SystemState;

public class HelpMenuItem extends MenuItem {
	private Item item;
	
	public HelpMenuItem(Item item, SystemState state) {
		super("Help",0,0,state);
		this.item = item;
	}
	
	public String execute() {
		
		return null;
	} 
}
