package actionmenu.goodsmenu.goodsactionmenu;

import java.util.ArrayList;

import actionmenu.goodsmenu.goodsactionmenu.use.PartyMemberSelectMenu;
import gamestate.PartyMember;
import gamestate.elements.items.Item;
import menu.MenuItem;
import menu.StartupNew;

public class DropMenuItem extends MenuItem {
	private Item item;
	private ArrayList<PartyMember> party;
	private int userIndex;
	
	public DropMenuItem(Item item, StartupNew state, ArrayList<PartyMember> party, int partyIndex) {
		super("Drop",0,0,state);
		this.item = item;
		this.party = party;
		this.userIndex = partyIndex;
	}
	
	public String execute() {
		state.getMenuStack().pop();
		party.get(userIndex).consumeItem(item);
		return null;
	} 
}
