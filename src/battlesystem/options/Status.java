package battlesystem.options;

import battlesystem.BattleMenu;
import menu.MenuItem;
import menu.StartupNew;

public class Status extends MenuItem{

	public Status(String t, int x, int y, StartupNew m) {
		super(t, x, y, m);
		// TODO Auto-generated constructor stub
	}
	public String execute() {
		BattleMenu m =  ((BattleMenu) state.getMenuStack().peek());
		m.setCurrentAction(new BattleAction());
		m.getCurrentAction().setAction("status");
		m.getCurrentAction().setUser(m.getCurrentPartyMember());
		m.getCurrentAction().setTarget(null);
		m.setDoneAction();
		return null;
	}
	
}
