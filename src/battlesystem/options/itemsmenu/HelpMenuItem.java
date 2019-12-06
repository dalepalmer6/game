package battlesystem.options.itemsmenu;

import gamestate.elements.items.Item;
import menu.MenuItem;
import system.SystemState;

public class HelpMenuItem extends MenuItem {
	private Item item;
	
	public HelpMenuItem(Item i, SystemState m) {
		super("Help", 0, 0, m);
		this.item = i;
	}
	
	public String execute() {
		item.getDescription();
		return null;
	}

}
