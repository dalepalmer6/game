package mapeditor;

import java.awt.Point;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import canvas.Clickable;
import canvas.Drawable;
import canvas.Hoverable;
import canvas.MainWindow;
import menu.LeftClickableItem;
import menu.StartupNew;

public class TileBar extends LeftClickableItem implements Drawable, Hoverable, Clickable {
	private int x;
	private int y;
	private int width;
	private int height;
	private int widthInTiles;
	private int heightInTiles;
	private int hoveredTileId;
	private int MAX_HEIGHT_IN_TILES = 16;
	private int MAX_WIDTH_IN_TILES = 32;
	private TileHashMap tileMap;
	private MapPreview mapPreview;
	private StartupNew state;
	private int TILE_SIZE = 32;
	
	public Point getMouseCoordinates() {
		int x = Mouse.getX();
		int y = Display.getHeight() - Mouse.getY();
		return new Point(x, y);
	}
	
	public TileBar(int x, int y, int maxWidthTiles, int maxHeightTiles, TileHashMap tm, MapPreview mapPreview, StartupNew state) {
		//set location
		this.x = x;
		this.y = y;
		this.MAX_HEIGHT_IN_TILES = maxHeightTiles;
		this.MAX_WIDTH_IN_TILES = maxWidthTiles;
		this.state= state;
		this.mapPreview = mapPreview;
		tileMap = tm;
		x = (21 + 3) * mapPreview.getTileSize();
		y = 3 * mapPreview.getTileSize();
		//create a grid that shows the tiles
		int numTiles = tileMap.size();
		int i = 0; int j = 0;
		while (i < numTiles) {
			if (i % this.MAX_WIDTH_IN_TILES == 0) {
				j++;
			}
			i++;
		}
		if (j > 1) {
			i = this.MAX_WIDTH_IN_TILES;
		}
		this.width = i*mapPreview.getTileSize();
		this.height = j*mapPreview.getTileSize();
		this.widthInTiles = i;
		this.heightInTiles = j;
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
		int index = xy.x + (xy.y * this.widthInTiles);
		try {
			this.hoveredTileId = tileMap.getTile(index).getId();
		} catch(NullPointerException e) {
			System.err.println("Tile Index: " + index +  " out of bounds, tile does not exist.");
			e.printStackTrace();
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
				m.render(this.x,j,width,1);
			}
			m.render(i,this.y,1,height);
			
		}
	}
	
	public void drawTiles() {
		// read the tileMap and for each tile, draw to the next open spot
		MainWindow m = state.getMainWindow();
		Tile.initDrawTiles(m);
		int i = 0;
		int j = 0;
		while (j < this.heightInTiles) {
			int drawingX = i * TILE_SIZE;
			int drawingY = j * TILE_SIZE;
			int tileNo =  i+(j*this.widthInTiles);
			Tile tile = tileMap.getTile(tileNo);
			m.renderTile(drawingX + this.x,drawingY + this.y,mapPreview.getTileSize(),mapPreview.getTileSize(), tile.getDx(),tile.getDy(),tile.getDw(),tile.getDh());
			i++;
			if (i == this.widthInTiles) {
				i=0;
				j++;
			}
		}
	}
	
	@Override
	public void draw(MainWindow m) {
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

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		mapPreview.setTool(hoveredTileId);
	}
}
