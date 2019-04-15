package actionmenu.psimenu;

import gamestate.psi.PSIClassification;
import menu.MenuItem;
import menu.StartupNew;

public class ClassificationMenuItem extends MenuItem {
	private PSIClassification psiClass;
	
	public ClassificationMenuItem(StartupNew state, PSIClassification psic) {
		super(psic.getName(),0,0,state);
		psiClass = psic;
	}
	
	public String execute() {
		
		return null;
	}
	
}
