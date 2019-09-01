package actionmenu;

import java.util.ArrayList;

import actionmenu.goodsmenu.GoodsMenu;
import actionmenu.goodsmenu.GoodsMenuOutOfBattle;
import actionmenu.goodsmenu.InventorySelectionTextWindows;
import battlesystem.menu.GoodsMenuInBattle;
import gamestate.PartyMember;
import menu.MenuItem;
import menu.StartupNew;
import menu.TexturedMenuItem;

public class GoodsMenuItem extends TexturedMenuItem {
	private ArrayList<PartyMember> party;
	
	public GoodsMenuItem(StartupNew state, ArrayList<PartyMember> party, int ty) {
		super("Goods",32,ty,14*4,14*4,state,"menu.png",32,0,14,14);
		this.party = party;
		targetY = 0;
	}
	
	public GoodsMenuItem(StartupNew state, PartyMember pm, int ty) {
		super("Goods",32,ty,16*4,16*4,state,"battlehud.png",16,16,16,16);
		setHovered(16,0,16,16);
		this.party = new ArrayList<PartyMember>();
		this.party.add(pm);
	}

	public String execute() {
		//create the goods Menu
//		GoodsMenu gm = new GoodsMenu(state,party);
//		gm.createMenu();
//		state.setSFX("window.wav");
//		state.playSFX();
		
		if (state.inBattle) {
			GoodsMenuInBattle gmib = new GoodsMenuInBattle(state,party.get(0));
			state.getMenuStack().push(gmib);
		}
		else {
			GoodsMenuOutOfBattle piw = new GoodsMenuOutOfBattle(state,party);
			state.getMenuStack().push(piw);
		}
		
//		InventorySelectionTextWindows istws = new InventorySelectionTextWindows(state,party);
//		istws.createMenu();
//		state.getMenuStack().push(istws);
		return null;
	}
	
}
