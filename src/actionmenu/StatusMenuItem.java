package actionmenu;

import java.util.ArrayList;

import actionmenu.statusmenu.StatusMenu;
import gamestate.PartyMember;
import menu.MenuItem;
import menu.StartupNew;
import menu.TexturedMenuItem;

public class StatusMenuItem extends TexturedMenuItem {
	private ArrayList<PartyMember> party;
	
	public StatusMenuItem(StartupNew state ,ArrayList<PartyMember> party, int ty) {
		super("Status",96,ty,96,96,state,"menu.png",74,0,14,14);
		this.party = party;
		targetY = 0;
	}
	
	public String execute() {
		StatusMenu sm = new StatusMenu(state,party);
		sm.createMenu();
		state.getMenuStack().push(sm);
		return null;
	}
}
