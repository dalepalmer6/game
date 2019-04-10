package gamestate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import canvas.Controllable;
import canvas.Drawable;
import global.InputController;
import mapeditor.Map;
import mapeditor.MapEditMenu;
import mapeditor.Tile;
import menu.AnimationMenu;
import menu.AnimationMenuFadeFromBlack;
import menu.DrawableObject;
import menu.StartupNew;

public class GameState {
	private ArrayList<DrawableObject> drawables = new ArrayList<DrawableObject>();
	private ArrayList<Entity> entities;
	private Player player;
	private Camera camera;
	private Map map;
	private MapRenderer mapRenderer;
	private StartupNew state;
	private ArrayList<RedrawObject> ro;
	private ArrayList<PartyMember> party = new ArrayList<PartyMember>();
	private final String pathToMaps = "/maps/";
	private String currentMapName;
	
	public void addPartyMember(PartyMember n) {
		this.party.add(n);
	}
	
	public ArrayList<PartyMember> getPartyMembers() {
		return party;
	}
	
	public Map getMap() {
		return map;
	}
	
	public void setCurrentMap(String s) {
		currentMapName = s;
	}
	
	public void loadMap(int scale) {
		map.parseMap(scale,currentMapName);
		System.out.println("Successfully loaded.");
		AnimationMenu m = new AnimationMenuFadeFromBlack(state);
		m.createAnimMenu();
		state.getMenuStack().push(m);
	}
	
	public ArrayList<DrawableObject> getDrawables() {
		return drawables;
	}
	
	public void addToDrawables(ArrayList<DrawableObject> d) {
		drawables.addAll(d);
	}
	
	public void addToDrawables(DrawableObject d) {
		drawables.add(d);
	}
	
	public ArrayList<Entity> getEntityList() {
		return entities;
	}
	
	public void createPlayer(int scale) {
		BufferedReader br;
		String data;
		try {
			br = new BufferedReader(new FileReader(new File("mapinfo")));
			while ((data = br.readLine()) != null) {
				String[] datasplit = data.split(",");
				String texture = datasplit[0];
				int x = Integer.parseInt(datasplit[1]);
				int y = Integer.parseInt(datasplit[2]);
				Entity test = state.getEntityFromEnum("ninten").createCopy(x,y,24,32);
				player = new Player(scale,test,camera,state);
//				player.scaleUp(4);
				this.entities.add(player);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public GameState(StartupNew s) {
		//load in the map name from the external file
		this(s,new Map("podunk",300,300,s.tileMap,s));
	}
	
	public GameState(StartupNew s, Map m) {
		this.currentMapName = m.getMapId();
		this.state = s;
		this.camera = new Camera(state);
		this.map = m;
		loadMap(4);
		this.entities = map.getEntities();
		createPlayer(4);
		this.mapRenderer = new MapRenderer(m,getCamera(),state); 
		camera.setMapRenderer(mapRenderer);
//		addToDrawables(mapRenderer);
		ro = new ArrayList<RedrawObject>();
		addPartyMember(new PartyMember("Ness"));
//		addPartyMember(new PartyMember("Player2"));
//		addPartyMember(new PartyMember("Player3"));
//		addPartyMember(new PartyMember("Player4"));
//		createTestEnemy();
//		createTestDoor();
	}
	
//	public void createTestDoor() {
//		DoorEntity test = new DoorEntity(300*4,400*4,128,128,state,700,700,"magicant");
//		this.entities.add(test);
//	}
	
	public void createTestEnemy() {
		Enemy test = new Enemy("mook.png","Mook",32,64,30,30);
		Enemy test2 = new Enemy("mook.png","Mook",32,64,30,30);
		ArrayList<Enemy> testList = new ArrayList<Enemy>();
		testList.add(test);testList.add(test2);
		
		Entity eStatic = state.allEntities.get("redDressLady");
		
		EnemyEntity ee = new EnemyEntity("entities.png", 1200, 1200, 32,64,state,"redDressLady",testList);
		ee.setSpriteCoords(eStatic.getSpriteCoordinates());
		this.entities.add(ee);
	}
	
	void sort()
    {
        int n = entities.size();
 
        // One by one move boundary of unsorted subarray
        for (int i = 0; i < n-1; i++)
        {
            // Find the minimum element in unsorted array
            int min_idx = i;
            for (int j = i+1; j < n; j++)
                if (entities.get(j).getYOnScreen() + entities.get(j).getHeight()
                		< entities.get(min_idx).getYOnScreen() + entities.get(min_idx).getHeight())
                    min_idx = j;
 
            // Swap the found minimum element with the first
            // element
            Entity temp = entities.get(min_idx);
            entities.remove(min_idx);
            entities.add(min_idx, entities.get(i));
            entities.remove(i);
            entities.add(i,temp);
        }
    }
	
	
	public void drawGameState() {
		ArrayList<ArrayList<Integer>> bg = mapRenderer.getAreaOfInterest(map.tileMapBG);
		ArrayList<ArrayList<Integer>> fg = mapRenderer.getAreaOfInterest(map.tileMapFG);
//		ArrayList<ArrayList<Integer>> currentMap = mapRenderer.returnAreaOfInterest();
		for (int i = 1; i <  mapRenderer.getHeightInTiles()-1; i++) {
			for (int j = 1; j < mapRenderer.getWidthInTiles()-1; j++) {
				mapRenderer.drawTileBase(state.getMainWindow(),j,i,state.tileMap.getTile(0),0);
			}
			for (Entity e : entities) {
				if ((1+(camera.getY()%128 + e.getYOnScreen() + e.getHeight())/128) == i) {
					e.draw(state.getMainWindow());
				}
			}
			for (int j = 1; j < mapRenderer.getWidthInTiles()-1; j++) {
				map.setChangeMap("BG");
				int val = bg.get(i).get(j);
				Tile tile = state.tileMap.getTile(val);
				int instance  = map.inspectSurroundings(j + camera.getX()/128,i + camera.getY()/128);
				mapRenderer.drawTile(state.getMainWindow(),j,i,state.tileMap.getTile(val),instance);
				
				map.setChangeMap("FG");
				val = fg.get(i).get(j);
				tile = state.tileMap.getTile(val);
				instance  = map.inspectSurroundings(j+ camera.getX()/128,i + camera.getY()/128);
				mapRenderer.drawTile(state.getMainWindow(),j,i,state.tileMap.getTile(val),instance);
			}
//			for (Entity e : entities) {
//				if ((1+(camera.getY()%128 + e.getYOnScreen())/128) == i+1) {
//					e.draw(state.getMainWindow());
//				}
//			}
		}
//		drawMapRenderer();
//		drawEntities();
		for (RedrawObject robj : ro) {
			robj.draw(state.getMainWindow(), mapRenderer);
		}
		ro = new ArrayList<RedrawObject>();
	}
	
	public void drawMapRenderer() {
		mapRenderer.draw(state.getMainWindow());
	}
	
	public void drawEntities() {
		for (Entity e : entities) {
			e.draw(state.getMainWindow());
		}
	}

	
	public void parseInput(InputController input) {
		for (Entity e : entities) {
			e.handleInput(input);
		}
	}
	
	public void update(InputController input) {
		sort();
		if (state.getMenuStack().isEmpty() || state.getMenuStack().peek() instanceof AnimationMenu) {
			for (Entity e : entities) {
				if (e instanceof Controllable) {
					e.handleInput(input);
				}
				checkEntityCollisions();
				e.update(this);
				e.act();
			}
		}
	}
	
	public void checkEntityCollisions() {
		for (Entity e : entities) {
			for (Entity e2 : entities) {
				if (!(e == e2)) {
					if (e.checkCollision(e2)) {
						e.deltaX = 0;
						e.deltaY = 0;
						e2.deltaX = 0;
						e2.deltaY = 0;
					}
					if (e.checkCollisionWithTolerance(e2,8)) {
						e.addToInteractables(e2);
					} else {
						e.removeFromInteractables(e2);
					}
				}
			}
		}
	}

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	public MapRenderer getMapRenderer() {
		// TODO Auto-generated method stub
		return mapRenderer;
	}

	public void parseInput() {
		// TODO Auto-generated method stub
		
	}

	public void addToRedrawing(RedrawObject redrawObject) {
		// TODO Auto-generated method stub
		ro.add(redrawObject);
	}
	
}
