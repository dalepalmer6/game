package battlesystem.menu.psi;

import gamestate.psi.PSIClassification;
import menu.MenuItem;
import system.SystemState;

public class ClassificationMenuItem extends MenuItem {
	private PSIClassification psiClass;
	
	public ClassificationMenuItem(SystemState state, PSIClassification psic) {
		super(psic.getName(),0,0,state);
		psiClass = psic;
	}
	
	public String execute() {
		((PSIMenuInBattle) state.getMenuStack().peek()).enterPSIMenu();
		return null;
	}
	
}
