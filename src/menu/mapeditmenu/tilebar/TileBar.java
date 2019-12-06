package menu.mapeditmenu.tilebar;

import java.awt.Point;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.newdawn.slick.opengl.Texture;

import menu.LeftClickableItem;
import menu.mapeditmenu.MapEditMenu;
import menu.mapeditmenu.mappreview.MapPreview;
import menu.mapeditmenu.tools.PremadeTileObjectTool;
import menu.mapeditmenu.tools.SingleTile;
import system.MainWindow;
import system.SystemState;
import system.interfaces.Clickable;
import system.interfaces.Drawable;
import system.interfaces.Hoverable;
import system.map.Tile;
import system.map.TileHashMap;
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
	private SystemState state;
	private int TILE_SIZE = 32;
	private ArrayList<ArrayList<Tile>> currentTilesOfInterest;
	private int tilesize;
	private TileBarMoveButton down;
	private TileBarMoveButton up;

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public Point getMouseCoordinates() {
		int x = Mouse.getX();
		int y = Display.getHeight() - Mouse.getY();
		return new Point(x, y);
	}

	public TileBar(int maxWidthTiles, int maxHeightTiles, TileHashMap tm, MapPreview mapPreview, SystemState state) {
		// set location
		this.x = (int) mapPreview.getX();
		this.y = (int) (mapPreview.getLowerEdge() + 0 * TILE_SIZE);
//		state.getTextureAtlas().setRectByName("img/tiles.png");
//		this.MAX_HEIGHT_IN_TILES = state.textures.get("img/tiles.png").getTextureHeight() / 16;
//		this.MAX_WIDTH_IN_TILES = state.textures.get("img/tiles.png").getTextureWidth() / 16;
		this.state = state;
		this.mapPreview = mapPreview;
		tileMap = tm;
		this.widthInTiles = maxWidthTiles;
		this.heightInTiles = maxHeightTiles;
		this.width = widthInTiles * TILE_SIZE;
		this.height = heightInTiles * TILE_SIZE;
		if (mapPreview != null) {
			 tilesize = mapPreview.getTileSize();
		}
		up = new TileBarMoveButton("",(double) (x + width + 32), (double) y,state,this);
		up.setAction("U");
		down = new TileBarMoveButton("",(double) (x + width + 32), (double) (y + height - 64),state,this);
		down.setAction("D");
	}
	
	public TileBar(int maxWidthTiles, int maxHeightTiles, TileHashMap tm, SystemState state) {
		// set location
		this.x = 128;
		this.y = 128;
		this.state = state;
		tileMap = tm;
		this.widthInTiles = maxWidthTiles;
		this.heightInTiles = maxHeightTiles;
		this.width = widthInTiles * TILE_SIZE;
		this.height = heightInTiles * TILE_SIZE;
		tilesize = 32;
		up = new TileBarMoveButton("",(double) (x + width + 32), (double) y,state,this);
		up.setAction("U");
		down = new TileBarMoveButton("",(double) (x + width + 32), (double) (y + height - 64),state,this);
		down.setAction("D");
	}

	public Point getTilePosition() {
		
		double tileX = Math
				.floor((-this.x + state.getMainWindow().getMouseCoordinates().getX()) / tilesize);
		double tileY = Math
				.floor((-this.y + state.getMainWindow().getMouseCoordinates().getY()) / tilesize);
		if (tileX > this.widthInTiles || tileX < 0) {
			tileX = -1;
		}
		if (tileY > this.heightInTiles || tileY < 0) {
			tileY = -1;
		}
		Point xy = new Point();
		xy.setLocation(tileX, tileY);
		return xy;
	}

	public void setHoveredTileId() {
		Point xy = getTilePosition();
//		int index = xy.x + (xy.y * this.widthInTiles);
		try {
			this.hoveredTileId = currentTilesOfInterest.get(xy.y).get(xy.x).getId();
		} catch (NullPointerException e) {
//			SystemState.err.println("Tile Index: " + " out of bounds, tile does not exist.");
			e.printStackTrace();
		} catch (IndexOutOfBoundsException e) {

		}

	}

	public int getHoveredTile() {
		return this.hoveredTileId;
	}

	public void drawCoordinates() {
		Point xy = getTilePosition();
//		SystemState.out.println("(" + xy.getX() + "," + xy.getY() + ")");
	}

	public void drawGrid(MainWindow m) {
		for (int i = this.x; i <= this.x + width; i += TILE_SIZE) {
			// for every row
			for (int j = this.y; j <= this.y + height; j += TILE_SIZE) {
				// for every column
				m.renderTile(this.x, j, width, 1, 1, 1, 1, 1);
			}
			m.renderTile(i, this.y, 1, height, 1, 1, 1, 1);

		}
	}

	public void getTilesOfInterest() {
		MainWindow m = state.getMainWindow();
//		m.setTexture(mapPreview.getMap().getTileset());
//		this.MAX_HEIGHT_IN_TILES = state.tilesetTexture.getTextureHeight() / 32;
//		this.MAX_WIDTH_IN_TILES = state.tilesetTexture.getTextureWidth() / 32;
//		this.MAX_HEIGHT_IN_TILES = 32;
//		this.MAX_WIDTH_IN_TILES = 8;
//		Texture t = m.getTexture();
		int numTilesX = state.getTextureAtlas().getCurrentRectangle().width / 32;
		ArrayList<ArrayList<Tile>> tiles = new ArrayList<ArrayList<Tile>>();
		// start from viewX,viewY as i, and increment by 1 each time while i <
		ArrayList<Tile> row = new ArrayList<Tile>();
		for (int j = viewY; j < this.heightInTiles + viewY; j++) {
			for (int i = viewX; i < widthInTiles + viewX; i++) {
				try {
					int curId = i + (j * widthInTiles);
					row.add(tileMap.getTile(curId));
				} 
				catch(NullPointerException err) {
					row.add(tileMap.getTile(0));
				}
			}
			tiles.add(row);
			row = new ArrayList<Tile>();
		}
		currentTilesOfInterest = tiles;
		
		
	}

	public Tile getCurrentTileFromInterestList(int x, int y) {
		try {
			return currentTilesOfInterest.get(y).get(x);
		} catch (IndexOutOfBoundsException err) {
			return tileMap.getTile(0);
		}
	}

	public void drawTiles() {
		// read the tileMap and for each tile, draw to the next open spot
		MainWindow m = state.getMainWindow();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D,state.tilesetTexture.getTextureID());
//		Tile.initDrawTiles(m, mapPreview.getMap().getTileset());
		for (int i = viewX; i < viewX + this.widthInTiles; i++) {
			for (int j = viewY; j < viewY + this.heightInTiles; j++) {
				int drawingX = (i) * TILE_SIZE;
				int drawingY = (j) * TILE_SIZE;
				Tile tile = getCurrentTileFromInterestList(i, j);
				if (tile != null) {
					m.renderTiles(drawingX + this.x, drawingY + this.y, tilesize,
							tilesize, tile.getDx(0), tile.getDy(0), tile.getDw(0), tile.getDh(0),false);
				}
			}
		}
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D,state.getTextureAtlas().getTexture().getTextureID());
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
	
	public void updateAnim() {
		state.getMenuStack().peek().addToMenuItems(down);
		state.getMenuStack().peek().addToMenuItems(up);
	}

	public void updateView(String movement) {
//		if (movement.equals("L")) {
//			if (viewX != 0) {
//				this.viewX--;
//			} else {
//				SystemState.err.println("View is already at beginning.");
//			}
//
//		} else if (movement.equals("R")) {
//			if (viewX < MAX_WIDTH_IN_TILES) {
//				// expand the system.map in the right
//				SystemState.out.println(viewX);
//				this.viewX++;
//
//			} else {
//				SystemState.err.println("View is already at the end of the file");
//			}
//		} 
		if (movement.equals("U")) {
			if (viewY != 0) {
				this.viewY--;
			} else {
//				SystemState.err.println("View is already at the end of the file");
			}
		} else if (movement.equals("D")) {
			if (true) {
				this.viewY++;
//				SystemState.err.println("MOVING DOWN");
			} else {
//				SystemState.err.println("View is already at the end of the file");
			}
		}
	}

	@Override
	public String execute() {
		// TODO Auto-generated method stub
		Tile t = tileMap.getTile(hoveredTileId);
		if (t instanceof PremadeTileObject) {
			((MapEditMenu)state.getMenuStack().peek()).setTool(new PremadeTileObjectTool(t, state));
		} else if (t instanceof SingleInstanceTile || t instanceof MultiInstanceTile) {
			((MapEditMenu)state.getMenuStack().peek()).setTool(new SingleTile(t, state));
		}
		return null;
	}
}
