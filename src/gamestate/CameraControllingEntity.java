package gamestate;

import menu.StartupNew;

public class CameraControllingEntity extends Entity {
	protected Camera camera;
	
	public void setCamera(Camera c) {
		this.camera = c;
	}
	
	public void moveWithCamera(double dx, double dy) {
		double THRESHOLD = 0.0001d;
		if (Math.abs(dy-0) < THRESHOLD && Math.abs(dx-0) < THRESHOLD) {
			this.updateActionTaken("idle");
		} 
//		else {
//			this.updateActionTaken("walking");
//		}
		if (dy < 0 && Math.abs(dx-0) < THRESHOLD) {
//			directionX = "";
//			directionY = "up";
			setMovementAction(null,"up");
		}
		else if (dy > 0 && Math.abs(dx-0) < THRESHOLD) {
//			directionX = "";
//			directionY = "down";
			setMovementAction(null,"down");
		}
		else if (dx > 0 && Math.abs(dy-0) < THRESHOLD) {
//			directionY = "";
//			directionX = "right";
			setMovementAction("right",null);
		}
		else if (dx < 0 && Math.abs(dy-0) < THRESHOLD) {
//			directionY = "";
//			directionX = "left";
			setMovementAction("left",null);
		}
		else if (dy < 0 && dx < 0) {
//			directionY = "up";
//			directionX = "left";
			setMovementAction("left","up");
		}
		else if (dy > 0 && dx > 0) {
//			directionY = "down";
//			directionX = "right";
			setMovementAction("right","down");
		}
		else if (dx > 0 && dy < 0) {
//			directionX = "right";
//			directionY = "up";
			setMovementAction("right","up");
		}
		else if (dx < 0 && dy > 0) {
//			directionY = "down";
//			directionX = "left";
			setMovementAction("left","down");
		}
		move();
		if (x > camera.getState().getMainWindow().getScreenWidth()/2) {
			camera.updateCamera(deltaX, 0);
		}
		if (y > camera.getState().getMainWindow().getScreenHeight()/2) {
			camera.updateCamera(0, deltaY);
		}
	}
	
	public void update(GameState gs) {
		forceAllowMovementY = false;
		lastBehind = behind;
		behind = false;
		moveWithCamera(deltaX,deltaY);
		xOnScreen = x - gs.getCamera().getX();
		yOnScreen = y - gs.getCamera().getY();
		camera.snapToEntity(x,y);
		updateFrameTicks();
	}

	public CameraControllingEntity(String texture,double x, double y, int width, int height, Camera c, StartupNew m,String name) {
		super(texture,x,y,width,height,m,name);
		this.camera = c;
		if (c != null) {
			c.snapToEntity(x,y);
		}
		
	}
	
}
