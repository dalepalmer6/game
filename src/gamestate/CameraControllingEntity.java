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
		} else {
			this.updateActionTaken("walking");
		}
		if (dy < 0 && Math.abs(dx-0) < THRESHOLD) {
			directionX = "";
			directionY = "up";
		}
		else if (dy > 0 && Math.abs(dx-0) < THRESHOLD) {
			directionX = "";
			directionY = "down";
		}
		else if (dx > 0 && Math.abs(dy-0) < THRESHOLD) {
			directionY = "";
			directionX = "right";
		}
		else if (dx < 0 && Math.abs(dy-0) < THRESHOLD) {
			directionY = "";
			directionX = "left";
		}
		else if (dy < 0 && dx < 0) {
			directionY = "up";
			directionX = "left";
		}
		else if (dy > 0 && dx > 0) {
			directionY = "down";
			directionX = "right";
		}
		else if (dx > 0 && dy < 0) {
			directionX = "right";
			directionY = "up";
		}
		else if (dx < 0 && dy > 0) {
			directionY = "down";
			directionX = "left";
		}
		move();
		if (x > camera.getState().getMainWindow().getScreenWidth()/2) {
			camera.updateCamera((int)deltaX, 0);
		}
		if (y > camera.getState().getMainWindow().getScreenHeight()/2) {
			camera.updateCamera(0, (int)deltaY);
		}
	}
	
	public void update(GameState gs) {
		moveWithCamera(deltaX,deltaY);
		xOnScreen = x - gs.getCamera().getX();
		yOnScreen = y - gs.getCamera().getY();
		camera.snapToEntity(x,y);
		updateFrameTicks();
	}

	public CameraControllingEntity(String texture,int x, int y, int width, int height, Camera c, StartupNew m,String name) {
		super(texture,x,y,width,height,m,name);
		this.camera = c;
		if (c != null) {
			c.snapToEntity(x,y);
		}
		
	}
	
}
