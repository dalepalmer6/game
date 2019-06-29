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
	protected double x;
	protected double y;
	private boolean doNotMove;
	protected int boundingBoxHeight;
	protected double deltaX;
	protected double deltaY;
	protected double xOnScreen;
	protected double yOnScreen;
	protected String texture;
	protected int width;
	protected int height;
	protected double stepSizeX = 8;
	protected double stepSizeY = 8;
	protected StartupNew state;
	protected ArrayList<Entity> interactables;
	private long delta;
	protected String name;
	private boolean needsRemove;
	protected String appearFlag = " ";
	protected String disappearFlag = " ";
	private double moveSpeedThisFrame;
	protected double angleDirection;
	protected double targetX = -1;
	protected double targetY = -1;
	protected boolean atTargetPoint = true;
	protected boolean ignoreCollisions;
	protected boolean behind;
	protected boolean lastBehind;
	protected long timer;
	protected boolean forceAllowMovementY;
	boolean collided;
	private int movementPattern = 0;
	private String savedDirX;
	private String savedDirY;
	
	public void setIgnoreCollisions(boolean b) {
		// TODO Auto-generated method stub
		ignoreCollisions = b;
	}
	
	public String getDirectionX() {
		return directionX;
	}
	
	public String getDirectionY() {
		return directionY;
	}
	
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
	
	public Entity createCopy(double newX, double newY, int width, int height, String name) {
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
	
	public void setCoordinates(double x, double y) {
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
	
	public void fillMovementData(double x, double y) {
		
	}
	
	public void setDeltaX(double dx) {
		// TODO Auto-generated method stub
		deltaX = dx;
	}
	
	public void setDeltaY(double dy) {
		deltaY = dy;
	}
	
	public void setXY(double d, double e) {
		this.x = d;
		this.y = e;
	}
	
	public void setName(String n) {
		this.name = n;
	}
	
//	public void move() {
//		if (targetX != -1 && targetY != -1) {
//			if (x - targetX < 0) {
//				deltaX = stepSize;
//				if (Math.abs(targetX - x - deltaX) <= stepSize) {
//					deltaX = x-targetX;
//				}
//				actionTaken="walking";
//				directionX = "right";
//			} else if (x - targetX > 0){
//				deltaX = -stepSize;
//				if (Math.abs(x - targetX - deltaX) <= stepSize) {
//					deltaX = x-targetX;
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
//				if (Math.abs(targetY - y - deltaY) <= stepSize) {
//					deltaY = targetY - y;
//				}
//				
//				actionTaken="walking";
//				directionY = "down";
//			} else if (y - targetY > 0){
//				deltaY = -stepSize;
//				if (Math.abs(y - targetY - deltaY) <= stepSize) {
//					deltaY = y - targetY;
//				}
//				
//				actionTaken="walking";
//				directionY = "up";
//			} else {
//				deltaY = 0;
////				directionY = "";
//				actionTaken = "idle";
//			}
//			if (Math.abs(deltaX - 0) <= 0.001 && Math.abs(deltaY - 0) <= 0.001) {
//				atTargetPoint = true;
//			}
//		} 
//			checkCollisions();
//		if (!ignoreCollisions) {
//			checkEntityCollisions();
//		}
//		if (!(Math.abs(deltaX - 0) < 0.001) && !(Math.abs(deltaY - 0) < 0.001)) {
//			boolean xNeg = false;
//			boolean yNeg = false;
//			if (deltaX < 0) {
//				xNeg = true;
//			}
//			if (deltaY < 0) {
//				yNeg = true;
//			}
//			deltaX = Math.sqrt(0.5*deltaX*deltaX);
//			deltaY = deltaX;
//			if (xNeg) {
//				deltaX *= -1;
//			}
//			if (yNeg) {
//				deltaY *= -1;
//			}
//		}
//		x += deltaX;
//		y += deltaY;
//	}
	
	public void move() {
		boolean ignoreDiagX = false;
		boolean ignoreDiagY = false;
		if (targetX != -1 && targetY != -1) {
			if (Math.abs(x-targetX) < 0.0001) {
				deltaX = 0;
//				directionX = "";
				actionTaken="idle";
			}
			else if (x < targetX) {
				deltaX = stepSizeX;
				if (x + deltaX > targetX) {
					ignoreDiagX = true;
					deltaX = Math.abs(targetX - x);
				}
				actionTaken="walking";
				directionX = "right";
			} else if (x > targetX){
				deltaX = -stepSizeX;
				if (x + deltaX < targetX) {
					ignoreDiagX = true;
					deltaX = Math.abs(targetX - x)*-1;
				}
				
				actionTaken="walking";
				directionX = "left";
			} 
			
			
			if (Math.abs(y-targetY) < 0.0001) {
				deltaY = 0;
				actionTaken="idle";
			}
			else if (y < targetY) {
				deltaY = stepSizeY;
				if (y + deltaY > targetY) {
					ignoreDiagY = true;
					deltaY = Math.abs(y - targetY);
				}
				
				actionTaken="walking";
				directionY = "down";
			} else if (y > targetY) {
				deltaY = -stepSizeY;
				if (y + deltaY < targetY) {
					ignoreDiagY = true;
					deltaY = Math.abs(y - targetY)*-1;
				}
				actionTaken="walking";
				directionY = "up";
			}
			
			if (Math.abs(deltaX-0) <= 0.0001 && Math.abs(deltaY-0) <= 0.0001) {
				setAtTargetPoint();
			}
		} 
		
			checkCollisions();
		if (!ignoreCollisions) {
			checkEntityCollisions();
		}
		if (!(Math.abs(deltaX - 0) < 0.001) && !(Math.abs(deltaY - 0) < 0.001)) {
			boolean xNeg = false;
			boolean yNeg = false;
			if (deltaX < 0) {
				xNeg = true;
			}
			if (deltaY < 0) {
				yNeg = true;
			}
			if (!ignoreDiagX ) {
				deltaX = Math.sqrt(0.5*deltaX*deltaX);
				
			}
			if (!ignoreDiagY) {
				deltaY = Math.sqrt(0.5*deltaY*deltaY);
			}
			
			if (xNeg && !ignoreDiagX) {
				deltaX *= -1;
			}
			if (yNeg && !ignoreDiagY) {
				deltaY *= -1;
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
	
	public void setAtTargetPoint(boolean b) {
		atTargetPoint = false;
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
	
	public void setPlayerAsTarget() {
		Player player = state.getGameState().getPlayer();
		targetX = player.getX();
		targetY = player.getY();
	}
	
	public void update(GameState gs) {
		forceAllowMovementY = false;
		timer++;
		
		if (movementPattern == 1) {
			//sine movement 
			setPlayerAsTarget();
//			if (timer % 15 == 0) {
				stepSizeX = 8*Math.sin(timer * Math.PI/100);
				stepSizeY = 8;
//			}
		}
		
		if (movementPattern == 2) {
			if (timer % 120 == 0) {
				//implement different movement patterns using this similar style
				//random pattern, will try to move in any open direction every 2 seconds randomly
				if (Math.random() < 0.3) {
					if (Math.random() < 0.5) {
						targetX = x - 8*6;
					} else {
						targetX = x + 8*6;
					}
				}
				if (Math.random() < 0.3) {
					if (Math.random() < 0.5) {
						targetY = y - 8*6;
					} else {
						targetY = y + 8*6;
					}
				}
			}
		}
		
		moveSpeedThisFrame = (int) (stepSizeX * state.getGameState().getDeltaTime());
		lastBehind = behind;
		behind = false;
		
		move();
//		determinePositionWithCamera();
		updateFrameTicks();
	}
	
	public void determinePositionWithCamera() {
		xOnScreen = x - state.getGameState().getCamera().getX();
		yOnScreen = y - state.getGameState().getCamera().getY();
		if (xOnScreen > state.getMainWindow().getScreenWidth() + 768|| yOnScreen > state.getMainWindow().getScreenHeight() + 768
		 || xOnScreen < -768 || yOnScreen < -768) {
			setToRemove(true);
		}
	}
	
	public void updateFrameTicks() {
		tickCount += ticksPerFrame;
	}
	
	public double getXOnScreen() {
		return xOnScreen;
	}
	
	public double getYOnScreen() {
		return yOnScreen;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
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
	
	public Entity(String texture, double x, double y, int width, int height,StartupNew m,String name) {
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
		try {
			Pose pose = getSpriteCoordinates().getPose(actionTaken, directionX,directionY);
			int i = (int) tickCount % pose.getNumStates();
			TileMetadata tm = pose.getStateByNum(i);
			m.renderTile(xOnScreen, yOnScreen, width, height, tm.getX(), tm.getY(), tm.getWidth(), tm.getHeight(),tm.getFlipState());
		} catch (NullPointerException e) {
			actionTaken = "idle";
			directionX = "";
			directionY = "down";
			drawEntity(m);
		}
	}
	
	public void saveDirection() {
		savedDirX = directionX;
		savedDirY = directionY;
	}
	
	public void restoreSavedDirection() {
		directionX = savedDirX;
		directionY = savedDirY;
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
	
	public void drawIfBehind(MainWindow m) {
		if (behind) {
			draw(m);
		}
	}
	
	public void drawIfFront(MainWindow m) {
		if (!behind) {
			draw(m);
		}
	}
	
	public static void initDrawEntity(MainWindow m, String texture) {
		m.setTexture("img\\" + texture);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
//		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public void stageForRedraw() {
		state.getGameState().addToRedrawing(new RedrawObject(this,state));
	}
	
	public boolean checkCollision(int collision, int x, int y, TileInstance tbg, TileInstance tfg) {
		return checkCollision(collision,x,y,tbg,tfg,false);
	}
	
	public boolean checkCollision(int collision, int x, int y, TileInstance tbg, TileInstance tfg, boolean isBottom) { 
//		if (((collision & 1) & 15) == 1) {
//			deltaX = 0;
//			deltaY = 0;
//		}
		if (isBottom) {
			if (collision == 1 && lastBehind) {
				behind = true;
			} else {
				if (collision == 2) {
					behind = behind || true;
				} else {
					behind = behind || false;
				}
			}
		}
		
		collided = false;
		if (!ignoreCollisions) {
			int val = ((collision & 13) & 15);
			switch(val) {
				case 1: 
					deltaX = 0; 
					if (!forceAllowMovementY) {
						deltaY = 0; 
						collided = true;
					} else {
						collided = false;
					}
					break;
				case 4: break;
				case 8: forceAllowMovementY = true;
						break;
				case 9: deltaX=0;
						collided = false;
						forceAllowMovementY = true;
						break;
				case 5: break;
			}
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
		boolean bl = checkCollision(collisiont3,leftEdge,lowerEdge,t3BG,t3FG,true);
		boolean br = checkCollision(collisiont4,rightEdge,upperEdge,t4BG,t4FG,true);
		
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
			deltaY = stepSizeY;
			checkTileCollision();
		}
		if (colmask == 8 && directionY.equals("up")) {
			deltaX = stepSizeX;
			deltaY = 0;
			checkTileCollision();
		}
		
		if (colmask == 1 && directionX.equals("right")) {
			deltaX = 0;
			deltaY = -stepSizeY;
			checkTileCollision();
		}
		if (colmask == 1 && directionY.equals("down")) {
			deltaX = stepSizeX;
			deltaY = 0;
			checkTileCollision();
		}
		
		if (colmask == 4 && directionX.equals("right")) {
			deltaX = 0;
			deltaY = stepSizeY;
			checkTileCollision();
		}
		if (colmask == 4 && directionY.equals("up")) {
			deltaX = -stepSizeX;
			deltaY = 0;
			checkTileCollision();
		}
		
		if (colmask == 2 && directionX.equals("left")) {
			deltaX = 0;
			deltaY = -stepSizeY;
			checkTileCollision();
		}
		if (colmask == 2 && directionY.equals("down")) {
			deltaX = -stepSizeX;
			deltaY = 0;
			checkTileCollision();
		}
		
		if ((colmask & 15) != 0) {
			return true;
		}
		return false;
	}
	
	public void handleInput(InputController input) {
		
	} 
	
	public String toString() {
		return name;
	}

	public void interact() {
		if (this.text != null) {
			SimpleDialogMenu.createDialogBox(state,this.text,this.name);
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

	public void applyMovementData(double x, double y, String actionTaken, String dirX, String dirY) {
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

	public void setXYOnScreen(int x2, int y2) {
		// TODO Auto-generated method stub
		xOnScreen = x2;
		yOnScreen = y2;
	}

	public void moveOnScreen(int arg0, int arg1) {
		xOnScreen += arg0;
		yOnScreen += arg1;
	}

	public void setNewParams(double x, double y, int w, int h, String name, String texture, String appFlag, String disFlag) {
		// TODO Auto-generated method stub
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
		this.texture = texture;
		this.name = name;
		this.appearFlag = appFlag;
		this.disappearFlag = disFlag;
	}
	
}