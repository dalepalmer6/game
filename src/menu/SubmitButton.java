package menu;

import menu.namecharactersmenu.CharacterNamingMenu;

public class SubmitButton extends MenuItem {
	private String output = "";
	
	public void setOutput(String s) {
		this.output = s;
	}
	
	public SubmitButton(StartupNew m) {
		super("Done",0,0,m);
	}
	
	public String execute() {
		Menu m = state.getMenuStack().pop();
		setOutput(m.getInput());
		m = state.getMenuStack().peek();
		m.setInput(output);
		((CharacterNamingMenu)m).increaseIndex(1);
		return null;
	}
}
