package mapeditor;

import canvas.Clickable;
import global.InputController;
import menu.LeftClickableItem;

public class LeftAndRightClickableItem extends LeftClickableItem implements Clickable{

	@Override
	public String execute() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void executeRightClick() {
		//Needs to be overrided by the super class
	}

	@Override
	public void checkInputs(InputController input) {
		// TODO Auto-generated method stub
		if (input.getSignals().get("MOUSE_LEFT_DOWN")) {
			execute();
		} else if (input.getSignals().get("MOUSE_RIGHT_DOWN")) {
			executeRightClick();
		}
	}
}
