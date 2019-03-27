package gamestate;

import canvas.Controllable;
import canvas.Drawable;
import canvas.MainWindow;
import font.SimpleDialogMenu;
import font.TextWindow;
import global.InputController;
import menu.StartupNew;

public class Player extends CameraControllingEntity implements Controllable{
	
//	public Entity(String texture, int x, int y, int width, int height,StartupNew m) {
	public Player(String texture,int x, int y, int width, int height,Camera camera,StartupNew s) {
		super(texture,x,y,width,height,camera,s);
		this.width = width;
		this.height = height;
	}
	
	public void act() {
		if (interactables.size() != 0) {
			SimpleDialogMenu.createDialogBox(state,interactables.get(0).interactText());
			setDirectionDuringAct(interactables.get(0));
		} else {
			SimpleDialogMenu.createDialogBox(state,"Nothing here.");
		}
	}
	
	
	
	@Override
	public void handleInput(InputController input) {
		input.setHoldable(true);
		deltaX = 0; deltaY = 0;
		if (input.getSignals().get("UP")) {
			deltaY = -stepSize;
		} if (input.getSignals().get("DOWN")) {
			deltaY = stepSize;
		}if (input.getSignals().get("RIGHT")) {
			deltaX = stepSize;
		} if (input.getSignals().get("LEFT")) {
			deltaX = -stepSize;
		}  if (input.getSignals().get("CONFIRM")) {
			System.out.println("Acting");
			act();
		}
}
	
}
