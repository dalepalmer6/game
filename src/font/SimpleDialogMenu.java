package font;

import menu.Menu;
import menu.StartupNew;

public class SimpleDialogMenu extends Menu {
	private TextWindow tw;
	private String text;
	
	public SimpleDialogMenu(StartupNew m) {
		super(m);
		backShouldExit = false;
	}
	
	public boolean getCanUpdateGameState() {
		if (tw == null) {
			return false;
		}
		return tw.getCanUpdateGameState();
	}

	public static void createDialogBox(StartupNew state, String s) {
		// TODO Auto-generated method stub
		SimpleDialogMenu sdm = new SimpleDialogMenu(state);
			TextWindow m= null;
			if (state.getGameState().getDoIntro()) {
				m = new IntroTextWindow(false,s,state);
			} else {
				m = new DialogTextWindow(s,state);
			}
			sdm.tw = m;
			sdm.addMenuItem(m);
		state.getMenuStack().push(sdm);
	}
	
	public static void createDialogBox(StartupNew state, String s, String name) {
		// TODO Auto-generated method stub
		SimpleDialogMenu sdm = new SimpleDialogMenu(state);
		DialogTextWindow m = new DialogTextWindow(s,name,state);
			sdm.tw = m;
			sdm.addMenuItem(m);
		state.getMenuStack().push(sdm);
	}
	
}
