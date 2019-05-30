package battlesystem.options;

import battlesystem.BattleMenu;
import menu.MenuItem;
import menu.StartupNew;

public class Defend extends MenuItem{

	public Defend(String t, int x, int y, StartupNew m) {
		super(t, x, y, m);
		// TODO Auto-generated constructor stub
	}
	public String execute() {
		BattleMenu m =  ((BattleMenu) state.getMenuStack().peek());
		m.setCurrentAction(new BattleAction(state));
		m.getCurrentAction().setUser(m.getCurrentPartyMember());
		m.getCurrentAction().setAction("defend");
		
		m.getCurrentAction().setTarget(m.getCurrentPartyMember());
		m.setDoneAction();
		return null;
	}
	
}
