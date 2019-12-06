package menu.namecharactersmenu;

import menu.MenuItem;
import system.SystemState;

public class DontCareButton extends MenuItem {
	int index;
	
	public DontCareButton(SystemState state, int index) {
		super("Don't Care", 0, 0, state);
		this.index = index;
	}
	
	public String execute() {
		state.getMenuStack().peek().setInput(state.defaultNames[index]);
		return null;
	}
	
	public void setInput(int val) {
		index = val;
	}
	
	
}
