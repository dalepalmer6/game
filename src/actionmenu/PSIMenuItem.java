package actionmenu;

import java.util.ArrayList;

import actionmenu.psimenu.PSIMenuSelectMember;
import gamestate.PartyMember;
import menu.MenuItem;
import menu.StartupNew;

public class PSIMenuItem extends MenuItem {
	private ArrayList<PartyMember> party;
	
	public PSIMenuItem(StartupNew state, ArrayList<PartyMember> party) {
		super("PSI",0,0,state);
		this.party = party;
	}

	public String execute() {
		//create the goods Menu
//		GoodsMenu gm = new GoodsMenu(state,party);
//		gm.createMenu();
		PSIMenuSelectMember psim = new PSIMenuSelectMember(state,party);
		psim.createMenu();
		state.getMenuStack().push(psim);
		return null;
	}
}
