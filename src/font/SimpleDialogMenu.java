package font;

import menu.Menu;
import menu.StartupNew;

public class SimpleDialogMenu extends Menu {
	private TextWindow tw;
	private String text;
	
	public SimpleDialogMenu(StartupNew m) {
		super(m);
	}
	
//	public TextWindow(boolean shouldDrawAll, String s, int x, int y, int width, int height, StartupNew m)


	public static void createDialogBox(StartupNew state, String s) {
		// TODO Auto-generated method stub
		SimpleDialogMenu sdm = new SimpleDialogMenu(state);
			TextWindow m = new TextWindow(false,s,30,30,10,5,state);
			sdm.addMenuItem(m);
		state.getMenuStack().push(sdm);
	}
	
}
