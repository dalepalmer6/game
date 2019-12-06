package menu.actionmenu;

import java.util.ArrayList;

import battlesystem.menu.psi.PSIMenuInBattle;
import battlesystem.menu.psi.PSIMenuSelectMember;
import gamestate.partymembers.PartyMember;
import menu.MenuItem;
import menu.TexturedMenuItem;
import menu.actionmenu.psimenu.PSIMenuOutOfBattle;
import system.SystemState;

public class PSIMenuItem extends TexturedMenuItem {
	private ArrayList<PartyMember> party;
	
	public PSIMenuItem(SystemState state, ArrayList<PartyMember> party, int ty) {
		super("PSI",64,ty,14*4,14*4,state,"menu.png",60,0,14,14);
		this.party = party;
		targetY = 0;
	}
	
	public PSIMenuItem(SystemState state, PartyMember pm, int ty) {
		super("PSI",64,ty,16*4,16*4,state,"battlehud.png",48,16,16,16);
		setHovered(48,0,16,16);
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
//		
		return null;
	}
}
