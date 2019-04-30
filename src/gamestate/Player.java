package gamestate;

import actionmenu.ActionMenu;
import canvas.Controllable;
import canvas.Drawable;
import canvas.MainWindow;
import font.SimpleDialogMenu;
import font.TextWindow;
import global.InputController;
import menu.StartupNew;

public class Player extends CameraControllingEntity implements Controllable{
	
	private boolean confirmButtonDown = false;
	MovementData[] movementData = new MovementData[64];
	
	public void move() {
		super.move();
		if (deltaX != 0 || deltaY != 0) {
			for(int i = movementData.length-1; i > 0; i--){
				movementData[i] = movementData[i-1];
		    }
		    //record new position
			movementData[0] = new MovementData(x,y,actionTaken,directionX,directionY);
		}
	}

	public Player(int scale, Entity e, Camera c, StartupNew s) {
		super(e.getTexture(),scale*e.getX(),scale*e.getY(),scale*e.getWidth(),scale*e.getHeight(),c,s,e.getName());
		spriteCoordinates = e.getSpriteCoordinates();
		resetMovement();
	}
	
	public void act() {
		if (confirmButtonDown && state.getMenuStack().isEmpty()) {
			ActionMenu am = new ActionMenu(state,state.getGameState().getPartyMembers());
			am.createMenu();
		}
	}
	
	public void talkTo() {
			if (interactables.size() != 0) {
				int i = 0;
				while (interactables.get(i) instanceof DoorEntity) {
					if (i >= interactables.size()) {
						break;
					}
					i++;
				}
				interactables.get(i).interact();
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

	public void resetMovement() {
		// TODO Auto-generated method stub
		movementData = new MovementData[64];
		for (int i = 0; i < movementData.length; i++) {
			movementData[i] = new MovementData(x,y,actionTaken,directionX,directionY);
		}
	}
	
}
