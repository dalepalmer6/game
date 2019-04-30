package gamestate;

import menu.StartupNew;

public class FollowingPlayer extends Entity {
	private int indexInParty;
	
	public void move() {
		checkCollisions();
		if (state.getGameState().getPlayer().deltaX != 0 || state.getGameState().getPlayer().deltaY != 0) {
			MovementData[] dataArr = state.getGameState().getPlayer().movementData;
			MovementData data = dataArr[9*indexInParty];
			x = data.getX();
			y = data.getY();
			actionTaken = data.getState();
			directionX = data.getDirectionX();
			directionY = data.getDirectionY();
		} else {
			actionTaken = state.getGameState().getPlayer().actionTaken;
		}
	}
	
	public FollowingPlayer(int scale, Entity e, StartupNew m, int i) {
		super(e.getTexture(),scale*e.getX(),scale*e.getY(),scale*e.getWidth(),scale*e.getHeight(),m,e.getName());
		spriteCoordinates = e.getSpriteCoordinates();
		indexInParty = i;
	}
	
}
