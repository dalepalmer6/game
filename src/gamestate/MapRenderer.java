package gamestate;

import java.util.ArrayList;

import canvas.Drawable;
import canvas.MainWindow;
import mapeditor.Map;
import mapeditor.Tile;
import mapeditor.TileHashMap;
import menu.DrawableObject;
import menu.StartupNew;
import tiles.ChangeWithFlagTile;
import tiles.PremadeTileObject;
import tiles.SingleInstanceTile;
import tiles.TileInstance;
import tiles.types.Plain;
import tiles.types.Tree;

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
	private int scaleUp = 4;
	private final int TILE_SIZE = 32 * scaleUp;
	private StartupNew state;
	
	public Map getMap() {
		return map;
	}
	
	public void initDrawTiles(MainWindow m) {
		Tile.initDrawTiles(m,map.getTileset());
	}
	
	public int getWidthInTiles() {
		return widthInTiles;
	}
	
	public int getHeightInTiles() {
		return heightInTiles;
	}
	
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
		tileInstanceMap = map.tileInstanceMapBG;
		areaOfInterestTileInstances = new ArrayList<ArrayList<TileInstance>> ();
	}

	public void redrawTile(MainWindow m,int x, int y,TileInstance instance) {
		Tile.initDrawTiles(m, map.getTileset());
		if (instance == tileMap.getTile(0).getInstance(0)) {
			return;
		}
		m.renderTile(-camera.getX() + this.x + (x-1)*TILE_SIZE, -camera.getY()+ this.y + (y-1)*TILE_SIZE,TILE_SIZE,TILE_SIZE, instance.getDx(),instance.getDy(),instance.getDw(),instance.getDh());
	}
	
	public void drawTiles(MainWindow m) {
		Tile.initDrawTiles(m,map.getTileset());
		Tile tile;
		for (int i = 1; i < widthInTiles-1; i++) {
			//for every row
			for (int j = 1; j < heightInTiles-1; j++) {
				int val = this.areaOfInterest.get(j).get(i);
				tile = tileMap.getTile(val);
				if (val == 0) {
					continue;
				}
				int instance  = map.inspectSurroundings(i + camera.getX()/TILE_SIZE,j + camera.getY()/TILE_SIZE);
				m.renderTile(-camera.getX()%TILE_SIZE + x + (i-1)*TILE_SIZE, -camera.getY()%TILE_SIZE+ y + (j-1)*TILE_SIZE,TILE_SIZE,TILE_SIZE, tile.getDx(instance),tile.getDy(instance),tile.getDw(instance),tile.getDh(instance));
			}
		}
	}
	
	public void drawTilesBG(MainWindow m) {
		Tile.initDrawTiles(m,map.getTileset());
		Tile tile;
		for (int i = 1; i < widthInTiles-1; i++) {
			//for every row
			for (int j = 1; j < heightInTiles-1; j++) {
				int val = this.areaOfInterest.get(j).get(i);
				tile = tileMap.getTile(val);
				if (tile instanceof ChangeWithFlagTile) {
					String flag = ((ChangeWithFlagTile) tile).getFlagName();
					if (state.getGameState().getFlag(flag)) {
						tile = tileMap.getTile(((ChangeWithFlagTile) tile).getNewTileId());
					}
				}
				int instance  = map.inspectSurroundings(i + camera.getX()/TILE_SIZE,j + camera.getY()/TILE_SIZE);
//				m.renderTile(x + (i-1)*TILE_SIZE, y + (j-1)*TILE_SIZE,TILE_SIZE,TILE_SIZE, tileMap.getTile(0).getInstance(0).getDx(),tileMap.getTile(0).getInstance(0).getDy(),tileMap.getTile(0).getInstance(0).getDw(),tileMap.getTile(0).getInstance(0).getDh());
				m.renderTile(-camera.getX()%TILE_SIZE + x + (i-1)*TILE_SIZE, -camera.getY()%TILE_SIZE+ y + (j-1)*TILE_SIZE,TILE_SIZE,TILE_SIZE, tile.getDx(instance),tile.getDy(instance),tile.getDw(instance),tile.getDh(instance));
			}
		}
	}
	
	public void drawTileBase(MainWindow m, int x, int y, Tile t, int instance) {
		Tile.initDrawTiles(m,map.getTileset());
		m.renderTiles(-camera.getX()%TILE_SIZE + this.x + (x-1)*TILE_SIZE, -camera.getY()%TILE_SIZE+ this.y + (y-1)*TILE_SIZE,TILE_SIZE,TILE_SIZE, t.getDx(instance),t.getDy(instance),t.getDw(instance),t.getDh(instance),false);
	}
	
	public void drawTile(MainWindow m, int x, int y, Tile tbg, int instancebg, Tile tfg, int instancefg) {
		boolean drawFG = true;
		boolean drawBG = true;
		boolean redrawBG = false;
		boolean redrawFG = false;
		Tile.initDrawTiles(m,map.getTileset());
		if (tbg == state.tileMap.getTile(0)) {
			drawBG = false;
		} 
		if (tfg == state.tileMap.getTile(0)) {
			drawFG = false;
		}
		//split the tile into 4x4 pieces (each is 8px by 8px) (16)
		for (int i = 0; i < 4; i++) { //y
			for (int j = 0; j < 4; j++) { //x
				if ((tbg.getInstance(instancebg).getCollisionInfoAtIndex(j,i)&2&15) == 2) {
						RedrawObject robj = new RedrawObject(-camera.getX()%TILE_SIZE + this.x + (x-1)*TILE_SIZE + (j*8*4), -camera.getY()%TILE_SIZE+ this.y + (y-1)*TILE_SIZE + (i*8*4),TILE_SIZE/4,TILE_SIZE/4, tbg.getDx(instancebg) + (j*8),tbg.getDy(instancebg) + (i*8),8,8,state);
						state.getGameState().addToRedrawing(robj);
						redrawBG = true;
				}
				if (((tfg.getInstance(instancefg).getCollisionInfoAtIndex(j,i)&2)&15) == 2 || (drawFG && redrawBG)) {
						RedrawObject robj2 = new RedrawObject(-camera.getX()%TILE_SIZE + this.x + (x-1)*TILE_SIZE + (j*8*4), -camera.getY()%TILE_SIZE+ this.y + (y-1)*TILE_SIZE + (i*8*4),TILE_SIZE/4,TILE_SIZE/4, tfg.getDx(instancefg) + (j*8),tfg.getDy(instancefg) + (i*8),8,8,state);
						state.getGameState().addToRedrawing(robj2);
				}
				if (drawBG)
					m.renderTiles(-camera.getX()%TILE_SIZE + this.x + (x-1)*TILE_SIZE + (j*8*4), -camera.getY()%TILE_SIZE+ this.y + (y-1)*TILE_SIZE + (i*8*4),TILE_SIZE/4,TILE_SIZE/4, tbg.getDx(instancebg) + (j*8),tbg.getDy(instancebg) + (i*8),8,8,false);
				if (drawFG)
					m.renderTiles(-camera.getX()%TILE_SIZE + this.x + (x-1)*TILE_SIZE + (j*8*4), -camera.getY()%TILE_SIZE+ this.y + (y-1)*TILE_SIZE + (i*8*4),TILE_SIZE/4,TILE_SIZE/4, tfg.getDx(instancefg) + (j*8),tfg.getDy(instancefg) + (i*8),8,8,false);
			}
		}
//		m.renderTile(-camera.getX()%TILE_SIZE + this.x + (x-1)*TILE_SIZE, -camera.getY()%TILE_SIZE+ this.y + (y-1)*TILE_SIZE,TILE_SIZE,TILE_SIZE, t.getDx(instance),t.getDy(instance),t.getDw(instance),t.getDh(instance));
	}
	
	public void draw(MainWindow m) {
//		map.setChangeMap("BG");
//		getAreaOfInterest();
//		drawTilesBG(m);
//		map.setChangeMap("FG");
//		getAreaOfInterest();
//		drawTiles(m);
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
	
	public ArrayList<ArrayList<Integer>> returnAreaOfInterest() {
		return areaOfInterest;
	}
	
	public ArrayList<ArrayList<Integer>> getAreaOfInterest(ArrayList<ArrayList<Integer>> map) {
		//viewX,viewY
		ArrayList<ArrayList<Integer>> rows = new ArrayList<ArrayList<Integer>>();
		//start from viewX,viewY as i, and increment by 1 each time while i < widthInTiles
		ArrayList<Integer> row = new ArrayList<Integer>();
		for (int j = camera.getY()/TILE_SIZE; j < this.heightInTiles + camera.getY()/TILE_SIZE + 2; j++) {
			for (int i = Math.max(0,camera.getX()/TILE_SIZE); i < widthInTiles + camera.getX()/TILE_SIZE + 2; i++) {
				int curId = map.get(j).get(i);
				row.add(curId);
			}
			rows.add(row);
			row = new ArrayList<Integer>();
		}
		return rows;
	}
	
}