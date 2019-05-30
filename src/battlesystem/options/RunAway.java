package battlesystem.options;

import battlesystem.BattleMenu;
import menu.MenuItem;
import menu.StartupNew;

public class RunAway extends MenuItem{

	public RunAway(String t, int x, int y, StartupNew m) {
		super(t, x, y, m);
		// TODO Auto-generated constructor stub
	}
	public String execute() {
		BattleMenu m =  ((BattleMenu) state.getMenuStack().peek());
		m.setCurrentAction(new BattleAction(state));
		m.getCurrentAction().setUser(m.getCurrentPartyMember());
		m.getCurrentAction().setAction("run");
		
		m.getCurrentAction().setTarget(null);
		m.setDoneAction();
		return null;
	}
	
}
