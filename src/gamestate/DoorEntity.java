package gamestate;

import battlesystem.BattleMenu;
import canvas.MainWindow;
import menu.StartupNew;

public class DoorEntity extends Entity {
	//do not draw these
	private int destX;
	private int destY;
	private String newMap;
	
	public DoorEntity(int x, int y, int width, int height, StartupNew m, int destX, int destY, String map) {
		super("NOTEXTURE", x, y, width, height, m, "door");
		this.destX = destX;
		this.destY = destY;
		this.newMap = map;
	}
	
	public void act() {
		for (Entity e : interactables) {
			if (e instanceof Player) {
				state.getGameState().setCurrentMap(newMap);
				e.setCoordinates(destX,destY);
				state.getGameState().loadMap(1);
				state.getGameState().getCamera().snapToEntity(destX,destY);
				
			}
		}
	}
	
	@Override
	public void draw(MainWindow m) {}

}
