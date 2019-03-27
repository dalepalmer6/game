package gamestate;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import canvas.Drawable;
import canvas.MainWindow;
import global.InputController;
import mapeditor.Tile;
import menu.DrawableObject;
import menu.StartupNew;

public class Entity extends DrawableObject implements Drawable,EntityInterface {
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
	protected int boundingBoxHeight;
	protected int deltaX;
	protected int deltaY;
	protected int xOnScreen;
	protected int yOnScreen;
	private String texture;
	protected int width;
	protected int height;
	protected int stepSize = 4;
	protected StartupNew state;
	protected ArrayList<Entity> interactables;
	private long delta;
	
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
	
	public void move() {
		checkCollisions();
		x += deltaX;
		y += deltaY;
	}
	
	public void update(GameState gs) {
		xOnScreen = x - gs.getCamera().getX();
		yOnScreen = y - gs.getCamera().getY();
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
	
	public int getHeight() {
		return height;
	}
	
	public Entity(String texture, int x, int y, int width, int height,StartupNew m) {
//		this.createdAt = (System.nanoTime());
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
	}

	public String interactText() {
		return null;
	}
	
	public void addToInteractables(Entity e) {
		if (!interactables.contains(e)) {
			this.interactables.add(e);
		}
	}
	
	public void removeFromInteractables(Entity e) {
		this.interactables.remove(e);
	}
	
//	public List<String> setStates() {
//		if (entityState.contains(""))
//	}
	
	public void drawEntity(MainWindow m) {
		initDrawEntity(m,texture);
		Pose pose = spriteCoordinates.getPose(actionTaken, directionX,directionY);
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
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	
	
	public void checkCollisions() {
		//adding 1 to each to account for the offset of the map renderer
		int ts = state.getGameState().getMapRenderer().getTileSize();
		int leftEdge = 1+(this.x + deltaX)/ts;
		int rightEdge = 1+(this.x + this.width + deltaX)/ts;
		int upperEdge = 1+(this.y + this.height*3/4 + deltaY)/ts;
		int lowerEdge = 1+(this.y + this.height + deltaY)/ts;
		
		int leftEdgeTest = (this.x + deltaX)%ts / 16;
		int rightEdgeTest = (this.x + this.width + deltaX)%ts / 16;
		int upperEdgeTest = (this.y + this.height*3/4 + deltaY)%ts / 16;
		int lowerEdgeTest = (this.y + this.height + deltaY)%ts / 16;
		
		Tile t1 = state.getGameState().getMap().getTile(leftEdge, upperEdge);
		Tile t2 = state.getGameState().getMap().getTile(rightEdge, upperEdge);
		Tile t3 = state.getGameState().getMap().getTile(rightEdge, lowerEdge);
		Tile t4 = state.getGameState().getMap().getTile(leftEdge, lowerEdge);
		
		String collisionStatet1 = t1.getCollisionInfoAtIndex(leftEdgeTest,upperEdgeTest);
		String collisionStatet2 = t2.getCollisionInfoAtIndex(rightEdgeTest,upperEdgeTest);
		String collisionStatet3 = t3.getCollisionInfoAtIndex(rightEdgeTest,lowerEdgeTest);
		String collisionStatet4 = t4.getCollisionInfoAtIndex(leftEdgeTest,lowerEdgeTest);
		if (collisionStatet1.equals("STOP")) {
			System.out.println("Upper left collision");
			deltaX = 0;
			deltaY = 0;
		}
		if (collisionStatet2.equals("STOP")) {
			System.out.println("Upper right collision");
			deltaX = 0;
			deltaY = 0;
		}
		if (collisionStatet3.equals("STOP")) {
			System.out.println("Lower right collision");
			deltaX = 0;
			deltaY = 0;
		}
		if (collisionStatet4.equals("STOP")) {
			System.out.println("Lower left collision");
			deltaX = 0;
			deltaY = 0;
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
}
