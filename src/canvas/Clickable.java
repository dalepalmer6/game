package canvas;

import global.InputController;

public interface Clickable {
	public void execute();
	public void checkInputs(InputController input);
}
