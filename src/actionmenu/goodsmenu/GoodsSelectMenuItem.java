package actionmenu.goodsmenu;

import java.util.ArrayList;

import actionmenu.goodsmenu.goodsactionmenu.GoodsActionMenu;
import gamestate.PartyMember;
import gamestate.elements.items.Item;
import menu.MenuItem;
import menu.StartupNew;

public class GoodsSelectMenuItem extends MenuItem {
	private Item i;
	private ArrayList<PartyMember> party;
	private int partyIndex;
	
	public GoodsSelectMenuItem(Item i, int index, StartupNew m, ArrayList<PartyMember> party) {
		super(i.getName(),0,0, m);
		this.i = i;
		this.partyIndex = index;
		this.party = party;
		// TODO Auto-generated constructor stub
	}
	
	public Item getItem() {
		return i;
	}
	
	public String execute() {
		//create the menu "Use/Drop/Give/Help"
		GoodsActionMenu gam = new GoodsActionMenu(state,partyIndex,i,party);
		gam.createMenu();
		state.getMenuStack().push(gam);
		return null;
	}
	
}
