package gamestate;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import canvas.Drawable;
import canvas.MainWindow;
import font.SimpleDialogMenu;
import global.InputController;
import mapeditor.Tile;
import menu.DrawableObject;
import menu.StartupNew;
import tiles.TileInstance;

public class Entity implements Drawable,EntityInterface {
	protected String text = "";
	protected double tickCount = 0;
	protected double ticksPerFrame = 0.1;
	protected long now;
	protected SpritesheetCoordinates spriteCoordinates;
	protected ArrayList<String> entityStates;
	protected String directionX="";
	protected String directionY="";
	protected String actionTaken;
	protected String inputLast = "";
	protected int x;
	protected int y;
	private boolean doNotMove;
	protected int boundingBoxHeight;
	protected double deltaX;
	protected double deltaY;
	protected int xOnScreen;
	protected int yOnScreen;
	private String texture;
	protected int width;
	protected int height;
	protected int stepSize = 8;
	protected StartupNew state;
	protected ArrayList<Entity> interactables;
	private long delta;
	private String name;
	private boolean needsRemove;
	private String appearFlag = " ";
	private String disappearFlag = " ";
	private double moveSpeedThisFrame;
	protected double angleDirection;
	protected int targetX = -1;
	protected int targetY = -1;
	protected boolean atTargetPoint = true;
	
	public void setAppearFlag(String a) {
		appearFlag = a;
	}
	
	public void setDisappearFlag(String d) {
		disappearFlag = d;
	}
	
	public void setToRemove(boolean b) {
		this.needsRemove = b;
	}
	
	public boolean getNeedToRemoveState() {
		return needsRemove;
	}
	
	public String getText() {
		return text;
	}
	
	public String getTexture() {
		return texture;
	}
	
	public String getTextureNoExt() {
		return texture.substring(0,texture.indexOf("."));
	}
	
	public StartupNew getState() {
		return state;
	}
	
	public void setSpriteCoords(SpritesheetCoordinates sc) {
		spriteCoordinates = sc;
	}
	
	public Entity createCopy(int newX, int newY, int width, int height, String name) {
		Entity copy = new Entity(texture,newX,newY,width,height,state,name);
		copy.setSpriteCoords(spriteCoordinates);
		return copy;
	}
	
	public void updateActionTaken(String s) {
		this.actionTaken = s;
	}
	
	public void updateDirection(String s) {
		switch(s) {
			case "up" : 	directionY = s; break;
			case "down" :	directionY = s; break;
			case "left" :	directionX = s; break;
			case "right" :	directionX = s; break;
			default :  break;
		}
	}
	
	public void setCoordinates(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void checkEntityCollisions() {
		this.clearInteractables();
		if (this instanceof FollowingPlayer) {
			return;
		}
		for (Entity e2 : state.getGameState().getEntityList()) {
			if (e2 instanceof FollowingPlayer) {
				continue;
			}
			if ((this instanceof EnemyEntity && e2 instanceof Player) || (this instanceof Player && e2 instanceof EnemyEntity)) {
				if (state.getGameState().isInvincible()) {
					continue;
				}
			}
			if (this instanceof EnemyEntity && e2 instanceof EnemyEntity) {
				continue;
			}
			if (!(this == e2)) {
				if (!(this instanceof DoorEntity || this instanceof EnemySpawnEntity)) {
					if (this.checkCollision(e2)) {
						this.deltaX = 0;
						this.deltaY = 0;
						e2.deltaX = 0;
						e2.deltaY = 0;
					}
				}
					if (this.checkCollisionWithTolerance(e2,8)) {
						this.addToInteractables(e2);
					} else {
//						e.removeFromInteractables(e2);
					}
			}
		}
	}
	
	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void move() {
		checkCollisions();
		checkEntityCollisions();
		if (targetX != -1 && targetY != -1) {
			if (x - targetX < 0) {
				deltaX = stepSize;
				while (x + deltaX > targetX) {
					deltaX--;
				}
				actionTaken="walking";
				directionX = "right";
			} else if (x - targetX > 0){
				deltaX = -stepSize;
				while (x + deltaX < targetX) {
					deltaX++;
				}
				
				actionTaken="walking";
				directionX = "left";
			} else {
				deltaX = 0;
//				directionX = "";
				actionTaken="idle";
			}
			if (y - targetY < 0) {
				deltaY = stepSize;
				while (y + deltaY > targetY) {
					deltaY--;
				}
				
				actionTaken="walking";
				directionY = "down";
			} else if (y - targetY > 0){
				deltaY = -stepSize;
				while (y + deltaY < targetY) {
					deltaY++;
				}
				
				actionTaken="walking";
				directionY = "up";
			} else {
				deltaY = 0;
//				directionY = "";
				actionTaken = "idle";
			}
			if (deltaX == 0 && deltaY == 0) {
				atTargetPoint = true;
			}
		} 
		x += deltaX;
		y += deltaY;
	}
	
	public void setAtTargetPoint() {
		atTargetPoint = true;
		targetX = -1;
		targetY = -1;
	}
	
	public boolean getAtTargetPoint() {
		return atTargetPoint;
	}
	
//	public void move() {
//		int colmask;
//		if (((colmask = checkTileCollision())&15) != 0) {
//			int sweepInterval = 10;
//			boolean exit = false;
//			double savedDeltaX = deltaX;
//			double savedDeltaY = deltaY;
//			double vectorLength = Math.sqrt(deltaX*deltaX + deltaY*deltaY);
//			double angleOfInitialMovement = Math.acos(deltaX/vectorLength);
//			for (int angle = sweepInterval; angle < 80; angle+=sweepInterval) {
//				for (int mult = -1; mult <= 1; mult +=2) {
//					deltaX = savedDeltaX;
//					deltaY = savedDeltaY;
//					double angleToCheck = angleOfInitialMovement + Math.toRadians(angle*mult);
//					if (this instanceof Player)
//					System.out.println("Trying to find new movement vector.");
//					deltaX = (int) (vectorLength * Math.cos(angleToCheck));
//					deltaY = (int) (vectorLength * Math.sin(angleToCheck));
////					x += deltaX;
////					y += deltaY;
//					if (((colmask = checkTileCollision())&15) == 0) {
//						System.out.println("Suitable found " + deltaX + "," + deltaY);
//						exit = true;
//						break;
//					} else {
//						deltaX = 0;
//						deltaY = 0;
//					}
//				}
//				if (exit) {
//					break;
//				}
//			}
//		} 
////		checkCollisions();
//		checkEntityCollisions();
//		x += deltaX;
//		y += deltaY;
//		
//	}
	
	public void kill() {
		
	}
	
	public void update(GameState gs) {
		moveSpeedThisFrame = (int) (stepSize * state.getGameState().getDeltaTime());
		move();
		xOnScreen = x - gs.getCamera().getX();
		yOnScreen = y - gs.getCamera().getY();
		if (xOnScreen > state.getMainWindow().getScreenWidth() + 768|| yOnScreen > state.getMainWindow().getScreenHeight() + 768
		 || xOnScreen < -768 || yOnScreen < -768) {
			setToRemove(true);
		}
		updateFrameTicks();
	}
	
	public void updateFrameTicks() {
		tickCount += ticksPerFrame;
	}
	
	public int getXOnScreen() {
		return xOnScreen;
	}
	
	public int getYOnScreen() {
		return yOnScreen;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setText(String s) {
		text = s;
	}
	
	public Entity(String texture, int x, int y, int width, int height,StartupNew m,String name) {
		this.spriteCoordinates = new SpritesheetCoordinates();
		directionY = "down";
		actionTaken = "idle";
		this.texture = texture;
		this.x = x;
		this.y = y;
		this.boundingBoxHeight = height * 3/4;
		xOnScreen = 0;
		yOnScreen = 0;
		this.width = width;
		this.height = height;
		this.state = m;
		this.interactables = new ArrayList<Entity>();
		this.name = name;
	}
	
	public String interactText() {
		return null;
	}
	
	public void addToInteractables(Entity e) {
		if (!interactables.contains(e)) {
			this.interactables.add(e);
		}
	}
	
	public void clearInteractables() {
		this.interactables.clear();
	}
	
	public void drawEntity(MainWindow m) {
		initDrawEntity(m,texture);
		Pose pose = getSpriteCoordinates().getPose(actionTaken, directionX,directionY);
		int i = (int) tickCount % pose.getNumStates();
		TileMetadata tm = pose.getStateByNum(i);
		m.renderTile(xOnScreen, yOnScreen, width, height, tm.getX(), tm.getY(), tm.getWidth(), tm.getHeight(),tm.getFlipState());
	}
	
	public void drawEntity(MainWindow m, int x, int y) {
		initDrawEntity(m,texture);
		Pose pose = getSpriteCoordinates().getPose(actionTaken, directionX,directionY);
		int i = (int) tickCount % pose.getNumStates();
		TileMetadata tm = pose.getStateByNum(i);
		m.renderTile(x, y, width, height, tm.getX(), tm.getY(), tm.getWidth(), tm.getHeight(),tm.getFlipState());
	}
	
	public void draw(MainWindow m) {
		// TODO Auto-generated method stub
		drawEntity(m);
	}
	
	public static void initDrawEntity(MainWindow m, String texture) {
		m.setTexture("img\\" + texture);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
//		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public void stageForRedraw() {
		state.getGameState().addToRedrawing(new RedrawObject(this));
	}
	
	public boolean checkCollision(int collision, int x, int y, TileInstance tbg, TileInstance tfg) { 
//		if (((collision & 1) & 15) == 1) {
//			deltaX = 0;
//			deltaY = 0;
//		}
		boolean collided = false;
		int val = ((collision & 5) & 15);
		switch(val) {
			case 1: 
				deltaX = 0; 
				deltaY = 0; 
				collided = true; break;
			case 4: break;
			case 5: break;
		}
		
		return collided;
//		if (val == 1) {
//			deltaX = 0;
//			deltaY = 0;
//		}
//		if (((collision & 2) & 15) == 2) {
//			System.out.println("TEST");
//		}
	}
	
	public int checkTileCollision() {
		int ts = state.getGameState().getMapRenderer().getTileSize();
		int leftEdge = (int) ((this.x +deltaX)/ts);
		int rightEdge = (int) ((this.x + this.width +deltaX)/ts);
		int upperEdge = (int) ((this.y + this.height*3/4 +deltaY)/ts);
		int lowerEdge = (int) ((this.y + this.height +deltaY)/ts);
				
		int leftEdgeTest = (int) ((this.x +deltaX)%ts / (ts/4));
		int rightEdgeTest = (int) ((this.x + this.width +deltaX)%ts / (ts/4));
		int upperEdgeTest = (int) ((this.y + this.height*3/4 +deltaY)%ts / (ts/4));
		int lowerEdgeTest = (int) ((this.y + this.height +deltaY)%ts / (ts/4));
		
		TileInstance t1BG = state.getGameState().getMap().tileInstanceMapBG.get(upperEdge).get(leftEdge);
		TileInstance t2BG = state.getGameState().getMap().tileInstanceMapBG.get(upperEdge).get(rightEdge);
		TileInstance t3BG = state.getGameState().getMap().tileInstanceMapBG.get(lowerEdge).get(rightEdge);
		TileInstance t4BG = state.getGameState().getMap().tileInstanceMapBG.get(lowerEdge).get(leftEdge);
		
		int collisionStatet1BG = t1BG.getCollisionInfoAtIndex(leftEdgeTest,upperEdgeTest);
		int collisionStatet2BG = t2BG.getCollisionInfoAtIndex(rightEdgeTest,upperEdgeTest);
		int collisionStatet3BG = t3BG.getCollisionInfoAtIndex(rightEdgeTest,lowerEdgeTest);
		int collisionStatet4BG = t4BG.getCollisionInfoAtIndex(leftEdgeTest,lowerEdgeTest);
		
		TileInstance t1FG = state.getGameState().getMap().tileInstanceMapFG.get(upperEdge).get(leftEdge);
		TileInstance t2FG = state.getGameState().getMap().tileInstanceMapFG.get(upperEdge).get(rightEdge);
		TileInstance t3FG = state.getGameState().getMap().tileInstanceMapFG.get(lowerEdge).get(rightEdge);
		TileInstance t4FG = state.getGameState().getMap().tileInstanceMapFG.get(lowerEdge).get(leftEdge);
		
		int collisionStatet1FG = t1FG.getCollisionInfoAtIndex(leftEdgeTest,upperEdgeTest);
		int collisionStatet2FG = t2FG.getCollisionInfoAtIndex(rightEdgeTest,upperEdgeTest);
		int collisionStatet3FG = t3FG.getCollisionInfoAtIndex(rightEdgeTest,lowerEdgeTest);
		int collisionStatet4FG = t4FG.getCollisionInfoAtIndex(leftEdgeTest,lowerEdgeTest);
		
		int collisiont1=0,collisiont2=0,collisiont3=0,collisiont4=0;

		collisiont1 = (collisionStatet1FG | collisionStatet1BG);
		collisiont2 = (collisionStatet2FG | collisionStatet2BG);
		collisiont3 = (collisionStatet3FG | collisionStatet3BG);
		collisiont4 = (collisionStatet4FG | collisionStatet4BG);
		
		boolean tl = checkCollision(collisiont1,leftEdge,upperEdge,t1BG,t1FG);
		boolean tr = checkCollision(collisiont2,rightEdge,upperEdge,t2BG,t2FG);
		boolean bl = checkCollision(collisiont3,leftEdge,lowerEdge,t3BG,t3FG);
		boolean br = checkCollision(collisiont4,rightEdge,upperEdge,t4BG,t4FG);
		
		int colmask = 0;
		if (tl) {
			colmask |= 8;
		}
		if (tr) {
			colmask |= 4;
		}
		if (bl) {
			colmask |= 2;
		}
		if (br) {
			colmask |= 1;
		}
		return colmask;
	}
	
	public boolean checkCollisions() {
		//collisions are cumulative over all layers
		
		int colmask = checkTileCollision();
		
		if (colmask == 8 && directionX.equals("left")) {
			deltaX = 0;
			deltaY = 8;
			checkTileCollision();
		}
		if (colmask == 8 && directionY.equals("up")) {
			deltaX = 8;
			deltaY = 0;
			checkTileCollision();
		}
		
		if (colmask == 1 && directionX.equals("right")) {
			deltaX = 0;
			deltaY = -8;
			checkTileCollision();
		}
		if (colmask == 1 && directionY.equals("down")) {
			deltaX = -8;
			deltaY = 0;
			checkTileCollision();
		}
		
		if (colmask == 4 && directionX.equals("right")) {
			deltaX = 0;
			deltaY = 8;
			checkTileCollision();
		}
		if (colmask == 4 && directionY.equals("up")) {
			deltaX = -8;
			deltaY = 0;
			checkTileCollision();
		}
		
		if (colmask == 2 && directionX.equals("left")) {
			deltaX = 0;
			deltaY = -8;
			checkTileCollision();
		}
		if (colmask == 2 && directionY.equals("down")) {
			deltaX = 8;
			deltaY = 0;
			checkTileCollision();
		}
		
//		if (
//				//corners still dont work entirely (see tree corners)
//				(((collisiont1 & 2)&15) ==0 && ((collisiont2 & 2)&15) ==0 &&
//				((collisiont1 & 1)&15) ==0 && ((collisiont2 & 1)&15) ==0) ||
//				
//				(((collisiont3 & 2)&15) ==0 && ((collisiont4 & 2)&15) == 0 && 
//				((collisiont4 & 1)&15) == 0 && ((collisiont4 & 1)&15) == 0) ||
//				
//				(((collisiont1 & 4)&15) == 4 && ((collisiont2 & 4)&15) == 4 &&
//				((collisiont3 & 4)&15) == 4 && ((collisiont4 & 4)&15) == 4)) {
////			stageForRedraw();
//		}
//		
		if ((colmask & 15) != 0) {
			return true;
		}
		return false;
	}
	
	public void handleInput(InputController input) {
		
	} 
	
//	public boolean checkCollisionsWithEntity(Entity e) {
//		int leftEdge = this.x + deltaX;
//		int rightEdge = this.x + this.width + deltaX;
//		int upperEdge = this.y + this.height*3/4 + deltaY;
//		int lowerEdge = this.y + this.height + deltaY;
//		boolean collision = false;
//		if ((rightEdge >= e.x + e.deltaX)) {
//			System.out.println(this.toString() + " is col w/ " + e.toString());
//			collision = true;
//		}
//		return collision;
//	}

	public void interact() {
		if (this.text != null) {
			SimpleDialogMenu.createDialogBox(state,this.text);
		} else {
			
		}
	}
	
	@Override
	public void act() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
	
	public void setDirectionDuringAct(Entity e2) {
		if (e2.y - this.y > 0) {
			//set down
			this.directionY = "down";
		}
		if (e2.y - this.y < 0) {
			//set up
			this.directionY = "up";
		}
		if (e2.x - this.x > 0) {
			//set r
			this.directionX = "right";
		}
		if (e2.x - this.x < 0) {
			//set l
			this.directionX = "left";
		}
	}
	
	public boolean checkCollisionWithTolerance(Entity e2, int tol) {
		if (this.x -tol < e2.x + e2.width && this.x + tol +this.width > e2.x &&
			    this.y +tol + this.height*1/4 < e2.y + e2.height && this.y +tol+ this.height > e2.y + e2.height*3/4) {
//			System.out.println("Detected a collide");
			return true;
	}
	return false;
	}
	
	public boolean checkCollision(Entity e2) {
		if (e2 instanceof DoorEntity || e2 instanceof EnemySpawnEntity) {
			return false;
		}
		if (this.x + deltaX < e2.x + e2.width && this.x + deltaX +this.width > e2.x &&
			this.y +deltaY + this.height*3/4 < e2.y + e2.height && this.y +deltaY+ this.height > e2.y + e2.height*3/4) {
			return true;
		}
		return false;
	}

	public SpritesheetCoordinates getSpriteCoordinates() {
		return spriteCoordinates;
	}

	public void setSpriteCoordinates(SpritesheetCoordinates spriteCoordinates) {
		this.spriteCoordinates = spriteCoordinates;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	public void scaleUp(int i) {
		// TODO Auto-generated method stub
		this.x *= 4;
		this.y *= 4;
		this.width *= 4;
		this.height *= 4;
	}

	public void applyMovementData(int x, int y, String actionTaken, String dirX, String dirY) {
		this.targetX = x;
		this.targetY = y;
		atTargetPoint = false;
//		this.actionTaken = actionTaken;
//		this.directionX = dirX;
//		this.directionY = dirY;
	}

	public void resetMovement() {
		// TODO Auto-generated method stub
		targetX = -1;
		targetY = -1;
	}

	public void setDeltaXY(int x,int y) {
		// TODO Auto-generated method stub
		deltaX = x;
		deltaY = y;
	}

	public String getTextureName() {
		// TODO Auto-generated method stub
		String[] name = texture.split(".");
		return name[0];
	}

	public String getAppearFlag() {
		// TODO Auto-generated method stub
		return appearFlag;
	}
	
	public String getDisappearFlag() {
		return disappearFlag;
	}

	public void increaseSize(int i, int j) {
		// TODO Auto-generated method stub
		width += i;
		height += j;
		if (width < 1) {
			width = 1;
		}
		if (height < 1) {
			height = 1;
		}
	}

	public String getInfoForTool() {
		// TODO Auto-generated method stub
		return text;
	}

}
