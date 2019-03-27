package font;

import canvas.Controllable;
import global.InputController;
import menu.StartupNew;

public class TextWindowWithPrompt extends TextWindow implements Controllable{
	
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
}
