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
import menu.SwirlAnimation;
import tiles.ChangeWithFlagTile;
import tiles.TileInstance;

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
	private HashMap<String, Entity> removed;
	private ArrayList<Entity> partyMembersInEntities;
	private boolean enemiesCanJoin;
	private ArrayList<EnemyEntity> enemies;
	public boolean canEncounter = true;
	
	public void setCanEncounter(boolean b) {
		canEncounter = b;
	}
	
	public boolean getCanEncounter() {
		return canEncounter;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public boolean addPartyMember(String member) {
		for (PartyMember pm : party) {
			if (pm.getId().equals(member)) {
				return false;
			}
		}
		this.party.add(new PartyMember(member,party.size()+1,state));
		return true;
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
				teddy = state.getEntityFromEnum("teddy").createCopy(x,y,24,32,"ana");
				pippi = state.getEntityFromEnum("pippi").createCopy(x,y,24,32,"ana");
				partyMemberEntities.put("NINTEN",ninten);
				partyMemberEntities.put("LOID",loid);
				partyMemberEntities.put("ANA",ana);
				partyMemberEntities.put("TEDDY",teddy);
				partyMemberEntities.put("PIPPI",pippi);
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
		removed = new HashMap<String, Entity>();
	}
	
	public void loadMapData() {
		loadMap(4);
		state.setBGM(map.getBGM());
		state.playBGM();
		
		createPlayer(4);
		this.mapRenderer = new MapRenderer(map,getCamera(),state); 
		camera.setMapRenderer(mapRenderer);
		ro = new ArrayList<RedrawObject>();
		addPartyMember("NINTEN");
//		addPartyMember("ANA");
//		addPartyMember(new PartyMember("LOID",state));
//		addPartyMember(new PartyMember("TEDDY",4,state));
//		addPartyMember("PIPPI");
		createTestEnemy();
//		createTestDoor();
	}
	
	public void createTestEnemy() {
//		Enemy test = state.enemies.get(0).clone();
//		Enemy test2 = state.enemies.get(0).clone();
		ArrayList<Enemy> testList = new ArrayList<Enemy>();
//		testList.add(test);
//		testList.add(test2);
		
		Entity eStatic = state.allEntities.get("redDressLady");
		Enemy test2 = state.enemies.get("Lamp").clone();
		testList.add(test2);
		EnemyEntity ee = new EnemyEntity(1300, 2600, 24*4,32*4,state,testList);
		ee.setSpriteCoords(eStatic.getSpriteCoordinates());
		this.entities.add(ee);
		test2 = state.enemies.get("Stray Dog").clone();
		testList.add(test2);
		ee = new EnemyEntity(1100, 2600, 24*4,32*4,state,testList);
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
		for (int i = 1; i < mapRenderer.getHeightInTiles()-1; i++) {
			for (int j = 1; j < mapRenderer.getWidthInTiles()-1; j++) {
				map.setChangeMap("BASE");
				int val = base.get(i).get(j);
				Tile tile = state.tileMap.getTile(val);
				int instance  = map.inspectSurroundings(j + camera.getX()/128,i + camera.getY()/128);
				mapRenderer.drawTileBase(state.getMainWindow(),j,i,state.tileMap.getTile(val),instance);
			}
		}
		for (int i = 1; i < mapRenderer.getHeightInTiles()-1; i++) {
			int val;
			Tile tile;
			int instance;
			for (int j = 1; j < mapRenderer.getWidthInTiles()-1; j++) {
				map.setChangeMap("BG");
				int valbg = bg.get(i).get(j);
				Tile tilebg = state.tileMap.getTile(valbg);
				int instancebg  = map.inspectSurroundings(j + camera.getX()/128,i + camera.getY()/128);
				map.setChangeMap("FG");
				int valfg = fg.get(i).get(j);
				Tile tilefg = state.tileMap.getTile(valfg);
				int instancefg  = map.inspectSurroundings(j+ camera.getX()/128,i + camera.getY()/128);
				mapRenderer.drawTile(state.getMainWindow(),j,i,tilebg,instancebg,tilefg,instancefg);
//				mapRenderer.drawTile(state.getMainWindow(),j,i,state.tileMap.getTile(val),instance);
			}
			for (Entity e : entities) {
				e.draw(state.getMainWindow());
			}
		}
		for (RedrawObject robj : ro) {
			robj.draw(state.getMainWindow(),mapRenderer);
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
	
	public void updateSystemFlags() {
		//check if party members are in your party
		ArrayList<String> partyMemberIds = new ArrayList<String>();
		for (PartyMember m : party) {
			partyMemberIds.add(m.getId());
		}
		for (String id : partyMemberIds) {
			if (partyMemberIds.contains("NINTEN")) {
				flags.put("nintenInParty",true);
			} else {
				flags.put("nintenInParty",false);
			}
			if (partyMemberIds.contains("ANA")) {
				flags.put("anaInParty",true);	
			} else {
				flags.put("anaInParty",false);				
			}
			if (partyMemberIds.contains("LOID")) {
				flags.put("loidInParty",true);
			} else {
				flags.put("loidInParty",false);
			}
			if (partyMemberIds.contains("TEDDY")) {
				flags.put("teddyInParty",true);
			} else {
				flags.put("teddyInParty",false);
			}
			if (partyMemberIds.contains("PIPPI")) {
				flags.put("pippiInParty",true);
			} else {
				flags.put("pippiInParty",false);
			}
		}
		
		//check if certain items are in your inventory
	}
	
	public void updateTiles() {
		//only do this on a flag change or map load if slowdown occurs
		ArrayList<ChangeWithFlagTile> flagTiles = new ArrayList<ChangeWithFlagTile>();
		for (int i : state.tileMap.getTileMap().keySet()) {
			if (state.tileMap.getTile(i) instanceof ChangeWithFlagTile) {
				flagTiles.add((ChangeWithFlagTile) state.tileMap.getTile(i));
			}
		}
		if (flagTiles.isEmpty()) {
			return;
		}
		for (int i = 1; i < map.getHeight()-1; i++) {
			for (int j = 1; j < map.getWidth()-1; j++) {
				ArrayList<ArrayList<Integer>> cMap;
				ArrayList<ArrayList<TileInstance>> tiMap;
				map.setChangeMap("BG");
				cMap = map.getLayerMap();
				tiMap = map.getLayerInstances();
				int val = cMap.get(i).get(j);
				Tile tile = state.tileMap.getTile(val);
				if (tile instanceof ChangeWithFlagTile) {
					String fName = ((ChangeWithFlagTile) tile).getFlagName();
					if (getFlag(fName)) {
						for (ChangeWithFlagTile t : flagTiles) {
							if (tile == t) {
								cMap.get(i).set(j,t.getNewTileId());
								TileInstance newTi = state.tileMap.getTile(t.getNewTileId()).getInstance(0);
								tiMap.get(i-1).set(j-1,newTi);
								continue;
							}
						}
					}
				}
				map.setChangeMap("FG");
				cMap = map.getLayerMap();
				tiMap = map.getLayerInstances();
				val = cMap.get(i).get(j);
				tile = state.tileMap.getTile(val);
				if (tile instanceof ChangeWithFlagTile) {
					String fName = ((ChangeWithFlagTile) tile).getFlagName();
					if (getFlag(fName)) {
						for (ChangeWithFlagTile t : flagTiles) {
							if (tile == t) {
								cMap.get(i).set(j,t.getNewTileId());
								tiMap.get(i-1).set(j-1,state.tileMap.getTile(t.getNewTileId()).getInstance(0));
								continue;
							}
						}
					}
				}
			}
		}
	}
	
	public void update(InputController input) {
		updateSystemFlags();
		updateTiles();
		//get all entities that belong in the map (in the viewport bounds)
		while (numPartyMembers < party.size()) {
			for (PartyMember m : party) {
				for (String id : partyMemberEntities.keySet()) {
					if (m.getId().equals(id)) {
						if (numPartyMembers == 0) {
							player = new Player(4,partyMemberEntities.get(id),camera,state);
							removed.put(id,partyMemberEntities.get(id));
							this.entities.add(player);
						} else if (numPartyMembers < 4) {
							FollowingPlayer fent = new FollowingPlayer(4,partyMemberEntities.get(id),state,numPartyMembers);
							removed.put(id,partyMemberEntities.get(id));
							this.entities.add(fent);
						}
						numPartyMembers++;
					}
				}
				for (String id : removed.keySet()) {
					partyMemberEntities.remove(id);
				}
			}
		}
		
		
		for (Entity e : map.getEntitiesInView(camera.getX(), camera.getY())) {
			if (!entities.contains(e) && entityReady(e)) {
				entities.add(e);
			} else if (entities.contains(e) && getFlag(e.getDisappearFlag())) {
				e.setToRemove(true);
			}
			for (Entity entityFromMap : map.getEntities()) {
				if (!map.getEntitiesInView(camera.getX(), camera.getY()).contains(entityFromMap)) {
					entityFromMap.setToRemove(true);
				}
			}
		}
		
		sort();
		if (state.getMenuStack().isEmpty() || state.getMenuStack().peek() instanceof AnimationMenu || state.getMenuStack().peek() instanceof Cutscene) {
			updatePlayer(input);
			updateEntities(input,true);
		}
	}
	
	private boolean entityReady(Entity e) {
		if (getFlag(e.getDisappearFlag())) {
			return false;
		}
		if ((e.getAppearFlag().equals(" ") ||
			getFlag(e.getAppearFlag()))) {
			return true;
		}
		return false;
	}

	public void updatePlayer(InputController input) {
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			if (e instanceof Controllable) {
				e.handleInput(input);
				break;
			}
		}
	}
	
	public void updateEntities(InputController input, boolean entitiesCanMove) {
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			if (e.getNeedToRemoveState()) {
				e.setToRemove(false);
				e.kill();
				entities.remove(e);
				continue;
			}
			checkEntityCollisions();
			if (!entitiesCanMove) {
				e.setDeltaXY(0,0);
			}

			e.update(this);
			if (entitiesCanMove) {
				e.update();
				e.act();			
			}
			
			if (state.getMenuStack().peek() instanceof AnimationMenu) {
//				if (state.getMenuStack().peek().isSwirl()) {
					if (e instanceof EnemyEntity) {
						e.update();
						e.act();
					}
//				}
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
					if (!(e instanceof DoorEntity || e instanceof EnemySpawnEntity)) {
						if (e.checkCollision(e2)) {
							e.deltaX = 0;
							e.deltaY = 0;
							e2.deltaX = 0;
							e2.deltaY = 0;
						}
					}
						if (e.checkCollisionWithTolerance(e2,8)) {
							e.addToInteractables(e2);
						} else {
//							e.removeFromInteractables(e2);
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

	public void setEnemiesCanJoin(boolean b) {
		// TODO Auto-generated method stub
		enemiesCanJoin = b;
	}

	public boolean getEnemiesCanJoin() {
		// TODO Auto-generated method stub
		return enemiesCanJoin;
	}

	public void setBattleEnemyList(ArrayList<EnemyEntity> enemies) {
		// TODO Auto-generated method stub
		this.enemies = enemies;
	}

	public void addEnemyToBattleList(EnemyEntity e) {
		// TODO Auto-generated method stub
		if (!enemies.contains(e)) {
			enemies.add(e);
		}
		
	}
	
}
