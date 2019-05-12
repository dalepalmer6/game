package gamestate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import canvas.Controllable;
import canvas.Drawable;
import gamestate.elements.items.Item;
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
	private Map lastSavedMap;
	private int[] savedCoordinates;
	private int startX;
	private int startY;
	private int invincibleCounter;
	private int numEnemies;
	private long timeCreated;
	private long timeNow;
	private long timeLast = 0;
	private ArrayList<Entity> savedParty;
	private boolean trainNotAdded;
	private boolean trainAdded;
	private TrainEntity train;
	private int trainStartIndex = 0;
	private int trainEndIndex = 0;
	private Item itemToBuy;
	private int funds = 20;
	
	public long getDeltaTime() {
		return (long) ((System.nanoTime() - timeNow) / 1e9);
	}
	
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
	
	public void reloadInitialMap() {
		createStartPosition();
		ArrayList<Entity> players = new ArrayList<Entity>();
		for (Entity e : entities) {
			if (e instanceof Player) {
				e.setCoordinates(startX,startY);
				players.add(e);
			} else if (e instanceof FollowingPlayer) {
				e.setCoordinates(startX,startY);
				players.add(e);
			}
		}
		state.getGameState().getEntityList().clear();
		state.getGameState().getEntityList().addAll(players);
		state.getGameState().getCamera().snapToEntity(startX,startY);
		state.getGameState().setCurrentMap("house - myhome");
		state.getGameState().loadMap(4);
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
	
	public void createStartPosition() {
		String data;
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(new File("savedata/mapinfo")));
			while ((data = br.readLine()) != null) {
				String[] datasplit = data.split(",");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void createPlayer(int scale) {
		BufferedReader br;
		String data;
		try {
			br = new BufferedReader(new FileReader(new File("savedata/mapinfo")));
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
		//should be loaded from the save data if using a continue
		this(s,new Map("house - myhome",5,5,s.tileMap,s));
	}
	
	public GameState(StartupNew s, Map m) {
		this.timeCreated = System.nanoTime();
		lastSavedMap = m;
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
//		createTestEnemy();
	}
	
	public void createTestEnemy() {
		ArrayList<Enemy> testList = new ArrayList<Enemy>();
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
	
	public void setInvincibleCounter() {
		invincibleCounter = 180; //3 secs
	}
	
	public boolean isInvincible() {
		if (invincibleCounter > 0) {
			return true;
		}
		return false;
	}
	
	public void update(InputController input) {
		if (getFlag("train") && !trainAdded) {
			//replace the party with a single train entity which extends the CameraControllingEntity
			entities.removeAll(savedParty);
			Entity e = state.getEntityFromEnum("bus").createCopy(player.getX(),player.getY(),64*4,48*4,"train");
			train = new TrainEntity(e, camera, state);
			entities.add(train);
			TrainCutscene trainCutscene = new TrainCutscene(state,trainStartIndex,trainEndIndex);
			trainCutscene.loadEntityToCutsceneData();
			state.getMenuStack().push(trainCutscene);
			trainAdded = true;
			
		} else if (!getFlag("train") && trainAdded) {
			entities.remove(train);
			entities.addAll(savedParty);
			player.setXY(train.getX(),train.getY());
			trainAdded = false;
		}
		timeNow = System.nanoTime();
		if (invincibleCounter > 0) {
			invincibleCounter--;
			canEncounter = false;
		} else {
			canEncounter = true;
		}
		updateSystemFlags();
		updateTiles();
		//get all entities that belong in the map (in the viewport bounds)
		while (numPartyMembers < party.size() && !getFlag("train")) {
			savedParty = new ArrayList<Entity>();
			for (PartyMember m : party) {
				for (String id : partyMemberEntities.keySet()) {
					if (m.getId().equals(id)) {
						if (numPartyMembers == 0) {
							player = new Player(4,partyMemberEntities.get(id),camera,state);
							removed.put(id,partyMemberEntities.get(id));
							savedParty.add(player);
							this.entities.add(player);
						} else if (numPartyMembers < 4) {
							FollowingPlayer fent = new FollowingPlayer(4,partyMemberEntities.get(id),state,numPartyMembers);
							removed.put(id,partyMemberEntities.get(id));
							this.entities.add(fent);
							savedParty.add(fent);
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
//			checkEntityCollisions();
			if (!entitiesCanMove) {
				e.setDeltaXY(0,0);
			}

//			e.update(this);
			if (entitiesCanMove) {
				e.update(this);
				e.act();			
			}
			
			if (state.getMenuStack().peek() instanceof AnimationMenu) {
				if (state.getMenuStack().peek().isSwirl()) {
					if (e instanceof EnemyEntity) {
						e.update(this);
						e.act();
					}
				}	
				if (e instanceof DoorEntity) {
					e.update(this);
					e.act();
				}
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
						if (e.checkCollisionWithTolerance(e2,32)) {
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
	
	public void setFlag(String flagName, boolean state) {
		flags.put(flagName, state);
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

	public void addNumEntities(int size) {
		// TODO Auto-generated method stub
		numEnemies += size;
	}

	public int getNumEnemies() {
		// TODO Auto-generated method stub
		return numEnemies;
	}

	public TrainEntity getTrain() {
		// TODO Auto-generated method stub
		return train;
	}

	public void setTrainRide(int x, int y) {
		// TODO Auto-generated method stub
		trainStartIndex = x;
		trainEndIndex = y;
	}

	public void setItemToBuy(Item item) {
		// TODO Auto-generated method stub
		itemToBuy = item;
	}

	public int getFundsOnHand() {
		// TODO Auto-generated method stub
		return funds ;
	}
	
	public void spendFunds(int amt) {
		funds -= amt;
	}

	public void saveData() throws IOException {
		//write all flags to a file
		File file;
		String savedataLoc = "savedata/test/";
		file = new File(savedataLoc);
		file.mkdirs();
		file = new File(savedataLoc + "flags");
		file.createNewFile();
		PrintWriter flagsOut = new PrintWriter(file);
		for (String flagName : flags.keySet()) {
			flagsOut.println(flagName + "," + getFlag(flagName));
		}
		flagsOut.flush();
		flagsOut.close();
		//write all party members to their respective files
		for (PartyMember pm : party) {
			file = new File(savedataLoc + pm.getId());
			file.createNewFile();
			PrintWriter pw = new PrintWriter(file);
			pw.println(pm.getName() + "," + pm.getStats().toString());
			String items = "";
			for (Item i : pm.getItemsList()) {
				items += i.getId() + ",";
			}
			pw.println(items);
			pw.println(Long.toBinaryString(pm.getKnownPSI()));
			String equips="";
			for (Item i : pm.getEquips()) {
				equips += i.getId() +  ",";
			}
			pw.println(equips);
			pw.flush();
			pw.close();
		}
		
		//write all system info, money on hand, in bank, x,y, map name
		file = new File(savedataLoc + "sys");
		file.createNewFile();
		PrintWriter pw = new PrintWriter(file);
		pw.println(currentMapName + "," + player.getX() + "," + player.getY() + "," + funds);
		pw.flush();
		pw.close();
	}
	
}
