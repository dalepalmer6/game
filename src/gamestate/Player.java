package gamestate;

import java.util.Map;

import actionmenu.ActionMenu;
import canvas.Controllable;
import canvas.Drawable;
import canvas.MainWindow;
import font.SimpleDialogMenu;
import font.TextWindow;
import global.InputController;
import menu.StartupNew;

public class Player extends CameraControllingEntity implements Controllable{
	private double invincibilityFrameCount = 0;
	private double invincibilityTicks = 0.5;
	private boolean confirmButtonDown = false;
	
	MovementData[] movementData = new MovementData[64];
	private boolean enterButtonDown;
	
	public void update(GameState gs) {
		super.update(gs);
		if (state.getGameState().isInvincible()) {
			invincibilityFrameCount += invincibilityTicks;
		}
		
	}
	
	public void draw(MainWindow m) {
		if (state.getGameState().isInvincible()) {
			if ((int)invincibilityFrameCount % 2 == 0) {
				super.draw(m);
			}
		} else {
			super.draw(m);
		} 
	}
	
	public void fillMovementData(double x, double y) {
		for (MovementData md : movementData) {
			md.setX(x);
			md.setY(y);
		}
	}
	
	public void move() {
		super.move();
		if (forceAllowMovementY) {
			actionTaken = "";
			deltaX=0;
			directionX = "";
			if (deltaY == 0) {
				actionTaken = "idle";
			}
			directionY = "climb";
		}
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
		stepSizeX = 6;
		stepSizeY = 6;
		spriteCoordinates = e.getSpriteCoordinates();
		resetMovement();
	}
	
	public void act() {
		if (enterButtonDown && state.getMenuStack().isEmpty()) {
			ActionMenu am = new ActionMenu(state,state.getGameState().getPartyMembers());
			am.createMenu();
		}
	}
	
	public void check() {
			if (interactables.size() != 0) {
				int i = 0;
				while (interactables.get(i) instanceof DoorEntity || interactables.get(i) instanceof EnemySpawnEntity) {
					if (i+1 >= interactables.size()) {
						break;
					}
					i++;
				}
				interactables.get(i).interact();
				setDirectionDuringAct(interactables.get(0));
			} 
//			else {
//				SimpleDialogMenu.createDialogBox(state,"Nothing here.");
//			}
	}
	
	@Override
	public void handleInput(InputController input) {
//		input.setHoldable(true);
		deltaX = 0; deltaY = 0;
//		int angle;
		if (input.getSignals().get("UP")) {
			if (state.getGameState().getFlag("teleporting") && directionY.equals("down")) {
				
			} else {
				angleDirection = 3*Math.PI/2;
				deltaX += stepSizeX * Math.cos(angleDirection);
				deltaY += stepSizeY * Math.sin(angleDirection);
				directionY = "up";
			}
			
		} else if (input.getSignals().get("DOWN")) {
			if (state.getGameState().getFlag("teleporting") && directionY.equals("up")) {
				
			} else {
				angleDirection = Math.PI/2;
				deltaX += stepSizeX * Math.cos(angleDirection);
				deltaY += stepSizeY * Math.sin(angleDirection);
				directionY = "down";
			
			}
		} else {
			if (state.getGameState().getFlag("teleporting") && (input.getSignals().get("LEFT") || input.getSignals().get("RIGHT"))) {
				directionY = "";
			}
		} 
		
		if (input.getSignals().get("RIGHT")) {
			if (state.getGameState().getFlag("teleporting") && directionX.equals("left")) {
				
			} else {
				angleDirection = 0;
				deltaX += stepSizeX * Math.cos(angleDirection);
				deltaY += stepSizeY * Math.sin(angleDirection);
				if (!forceAllowMovementY)
				directionX = "right";
			}
		} else if (input.getSignals().get("LEFT")) {
			if (state.getGameState().getFlag("teleporting") && directionX.equals("right")) {
				
			} else {
				angleDirection = Math.PI;
				deltaX += stepSizeX * Math.cos(angleDirection);
				deltaY += stepSizeY * Math.sin(angleDirection);
				if (!forceAllowMovementY)
				directionX = "left";
			}
			
		} else {
			if (state.getGameState().getFlag("teleporting") && (input.getSignals().get("UP") || input.getSignals().get("DOWN"))) {
				directionX = "";
			}
		}  
		
		if (input.getSignals().get("ENTER")) {
			enterButtonDown = true;
		} else {
			enterButtonDown = false;
		}
		
		if (input.getSignals().get("CONFIRM")) {
			check();
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
