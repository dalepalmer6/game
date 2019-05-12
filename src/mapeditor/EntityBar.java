package mapeditor;

import java.awt.Point;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import canvas.Clickable;
import canvas.Hoverable;
import canvas.MainWindow;
import gamestate.Entity;
import gamestate.Pose;
import gamestate.TileMetadata;
import mapeditor.tools.PremadeTileObjectTool;
import mapeditor.tools.SingleEntity;
import mapeditor.tools.SingleTile;
import menu.LeftClickableItem;
import menu.MenuItem;
import menu.StartupNew;
import tiles.MultiInstanceTile;
import tiles.PremadeTileObject;
import tiles.SingleInstanceTile;

public class EntityBar extends MenuItem implements Hoverable, Clickable{
	private int x;
	private int y;
	private int width;
	private int height;
	private int viewX = 0;
	private int viewY = 0;
	private int widthInTiles;
	private int heightInTiles;
	private int hoveredTileId;
	private int MAX_HEIGHT_IN_TILES = 16;
	private int MAX_WIDTH_IN_TILES = 32;
	private TileHashMap tileMap;
	private MapPreview mapPreview;
	private TileBar tileBar;
	private StartupNew state;
	private int TILE_SIZE = 32;
	private ArrayList<Entity> currentEntitiesOfInterest;
	private Entity hoveredEntity;
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	public Point getMouseCoordinates() {
		int x = Mouse.getX();
		int y = Display.getHeight() - Mouse.getY();
		return new Point(x, y);
	}
	
	public EntityBar(int maxWidthTiles, int maxHeightTiles, MapPreview mapPreview,TileBar tilebar, StartupNew state) {
		super("",0,0,state);
		//set location
		this.tileBar = tilebar;
		this.x = mapPreview.getRightEdge() + 3*TILE_SIZE;
		this.y = mapPreview.getY() + 3*TILE_SIZE;
//		state.getTextureAtlas().setRectByName("img/entities.png");
		this.state= state;
		this.mapPreview = mapPreview;
		this.widthInTiles = maxWidthTiles;
		this.heightInTiles = maxHeightTiles;
		this.width = widthInTiles * TILE_SIZE;
		this.height = heightInTiles * TILE_SIZE;
	}
	
	public Point getTilePosition() {
		double tileX = Math.floor((-this.x + state.getMainWindow().getMouseCoordinates().getX())/mapPreview.getTileSize());
		double tileY = Math.floor((-this.y + state.getMainWindow().getMouseCoordinates().getY())/mapPreview.getTileSize());
		if (tileX > this.widthInTiles || tileX < 0) {
			tileX = -1;
		}
		if (tileY > this.heightInTiles || tileY < 0) {
			tileY = -1;
		}
		Point xy = new Point();
		xy.setLocation(tileX,tileY);
		return xy;
	}
	
	public void setHoveredEntity() {
		Point xy = getTilePosition();
		try {
			int index = (xy.y) * widthInTiles + xy.x;
			this.hoveredEntity = currentEntitiesOfInterest.get(index);
		} catch(NullPointerException e) {
			System.err.println("Tile Index: " +  " out of bounds, tile does not exist.");
			e.printStackTrace();
		} catch(IndexOutOfBoundsException e) {
			
		} 
		
	}
	
	public Entity getHoveredEntity() {
		return this.hoveredEntity;
	}
	
	public void drawCoordinates() {
		Point xy = getTilePosition();
	}
	
	public void drawGrid(MainWindow m) {
		for (int i = this.x; i <= this.x + width; i+= TILE_SIZE) {
			//for every row
			for (int j = this.y; j <= this.y + height; j+= TILE_SIZE) {
				//for every column
				m.renderTile(this.x,j,width,1,1,1,1,1);
			}
			m.renderTile(i,this.y,1,height,1,1,1,1);
			
		}
	}
	
	public void getTilesOfInterest() {
		MainWindow m = state.getMainWindow();
		m.setTexture("img\\entities.png");
		this.MAX_HEIGHT_IN_TILES = state.getTextureAtlas().getCurrentRectangle().height / 16;
		this.MAX_WIDTH_IN_TILES =  state.getTextureAtlas().getCurrentRectangle().width / 16;
//		Texture t = m.getTexture();
		int numTilesX = state.getTextureAtlas().getCurrentRectangle().width / 16;
		ArrayList<ArrayList<Entity>> tiles = new ArrayList<ArrayList<Entity>>();
		//start from viewX,viewY as i, and increment by 1 each time while i < widthInTiles
		ArrayList<Entity> row = new ArrayList<Entity>();
		currentEntitiesOfInterest = mapPreview.getMap().getEntities();
//		currentEntitiesOfInterest = row;
	}
	
	public Entity getCurrentTileFromInterestList(int x) {
		return currentEntitiesOfInterest.get(x);
	}
	
	public void drawTiles() {
		// read the tileMap and for each tile, draw to the next open spot
		MainWindow m = state.getMainWindow();
//		Tile.initDrawTiles(m);
		int i = 0;
		for (Entity e : currentEntitiesOfInterest) {
			if (e != null) {
				m.setTexture("img\\" + e.getTexture());
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
//				GL11.glEnable(GL11.GL_TEXTURE_2D);
				int drawingX =  ((i++) * TILE_SIZE) % width;
				int drawingY =  (i/widthInTiles) * TILE_SIZE;
				Pose pose = e.getSpriteCoordinates().getPose("idle","","down");
				TileMetadata tm = pose.getStateByNum(0);
				m.renderTile(drawingX + this.x,drawingY + this.y,mapPreview.getTileSize(),mapPreview.getTileSize(), tm.getX(),tm.getY(),tm.getWidth(),tm.getHeight());
			}
		}
//		for (int i = 0; i < this.widthInTiles; i++) {
//			for (int j = 0; j < this.heightInTiles; j++) {
//				int drawingX =  (i) * TILE_SIZE;
//				int drawingY =  (j) * TILE_SIZE;
//				Entity e = getCurrentTileFromInterestList(i,j);
//				if (e != null) {
//					Pose pose = e.getSpriteCoordinates().getPose("idle","","down");
//					TileMetadata tm = pose.getStateByNum(0);
//					m.renderTile(drawingX + this.x,drawingY + this.y,mapPreview.getTileSize(),mapPreview.getTileSize(), tm.getX(),tm.getY(),tm.getWidth(),tm.getHeight());
//				}
//			}
//		}
	}
	
	@Override
	public void draw(MainWindow m) {
		getTilesOfInterest();
		drawTiles();
//		drawGrid(m);
//		drawCoordinates();
	}

	@Override
	public boolean hovered(double mousex, double mousey) {
		if (mousex >= this.x && mousex <= this.x + this.width && mousey >= this.y && mousey <= this.y + this.height) {
			return true;
		}
		return false;
	}

	@Override
	public void hoveredAction() {
		// TODO Auto-generated method stub
		setHoveredEntity();
	}

	@Override
	public void unhoveredAction() {
		// TODO Auto-generated method stub
		
	}

	public void updateView(String movement) {
		if (movement.equals("L")) {
			if (viewX != 0) {
				this.viewX--;
			} else {
				System.err.println("View is already at beginning.");
			}
			
		} else if (movement.equals("R")) {
			if (widthInTiles + viewX < MAX_WIDTH_IN_TILES) {
				//expand the map in the right
				System.out.println(viewX);
				this.viewX++;
				
			} else {
				System.err.println("View is already at the end of the file");
			}
		} else if (movement.equals("U")) {
			if (viewY != 0) {
				this.viewY--;
				
			} else {
				System.err.println("View is already at the end of the file");
			}
		} else if (movement.equals("D")) {
			if (heightInTiles + viewY >= MAX_HEIGHT_IN_TILES) {
				this.viewY++;
				
			} else {
				System.err.println("View is already at the end of the file");
			}
		}
	}
	
	@Override
	public String execute() {
		// TODO Auto-generated method stub
		Entity e = hoveredEntity;
		mapPreview.setTool(new SingleEntity(e,state));
		return null;
	}
}
