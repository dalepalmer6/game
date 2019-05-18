package menu;

public class TeleportDestinationMenuItem extends MenuItem {
	private int newX;
	private int newY;
	private String newMapName;
	
	public TeleportDestinationMenuItem(String text, int x, int y, String mapName, StartupNew state) {
		super(text, 0, 0, state);
		this.newX = x;
		this.newY = y;
		this.newMapName = mapName;
	}
	
	public int getNewX() {
		return newX;
	}
	
	public int getNewY() {
		return newY;
	}
	
	public String getNewMap() {
		return newMapName;
	}
	
	public String execute() {
		state.setTeleportVariables(newMapName, newX, newY, true);
//		state.getMenuStack().pop();
		state.getMenuStack().clear();
		return null;
	}
	
}
