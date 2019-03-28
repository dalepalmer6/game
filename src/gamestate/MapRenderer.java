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
import tiles.TileInstance;

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
	private ArrayList<ArrayList<TileInstance>> areaOfInterestTileInstances;
	private ArrayList<ArrayList<TileInstance>> tileInstanceMap;
	private MainWindow mainWindow;
	private TileHashMap tileMap;
	private final int TILE_SIZE = 64;
	private StartupNew state;
	
	public ArrayList<ArrayList<TileInstance>> getAreaOfInterestTiles() {
		return tileInstanceMap;
	}
	
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
		tileInstanceMap = map.tileInstanceMap;
		areaOfInterestTileInstances = new ArrayList<ArrayList<TileInstance>> ();
	}

	public void redrawTile(MainWindow m,int x, int y,TileInstance instance) {
		Tile.initDrawTiles(m);
		m.renderTile(-camera.getX() + this.x + (x-1)*TILE_SIZE, -camera.getY()+ this.y + (y-1)*TILE_SIZE,TILE_SIZE,TILE_SIZE, instance.getDx(),instance.getDy(),instance.getDw(),instance.getDh());
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
				TileInstance instance = areaOfInterestTileInstances.get(j).get(i);
				m.renderTile(-camera.getX()%TILE_SIZE + x + (i-1)*TILE_SIZE, -camera.getY()%TILE_SIZE+ y + (j-1)*TILE_SIZE,TILE_SIZE,TILE_SIZE, tileMap.getTile(0).getInstance(0).getDx(),tileMap.getTile(0).getInstance(0).getDy(),tileMap.getTile(0).getInstance(0).getDw(),tileMap.getTile(0).getInstance(0).getDh());
				m.renderTile(-camera.getX()%TILE_SIZE + x + (i-1)*TILE_SIZE, -camera.getY()%TILE_SIZE+ y + (j-1)*TILE_SIZE,TILE_SIZE,TILE_SIZE, instance.getDx(),instance.getDy(),instance.getDw(),instance.getDh());
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
		ArrayList<ArrayList<TileInstance>> rowsInst = new ArrayList<ArrayList<TileInstance>>();
		ArrayList<TileInstance> rowInst = new ArrayList<TileInstance>();
		for (int j = camera.getY()/TILE_SIZE; j < this.heightInTiles + camera.getY()/TILE_SIZE + 2; j++) {
			for (int i = Math.max(0,camera.getX()/TILE_SIZE); i < widthInTiles + camera.getX()/TILE_SIZE + 2; i++) {
				int curId = map.getTileId(i, j);
				rowInst.add(tileInstanceMap.get(j).get(i));
				row.add(curId);
			}
			rows.add(row);
			rowsInst.add(rowInst);
			row = new ArrayList<Integer>();
			rowInst = new ArrayList<TileInstance>();
		}
		areaOfInterest = rows;
		areaOfInterestTileInstances = rowsInst;
	}
	
}