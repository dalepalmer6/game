package menu.text;

import menu.MenuItem;
import system.SystemState;

public class DecisionMenuItem extends MenuItem {
	private int index;
	
	public DecisionMenuItem(String s, int index, SystemState state) {
		super(s,0,0,state);
		this.index = index;
	}
	
	public int getIndex() {
		return index;
	}
	
}
