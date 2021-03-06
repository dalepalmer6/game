package menu.mapeditmenu.mappreview;

import java.awt.Point;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import gamestate.entities.Entity;
import menu.MenuItem;
import menu.mapeditmenu.tools.MapTool;
import system.MainWindow;
import system.SystemState;
import system.controller.InputController;
import system.interfaces.Clickable;
import system.interfaces.Controllable;
import system.interfaces.Drawable;
import system.interfaces.Hoverable;
import system.map.Map;
import system.map.Tile;
import system.map.TileHashMap;
import system.sprites.Pose;
import system.sprites.TileMetadata;

public class MapPreview extends MenuItem implements Controllable, Drawable, Hoverable, Clickable {
	private Map map;
	private ArrayList<ArrayList<Integer>> areaOfInterest;
	private int widthInTiles;
	private int heightInTiles;
	public int viewX=0;
	public int viewY=0;
	private MapTool tileTool;
	private int TILE_SIZE = 32;
	private int instance = 0;
	private TileHashMap tileMap;
	private boolean holdableState = true;
	private boolean drawGrid = true;
	private Entity addedEntity;
	
	public void toggleDrawGrid() {
		drawGrid = !drawGrid;
	}
	
	public void setAddedEntity(Entity e) {
		addedEntity = e;
	}
	
	public void updateAnim() {
		if (addedEntity != null) {
			map.getEntities().add(addedEntity);
		}
		addedEntity = null;
	}
	
	public int getViewX() {
		return viewX;
	}
	
	public int getViewY() {
		return viewY;
	}
	
	public void setView(int x, int y) {
		viewX = x;
		viewY = y;
	}
	
	public void setMap(Map m) {
		this.map = m;
	}
	
	public Map getMap() {
		return map;
	}
	
	public double getRightEdge() {
		return x + widthInTiles * TILE_SIZE;
	}
	
	public double getLowerEdge() {
		return y + heightInTiles * TILE_SIZE;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getTileSize() {
		return TILE_SIZE;
	}
	
	public void setTool(MapTool t) {
		this.tileTool = t;
	}
	
	public MapTool getTool() {
		return tileTool;
	}
	
	public void getAreaOfInterest() {
		//viewX,viewY
		ArrayList<ArrayList<Integer>> rows = new ArrayList<ArrayList<Integer>>();
		//start from viewX,viewY as i, and increment by 1 each time while i < widthInTiles
		ArrayList<Integer> row = new ArrayList<Integer>();
		for (int j = viewY; j < this.heightInTiles + viewY + 2; j++) {
			for (int i = viewX; i < widthInTiles + viewX + 2; i++) {
				int curId = map.getTileId(i, j);
				row.add(curId);
			}
			rows.add(row);
			row = new ArrayList<Integer>();
		}
		areaOfInterest = rows;
	}
	
	public void editMap(MapTool currentTool) {
		//on click, change the tile to the new tile_id
			Point xyMouse = getMouseCoordinates();
			int xMouse = (int) xyMouse.getX();
			int yMouse = (int) xyMouse.getY();
			Point xy = getTilePosition();
			int x = (int) xy.getX() + viewX;
			int y = (int) xy.getY() + viewY;
//			SystemState.out.println("Putting tile " + currentTileId + " at (" + x + "," + y + ")");
			if (currentTool == null) {
				return;
			}
			setHoldableState(true);
			currentTool.setMap(map);
			currentTool.doActionOnMap(x,y,xMouse,yMouse);
	}
	
	public void resetView() {
		viewX = 0;
		viewY = 0;
	}
	
	public void editMapRightClick(MapTool currentTool) {
		Point xyMouse = getMouseCoordinates();
		int xMouse = (int) xyMouse.getX();
		int yMouse = (int) xyMouse.getY();
		Point xy = getTilePosition();
		int x = (int) xy.getX() + viewX;
		int y = (int) xy.getY() + viewY;
		if (currentTool == null) {
			return;
		}
		setHoldableState(true);
		currentTool.setMap(map);
		currentTool.doActionOnMapRightClick(x,y,xMouse,yMouse);
	}
	
	public MapPreview(int ts, int x, int y, Map m, TileHashMap tm,SystemState state) {
		super("",0,0,state);
		this.map = m;
		this.TILE_SIZE = ts;
		//set at the default position
		this.x = x;
		this.y = y;
		this.widthInTiles = 30+2;
		this.heightInTiles = 20+2;
		targetY = y;
		this.width = (-2 + this.widthInTiles) * TILE_SIZE;
		this.height = (-2 + this.heightInTiles) * TILE_SIZE;
		this.tileMap = tm;
		this.tileTool = new MapTool(state);
	}
	
	//can toggle this off!
	public void drawGrid(MainWindow m) {
		m.setTexture("img\\line.png");
		for (int i = (int) this.x; i <= this.x + width ; i+= TILE_SIZE) {
			//for every row
			for (int j = (int) this.y; j <= this.y + height; j+= TILE_SIZE) {
				//for every column
				m.renderTile(this.x,j,width,1,1,1,1,1,false);
			}
			m.renderTile(i,this.y,1,height,1,1,1,1,false);
		}
	}

	public Point getMouseCoordinates() {
		//relative to the tool
		double x = Mouse.getX() - this.x;
		double y = Display.getHeight() - Mouse.getY() - this.y;
		return new Point((int)x, (int)y);
	}
	
	public Point getTilePosition() {
		double tileX = Math.floor((getMouseCoordinates().getX())/TILE_SIZE+1);
		double tileY = Math.floor((getMouseCoordinates().getY())/TILE_SIZE+1);
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
	
	public void drawTilesBG(MainWindow m) {
		Tile.initDrawTiles(m,map.getTileset());
		Tile tile;
		for (int i = 1; i < widthInTiles-1; i++) {
			//for every row
			for (int j = 1; j < heightInTiles-1; j++) {
				//for every column
				int val = areaOfInterest.get(j).get(i);
				tile = tileMap.getTile(val);
				int instance  = map.inspectSurroundings(i+viewX,j+viewY);
				if (val == 0) {
					continue;
				}
//				m.renderTile(x + (i-1)*TILE_SIZE, y + (j-1)*TILE_SIZE,TILE_SIZE,TILE_SIZE, tileMap.getTile(0).getInstance(0).getDx(),tileMap.getTile(0).getInstance(0).getDy(),tileMap.getTile(0).getInstance(0).getDw(),tileMap.getTile(0).getInstance(0).getDh());
				m.renderTiles(x + ((i-1)*TILE_SIZE),y + (j-1)*TILE_SIZE,TILE_SIZE,TILE_SIZE, tile.getDx(instance),tile.getDy(instance),tile.getDw(instance),tile.getDh(instance),false);
			}
		}
	}
	
	public void drawTilesFG(MainWindow m) {
		Tile.initDrawTiles(m,map.getTileset());
		Tile tile;
		for (int i = 1; i < widthInTiles-1; i++) {
			//for every row
			for (int j = 1; j < heightInTiles-1; j++) {
				//for every column
				int val = this.areaOfInterest.get(j).get(i);
				tile = tileMap.getTile(val);
				if (val == 0) {
					continue;
				}
				int instance  = map.inspectSurroundings(i+viewX,j+viewY);
//				m.renderTile(x + (i-1)*TILE_SIZE, y + (j-1)*TILE_SIZE,TILE_SIZE,TILE_SIZE, tileMap.getTile(0).getInstance(0).getDx(),tileMap.getTile(0).getInstance(0).getDy(),tileMap.getTile(0).getInstance(0).getDw(),tileMap.getTile(0).getInstance(0).getDh());
				m.renderTiles(x + ((i-1)*TILE_SIZE),y + (j-1)*TILE_SIZE,TILE_SIZE,TILE_SIZE, (float)tile.getDx(instance),(float)tile.getDy(instance),(float)tile.getDw(instance),(float)tile.getDh(instance),false);
			}
		}
}
	
	public void drawTiles(MainWindow m) {
			Tile.initDrawTiles(m,map.getTileset());
			Tile tile;
			for (int i = 1; i < widthInTiles-1; i++) {
				//for every row
				for (int j = 1; j < heightInTiles-1; j++) {
					//for every column
					int val = this.areaOfInterest.get(j).get(i);
					tile = tileMap.getTile(val);
					if (val == 0) {
						continue;
					}
					int instance  = map.inspectSurroundings(i+viewX,j+viewY);
//					m.renderTile(x + (i-1)*TILE_SIZE, y + (j-1)*TILE_SIZE,TILE_SIZE,TILE_SIZE, tileMap.getTile(0).getInstance(0).getDx(),tileMap.getTile(0).getInstance(0).getDy(),tileMap.getTile(0).getInstance(0).getDw(),tileMap.getTile(0).getInstance(0).getDh());
					m.renderTiles(x + ((i-1)*TILE_SIZE),y + (j-1)*TILE_SIZE,TILE_SIZE,TILE_SIZE, tile.getDx(instance),tile.getDy(instance),tile.getDw(instance),tile.getDh(instance),false);
				}
			}
	}
	
	public void drawTilesBase(MainWindow m) {
//		Tile.initDrawTiles(m,system.map.getTileset());
		Tile tile;
		for (int i = 1; i < widthInTiles-1; i++) {
			//for every row
			for (int j = 1; j < heightInTiles-1; j++) {
				//for every column
				int val = this.areaOfInterest.get(j).get(i);
				tile = tileMap.getTile(val);
//				if (val == 0) {
//					continue;
//				}
				int instance  = map.inspectSurroundings(i+viewX,j+viewY);
//				m.renderTile(x + (i-1)*TILE_SIZE, y + (j-1)*TILE_SIZE,TILE_SIZE,TILE_SIZE, tileMap.getTile(0).getInstance(0).getDx(),tileMap.getTile(0).getInstance(0).getDy(),tileMap.getTile(0).getInstance(0).getDw(),tileMap.getTile(0).getInstance(0).getDh());
				m.renderTiles(x + ((i-1)*TILE_SIZE),y + (j-1)*TILE_SIZE,TILE_SIZE,TILE_SIZE, tile.getDx(instance),tile.getDy(instance),tile.getDw(instance),tile.getDh(instance),false);
			}
		}
	}
	
	public void drawCoordinates(MainWindow m) {
		Point xy = getTilePosition();
	}
	
	
	public void drawEntitiesFromMap(MainWindow m) {
		for (Entity e : map.getEntities()) {
			Pose pose = e.getSpriteCoordinates().getPose("idle", "","down");
			TileMetadata tm = pose.getStateByNum(0);
			e.initDrawEntity(m,e.getTexture());
			m.renderTile(e.getX()+x-(viewX)*TILE_SIZE, e.getY()+y-(viewY)*TILE_SIZE, e.getWidth(), e.getHeight(), (float) tm.getX(), (float) tm.getY(), (float) tm.getWidth(), (float) tm.getHeight());
		}
	}
	
	@Override
	public void draw(MainWindow m) {
		ArrayList<ArrayList<Integer>> saved = map.layerMap;
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D,state.tilesetTexture.getTextureID());
		map.setChangeMap("BASE");
		getAreaOfInterest();
		drawTilesBase(m);
		map.setChangeMap("BG");
		getAreaOfInterest();
		drawTilesBG(m);
		map.setChangeMap("FG");
		getAreaOfInterest();
		drawTiles(m);
		map.layerMap = saved;
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D,state.getTextureAtlas().getTexture().getTextureID());

		drawEntitiesFromMap(m);
		
		if (drawGrid ) {
			drawGrid(m);
		}
		
		drawCoordinates(m);
	}

	public boolean hovered(double mousex, double mousey) {
		if (mousex >= this.x && mousex <= this.x + this.width && mousey >= this.y && mousey <= this.y + this.height) {
			return true;
		}
		return false;
	}
	
	public void hoveredAction() {
	}

	@Override
	public void unhoveredAction() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String execute() {
		editMap(tileTool);
		return null;
	}
	
	public void executeRightClick() {
		editMapRightClick(tileTool);
	}

	
	
	public void updateView(String movement) {
		if (movement.equals("L")) {
			if (viewX != 0) {
				this.viewX--;
			} else {
//				SystemState.err.println("View is already at beginning.");
			}
			
		} else if (movement.equals("R")) {
			if (widthInTiles + viewX + 1 >= map.getWidth()-1) {
//				SystemState.out.println("Expand right");
				//expand the system.map in the right
				map.expandMap(1);
				this.viewX++;
				
			} else {
				this.viewX++;
			}
		} else if (movement.equals("U")) {
			if (viewY != 0) {
				this.viewY--;
				
			} else {
//				SystemState.err.println("View is already at the end of the file");
			}
		} else if (movement.equals("D")) {
			if (heightInTiles + viewY + 1>= map.getHeight()-1) {
				//expand the system.map in the right
				map.expandMap(1);
				this.viewY++;
				
			} else {
				this.viewY++;
			}
		}
	}
	
	public void setHoldableState(boolean b) {
		holdableState= b;
	}
	
	@Override
	public void handleInput(InputController input) {
		input.setHoldable(holdableState);
		if (input.getSignals().get("LEFT")) {
			updateView("L");
		} else if (input.getSignals().get("RIGHT")) {
			SystemState.out.println("Moving view to right.");
			updateView("R");
		} else if (input.getSignals().get("UP")) {
			updateView("U");
		} else if (input.getSignals().get("DOWN")) {
			updateView("D");
		} else if (input.getSignals().get("MOUSE_RIGHT_DOWN")) {
			executeRightClick();
		} else if (input.getSignals().get("ENTER")) {
			fillInGrass();
		}
	}

	private void fillInGrass() {
		// TODO Auto-generated method stub
		map.fillInGrass();
	}

	@Override
	public void setFocused(boolean b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getFocused() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
