package actionmenu;

import java.util.ArrayList;

import actionmenu.psimenu.PSIMenuOutOfBattle;
import actionmenu.psimenu.PSIMenuSelectMember;
import gamestate.PartyMember;
import menu.MenuItem;
import menu.StartupNew;
import menu.TexturedMenuItem;

public class PSIMenuItem extends TexturedMenuItem {
	private ArrayList<PartyMember> party;
	
	public PSIMenuItem(StartupNew state, ArrayList<PartyMember> party, int ty) {
		super("PSI",64,ty,14*4,14*4,state,"menu.png",60,0,14,14);
		this.party = party;
		targetY = 0;
	}

	public String execute() {
		//create the goods Menu
//		GoodsMenu gm = new GoodsMenu(state,party);
//		gm.createMenu();
		PSIMenuOutOfBattle psim = new PSIMenuOutOfBattle(state,party);
//		psim.createMenu();
		state.getMenuStack().push(psim);
		return null;
	}
}
