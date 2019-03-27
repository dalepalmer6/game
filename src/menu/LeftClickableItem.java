package menu;

import org.lwjgl.opengl.GL11;

import canvas.Clickable;
import canvas.MainWindow;
import global.InputController;

public class LeftClickableItem extends DrawableObject implements Clickable {
	
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
