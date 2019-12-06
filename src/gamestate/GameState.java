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

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import gamestate.camera.Camera;
import gamestate.elements.items.Item;
import gamestate.entities.DoorEntity;
import gamestate.entities.EnemyEntity;
import gamestate.entities.Entity;
import gamestate.entities.FollowingPlayer;
import gamestate.entities.IntroEntity;
import gamestate.entities.Player;
import gamestate.entities.TrainEntity;
import gamestate.map.MapRenderer;
import gamestate.partymembers.PartyMember;
import menu.DrawableObject;
import menu.animation.AnimationMenu;
import menu.animation.AnimationMenuFadeFromBlack;
import system.SystemState;
import system.controller.InputController;
import system.map.Map;
import system.map.Tile;
import tiles.ChangeWithFlagTile;
import tiles.TileInstance;

public class GameState {
	private ArrayList<DrawableObject> drawables = new ArrayList<DrawableObject>();
	private ArrayList<Entity> entities;
	private Player player;
	private Camera camera;
	private Map map;
	private MapRenderer mapRenderer;
	private SystemState state;
	private ArrayList<RedrawObject> ro;
	private ArrayList<PartyMember> party = new ArrayList<PartyMember>();
	private final String pathToMaps = "/maps/";
	public String currentMapName;
	private boolean canSpawnEnemies = true;
	private int[] dx = new int[48];
	private int[] dy = new int[48];
	private int numPartyMembers=0;
	private HashMap<String,Entity> partyMemberEntities = new HashMap<String,Entity>();
	private HashMap<String,PartyMember> partyMembers = new HashMap<String,PartyMember>();
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
	private int teleportCounter = 0;
	private int teleportTimer;
	private String teleportDest;
	private int teleportDestX;
	private int teleportDestY;
	private double counter;
	private int bankFunds = 0;
	private int depositedFunds = 0;
	private boolean shouldDraw = true;
	private int windowArgument;
	private double initialX=-1;
	private double initialY=-1;
	private String initialMapName;
	private boolean loadOriginalStats;
	private int teleportFailedTimer = -1;
	private boolean doIntro;
	private boolean loadedNewGameData;
	private String savedBGM;
	private int maxAlliesCanJoin;
	private List<EnemyEntity> savedEnemyEntities;
	private long timer;
	private boolean canRun;
	
	public long getTimePlayed() {
		return timer;
	}
	
	public String getFormattedTimePlayed() {
		//the timer represents the number of frames elapsed in gameplay.
		//use % and / to get mins and hours -> divide by 60 to get seconds
		String time = "";
		long secs = timer / 60;
		long mins = secs / 60;
		long hours = secs / 3600;
		time = String.format("%02d : %02d",hours, mins);
		return time;
	}
	
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
		PartyMember pm = partyMembers.get(member);
		pm.setIndex(party.size()+1);
		this.party.add(pm);
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
//		createStartPosition();
		ArrayList<Entity> players = new ArrayList<Entity>();
		for (Entity e : entities) {
			if (e instanceof Player) {
				e.setCoordinates(initialX,initialY);
				players.add(e);
			} 
			else if (e instanceof FollowingPlayer) {
				e.setCoordinates(initialX,initialY);
				players.add(e);
			}
		}
		state.getGameState().getEntityList().clear();
		state.getGameState().getEntityList().addAll(players);
		state.getGameState().getCamera().snapToEntity(initialX,initialY);
		state.getGameState().setCurrentMap(initialMapName);
		state.getGameState().loadMap(4);
	}
	
	public void loadMap(int scale) {
		camera.setStop(false);
		map.parseMap(scale,currentMapName);
//		SystemState.out.println("Successfully loaded.");
		AnimationMenu m = new AnimationMenuFadeFromBlack(state);
		state.setBGM(map.getBGM());
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
				double x = 0;
				double y = 0;
				if (initialX == -1 && initialY == -1) {
					x = Integer.parseInt(datasplit[1]);
					y = Integer.parseInt(datasplit[2]);
				}else {
					x = initialX;
					y = initialY;
					initialX *= 4;
					initialY *= 4;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public GameState() {
		shouldDraw = false;
		flags = new HashMap<String,Boolean>();
	}
	
	public GameState(SystemState s) {
		//load in the system.map name from the external file
		//should be loaded from the save data if using a continue
		this(s,new Map("house - myhome",5,5,s.tileMap,s));
	}
	
	public GameState(SystemState s, Map m) {
		this.timeCreated = System.nanoTime();
		lastSavedMap = m;
		this.entities = new ArrayList<Entity>();
		if (m != null) {
			this.currentMapName = m.getMapId();
		}
		timer = 0;
		this.state = s;
		this.camera = new Camera(state);
		this.map = m;
		flags = new HashMap<String,Boolean>();
		removed = new HashMap<String, Entity>();
	}
	
	public void createAllPartyMembersInMem(String savedata) {
		if (savedata.equals("")) {
			savedata = "players";
		}
		partyMembers.put("NINTEN",new PartyMember("NINTEN",savedata,state));
		partyMembers.put("ANA",new PartyMember("ANA",savedata,state));
		partyMembers.put("LOID",new PartyMember("LOID",savedata,state));
		partyMembers.put("TEDDY",new PartyMember("TEDDY",savedata,state));
		partyMembers.put("PIPPI",new PartyMember("PIPPI",savedata,state));
	}

	public void loadFromSaveFile(String saveName) {
		String path = "savedata/" + saveName + "/";
		File f = new File(path);
		if (f.isDirectory()) {
			//load the game
			createAllPartyMembersInMem(saveName);
			f = new File(path + "sys");
			try {
				BufferedReader br = new BufferedReader(new FileReader(f));
				String[] data = br.readLine().split(",");
				String mapName = data[0];
				double x = Double.parseDouble(data[1]);
				double y = Double.parseDouble(data[2]);
				int funds = Integer.parseInt(data[3]);
				int fundsInBank = Integer.parseInt(data[4]);
				int fundsDeposited = Integer.parseInt(data[5]);
				long timer = Long.parseLong(data[6]);
				
				currentMapName = mapName;
				initialX = x;
				initialY = y;
				initialMapName = mapName;
//				createPlayer(4);
				this.funds = funds;
				this.bankFunds = fundsInBank;
				this.depositedFunds = fundsDeposited;
				this.timer = timer;
				
				f = new File(path + "flags");
				br = new BufferedReader(new FileReader(f));
				String row;
				while ((row=br.readLine()) != null) {
					String[] split = row.split(",");
					String name = split[0];
					boolean val = Boolean.parseBoolean(split[1]);
					flags.put(name,val);
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			createAllPartyMembersInMem("");
		}
	}
	
	public String getMapName() {
		return currentMapName;
	}
	
	public String getNintenName() {
		return partyMembers.get("NINTEN").getName();
	}
	
	public int getNintenLevel() {
		return partyMembers.get("NINTEN").getStats().getStat("LVL");
	}
	
	public void saveBGMFromMap() {
		savedBGM = map.getBGM();
	}
	
	public void loadMapData() {
		boolean override = false;
//		if (system.map != null) {
//			if (system.map.getBGM() != null && savedBGM != null) {
//				if (!system.map.getBGM().equals(savedBGM)) {
//					//save the override bgm and overwrite them
//					override = true;
//					saveBGMFromMap();
//				}
//			}
//		}
		loadMap(4);
//		if (override) {
//			system.map.setBGM(system.map.getBGM());
//		} else if (savedBGM != null) {
//			state.setBGM(savedBGM);
//		}
		
		state.setBGM(map.getBGM());
		
//		state.playBGM();
		if (player == null) {
			createPlayer(4);
		}
		
		this.mapRenderer = new MapRenderer(map,getCamera(),state); 
		camera.setMapRenderer(mapRenderer);
		ro = new ArrayList<RedrawObject>();
		addPartyMembersNeeded();
//		addPartyMember("NINTEN");
//		addPartyMember("PIPPI");
//		addPartyMember("LOID");
		
//		createTestEnemy();
	}
	
private void addPartyMembersNeeded() {
		// TODO Auto-generated method stub
		if (getFlag("nintenInParty")) {
			addPartyMember("NINTEN");
		}
		if (getFlag("anaInParty")) {
			addPartyMember("ANA");
		}
		if (getFlag("loidInParty")) {
			addPartyMember("LOID");
		}
		if (getFlag("teddyInParty")) {
			addPartyMember("TEDDY");
		}
		if (getFlag("pippiInParty")) {
			addPartyMember("PIPPI");
		}
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
	
	
	void sortRo()
    {
        int n = ro.size();
 
        // One by one move boundary of unsorted subarray
        for (int i = 0; i < n-1; i++)
        {
            // Find the minimum element in unsorted array
            int min_idx = i;
            for (int j = i+1; j < n; j++)
                if (ro.get(j).getY() + ro.get(j).getHeight()
                		< ro.get(min_idx).getY() + ro.get(min_idx).getHeight())
                    min_idx = j;
 
            // Swap the found minimum element with the first
            // element
            RedrawObject temp = ro.get(min_idx);
            ro.remove(min_idx);
            ro.add(min_idx, ro.get(i));
            ro.remove(i);
            ro.add(i,temp);
        }
    }
	
	public void drawGameState() {
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, state.tilesetTexture.getTextureID());
		boolean redrawPrev = false;
		ArrayList<ArrayList<Integer>> bg = mapRenderer.getAreaOfInterest(map.tileMapBG);
		ArrayList<ArrayList<Integer>> fg = mapRenderer.getAreaOfInterest(map.tileMapFG);
		ArrayList<ArrayList<Integer>> base = mapRenderer.getAreaOfInterest(map.tileMapBase);
		for (int i = 1; i < mapRenderer.getHeightInTiles()-1; i++) {
			for (int j = 1; j < mapRenderer.getWidthInTiles()-1; j++) {
				map.setChangeMap("BASE");
				int val = base.get(i).get(j);
				Tile tile = state.tileMap.getTile(val);
				int instance  = map.inspectSurroundings((int)(j + camera.getX()/128),(int)(i + camera.getY()/128));
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
				int instancebg  = map.inspectSurroundings((int)(j + camera.getX()/128),(int)(i + camera.getY()/128));
				map.setChangeMap("FG");
				int valfg = fg.get(i).get(j);
				Tile tilefg = state.tileMap.getTile(valfg);
				int instancefg  = map.inspectSurroundings((int)(j + camera.getX()/128),(int)(i + camera.getY()/128));
				mapRenderer.drawTile(state.getMainWindow(),j,i,tilebg,instancebg,tilefg,instancefg);
			}
		}
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, state.getTextureAtlas().getTexture().getTextureID());
		for (Entity e : entities) {
			e.drawIfBehind(state.getMainWindow());
//			e.stageForRedraw();
		}
//		sortRo();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, state.tilesetTexture.getTextureID());
		for (RedrawObject robj : ro) {
			robj.draw(state.getMainWindow(),mapRenderer);
		}
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, state.getTextureAtlas().getTexture().getTextureID());
		for (Entity e : entities) {
			e.drawIfFront(state.getMainWindow());
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
		
		//check if certain items are in your inventory like Bottle Rockets or Ana's hat.
	}
	
	public void updateTiles() {
		//only do this on a flag change or system.map load if slowdown occurs
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
	
	public void createParty() {
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
							fent.setXY(player.getX(),player.getY());
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
	}
	
	public boolean getDoIntro() {
		return doIntro;
	}
	
	public void updateTimer() {
		timer++;
	}
	
	public boolean canRun() {
		return canRun && state.getMenuStack().isEmpty();
	}
	
	public void update(InputController input) {
		
	}
	
	public void updateEntities(InputController input, boolean entitiesCanMove) {
		
	}
	
	public void checkEntityCollisions() {
		
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

	public void setMaxAllies(int maxAlliesCanJoin) {
		this.maxAlliesCanJoin = maxAlliesCanJoin;
	}
	
	public int getMaxAllies() {
		return maxAlliesCanJoin;
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
	
	public void resetNumEntities() {
		numEnemies = 0;
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

	public void saveData(String saveName) throws IOException {
		//write all flags to a file
		File file;
		String savedataLoc = "savedata/" + saveName + "/";
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
		for (String s : partyMembers.keySet()) {
			PartyMember pm = partyMembers.get(s);
			file = new File(savedataLoc + pm.getId().toLowerCase());
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
		
		//write all system info, money on hand, in bank, x,y, system.map name
		file = new File(savedataLoc + "sys");
		file.createNewFile();
		PrintWriter pw = new PrintWriter(file);
		pw.println(currentMapName + "," + player.getX()/4 + "," + player.getY()/4 + "," + funds + "," + bankFunds + "," + depositedFunds + "," + timer);
		pw.flush();
		pw.close();
		initialX = player.getX();
		initialY = player.getY();
		initialMapName = currentMapName;
	}

	public void setTeleportVariables(String newMapName, int newX, int newY) {
		// TODO Auto-generated method stub
		teleportDest = newMapName;
		teleportDestX = newX;
		teleportDestY = newY;
	}
	
	public void depositFunds(int amount) {
		funds -= amount;
		bankFunds += amount;
	}
	
	public void withdrawFunds(int amount) {
		funds += amount;
		bankFunds -= amount;
	}

	public int getFundsInBank() {
		return bankFunds;
	}
	
	public void addFundsToBank(int moneyGained) {
		// TODO Auto-generated method stub
		bankFunds += moneyGained;
		depositedFunds += moneyGained;
	}

	public boolean shouldDraw() {
		// TODO Auto-generated method stub
		return shouldDraw;
	}

	public void clearFlags() {
		// TODO Auto-generated method stub
		flags.clear();
	}

	public void setWindowArgument(int arg) {
		// TODO Auto-generated method stub
		windowArgument = arg;
	}

	public int getWindowArgument() {
		// TODO Auto-generated method stub
		return windowArgument;
	}

//	public void addFundsDeposited(int moneyGained) {
//		// TODO Auto-generated method stub
//		depositedFunds += moneyGained;
//	}
	
	public int getFundsDeposited() {
		return depositedFunds;
	}

	public void updateEntityPositions() {
		// TODO Auto-generated method stub
		for (Entity e : entities) {
			e.determinePositionWithCamera();
		}
	}
	
	public void createWarp(String dest, int x, int y) {
		Entity testTeleport=null;
		if (player != null) {
			testTeleport = new DoorEntity("",player.getX(),player.getY(),300,300,state,x,y,dest,"");
			testTeleport.addToInteractables(player);
		} else {
			testTeleport = new DoorEntity("",entities.get(0).getX(),entities.get(0).getY(),300,300,state,x,y,dest,"");
			testTeleport.addToInteractables(entities.get(0));
		}
		entities.add(testTeleport);
	}

	public void setDoIntro(boolean b) {
		// TODO Auto-generated method stub
		doIntro = b;
		if (doIntro) {
			currentMapName = "podunk";
			initialX = 438*4;
			initialY = 675*4;
			loadMapData();
			Entity cameraEntity = new IntroEntity("ninten.png",803*4,3356*4,32,32,camera,state,"cameraguy");
			cameraEntity.setSpriteCoords(state.allEntities.get("ninten").getSpriteCoordinates());
			entities.add(cameraEntity);
		}
	}

	public void restoreEnemyEntities() {
		// TODO Auto-generated method stub
		if (savedEnemyEntities != null) {
			entities.addAll(savedEnemyEntities);
		}
		
	}
	
	public void saveEnemyEntities(List<EnemyEntity> savedEnemies) {
		savedEnemyEntities = savedEnemies;
	}
	
}
