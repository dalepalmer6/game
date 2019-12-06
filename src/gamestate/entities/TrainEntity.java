package gamestate.entities;

import gamestate.camera.Camera;
import system.SystemState;

public class TrainEntity extends CameraControllingEntity {
	
//	public Entity(String texture, int x, int y, int width, int height,SystemState m,String name) {
	public TrainEntity(Entity e, Camera c, SystemState state) {
		super(e.getTexture(),e.getX(),e.getY(), e.getWidth(),e.getHeight(),c,state,"train");
		spriteCoordinates = e.getSpriteCoordinates();
		resetMovement();
		stepSizeX = 26;
		stepSizeY = 26;
		setIgnoreCollisions(true);
	}
	
//	public void move() {
//		if (targetX != -1 && targetY != -1) {
//			if (x - targetX < 0) {
//				deltaX = stepSize;
//				while (x + deltaX > targetX) {
//					deltaX--;
//				}
//				actionTaken="walking";
//				directionX = "right";
//			} else if (x - targetX > 0){
//				deltaX = -stepSize;
//				while (x + deltaX < targetX) {
//					deltaX++;
//				}
//				
//				actionTaken="walking";
//				directionX = "left";
//			} else {
//				deltaX = 0;
////				directionX = "";
//				actionTaken="idle";
//			}
//			if (y - targetY < 0) {
//				deltaY = stepSize;
//				while (y + deltaY > targetY) {
//					deltaY--;
//				}
//				
//				actionTaken="walking";
//				directionY = "down";
//			} else if (y - targetY > 0){
//				deltaY = -stepSize;
//				while (y + deltaY < targetY) {
//					deltaY++;
//				}
//				
//				actionTaken="walking";
//				directionY = "up";
//			} else {
//				deltaY = 0;
////				directionY = "";
//				actionTaken = "idle";
//			}
//			if (deltaX == 0 && deltaY == 0) {
//				atTargetPoint = true;
//			}
//		} 
//			checkCollisions();
//		if (!ignoreCollisions) {
//			checkEntityCollisions();
//		}
//		x += deltaX;
//		y += deltaY;
//	}
	
	public void act() {}
	
}
