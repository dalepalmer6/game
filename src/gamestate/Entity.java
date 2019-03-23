package gamestate;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import canvas.Drawable;
import canvas.MainWindow;
import mapeditor.Tile;
import menu.StartupNew;

public class Entity implements Drawable,EntityInterface {
	protected int x;
	protected int y;
	protected int deltaX;
	protected int deltaY;
	protected int dx=0;
	protected int dy=0;
	protected int dw;
	protected int dh;
	protected int xOnScreen;
	protected int yOnScreen;
	private String texture;
	protected int width;
	protected int height;
	protected int stepSize = 4;
	protected StartupNew state;
	protected ArrayList<Entity> interactables;
	
	public void move(int x, int y) {
		
	}
	
	public void update(GameState gs) {
		xOnScreen = x - gs.getCamera().getX();
		yOnScreen = y - gs.getCamera().getY();
	}
	
	public Entity(String texture, int x, int y, int width, int height,int dw, int dh,StartupNew m) {
		this.texture = texture;
		this.x = x;
		this.y = y;
		this.dw = dw;
		this.dh = dh;
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
	
	public void drawEntity(MainWindow m) {
		initDrawEntity(m,texture);
		m.renderTile(xOnScreen, yOnScreen, width, height, dx, dy, dw, dh);
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
		int leftEdge = (this.x + deltaX)/64;
		int rightEdge = (this.x + this.width + deltaX)/64;
		int upperEdge = (this.y + this.height*3/4 + deltaY)/64;
		int lowerEdge = (this.y + this.height + deltaY)/64;
		Tile t1 = state.getGameState().getMap().getTile(leftEdge, upperEdge);
		Tile t2 = state.getGameState().getMap().getTile(rightEdge, upperEdge);
		Tile t3 = state.getGameState().getMap().getTile(rightEdge, lowerEdge);
		Tile t4 = state.getGameState().getMap().getTile(leftEdge, lowerEdge);
		if (t1.getId() != 7) {
			System.out.println("Upper left collision");
			
			deltaX = 0;
			deltaY = 0;
		}
		if (t2.getId() != 7) {
			System.out.println("Upper right collision");
			deltaX = 0;
			deltaY = 0;
		}
		if (t3.getId() != 7) {
			System.out.println("Lower right collision");
			deltaX = 0;
			deltaY = 0;
		}
		if (t4.getId() != 7) {
			System.out.println("Lower left collision");
			deltaX = 0;
			deltaY = 0;
		}
		
	}
	
	@Override
	public void move() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void act() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
	
	public boolean checkCollision(Entity e2, int tol) {
			if (this.x < e2.x + e2.width && this.x+this.width > e2.x &&
				    this.y + this.height*3/4 < e2.y + e2.height && this.y + this.height > e2.y + e2.height*3/4) {
			return true;
		}
		return false;
	}
}
