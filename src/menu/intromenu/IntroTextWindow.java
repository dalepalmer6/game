package menu.intromenu;

import menu.windows.TextWindow;
import system.MainWindow;
import system.SystemState;

public class IntroTextWindow extends TextWindow {

	public IntroTextWindow(boolean shouldDrawAll, String s, SystemState m) {
		super(shouldDrawAll, s, 100, 100, 1720, 300, m);
		// TODO Auto-generated constructor stub
	}

	public void draw(MainWindow m) {
		drawText(m);
	}
	
	public void updateAnim() {
		super.updateAnim();
		if (text.getDone()) {
			state.getMenuStack().pop();
		}
	}
	
	public boolean getCanUpdateGameState() {
		return true;
	}
	
}
