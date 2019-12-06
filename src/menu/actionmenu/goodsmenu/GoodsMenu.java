package menu.actionmenu.goodsmenu;

import java.util.ArrayList;

import gamestate.elements.items.Item;
import gamestate.partymembers.PartyMember;
import menu.Menu;
import menu.windows.SelectionTextWindow;
import system.SystemState;

public class GoodsMenu extends Menu {
	private int index;
	private ArrayList<PartyMember> party;
	private boolean sellingState;
	
	public GoodsMenu(SystemState m, ArrayList<PartyMember> party, int i) {
		super(m);
		index = i;
		this.party = party;
	}

	public void createMenu() {
		SelectionTextWindow stw = new SelectionTextWindow(0,0,15,15,state);
		stw.setSteps(480,0);
		for (Item i : party.get(index).getItemsList()) {
			stw.add(new GoodsSelectMenuItem(i,index,state,party,sellingState));
		}
		addMenuItem(stw);
	}

	public void setSelling(boolean b) {
		// TODO Auto-generated method stub
		sellingState = b;
	}
	
}
