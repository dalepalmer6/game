package mapeditor;

import java.awt.Point;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;

import canvas.Clickable;
import canvas.Controllable;
import canvas.Drawable;
import canvas.Hoverable;
import canvas.MainWindow;
import global.InputController;

public class MapPreview extends LeftAndRightClickableItem implements Controllable, Drawable, Hoverable, Clickable {
	private Map map;
	private ArrayList<ArrayList<Integer>> areaOfInterest;
	private int x;
	private int y;
	private int widthInTiles;
	private int heightInTiles;
	private int viewX=0;
	private int viewY=0;
	private int width;
	private int height;
	private int tileTool;
	private int TILE_SIZE = 32;
	private TileHashMap tileMap;
	
	public int getRightEdge() {
		return x + widthInTiles * TILE_SIZE;
	}
	
	public int getY() {
		return y;
	}
	
	public int getTileSize() {
		return TILE_SIZE;
	}
	
	public void setTool(int t_id) {
		this.tileTool = t_id;
	}
	
	public int getTool() {
		return tileTool;
	}
	
	public void getAreaOfInterest() {
		//viewX,viewY
		ArrayList<ArrayList<Integer>> rows = new ArrayList<ArrayList<Integer>>();
		//start from viewX,viewY as i, and increment by 1 each time while i < widthInTiles
		ArrayList<Integer> row = new ArrayList<Integer>();
		for (int j = viewY; j < this.heightInTiles + viewY; j++) {
			for (int i = viewX; i < widthInTiles + viewX; i++) {
				int curId = map.getTileId(i, j);
				row.add(curId);
			}
			rows.add(row);
			row = new ArrayList<Integer>();
		}
		areaOfInterest = rows;
	}
	
	public void editMap(int currentTileId) {
		//on click, change the tile to the new tile_id
			Point xy = getTilePosition();
			int x = (int) xy.getX() + viewX;
			int y = (int) xy.getY() + viewY;
//			System.out.println("Putting tile " + currentTileId + " at (" + x + "," + y + ")");
			this.map.setTile(currentTileId, x, y);
	}
	
	public MapPreview(int ts, int x, int y, Map m, TileHashMap tm) {
		this.map = m;
		this.TILE_SIZE = ts;
		//set at the default position
		this.x = x;
		this.y = y;
		this.widthInTiles = 30;
		this.heightInTiles = 30;
		this.width = this.widthInTiles * TILE_SIZE;
		this.height = this.heightInTiles * TILE_SIZE;
		this.tileMap = tm;
	}
	
	//can toggle this off!
	public void drawGrid(MainWindow m) {
		m.setTexture("img/line.png");
		for (int i = this.x; i <= this.x + width; i+= TILE_SIZE) {
			//for every row
			for (int j = this.y; j <= this.y + height; j+= TILE_SIZE) {
				//for every column
				m.renderTile(this.x,j,width,1,1,1,1,1);
			}
			m.renderTile(i,this.y,1,height,1,1,1,1);
		}
	}

	public Point getMouseCoordinates() {
		//relative to the tool
		int x = Mouse.getX() - this.x;
		int y = Display.getHeight() - Mouse.getY() - this.y;
		return new Point(x, y);
	}
	
	public Point getTilePosition() {
		double tileX = Math.floor((getMouseCoordinates().getX())/TILE_SIZE);
		double tileY = Math.floor((getMouseCoordinates().getY())/TILE_SIZE);
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
	
	public void drawTiles(MainWindow m) {
			Tile.initDrawTiles(m);
			Tile tile;
			for (int i = 0; i < widthInTiles; i++) {
				//for every row
				for (int j = 0; j < heightInTiles; j++) {
					//for every column
					int val = this.areaOfInterest.get(j).get(i);
					tile = tileMap.getTile(val);
					m.renderTile(x + (i*TILE_SIZE),y + j*TILE_SIZE,TILE_SIZE,TILE_SIZE, tile.getDx(),tile.getDy(),tile.getDw(),tile.getDh());
				}
			}
	}
	
	public void drawCoordinates(MainWindow m) {
		Point xy = getTilePosition();
		if (!(xy.getX() < 0) && !(xy.getY() < 0)) {}
//		System.out.println("(" + getTilePosition().getX() + "," + getTilePosition().getY() + ")");
	}
	
	
	
	
	@Override
	public void draw(MainWindow m) {
		getAreaOfInterest();
		drawTiles(m);
		drawGrid(m);
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
	
	@Override
	public void executeRightClick() {
		editMap(0);
	}

	
	
	public void updateView(String movement) {
		if (movement.equals("L")) {
			if (viewX != 0) {
				this.viewX--;
			} else {
				System.err.println("View is already at beginning.");
			}
			
		} else if (movement.equals("R")) {
			if (widthInTiles + viewX >= map.getWidth()) {
				//expand the map in the right
				map.expandMap(1,0);
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
			if (heightInTiles + viewY >= map.getHeight()) {
				//expand the map in the right
				map.expandMap(0,1);
				this.viewY++;
				
			} else {
				System.err.println("View is already at the end of the file");
			}
		}
	}
	
	@Override
	public void handleInput(InputController input) {
		input.setHoldable(true);
		if (input.getSignals().get("LEFT")) {
			updateView("L");
		} else if (input.getSignals().get("RIGHT")) {
			System.out.println("Moving view to right.");
			updateView("R");
		} else if (input.getSignals().get("UP")) {
			updateView("U");
		} else if (input.getSignals().get("DOWN")) {
			updateView("D");
		}
	}
	
}
