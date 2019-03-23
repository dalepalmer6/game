package gamestate;

import java.util.ArrayList;

import canvas.Drawable;
import canvas.MainWindow;
import mapeditor.Map;
import mapeditor.Tile;
import mapeditor.TileHashMap;
import menu.StartupNew;

public class MapRenderer implements Drawable{
	private Map map;
	private int x;
	private int y;
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
		this.widthInTiles = 2 + mainWindow.getScreenWidth() / TILE_SIZE;
		this.heightInTiles = 2 + mainWindow.getScreenHeight() / TILE_SIZE;
		this.state = s;
		this.tileMap = s.tileMap;
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
				m.renderTile(-camera.getX()%TILE_SIZE + x + (i*TILE_SIZE), -camera.getY()%TILE_SIZE+ y + j*TILE_SIZE,TILE_SIZE,TILE_SIZE, tile.getDx(),tile.getDy(),tile.getDw(),tile.getDh());
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
		for (int j = camera.getY()/TILE_SIZE; j < this.heightInTiles + camera.getY()/TILE_SIZE; j++) {
			for (int i = camera.getX()/TILE_SIZE; i < widthInTiles + camera.getX()/TILE_SIZE; i++) {
				int curId = map.getTileId(i, j);
				row.add(curId);
			}
			rows.add(row);
			row = new ArrayList<Integer>();
		}
		areaOfInterest = rows;
	}
	
}
