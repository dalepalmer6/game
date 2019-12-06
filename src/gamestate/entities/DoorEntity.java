package gamestate.entities;

import java.util.ArrayList;

import battlesystem.menu.BattleMenu;
import gamestate.GameState;
import menu.Menu;
import menu.animation.Animation;
import menu.animation.AnimationFadeToBlack;
import menu.animation.AnimationMenu;
import system.MainWindow;
import system.MotherSystemState;
import system.SystemState;
import system.sprites.SpritesheetCoordinates;

public class DoorEntity extends MotherEntity {
	//do not draw these
	private int destX;
	private int destY;
	private String newMap;
	private String text;
	
	public void draw(MainWindow m) {
		
	}
	
	@Override
	public void drawEntity(MainWindow m) {
	
	}
	
	public void interact() {}
	
	public void setNewParams(double x, double y, int w, int h, String name, String appFlag, String disFlag, String newMap) {
		// TODO Auto-generated method stub
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
		this.name = name;
		this.appearFlag = appFlag;
		this.disappearFlag = disFlag;
		this.newMap = newMap;
	}
	
	public DoorEntity(String desc,double x, double y, int width, int height, SystemState m, int destX, int destY, String map,String text) {
		super("door.png", x, y, width, height, m, "door");
		this.destX = destX;
		this.destY = destY;
		this.newMap = map;
		this.spriteCoordinates = new SpritesheetCoordinates();
		spriteCoordinates.setPose("idle_down");
		spriteCoordinates.addStateToPose("idle_down",0,0,12,12);
		this.name = desc;
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
	
	public String getText() {
		return text;
	}
	
	public boolean checkCollisions() {
		return false;
	}
	
	
	@Override
	public String getInfoForTool() {
		return getName() + "" + x + "," + y + ": to " + newMap;
	}
	
	public void act() {
		MotherSystemState state = (MotherSystemState) getState();
		for (Entity e : interactables) {
//			if (state.getGameState().getFlag("teleportingIn")) {
//				return;
//			}
			if (e instanceof CameraControllingEntity) {
				//remove the door from the gamestate
//				state.getGameState().getEntityList().remove(this);
//				if (state.getMenuStack().peek() != null || state.getMenuStack().peek() instanceof TrainCutscene) {
					if (state.getDoorCollided()) {
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
							} else if (e instanceof IntroEntity) {
								e.setCoordinates(destX, destY);
								e.setAtTargetPoint();
								state.getGameState().getEntityList().clear();
								state.getGameState().getEntityList().add(e);
							}
							
							state.getGameState().getCamera().snapToEntity(destX,destY);
							state.getGameState().setCurrentMap(newMap);
							state.getMenuStack().pop();
							state.setDoorCollided(false);
							
							state.getGameState().loadMap(4);
							return;
//							state.setShouldFadeIn();
						}
					}
					if (!state.getDoorCollided()) {
						state.setDoorCollided(true);
//						SystemState.out.println("Door Collide");
						AnimationMenu anim = new AnimationMenu(state);
						anim.createAnimMenu();
						state.getMenuStack().push(anim);
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
