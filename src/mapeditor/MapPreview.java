package mapeditor;

import java.awt.Point;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import canvas.Clickable;
import canvas.Drawable;
import canvas.Hoverable;
import canvas.MainWindow;

public class MapPreview extends LeftAndRightClickableItem implements Drawable, Hoverable, Clickable {
	private Map map;
	private int x;
	private int y;
	private int widthInTiles;
	private int heightInTiles;
	private int width;
	private int height;
	private int tileTool;
	private int TILE_SIZE = 32;
	private TileHashMap tileMap;
	
	public int getTileSize() {
		return TILE_SIZE;
	}
	
	public void setTool(int t_id) {
		this.tileTool = t_id;
	}
	
	public int getTool() {
		return tileTool;
	}
	
	public void editMap(int currentTileId) {
		//on click, change the tile to the new tile_id
			Point xy = getTilePosition();
			int x = (int) xy.getX();
			int y = (int) xy.getY();
//			System.out.println("Putting tile " + currentTileId + " at (" + x + "," + y + ")");
			this.map.setTile(currentTileId, x, y);
	}
	
	public MapPreview(int ts, int x, int y, Map m, TileHashMap tm) {
		this.map = m;
		this.TILE_SIZE = ts;
		//set at the default position
		this.x = x;
		this.y = y;
		this.widthInTiles = 20;
		this.heightInTiles = 20;
		this.width = this.widthInTiles * TILE_SIZE;
		this.height = this.heightInTiles * TILE_SIZE;
		this.tileMap = tm;
	}
	
	//can toggle this off!
	public void drawGrid(MainWindow m) {
		for (int i = this.x; i <= this.x + width; i+= TILE_SIZE) {
			//for every row
			for (int j = this.y; j <= this.y + height; j+= TILE_SIZE) {
				//for every column
				m.render(this.x,j,width,1);
			}
			m.render(i,this.y,1,height);
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
					tile = this.map.getTile(i,j);
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
	public void execute() {
		editMap(tileTool);
	}
	
	@Override
	public void executeRightClick() {
		editMap(0);
	}
	
}
