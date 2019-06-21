package font;

import canvas.MainWindow;
import menu.StartupNew;

public class IntroTextWindow extends TextWindow {

	public IntroTextWindow(boolean shouldDrawAll, String s, StartupNew m) {
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
