package gamestate;

import java.util.ArrayList;

import battlesystem.BattleMenu;
import canvas.MainWindow;
import menu.Animation;
import menu.AnimationFadeToBlack;
import menu.AnimationMenu;
import menu.Menu;
import menu.StartupNew;

public class DoorEntity extends Entity {
	//do not draw these
	private int destX;
	private int destY;
	private String newMap;
	private String description;
	private String text;
	
	public void draw(MainWindow m) {
		
	}
	
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
	
	public boolean checkCollisions() {
		return false;
	}
	
	
	@Override
	public String getInfoForTool() {
		return "" + x + "," + y + ": to " + newMap;
	}
	
	public void act() {
		for (Entity e : interactables) {
			if (e instanceof Player || e instanceof TrainEntity) {
				//remove the door from the gamestate
//				state.getGameState().getEntityList().remove(this);
				if (!(state.getMenuStack().peek() instanceof AnimationMenu)) {
					System.out.println("Door Collide");
					AnimationMenu anim = new AnimationMenu(state);
					anim.createAnimMenu();
					state.getMenuStack().push(anim);
				}
//				if (state.getMenuStack().peek() != null || state.getMenuStack().peek() instanceof TrainCutscene) {
					if (state.getMenuStack().peek() instanceof AnimationMenu) {
						if (((AnimationMenu)state.getMenuStack().peek()).isComplete()) {
							if (e instanceof Player) {
								e.setCoordinates(destX,destY);
								if (e instanceof Player)  {
									((Player)e).resetMovement();
								}
							
								for (Entity e2 : state.getGameState().getEntityList()) {
									if (e2 instanceof FollowingPlayer) {
										e2.setCoordinates(destX,destY);
									}
								}
								ArrayList<Entity> players = new ArrayList<Entity>();
								for (Entity et : state.getGameState().getEntityList()) {
									if (et instanceof Player) {
										players.add(et);
									} else if (et instanceof FollowingPlayer) {
										players.add(et);
									}
								}
								state.getGameState().getEntityList().clear();
								state.getGameState().getEntityList().addAll(players);
							} else if (e instanceof TrainEntity) {
								e.setCoordinates(destX, destY);
								e.setAtTargetPoint();
								state.getGameState().getEntityList().clear();
								state.getGameState().getEntityList().add(e);
							}
							
							state.getGameState().getCamera().snapToEntity(destX,destY);
							state.getGameState().setCurrentMap(newMap);
							state.getMenuStack().pop();
							state.getGameState().loadMap(4);
//							state.setShouldFadeIn();
						}
					}
//				} else  {
//					AnimationMenu anim = new AnimationMenu(state);
//					anim.createAnimMenu();
//					state.getMenuStack().push(anim);
//				}	
			}
		}
	}
	
//	public void move() {}
	
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

}
