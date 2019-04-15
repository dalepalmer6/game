package actionmenu.goodsmenu;

import java.util.ArrayList;

import font.SelectionTextWindow;
import gamestate.PartyMember;
import gamestate.elements.items.Item;
import menu.Menu;
import menu.StartupNew;

public class GoodsMenu extends Menu {
	private int index;
	private ArrayList<PartyMember> party;
	
	public GoodsMenu(StartupNew m, ArrayList<PartyMember> party, int i) {
		super(m);
		index = i;
		this.party = party;
	}

	public void createMenu() {
		SelectionTextWindow stw = new SelectionTextWindow(0,0,15,15,state);
		for (Item i : party.get(index).getItemsList()) {
			stw.add(new GoodsSelectMenuItem(i,index,state,party));
		}
		addMenuItem(stw);
	}
	
}
