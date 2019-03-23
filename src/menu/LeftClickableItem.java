package menu;

import canvas.Clickable;
import global.InputController;

public class LeftClickableItem implements Clickable {

	@Override
	public String execute() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void checkInputs(InputController input) {
		// TODO Auto-generated method stub
		if (input.getSignals().get("MOUSE_LEFT_DOWN")) {
			execute();
		}
	}

}
