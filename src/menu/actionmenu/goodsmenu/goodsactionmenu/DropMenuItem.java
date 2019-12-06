package menu.actionmenu.goodsmenu.goodsactionmenu;

import java.util.ArrayList;

import gamestate.elements.items.Item;
import gamestate.partymembers.PartyMember;
import menu.MenuItem;
import menu.actionmenu.goodsmenu.goodsactionmenu.use.PartyMemberSelectMenu;
import system.SystemState;

public class DropMenuItem extends MenuItem {
	private Item item;
	private ArrayList<PartyMember> party;
	private int userIndex;
	
	public DropMenuItem(Item item, SystemState state, ArrayList<PartyMember> party, int partyIndex) {
		super("Drop",0,0,state);
		this.item = item;
		this.party = party;
		this.userIndex = partyIndex;
	}
	
	public String execute() {
		party.get(userIndex).consumeItem(item);
		state.getMenuStack().pop();
		return null;
	} 
}
