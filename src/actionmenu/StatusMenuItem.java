package actionmenu;

import java.util.ArrayList;

import actionmenu.statusmenu.StatusMenu;
import gamestate.PartyMember;
import menu.MenuItem;
import menu.StartupNew;

public class StatusMenuItem extends MenuItem {
	private ArrayList<PartyMember> party;
	
	public StatusMenuItem(StartupNew state ,ArrayList<PartyMember> party) {
		super("Status",0,0,state);
		this.party = party;
	}
	
	public String execute() {
		StatusMenu sm = new StatusMenu(state,party);
		sm.createMenu();
		state.getMenuStack().push(sm);
		return null;
	}
}
