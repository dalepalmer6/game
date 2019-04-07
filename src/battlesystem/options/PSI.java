package battlesystem.options;

import battlesystem.BattleMenu;
import battlesystem.options.itemsmenu.ItemMenuItem;
import battlesystem.options.psimenu.PSIAttackMenuItem;
import font.SelectionTextWindow;
import gamestate.elements.items.Item;
import gamestate.elements.psi.PSIAttack;
import menu.MenuItem;
import menu.StartupNew;

public class PSI extends MenuItem{

	public PSI(String t, int x, int y, StartupNew m) {
		super(t, x, y, m);
		// TODO Auto-generated constructor stub
	}
	public String execute() {
		//show the PSI attack menu
		BattleMenu m =  ((BattleMenu) state.getMenuStack().peek());
		PSIAttackSelectionTextWindow stw = new PSIAttackSelectionTextWindow(state);
		for (PSIAttack i : state.psi) {
			stw.add(new PSIAttackMenuItem(i,state));
		}
		m.addToMenuItems(stw);
		stw.setKillWhenComplete();
		m.setCurrentAction(new BattleAction());
		m.getCurrentAction().setAction("psi");
		m.getCurrentAction().setUser(m.getCurrentPartyMember());
		return null;
	}
	
}
