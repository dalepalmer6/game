package gamestate;

import menu.StartupNew;

public class FollowingPlayer extends Entity {
	private int indexInParty;
	
	public void move() {
		checkCollisions();
//		if (state.getGameState().getPlayer().deltaX != 0 || state.getGameState().getPlayer().deltaY != 0) {
			MovementData[] dataArr = state.getGameState().getPlayer().movementData;
			MovementData data = dataArr[12*indexInParty];
			x = data.getX();
			y = data.getY();
			actionTaken = data.getState();
			directionX = data.getDirectionX();
			directionY = data.getDirectionY();
//		} 
//		else {
		if (state.getGameState().getPlayer().deltaX == 0 && state.getGameState().getPlayer().deltaY == 0) {
			actionTaken = state.getGameState().getPlayer().actionTaken;
		}
//		if (forceAllowMovementY) {
//			actionTaken = "";
//			deltaX=0;
//			directionX = "";
//			if (deltaY == 0) {
//				actionTaken = "idle";
//			}
//			directionY = "climb";
//		}
//		}
	}
	
	public FollowingPlayer(int scale, Entity e, StartupNew m, int i) {
		super(e.getTexture(),e.getX(),e.getY(),e.getWidth()*scale,scale*e.getHeight(),m,e.getName());
		spriteCoordinates = e.getSpriteCoordinates();
		indexInParty = i;
	}
	
}
