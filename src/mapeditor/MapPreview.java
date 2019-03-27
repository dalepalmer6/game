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
import tiles.SingleInstanceTile;

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
	private int instance = 0;
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
		this.widthInTiles = 30+2;
		this.heightInTiles = 30+2;
		this.width = (-2 + this.widthInTiles) * TILE_SIZE;
		this.height = (-2 + this.heightInTiles) * TILE_SIZE;
		this.tileMap = tm;
	}
	
	//can toggle this off!
	public void drawGrid(MainWindow m) {
		m.setTexture("img/line.png");
		for (int i = this.x; i <= this.x + width ; i+= TILE_SIZE) {
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
	
	public void drawTiles(MainWindow m) {
			Tile.initDrawTiles(m);
			Tile tile;
			for (int i = 1; i < widthInTiles-1; i++) {
				//for every row
				for (int j = 1; j < heightInTiles-1; j++) {
					//for every column
					int val = this.areaOfInterest.get(j).get(i);
					tile = tileMap.getTile(val);
					int instance  = inspectSurroundings(i,j);
					m.renderTile(x + ((i-1)*TILE_SIZE),y + (j-1)*TILE_SIZE,TILE_SIZE,TILE_SIZE, tile.getDx(instance),tile.getDy(instance),tile.getDw(instance),tile.getDh(instance));
				}
			}
	}
	
	public int inspectSurroundings(int x , int y) {
		//check the adjacent tiles, if they're the same, then draw the appropriate instance of the tile
		//middle tile that we are comparing with
		if (tileMap.getTile(areaOfInterest.get(y).get(x)) instanceof SingleInstanceTile) {
			return 0;
		}
		int mid = areaOfInterest.get(y).get(x);
		
//		if (x == 0 && y == 0 && viewX > 0 && viewY > 0) {
//			int l = map.getTile(viewX-1,y).getId();
//			int r = areaOfInterest.get(y).get(x+1);
//			int u = map.getTile(x,viewY-1).getId();
//			int d = areaOfInterest.get(y+1).get(x);
//			if (mid != u && mid != l && mid == r && mid == d) {
//				return 1;
//			}
//			if (mid == u && mid != l && mid == r && mid == d) {
//				return 4;
//			}
//			if (mid == u && mid != l && mid == r && mid != d) {
//				return 7;
//			}
//			if (mid == u && mid == l && mid == r && mid == d) {
//				return 5;
//			}
//		}
//		if (x == 0 && viewX > 0) {
//			int l = map.getTile(viewX-1,y).getId();
//			int r = areaOfInterest.get(y).get(x+1);
//			int u = areaOfInterest.get(y-1).get(x);
//			int d = areaOfInterest.get(y+1).get(x);
//			if (mid != u && mid != l && mid == r && mid == d) {
//				return 1;
//			}
//			if (mid == u && mid != l && mid == r && mid == d) {
//				return 4;
//			}
//			if (mid == u && mid != l && mid == r && mid != d) {
//				return 7;
//			}
//			if (mid == u && mid == l && mid == r && mid == d) {
//				return 5;
//			}
//		}
//		
//		if (y == 0 && viewY > 0) {
//			int u = map.getTile(x,viewY-1).getId();
//			int l = areaOfInterest.get(y).get(x-1);
//			int r = areaOfInterest.get(y).get(x+1);
//			int d = areaOfInterest.get(y+1).get(x);
//			if (mid != u && mid != l && mid == r && mid == d) {
//				return 1;
//			}
//			if (mid != u && mid == l && mid == r && mid == d) {
//				return 2;
//			}
//			if (mid != u && mid == l && mid != r && mid == d) {
//				return 3;
//			}
//			if (mid == u && mid == l && mid == r && mid == d) {
//				return 5;
//			}
//		}
		
		
		
		if (x > 0 && y > 0) {
			int l = areaOfInterest.get(y).get(x-1);
			int r = areaOfInterest.get(y).get(x+1);
			int u = areaOfInterest.get(y-1).get(x);
			int d = areaOfInterest.get(y+1).get(x);
			if (mid != u && mid != l && mid == r && mid == d) {
				return 1;
			}
			if (mid != u && mid == l && mid == r && mid == d) {
				return 2;
			}
			if (mid != u && mid == l && mid != r && mid == d) {
				return 3;
			}
			if (mid == u && mid != l && mid == r && mid == d) {
				return 4;
			}
			if (mid == u && mid == l && mid == r && mid == d) {
				return 5;
			}
			if (mid == u && mid == l && mid != r && mid == d) {
				return 6;
			}
			if (mid == u && mid != l && mid == r && mid != d) {
				return 7;
			}
			if (mid == u && mid == l && mid == r && mid != d) {
				return 8;
			}
			if (mid == u && mid == l && mid != r && mid != d) {
				return 9;
			}
		}
		return 0;
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
				this.viewX++;
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
				this.viewY++;
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
