package gamestate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

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
	private boolean canSpawnEnemies = true;
	private int[] dx = new int[48];
	private int[] dy = new int[48];
	private Entity ninten;
	private Entity ana;
	private Entity loid;
	private Entity teddy;
	private Entity pippi;
	private int numPartyMembers=0;
	private HashMap<String,Entity> partyMemberEntities = new HashMap<String,Entity>();
	private HashMap<String, Boolean> flags;
	
	public Player getPlayer() {
		return player;
	}
	
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
				//create all PartyMember entities
				ninten = state.getEntityFromEnum(texture).createCopy(x,y,24,32,"ninten");
				loid = state.getEntityFromEnum("loid").createCopy(x,y,24,32,"loid");
				ana = state.getEntityFromEnum("ana").createCopy(x,y,24,32,"ana");
				teddy = state.getEntityFromEnum("ana").createCopy(x,y,24,32,"ana");
				pippi = state.getEntityFromEnum("ana").createCopy(x,y,24,32,"ana");
				partyMemberEntities.put("NINTEN",ninten);
				partyMemberEntities.put("LOID",loid);
				partyMemberEntities.put("ANA",ana);
//				partyMemberEntities.put("TEDDY",teddy);
//				partyMemberEntities.put("PIPPI",pippi);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public GameState(StartupNew s) {
		//load in the map name from the external file
		this(s,new Map("house - myhome",5,5,s.tileMap,s));
	}
	
	public GameState(StartupNew s, Map m) {
		this.entities = new ArrayList<Entity>();
		this.currentMapName = m.getMapId();
		this.state = s;
		this.camera = new Camera(state);
		this.map = m;
		flags = new HashMap<String,Boolean>();
	}
	
	public void loadMapData() {
		loadMap(4);
		state.setBGM(map.getBGM());
		state.playBGM();
		
		createPlayer(4);
		this.mapRenderer = new MapRenderer(map,getCamera(),state); 
		camera.setMapRenderer(mapRenderer);
//		addToDrawables(mapRenderer);
		ro = new ArrayList<RedrawObject>();
		addPartyMember(new PartyMember("NINTEN",1,state));
		addPartyMember(new PartyMember("ANA",2,state));
		addPartyMember(new PartyMember("LOID",3,state));
//		addPartyMember(new PartyMember("TEDDY",4,state));
//		addPartyMember(new PartyMember("PIPPI",4,state));
		createTestEnemy();
//		createTestDoor();
	}
	
	public void createTestEnemy() {
//		Enemy test = state.enemies.get(0).clone();
//		Enemy test2 = state.enemies.get(0).clone();
		ArrayList<BattleEntity> testList = new ArrayList<BattleEntity>();
//		testList.add(test);
//		testList.add(test2);
		
		Entity eStatic = state.allEntities.get("redDressLady");
		Enemy test = state.enemies.get("Lamp").clone();
		Enemy test2 = state.enemies.get("Lamp").clone();
//		testList.add(test);
		testList.add(test2);
		EnemyEntity ee = new EnemyEntity("entities.png", 1300, 2600, 24*4,32*4,state,"redDressLady",testList);
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
		boolean redrawPrev = false;
		ArrayList<ArrayList<Integer>> bg = mapRenderer.getAreaOfInterest(map.tileMapBG);
		ArrayList<ArrayList<Integer>> fg = mapRenderer.getAreaOfInterest(map.tileMapFG);
		ArrayList<ArrayList<Integer>> base = mapRenderer.getAreaOfInterest(map.tileMapBase);
//		ArrayList<ArrayList<Integer>> currentMap = mapRenderer.returnAreaOfInterest();
		for (int i = 1; i < mapRenderer.getHeightInTiles()-1; i++) {
			for (int j = 1; j < mapRenderer.getWidthInTiles()-1; j++) {
				map.setChangeMap("BASE");
//				mapRenderer.drawTileBase(state.getMainWindow(),j,i,state.tileMap.getTile(0),0);
//				map.setChangeMap("BG");
				int val = base.get(i).get(j);
				Tile tile = state.tileMap.getTile(val);
				int instance  = map.inspectSurroundings(j + camera.getX()/128,i + camera.getY()/128);
				mapRenderer.drawTileBase(state.getMainWindow(),j,i,state.tileMap.getTile(val),instance);
			}
		}
		for (int i = 1; i < mapRenderer.getHeightInTiles()-1; i+=2) {
			int val;
			Tile tile;
			int instance;
			for (int j = 1; j < mapRenderer.getWidthInTiles()-1; j++) {
				map.setChangeMap("BG");
				val = bg.get(i).get(j);
				tile = state.tileMap.getTile(val);
				instance  = map.inspectSurroundings(j + camera.getX()/128,i + camera.getY()/128);
				mapRenderer.drawTile(state.getMainWindow(),j,i,state.tileMap.getTile(val),instance);
				val = bg.get(i+1).get(j);
				tile = state.tileMap.getTile(val);
				instance  = map.inspectSurroundings(j + camera.getX()/128,(i+1) + camera.getY()/128);
				mapRenderer.drawTile(state.getMainWindow(),j,i+1,state.tileMap.getTile(val),instance);
			}
			for (Entity e : entities) {
				if ((1+(camera.getY()%128 + e.getYOnScreen())/128) == i+1 && (1+(camera.getY()%128 + e.getYOnScreen())/128) == i) {
					e.draw(state.getMainWindow());
				}
				if ((1+(camera.getY()%128 + e.getYOnScreen())/128) == i+1) {
					e.draw(state.getMainWindow());
				}
				if ((1+(camera.getY()%128 + e.getYOnScreen())/128) == i) {
					e.draw(state.getMainWindow());
				}
			}
			for (int j = 1; j < mapRenderer.getWidthInTiles()-1; j++) { 
				map.setChangeMap("FG");
				val = fg.get(i).get(j);
				tile = state.tileMap.getTile(val);
				instance  = map.inspectSurroundings(j+ camera.getX()/128,i + camera.getY()/128);
				mapRenderer.drawTile(state.getMainWindow(),j,i,state.tileMap.getTile(val),instance);
				val = fg.get(i+1).get(j);
				tile = state.tileMap.getTile(val);
				instance  = map.inspectSurroundings(j + camera.getX()/128,(i+1) + camera.getY()/128);
				mapRenderer.drawTile(state.getMainWindow(),j,i+1,state.tileMap.getTile(val),instance);
			}
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
		//get all entities that belong in the map (in the viewport bounds)
		while (numPartyMembers < party.size()) {
			for (PartyMember m : party) {
				for (String id : partyMemberEntities.keySet()) {
					if (m.getId().equals(id)) {
						if (numPartyMembers == 0) {
							player = new Player(4,partyMemberEntities.get(id),camera,state);
							this.entities.add(player);
						} else if (numPartyMembers < 4) {
							FollowingPlayer fent = new FollowingPlayer(4,partyMemberEntities.get(id),state,numPartyMembers);
							this.entities.add(fent);
						}
						numPartyMembers++;
					}
				}
			}
		}
		
		
		for (Entity e : map.getEntitiesInView(camera.getX(), camera.getY())) {
			if (!entities.contains(e)) {
				entities.add(e);
			}
		}
		
		sort();
		if (state.getMenuStack().isEmpty() || state.getMenuStack().peek() instanceof AnimationMenu || state.getMenuStack().peek() instanceof Cutscene) {
//			if (camera.getDX() != 0 || camera.getDY() != 0) {
				//try 5 times per frame to spawn
				canSpawnEnemies =false;
				if (Math.random() < 0.01 && canSpawnEnemies ) {
					int spawnCenterX = player.getX();
					int spawnCenterY = player.getY();
					double posneg = Math.random();
					double spawnValX = 0d;
					double spawnValY = 0d;
					if (posneg < 0.5) {
						spawnValX = player.getX() + 1000 + (Math.random()*500);
						spawnValY = player.getY() + 1000 + (Math.random()*500);
					} 
					else {
						spawnValX = Math.max(0,player.getX() - 1000 - (Math.random()*500));
						spawnValY = Math.max(0,player.getY() - 1000 - (Math.random()*500));
					}
					
//					if (spawnValX > spawnCenterX + 1000 && spawnValX < spawnCenterX + 1500
//							&& spawnValY > spawnCenterY + 1000 && spawnValY > spawnCenterY + 1500) {
						ArrayList<BattleEntity> testList = new ArrayList<BattleEntity>();
						Entity eStatic = state.allEntities.get("redDressLady");
						Enemy test = state.enemies.get(0).clone();
						testList.add(test);
						EnemyEntity ee = new EnemyEntity("entities.png", (int) spawnValX, (int)spawnValY, 24*4,32*4,state,"redDressLady",testList);
						ee.setSpriteCoords(eStatic.getSpriteCoordinates());
						this.entities.add(ee);
//					}
				}
//					Random rand = new Random();
//					int rightFirstBound = rand.nextInt(spawnCenterX + 1000);
//					int rightSecondBound = rand.nextInt(spawnCenterX + 1500);
//					int leftFirstBound = rand.nextInt(spawnCenterX - 1000);
//					int leftSecondBound = rand.nextInt(spawnCenterX - 1500);
//					int upFirstBound = rand.nextInt(spawnCenterY - 1000);
//					int upSecondBound = rand.nextInt(spawnCenterY - 1500);
//					int downFirstBound = rand.nextInt(spawnCenterY + 1000);
//					int downSecondBound = rand.nextInt(spawnCenterY + 1500);
//				}
//			}
			updateEntities(input,true);
		}
	}
	
	public void updateEntities(InputController input, boolean entitiesCanMove) {
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			if (e instanceof Controllable) {
				e.handleInput(input);
			}
			if (e.getNeedToRemoveState()) {
				e.setToRemove(false);
				entities.remove(e);
				continue;
			}
			checkEntityCollisions();
			if (!entitiesCanMove) {
				e.setDeltaXY(0,0);
			}
			e.update(this);
			if (entitiesCanMove) {
				e.act();			
			}
		}
	}
	
	public void updatePartyMembers() {
		for (PartyMember pm : party) {
			pm.updateStats();
		}
	}
	
	public void checkEntityCollisions() {
		
		for (Entity e : entities) {
			e.clearInteractables();
			if (e instanceof FollowingPlayer) {
				continue;
			}
			for (Entity e2 : entities) {
				if (e2 instanceof FollowingPlayer) {
					continue;
				}
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
//						e.removeFromInteractables(e2);
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

	public void setFlag(String flagName) {
		flags.put(flagName,true);
		
	}
	
	public boolean getFlag(String flagName) {
		try {
			return flags.get(flagName);
		} catch(NullPointerException e) {
			return false;
		}
	}
	
}
