package gamestate;

import battlesystem.BattleMenu;
import canvas.MainWindow;
import menu.Animation;
import menu.AnimationFadeToBlack;
import menu.AnimationMenu;
import menu.StartupNew;

public class DoorEntity extends Entity {
	//do not draw these
	private int destX;
	private int destY;
	private String newMap;
	private String description;
	private String text;
	
	public DoorEntity(String desc,int x, int y, int width, int height, StartupNew m, int destX, int destY, String map,String text) {
		super("door.png", x, y, width, height, m, "door");
		this.destX = destX;
		this.destY = destY;
		this.newMap = map;
		this.spriteCoordinates = new SpritesheetCoordinates();
		spriteCoordinates.setPose("idle_down");
		spriteCoordinates.addStateToPose("idle_down",0,0,12,12);
		this.description = desc;
		this.text = text;
	}
	
	public void addToInteractables(Entity e) {
		if (!interactables.contains(e)) {
			this.interactables.add(e);
		}
	}
	public void update(GameState gs) {
		move();
		xOnScreen = x - gs.getCamera().getX();
		yOnScreen = y - gs.getCamera().getY();
		updateFrameTicks();
	}
	
	public String getDestMap() {
		return newMap;
	}
	
	public int getDestX() {
		return destX;
	}
	
	public int getDestY() {
		return destY;
	}
	
	public String getDesc() {
		return description;
	}
	
	public String getText() {
		return text;
	}
	
	public void act() {
		for (Entity e : interactables) {
			if (e instanceof Player) {
				if (state.canLoad) {
					state.canLoad = false;
					e.setCoordinates(destX,destY);
					state.getGameState().getCamera().snapToEntity(destX,destY);
					state.getGameState().setCurrentMap(newMap);
					state.getGameState().loadMap(4);
				} else  {
					AnimationMenu anim = new AnimationMenu(state);
					anim.createAnimMenu();
					state.getMenuStack().push(anim);
				}
				
			}
		}
	}

	public void interact() {}
	
	public void setLocation(int x, int y) {
		// TODO Auto-generated method stub
		this.x = x;
		this.y = y;
	}
	
	public void setDestination(String destMap, int destX, int destY) {
		this.newMap = destMap;
		this.destX = destX;
		this.destY = destY;
	}
	
//	@Override
//	public void draw(MainWindow m) {
////		m.setTexture("img/door.png");
////		Pose p = spriteCoordinates.getPose("idle","","down");
////		m.renderTile(x,y,width,height,,0,12,12);
//	}

}
