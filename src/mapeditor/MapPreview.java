package mapeditor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import org.lwjgl.input.Mouse;

import canvas.Clickable;
import canvas.Drawable;
import canvas.Hoverable;
import global.GlobalVars;

public class MapPreview implements Drawable, Hoverable, Clickable {
	private Map map;
	private Point currentTileId = getTilePosition();
	private boolean dragging = false;
	private int x;
	private int y;
	private int widthInTiles;
	private int heightInTiles;
	private int width;
	private int height;
	private int tileTool;
	
	public void setTool(int t_id) {
		this.tileTool = t_id;
	}
	
	public int getTool() {
		return tileTool;
	}
	
	public void toggleDragging(boolean b) {
		System.out.println("setting dragging to " + b );
		dragging = b;
	}
	
	public void editMap(int currentTileId) {
		//on click, change the tile to the new tile_id
			Point xy = getTilePosition();
			int x = (int) xy.getX();
			int y = (int) xy.getY();
//			System.out.println("Putting tile " + currentTileId + " at (" + x + "," + y + ")");
			this.map.setTile(currentTileId, x, y);
	}
	
	public MapPreview(Map m) {
		this.map = m;
		//set at the default position
		this.x = 3 * GlobalVars.TILE_SIZE;
		this.y = 3 * GlobalVars.TILE_SIZE;
		this.widthInTiles = 20;
		this.heightInTiles = 20;
		this.width = this.widthInTiles * GlobalVars.TILE_SIZE;
		this.height = this.heightInTiles * GlobalVars.TILE_SIZE;
	}
	
	//can toggle this off!
	public void drawGrid() {
		for (int i = this.x; i < this.x + 20*GlobalVars.TILE_SIZE; i+=GlobalVars.TILE_SIZE) {
			//for every row
			for (int j = this.y; j < this.y + 20*GlobalVars.TILE_SIZE; j+=GlobalVars.TILE_SIZE) {
				//for every column
				GlobalVars.mainWindow.render(j,this.x,1,this.heightInTiles * GlobalVars.TILE_SIZE);
			}
			GlobalVars.mainWindow.render(this.y,i,this.widthInTiles * GlobalVars.TILE_SIZE,1);
		}
	}

	public Point getTilePosition() {
		double tileX = Math.floor((-this.x + GlobalVars.mainWindow.getMouseCoordinates().getX())/GlobalVars.TILE_SIZE);
		double tileY = Math.floor((-this.y + GlobalVars.mainWindow.getMouseCoordinates().getY())/GlobalVars.TILE_SIZE);
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
	
	public int getTileAtPos(Point xy) {
		int x = (int) xy.getX();
		int y = (int) xy.getY();
		return this.map.getTile(x,y);
	}
	
	public void drawTiles() {
		int t_id;
		for (int i = 0; i < 20; i++) {
			//for every row
			for (int j = 0; j < 20; j++) {
				//for every column
				t_id = this.map.getTile(i,j);
				GlobalVars.mainWindow.render(GlobalVars.tileMap.getTile(t_id).getImageName(), x + (i*GlobalVars.TILE_SIZE),y + j*GlobalVars.TILE_SIZE,GlobalVars.TILE_SIZE,GlobalVars.TILE_SIZE);
			}
		}
	}
	
	public void drawCoordinates() {
		Point xy = getTilePosition();
		if (!(xy.getX() < 0) && !(xy.getY() < 0)) {}
//		System.out.println("(" + getTilePosition().getX() + "," + getTilePosition().getY() + ")");
	}
	
	
	
	
	@Override
	public void draw() {
		drawTiles();
		drawGrid();
		drawCoordinates();
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
		if (isMouseDown()) {
			editMap(GlobalVars.mapPreviewEditTool);
		}
	}

	@Override
	public boolean isMouseDown() {
		if (Mouse.isButtonDown(0)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean isMouseRightDown() {
		return false;
	}
	
}
