package system.interfaces;

import system.controller.InputController;

public interface Controllable {
	public void handleInput(InputController input);
	public void setFocused(boolean b);
	public boolean getFocused();
}
