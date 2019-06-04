package actionmenu.goodsmenu.goodsactionmenu;

import java.util.ArrayList;

import font.SelectionTextWindow;
import gamestate.PartyMember;
import gamestate.elements.items.Item;
import menu.Menu;
import menu.StartupNew;

public class GoodsActionMenu extends Menu {
	private Item item;
	private SelectionTextWindow stw;
	private ArrayList<PartyMember> party;
	private int partyIndex;
	
	public GoodsActionMenu(StartupNew state, int partyIndex, Item item, ArrayList<PartyMember> party) {
		super(state);
		this.item = item;
		this.party = party;
		this.partyIndex = partyIndex;
	} 
	
	public void createMenu() {
//		menuItems.clear();
		stw = new SelectionTextWindow(0,0,5,5,state);
		stw.add(new UseMenuItem(item,state,party,partyIndex));
		if (party.size() > 1) {
			stw.add(new GiveMenuItem(item,state,party,partyIndex));//needs index
		}
		
		stw.add(new DropMenuItem(item,state,party,partyIndex));//needs index
//		stw.add(new HelpMenuItem(item,state));
		addMenuItem(stw);
	}
}
