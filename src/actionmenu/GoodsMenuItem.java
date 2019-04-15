package actionmenu;

import java.util.ArrayList;

import actionmenu.goodsmenu.GoodsMenu;
import actionmenu.goodsmenu.InventorySelectionTextWindows;
import gamestate.PartyMember;
import menu.MenuItem;
import menu.StartupNew;

public class GoodsMenuItem extends MenuItem {
	private ArrayList<PartyMember> party;
	
	public GoodsMenuItem(StartupNew state, ArrayList<PartyMember> party) {
		super("Goods",0,0,state);
		this.party = party;
	}

	public String execute() {
		//create the goods Menu
//		GoodsMenu gm = new GoodsMenu(state,party);
//		gm.createMenu();
		state.setSFX("window.wav");
		state.playSFX();
		InventorySelectionTextWindows istws = new InventorySelectionTextWindows(state,party);
		istws.createMenu();
		state.getMenuStack().push(istws);
		return null;
	}
	
}
