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
	protected int deltaX;
	protected int deltaY;
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
	
	public void setToRemove() {
		this.needsRemove = true;
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
	
	public StartupNew getState() {
		return state;
	}
	
	public void setSpriteCoords(SpritesheetCoordinates sc) {
		spriteCoordinates = sc;
	}
	
	public Entity createCopy(int newX, int newY, int width, int height) {
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
	
	public void move() {
		checkCollisions();
		x += deltaX;
		y += deltaY;
	}
	
	public void update(GameState gs) {
		move();
		xOnScreen = x - gs.getCamera().getX();
		yOnScreen = y - gs.getCamera().getY();
		if (xOnScreen > state.getMainWindow().getScreenWidth() + 1000|| yOnScreen > state.getMainWindow().getScreenHeight() + 1000
		 || xOnScreen < -1000 || yOnScreen < -1000) {
			setToRemove();
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
//		scaleUp(4);
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
	
	public void draw(MainWindow m) {
		// TODO Auto-generated method stub
		drawEntity(m);
	}
	
	public static void initDrawEntity(MainWindow m, String texture) {
		m.setTexture("img/" + texture);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
//		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public void stageForRedraw() {
		state.getGameState().addToRedrawing(new RedrawObject(this));
	}
	
	public void checkCollision(int collision, int x, int y, TileInstance tbg, TileInstance tfg) {
		if ((collision & 1) == 1) {
			deltaX = 0;
			deltaY = 0;
		}
		if ((collision & 2) == 0) {
			System.out.println("TEST");
			
			//redraw the tile for overlay
//			stageForRedraw(x+1,y+1,tbg);
//			stageForRedraw(x+1,y+1,tfg);
		}
	}
	
//	public String collisionLogicalOr(String c1, String c2) {
//		
//	}
	
	public void checkCollisions() {
		//collisions are cumulative over all layers
		int ts = state.getGameState().getMapRenderer().getTileSize();
		int leftEdge = (this.x + deltaX)/ts;
		int rightEdge = (this.x + this.width + deltaX)/ts;
		int upperEdge = (this.y + this.height*3/4 + deltaY)/ts;
		int lowerEdge = (this.y + this.height + deltaY)/ts;
		
		int leftEdgeTest = (this.x + deltaX)%ts / (ts/4);
		int rightEdgeTest = (this.x + this.width + deltaX)%ts / (ts/4);
		int upperEdgeTest = (this.y + this.height*3/4 + deltaY)%ts / (ts/4);
		int lowerEdgeTest = (this.y + this.height + deltaY)%ts / (ts/4);
		
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
		
		if (t1BG == state.tileMap.getTile(0).getInstance(0)) {
			collisiont1 = collisionStatet1FG;
		} else if (t1FG == state.tileMap.getTile(0).getInstance(0)){
			collisiont1 = collisionStatet1BG;
		}
		
		if (t2BG == state.tileMap.getTile(0).getInstance(0)) {
			collisiont2 = collisionStatet2FG;
		} else if (t2FG == state.tileMap.getTile(0).getInstance(0)){
			collisiont2 = collisionStatet2BG;
		}
		
		if (t3BG == state.tileMap.getTile(0).getInstance(0)) {
			collisiont3 = collisionStatet3FG;
		} else if (t3FG == state.tileMap.getTile(0).getInstance(0)){
			collisiont3 = collisionStatet3BG;
		}
		
		if (t4BG == state.tileMap.getTile(0).getInstance(0)) {
			collisiont4 = collisionStatet4FG;
		} else if (t4FG == state.tileMap.getTile(0).getInstance(0)){
			collisiont4 = collisionStatet4BG;
		}

		checkCollision(collisiont1,leftEdge,upperEdge,t1BG,t1FG);
		checkCollision(collisiont2,rightEdge,upperEdge,t2BG,t2FG);
		checkCollision(collisiont3,leftEdge,lowerEdge,t3BG,t3FG);
		checkCollision(collisiont4,rightEdge,upperEdge,t4BG,t4FG);
		
		if ((collisiont1 & 2) == 0 && (collisiont2 & 2) == 0 && (collisiont3 & 2) ==0 && (collisiont4 & 2) == 0) {
			stageForRedraw();
		}
			
	}
	
	public void handleInput(InputController input) {
		
	} 
	
	public boolean checkCollisionsWithEntity(Entity e) {
		int leftEdge = this.x + deltaX;
		int rightEdge = this.x + this.width + deltaX;
		int upperEdge = this.y + this.height*3/4 + deltaY;
		int lowerEdge = this.y + this.height + deltaY;
		boolean collision = false;
		if ((rightEdge >= e.x + e.deltaX)) {
			System.out.println(this.toString() + " is col w/ " + e.toString());
			collision = true;
		}
		return collision;
	}

	public void interact() {
		SimpleDialogMenu.createDialogBox(state,this.text);
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
			System.out.println("Detected a collide");
			return true;
	}
	return false;
	}
	
	public boolean checkCollision(Entity e2) {
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

}
