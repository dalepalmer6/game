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
			DialogTextWindow m = new DialogTextWindow(s,state);
			sdm.addMenuItem(m);
		state.getMenuStack().push(sdm);
	}
	
	public static void createDialogBox(StartupNew state, String s, String name) {
		// TODO Auto-generated method stub
		SimpleDialogMenu sdm = new SimpleDialogMenu(state);
		DialogTextWindow m = new DialogTextWindow(s,name,state);
			sdm.addMenuItem(m);
		state.getMenuStack().push(sdm);
	}
	
}
