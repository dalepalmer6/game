package battlesystem.options;

import java.util.ArrayList;

import battlesystem.BattleMenu;
import gamestate.PartyMember;
import menu.MenuItem;
import menu.StartupNew;
import menu.TexturedMenuItem;

public class Defend extends TexturedMenuItem{

	public Defend(StartupNew state, PartyMember pm, int ty) {
		super("Defend",64,ty,16*4,16*4,state,"battlehud.png",64,16,16,16);
		setHovered(64,0,16,16);
		targetY = 0;
	}
	
	public String execute() {
		BattleMenu m =  state.battleMenu;
		m.setCurrentAction(new BattleAction(state));
		m.getCurrentAction().setUser(m.getCurrentPartyMember());
		m.getCurrentAction().setAction("defend");
		
		m.getCurrentAction().setTarget(m.getCurrentPartyMember());
		m.setDoneAction();
		return null;
	}
	
}
