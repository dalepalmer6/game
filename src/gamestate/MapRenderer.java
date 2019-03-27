package gamestate;

import java.util.ArrayList;

import canvas.Drawable;
import canvas.MainWindow;
import mapeditor.Map;
import mapeditor.Tile;
import mapeditor.TileHashMap;
import menu.DrawableObject;
import menu.StartupNew;
import tiles.SingleInstanceTile;

public class MapRenderer extends DrawableObject implements Drawable{
	private Map map;
	private double tickCount;
	private double ticksPerFrame = 0.1;
	private int x;
	private int y;
	private int offsetX;
	private int offsetY;
	private Camera camera;
	private int viewX;
	private int viewY;
	private int widthInTiles;
	private int heightInTiles;
	private ArrayList<ArrayList<Integer>> areaOfInterest;
	private MainWindow mainWindow;
	private TileHashMap tileMap;
	private final int TILE_SIZE = 64;
	private StartupNew state;
	
	public int getTileSize() {
		return TILE_SIZE;
	}
	
	public MapRenderer(Map m, Camera c, StartupNew s) {
		// TODO Auto-generated constructor stub
		this.camera = c;
		this.map = m;
		viewX = c.getX();
		viewY = c.getY();
		mainWindow = s.getMainWindow();
		this.widthInTiles = 3 + mainWindow.getScreenWidth() / TILE_SIZE;
		this.heightInTiles = 4 + mainWindow.getScreenHeight() / TILE_SIZE;
		this.state = s;
		this.tileMap = s.tileMap;
	}

	public int inspectSurroundings(int x , int y) {
		//check the adjacent tiles, if they're the same, then draw the appropriate instance of the tile
		//middle tile that we are comparing with
		int instance = 0;
		if (tileMap.getTile(areaOfInterest.get(y).get(x)) instanceof SingleInstanceTile) {
			return 0;
		}
		if (x == 0 || y == 0) {
			instance = 0;
			return 0;
		}
		int mid = areaOfInterest.get(y).get(x);
		int l = areaOfInterest.get(y).get(x-1);
		int r = areaOfInterest.get(y).get(x+1);
		int u = areaOfInterest.get(y+1).get(x);
		int d = areaOfInterest.get(y-1).get(x);
		if (mid != u && mid != l && mid == r && mid == d) {
			return 7;
		}
		if (mid != u && mid == l && mid == r && mid == d) {
			return 8;
		}
		if (mid != u && mid == l && mid != r && mid == d) {
			return 9;
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
			return 1;
		}
		if (mid == u && mid == l && mid == r && mid != d) {
			return 2;
		}
		if (mid == u && mid == l && mid != r && mid != d) {
			return 3;
		}
		return 0;
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
				int instance = inspectSurroundings(i,j);
				m.renderTile(-camera.getX()%TILE_SIZE + x + (i-1)*TILE_SIZE, -camera.getY()%TILE_SIZE+ y + (j-1)*TILE_SIZE,TILE_SIZE,TILE_SIZE, tile.getDx(instance),tile.getDy(instance),tile.getDw(instance),tile.getDh(instance));
			}
		}
}
	
	public void draw(MainWindow m) {
		getAreaOfInterest();
		drawTiles(m);
	}
	
	public void getAreaOfInterest() {
		//viewX,viewY
		ArrayList<ArrayList<Integer>> rows = new ArrayList<ArrayList<Integer>>();
		//start from viewX,viewY as i, and increment by 1 each time while i < widthInTiles
		ArrayList<Integer> row = new ArrayList<Integer>();
		for (int j = camera.getY()/TILE_SIZE; j < this.heightInTiles + camera.getY()/TILE_SIZE + 2; j++) {
			for (int i = Math.max(0,camera.getX()/TILE_SIZE); i < widthInTiles + camera.getX()/TILE_SIZE + 2; i++) {
				int curId = map.getTileId(i, j);
				row.add(curId);
			}
			rows.add(row);
			row = new ArrayList<Integer>();
		}
		areaOfInterest = rows;
	}
	
}
