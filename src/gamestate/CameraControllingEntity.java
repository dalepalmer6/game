package gamestate;

import menu.StartupNew;

public class CameraControllingEntity extends Entity {
	protected Camera camera;
	
	public void setCamera(Camera c) {
		this.camera = c;
	}
	
	public void moveWithCamera(int dx, int dy) {
		if (dx == 0 && dy == 0) {
			this.updateActionTaken("idle");
		} else {
			this.updateActionTaken("walking");
		}
		if (dy < 0 && dx == 0) {
			directionX = "";
			directionY = "up";
		}
		if (dy > 0 && dx == 0) {
			directionX = "";
			directionY = "down";
		}
		if (dx > 0 && dy == 0) {
			directionY = "";
			directionX = "right";
		}
		if (dx < 0 && dy == 0) {
			directionY = "";
			directionX = "left";
		}
		if (dy < 0 && dx < 0) {
			directionY = "up";
			directionX = "left";
		}
		if (dy > 0 && dx > 0) {
			directionY = "down";
			directionX = "right";
		}
		if (dx > 0 && dy < 0) {
			directionX = "right";
			directionY = "up";
		}
		if (dx < 0 && dy > 0) {
			directionY = "down";
			directionX = "left";
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
