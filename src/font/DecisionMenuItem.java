package font;

import menu.MenuItem;
import menu.StartupNew;

public class DecisionMenuItem extends MenuItem {
	private int index;
	
	public DecisionMenuItem(String s, int index, StartupNew state) {
		super(s,0,0,state);
		this.index = index;
	}
	
	public int getIndex() {
		return index;
	}
	
}
