package menu;

public class BackButton extends ButtonMenuItem {
	
	public BackButton(StartupNew m) {
		super("Back",m.getMainWindow().getScreenWidth()-200,m.getMainWindow().getScreenHeight()-200,100,50, m);
	}
	
	public String execute() {
		state.needToPop = true;
		return null;
	}
}
