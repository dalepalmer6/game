package battlesystem.options;

import battlesystem.BattleMenu;
import font.SelectionTextWindow;
import menu.MenuItem;
import menu.StartupNew;

public class Bash extends MenuItem{

	public Bash(String t, int x, int y, StartupNew m) {
		super(t, x, y, m);
		// TODO Auto-generated constructor stub
	}
	
	public String execute() {
		//show the options who can be attacked
		BattleMenu m =  ((BattleMenu) state.getMenuStack().peek());
		m.setCurrentAction(new BattleAction(state));
		m.getCurrentAction().setAction("bash");
		m.getCurrentAction().setUser(m.getCurrentPartyMember());
		EnemyOptionPanel eop = m.getEnemyOptionPanel();
		BattleSelectionTextWindow stw = new BattleSelectionTextWindow("vertical",m.getCurrentPlayerStatusWindow().getX(),m.getCurrentPlayerStatusWindow().getY()-3*64,3,3,state);
			for (EnemyOption eo : eop.getEnemyOptions()) {
				stw.add(eo);
			}
		stw.setKillWhenComplete();
		m.addToMenuItems(stw);
		return null;
	}

}
