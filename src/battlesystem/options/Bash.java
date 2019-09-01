package battlesystem.options;

import java.util.ArrayList;

import battlesystem.BattleMenu;
import battlesystem.SelectTargetMenu;
import font.SelectionTextWindow;
import gamestate.PartyMember;
import menu.MenuItem;
import menu.StartupNew;
import menu.TexturedMenuItem;

public class Bash extends TexturedMenuItem{

//	public Bash(String t, int x, int y, StartupNew m) {
//		super(t, x, y, m);
//		// TODO Auto-generated constructor stub
//	}
	
	public Bash(StartupNew state, PartyMember pm, int ty) {
		super("Bash",64,ty,16*4,16*4,state,"battlehud.png",0,16,16,16);
		setHovered(0,0,16,16);
//		this.party = new ArrayList<PartyMember>();
//		this.party.add(pm);
		targetY = 0;
	}
	
	public String execute() {
		//show the options who can be attacked
		BattleMenu m = state.battleMenu;
		m.setCurrentAction(new BattleAction(state));
		m.getCurrentAction().setUser(m.getCurrentPartyMember());
		m.getCurrentAction().setAction("bash");
		
		SelectTargetMenu stm = new SelectTargetMenu(state,false,m.getEnemies());
		state.getMenuStack().push(stm);
//		EnemyOptionPanel eop = m.getEnemyOptionPanel();
//		BattleSelectionTextWindow stw = new BattleSelectionTextWindow("vertical",m.getCurrentPlayerStatusWindow().getX(),m.getCurrentPlayerStatusWindow().getY()-3*64,3,3,state);
//			for (EnemyOption eo : eop.getEnemyOptions()) {
//				stw.add(eo);
//			}
//		stw.setKillWhenComplete();
//		m.addToMenuItems(stw);
		return null;
	}

}
