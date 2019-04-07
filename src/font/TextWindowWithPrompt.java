package font;

import canvas.Controllable;
import global.InputController;
import menu.StartupNew;

public class TextWindowWithPrompt extends TextWindow implements Controllable{
	private boolean focused;
	
	public TextWindowWithPrompt(String s, int x, int y, int width, int height, StartupNew m) {
		super(false,s, x,y,width,height,m);
	}
	
	@Override
	public void handleInput(InputController input) {
		// TODO Auto-generated method stub
		if (input.getSignals().get("CONFIRM")) {
			next();
		}
	}

	@Override
	public void setFocused(boolean b) {
		// TODO Auto-generated method stub
		this.focused = b;
	}

	@Override
	public boolean getFocused() {
		// TODO Auto-generated method stub
		return focused;
	}
}
