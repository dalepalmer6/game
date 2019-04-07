package gamestate;

import canvas.Controllable;
import canvas.Drawable;
import canvas.MainWindow;
import font.SimpleDialogMenu;
import font.TextWindow;
import global.InputController;
import menu.StartupNew;

public class Player extends CameraControllingEntity implements Controllable{
	
private boolean confirmButtonDown = false;



//	public Entity(String texture, int x, int y, int width, int height,StartupNew m) {
//	public Player(String texture,int x, int y, int width, int height,Camera camera,StartupNew s,String name) {
	public Player(int scale, Entity e, Camera c, StartupNew s) {
		super(e.getTexture(),scale*e.getX(),scale*e.getY(),scale*e.getWidth(),scale*e.getHeight(),c,s,e.getName());
		spriteCoordinates = e.getSpriteCoordinates();
	}
	
	public void act() {
		if (confirmButtonDown) {
			if (interactables.size() != 0) {
				interactables.get(0).interact();
				setDirectionDuringAct(interactables.get(0));
			} else {
				SimpleDialogMenu.createDialogBox(state,"Nothing here.");
			}
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
			confirmButtonDown  = true;
		} else {
			confirmButtonDown = false;
		}
		
}

	@Override
	public void setFocused(boolean b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getFocused() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
