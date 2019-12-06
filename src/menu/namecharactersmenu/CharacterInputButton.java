package menu.namecharactersmenu;

import menu.Menu;
import menu.MenuItem;
import system.SystemState;

public class CharacterInputButton extends MenuItem {

	public CharacterInputButton(char c,int x, int y, SystemState m) {
		super(Character.toString(c),0,0,m); 
	}
	
	public String prepareToExecute() {
		return execute();
	}
	
	public String execute() {
		//add to a String representing a name
		Menu m = state.getMenuStack().peek();
		m.appendInput(this.text);
		return null;
//		SystemState.out.println(this.text);
//		return this.text;
	}
}
