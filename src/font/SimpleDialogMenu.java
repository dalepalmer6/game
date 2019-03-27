package font;

import menu.Menu;
import menu.StartupNew;

public class SimpleDialogMenu extends Menu {
	private TextWindow tw;
	private String text;
	
	public SimpleDialogMenu(StartupNew m) {
		super(m);
	}


	public static void createDialogBox(StartupNew state, String s) {
		// TODO Auto-generated method stub
		SimpleDialogMenu sdm = new SimpleDialogMenu(state);
			TextWindowWithPrompt m = new TextWindowWithPrompt(s,30,30,10,5,state);
			sdm.addMenuItem(m);
		state.getMenuStack().push(sdm);
	}
	
}
