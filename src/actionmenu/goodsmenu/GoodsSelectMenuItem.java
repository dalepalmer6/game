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
	private boolean selling;
	
	public GoodsSelectMenuItem(Item i, int index, StartupNew m, ArrayList<PartyMember> party, boolean selling) {
		super(i.getName(),0,0, m);
		this.i = i;
		this.partyIndex = index;
		this.party = party;
		this.selling = selling;
		// TODO Auto-generated constructor stub
	}
	
	public Item getItem() {
		return i;
	}
	
	public String execute() {
		//create the menu "Use/Drop/Give/Help"
		if (!selling) {
			GoodsActionMenu gam = new GoodsActionMenu(state,partyIndex,i,party);
			gam.createMenu();
			state.getMenuStack().push(gam);
		} else {
			state.getSelectionStack().push(this);
			state.getMenuStack().pop();
//			state.setSelling(true);
			state.setIndexOfParty(partyIndex);
		}
		
		return null;
	}
	
}
