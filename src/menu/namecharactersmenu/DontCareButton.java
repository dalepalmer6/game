package menu.namecharactersmenu;

import menu.MenuItem;
import system.SystemState;
import system.characters.PartyCharacter;

public class DontCareButton extends MenuItem {
	int index;
	
	public DontCareButton(SystemState state, int index) {
		super("Don't Care", 0, 0, state);
		this.index = index;
	}
	
	public String execute() {
		state.getMenuStack().peek().setInput(PartyCharacter.getPCById(index).getDefaultName());
		return null;
	}
	
	public void setInput(int val) {
		index = val;
	}
	
	
}
