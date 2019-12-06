package menu;

import system.MainWindow;
import system.SystemState;

public class BackButton extends ButtonMenuItem {
	
	public BackButton(SystemState m) {
		super("Back",0,0,100,50, m);
		text = "Back";
		textObject = m.createTextEngine(true,text,(int)x,(int)y,0,0);
		textObject.setAsSingleString();
	}
	
	public void draw(MainWindow m) {
		
	}
	
	public String execute() {
		state.needToPop = true;
		return null;
	}
}
