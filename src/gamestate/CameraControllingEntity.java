package gamestate;

import menu.StartupNew;

public class CameraControllingEntity extends Entity {
	protected Camera camera;
	
	public void move(int dx, int dy) {
		this.deltaX = dx;
		this.deltaY = dy;
		checkCollisions();
		x += deltaX;
		y += deltaY;
		if (x > camera.getState().getMainWindow().getScreenWidth()/2) {
			camera.updateCamera(deltaX, 0);
		}
		if (y > camera.getState().getMainWindow().getScreenHeight()/2) {
			camera.updateCamera(0, deltaY);
		}
	}

	public CameraControllingEntity(String texture,int x, int y, int width, int height,int dw,int dh, Camera c,StartupNew m) {
		super(texture,x,y,width,height,dw,dh,m);
		this.camera = c;
		c.snapToEntity(x,y);
	}
	
}
