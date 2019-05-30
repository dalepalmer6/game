package menu;

import font.Text;

public class BackButton extends ButtonMenuItem {
	
	public BackButton(StartupNew m) {
		super("Back",m.getMainWindow().getScreenWidth()-200,m.getMainWindow().getScreenHeight()-200,100,50, m);
		text = "Back";
		textObject = new Text(true,text,x,y,0,0,state.charList);
		textObject.setAsSingleString();
	}
	
	public String execute() {
		state.needToPop = true;
		return null;
	}
}
