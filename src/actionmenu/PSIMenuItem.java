package actionmenu;

import java.util.ArrayList;

import actionmenu.psimenu.PSIMenuOutOfBattle;
import battlesystem.menu.psi.PSIMenuInBattle;
import battlesystem.menu.psi.PSIMenuSelectMember;
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
	
	public PSIMenuItem(StartupNew state, PartyMember pm, int ty) {
		super("PSI",64,ty,14*4,14*4,state,"menu.png",60,0,14,14);
		this.party = new ArrayList<PartyMember>();
		this.party.add(pm);
		targetY = 0;
	}

	public String execute() {
		//create the PSI Menu
		if (state.inBattle) {
			PSIMenuInBattle psim = new PSIMenuInBattle(state,party.get(0));
			state.getMenuStack().push(psim);
		} else {
			PSIMenuOutOfBattle psim = new PSIMenuOutOfBattle(state,party);
			state.getMenuStack().push(psim);
		}
		
		return null;
	}
}
