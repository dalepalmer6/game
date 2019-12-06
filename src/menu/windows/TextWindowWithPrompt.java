package menu.windows;

import system.SystemState;
import system.controller.InputController;
import system.interfaces.Controllable;

public class TextWindowWithPrompt extends TextWindow implements Controllable{
	private boolean focused;
	
	public TextWindowWithPrompt(String s, int x, int y, int width, int height, SystemState m) {
		super(false,s, x,y,width,height,m);
	}
	
	@Override
	public void handleInput(InputController input) {
		// TODO Auto-generated method stub
		if (input.getSignals().get("CONFIRM")) {
			if (text.getDoneState()) {
				next();
			} else {
				text.setTextRate(1d);
			} 
		} else {
			text.setTextRate(2d);
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
