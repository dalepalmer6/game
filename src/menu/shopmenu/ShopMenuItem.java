package menu.shopmenu;

import gamestate.elements.items.Item;
import menu.MenuItem;
import menu.windows.SimpleDialogMenu;
import system.SystemState;

public class ShopMenuItem extends MenuItem {
	Item item;
	
	public ShopMenuItem(Item i, SystemState state) {
		super(i.getName(),0,0,state);
		item = i;
	}
	
	public String execute() {
		SimpleDialogMenu.createDialogBox(state,"You're interested in the " + item.getName() + ", eh?[PROMPTINPUT][NEWLINE]It sells for $" + item.getValue() + ". Are you interested?"
				+ "[CHOOSE][CHOICE]Yes[TEXT][SETFLAG_buyingItem][SET_ITEM_TO_BUY_" + item.getId() + "][CHOICE]No[TEXT]Ok then.");
		return null;
	}
	
	public Item getItem() {
		return item;
	}
	
}
