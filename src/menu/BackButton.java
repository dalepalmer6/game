package menu;

import global.GlobalVars;

public class BackButton extends MenuItem {
	
	public BackButton() {
		super("Back",300,300,50,50);
	}
	
	public void execute() {
		GlobalVars.menuStack.popAndAddMenuItems();
	}
}
