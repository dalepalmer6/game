package gamestate;

import menu.StartupNew;

public class TrainEntity extends CameraControllingEntity {
	
//	public Entity(String texture, int x, int y, int width, int height,StartupNew m,String name) {
	public TrainEntity(Entity e, Camera c, StartupNew state) {
		super(e.getTexture(),e.getX(),e.getY(), e.getWidth(),e.getHeight(),c,state,"train");
		spriteCoordinates = e.getSpriteCoordinates();
		resetMovement();
		stepSize = 26;
	}
	
	public void act() {}
	
}
