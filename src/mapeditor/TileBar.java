package mapeditor;

import java.awt.Point;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;

import canvas.Clickable;
import canvas.Drawable;
import canvas.Hoverable;
import canvas.MainWindow;
import mapeditor.tools.PremadeTileObjectTool;
import mapeditor.tools.SingleTile;
import menu.LeftClickableItem;
import menu.StartupNew;
import tiles.MultiInstanceTile;
import tiles.PremadeTileObject;
import tiles.SingleInstanceTile;

public class TileBar extends LeftClickableItem implements Hoverable, Clickable {
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
	private StartupNew state;
	private int TILE_SIZE = 32;
	private ArrayList<ArrayList<Tile>> currentTilesOfInterest;
	
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
	
	public TileBar(int maxWidthTiles, int maxHeightTiles, TileHashMap tm, MapPreview mapPreview, StartupNew state) {
		//set location
		this.x = mapPreview.getRightEdge() + 3*TILE_SIZE;
		this.y = mapPreview.getY();
		state.getTextureAtlas().setRectByName("img/tiles.png");
//		this.MAX_HEIGHT_IN_TILES = state.textures.get("img/tiles.png").getTextureHeight() / 16;
//		this.MAX_WIDTH_IN_TILES = state.textures.get("img/tiles.png").getTextureWidth() / 16;
		this.state= state;
		this.mapPreview = mapPreview;
		tileMap = tm;
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
	
	public void setHoveredTileId() {
		Point xy = getTilePosition();
//		int index = xy.x + (xy.y * this.widthInTiles);
		try {
			this.hoveredTileId = currentTilesOfInterest.get(xy.y).get(xy.x).getId();
		} catch(NullPointerException e) {
			System.err.println("Tile Index: " +  " out of bounds, tile does not exist.");
			e.printStackTrace();
		} catch(IndexOutOfBoundsException e) {
			
		} 
		
	}
	
	public int getHoveredTile() {
		return this.hoveredTileId;
	}
	
	public void drawCoordinates() {
		Point xy = getTilePosition();
//		System.out.println("(" + xy.getX() + "," + xy.getY() + ")");
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
		m.setTexture(mapPreview.getMap().getTileset());
		this.MAX_HEIGHT_IN_TILES = state.getTextureAtlas().getCurrentRectangle().height / 16;
		this.MAX_WIDTH_IN_TILES =  state.getTextureAtlas().getCurrentRectangle().width / 16;
//		Texture t = m.getTexture();
		int numTilesX = state.getTextureAtlas().getCurrentRectangle().width / 16;
		ArrayList<ArrayList<Tile>> tiles = new ArrayList<ArrayList<Tile>>();
		//start from viewX,viewY as i, and increment by 1 each time while i < widthInTiles
		ArrayList<Tile> row = new ArrayList<Tile>();
		for (int j = viewY; j < this.heightInTiles + viewY; j++) {
			for (int i = viewX; i < widthInTiles + viewX; i++) {
				int curId = i + (j * numTilesX);
				row.add(tileMap.getTile(curId));
			}
			tiles.add(row);
			row = new ArrayList<Tile>();
		}
		currentTilesOfInterest = tiles;
	}
	
	public Tile getCurrentTileFromInterestList(int x, int y) {
		return currentTilesOfInterest.get(y).get(x);
	}
	
	public void drawTiles() {
		// read the tileMap and for each tile, draw to the next open spot
		MainWindow m = state.getMainWindow();
		Tile.initDrawTiles(m,mapPreview.getMap().getTileset());
		for (int i = 0; i < this.widthInTiles; i++) {
			for (int j = 0; j < this.heightInTiles; j++) {
				int drawingX =  (i) * TILE_SIZE;
				int drawingY =  (j) * TILE_SIZE;
				Tile tile = getCurrentTileFromInterestList(i,j);
				if (tile != null) {
					m.renderTile(drawingX + this.x,drawingY + this.y,mapPreview.getTileSize(),mapPreview.getTileSize(), tile.getDx(0),tile.getDy(0),tile.getDw(0),tile.getDh(0));
				}
			}
		}
	}
	
	@Override
	public void draw(MainWindow m) {
		getTilesOfInterest();
		drawTiles();
		drawGrid(m);
		drawCoordinates();
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
		setHoveredTileId();
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
		Tile t = tileMap.getTile(hoveredTileId);
		if (t instanceof PremadeTileObject) {
			mapPreview.setTool(new PremadeTileObjectTool(t));
		} else if (t instanceof SingleInstanceTile || t instanceof MultiInstanceTile) {
			mapPreview.setTool(new SingleTile(t));
		}
		return null;
	}
}
