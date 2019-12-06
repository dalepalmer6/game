package menu.actionmenu;

import java.util.ArrayList;

import gamestate.partymembers.PartyMember;
import menu.MenuItem;
import menu.TexturedMenuItem;
import menu.actionmenu.statusmenu.StatusMenu;
import system.SystemState;

public class StatusMenuItem extends TexturedMenuItem {
	private ArrayList<PartyMember> party;
	
	public StatusMenuItem(SystemState state ,ArrayList<PartyMember> party, int ty) {
		super("Status",96,ty,14*4,14*4,state,"menu.png",74,0,14,14);
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
