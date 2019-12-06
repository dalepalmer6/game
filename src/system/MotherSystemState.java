package system;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.BufferedImageUtil;
import org.newdawn.slick.util.ResourceLoader;

import battlesystem.Enemy;
import battlesystem.EnemyAction;
import battlesystem.menu.BattleMenu;
import battlesystem.menu.SelectTargetMenu;
import battlesystem.options.EnemyOptionPanel;
import battlesystem.options.PSI;
import canvas.renderer.models.RawModel;
import canvas.renderer.shaders.BattleBGShader;
import font.CharList;
import gamestate.EnemySpawnGroup;
import gamestate.EntityStats;
import gamestate.GameState;
import gamestate.cutscene.Cutscene;
import gamestate.elements.items.EquipmentItem;
import gamestate.elements.items.Item;
import gamestate.elements.psi.PSIAttack;
import gamestate.elements.psi.PSIAttackUsableInAndOutOfBattle;
import gamestate.elements.psi.PSIAttackUsableInBattle;
import gamestate.elements.psi.PSIAttackUsableOutOfBattle;
import gamestate.entities.DoorEntity;
import gamestate.entities.Entity;
import gamestate.partymembers.PartyMember;
import gamestate.psi.PSIClassification;
import gamestate.psi.PSIClassificationList;
import gamestate.psi.PSIFamily;
import menu.DrawableObject;
import menu.LeftClickableItem;
import menu.Menu;
import menu.MenuItem;
import menu.actionmenu.goodsmenu.InvisibleMenuItem;
import menu.animation.Animation;
import menu.animation.AnimationFadeFromBlack;
import menu.animation.AnimationMenu;
import menu.animation.AnimationMenuFadeFromBlack;
import menu.continuemenu.SelectSaveFileMenu;
import menu.mainmenu.ContinueMenuItem;
import menu.mainmenu.MainMenu;
import menu.mainmenu.MapPreviewTestButton;
import menu.mainmenu.NewGameMenuItem;
import menu.mainmenu.OptionsMenuItem;
import menu.mapeditmenu.mappreview.MapPreview;
import menu.windows.SelectionTextWindow;
import menu.windows.SimpleDialogMenu;
import system.controller.InputController;
import system.interfaces.Controllable;
import system.interfaces.Drawable;
import system.interfaces.Hoverable;
import system.map.Tile;
import system.map.TileHashMap;
import tiles.ChangeWithFlagTile;
import tiles.MultiInstanceTile;
import tiles.PremadeTileObject;
import tiles.SingleInstanceTile;

public class SystemState extends SystemState {
	public static PrintStream err = System.err;
	public static PrintStream out = System.out;
	private long sleepTime = 1000L / 60L;
	int frameCount = 0;
	long lastTime = System.nanoTime();
	public double delta = 0.0;
	double ns = 1000000000.0 / 60.0;
	long timer = System.currentTimeMillis();
	public long now;
	int updates = 0;
	int frames = 0;
	private InputController input = new InputController();
	private MainWindow mainWindow;
	private MenuStack menuStack;
//	private ArrayList<Controllable> c = new ArrayList<Controllable>();
	private Controllable c;
	private SelectionStack selectionStack;
	private List<DrawableObject> drawables = new ArrayList<DrawableObject>();
	public List<String> imageFileNames = new ArrayList<String>();
	public Map<String, BufferedImage> textures = new HashMap<String,BufferedImage>(); 
	public Map<String, BufferedImage> tilesets = new HashMap<String,BufferedImage>();
	public final int TILE_SIZE = 32;
	public final int SCREEN_WIDTH = 1920;
	public final int SCREEN_HEIGHT = 1080;
	public CharList charList;
	public final TileHashMap tileMap = new TileHashMap();
	private String pathToTilesets = "img/tilesets/";
	private String pathToMaps = "maps/";
	private String pathToAnimsTextures = "img/animations/";
	public HashMap<String,Entity> allEntities = new HashMap<String,Entity>();
	public ArrayList<String> allEntitiesNames;
	public final ArrayList<Entity> entities = new ArrayList<Entity>();
	private String outputFromSelect = "";
	public GameState gameState;
	private TextureAtlas textureAtlas = new TextureAtlas();
	public boolean needToPop;
//	public static int mapPreviewEditTool = 0;
	public ArrayList<PartyMember> party = new ArrayList<PartyMember>();
	public ArrayList<String> mapNames = new ArrayList<String>();
	public ArrayList<Item> items;
	public ArrayList<PSIAttack> psi;
	public PSIClassificationList psiClassList;
	public HashMap<Integer,Enemy> enemies;
	public HashMap<Integer,EnemySpawnGroup> enemySpawnGroups;
	private Map<String, BufferedImage> animations = new HashMap<String,BufferedImage>();
	private ImagePacker packer = new ImagePacker(2048,2048,0,false);
	private String currentTileset = null;
	private String currentAnimation = null;
	public boolean canLoad;
	private Menu removeThisMenu;
	private boolean clearTheMenuStack;
	private String resultOfMenuToDisplay;
	private boolean drawAllMenus;
	public boolean inBattle;
	public BattleMenu battleMenu;
	private Audio bgm;
	private float bgmStart;
	private float bgmEnd;
	private String curBGM = "";
	private Audio sfx;
	private boolean audioOverride; //forces the bgm loading from happening on loading a system.map
	private String pathToMusic = "audio/music/";
	private Audio prevAudio;
	private float prevPos;
	private boolean playOnce;
	private boolean savedAudio;
	private boolean fadeOutIsDone;
	private boolean shouldFadeIn;
	private Menu fadeOutMenu;
	private Item itemToBuy;
	private String teleportDest;
	private int teleportDestX;
	private int teleportDestY;
	private boolean doTeleportRoutine;
	private boolean doShakeScreen;
	private double shakeFactor;
	private double shakeTimer;
	public Texture tilesetTexture;
	private boolean doorCollided;
	private float oldBGMStart;
	private float oldBGMEnd;
	private Cutscene currentCutscene;
	public ArrayList<String> textData;
	private double savedXPos;
	private double savedYPos;
	private String savedMapName;
	public String[] defaultNames = {"Ninten", "Ana", "Lloyd", "Teddy", "Prime Rib"};
	public String[] namesOfCharacters = {"Ninten", "Ana", "Lloyd", "Teddy", "Prime Rib"};
	public String[] characterNamingStrings = {"Name this boy.", "And this girl.", "Name your friend.", "Name this macho guy.", "What's your favorite food?"};
	private boolean keepThreadOnExit;
	private boolean textEditor;
	public HashMap<Integer,EnemyAction> enemyActions;
	private Menu needToAddMenu;
	private Menu savedMenu;
	private boolean needAddSavedMenu;
	private EnemyOptionPanel eop;
	private int indexOfParty;
	private boolean createNewFile;
	private boolean restoreAudioWhenDone ;
	private boolean switchedBackToOldBGM;
	private boolean bgmEnded;
	private int cameraShake;
	private boolean setStartFlags;
	private boolean doneIntro;
	private String saveFileName;
	public boolean justTextData;
	private boolean shouldDrawBattleBG;
	
	public void setSaveFileName(String s) {
		saveFileName = s;
	}
	
	public void setStartFlags() {
		setStartFlags = true;
	}
	
	public void setNeedAddSavedMenu(Menu m) {
		needToAddMenu = m;
	}
	
	public static enum Characters {
		NINTEN("ninten"),
		ANA("ana"),
		LOID("loid"),
		TEDDY("teddy"),
		FAVFOOD("favfood");
		
		private String id;
		
		public String getId() {
			return id;
		}
		
		private Characters(String s) {
			this.id = s;
		}
		
	}
	
	public boolean getDoorCollided() {
		return doorCollided;
	}
	
	public void setDoorCollided(boolean  b) {
		doorCollided = b;
	}
	
	public boolean getFadeOutIsDone() {
		return fadeOutIsDone;
	}
	
	public void setBGM(String path) {
		setBGM(path,false);
	}
	
	public void saveAudio() {
		prevAudio = bgm;
		oldBGMStart = bgmStart;
		oldBGMEnd = bgmEnd;
		prevPos = bgm.getPosition();
		savedAudio = true;
	}
	
	public void setBGM(String path, boolean shouldSaveAudio) {
		if (audioOverride) {
			return;
		}
		if (curBGM.equals(path)) {
			return;
		}
		try {
			if (!audioOverride) {
				playOnce = false;
				curBGM = path;
				String propsFilePath = curBGM.substring(0,curBGM.indexOf("."));
				BufferedReader br = new BufferedReader(new FileReader(new File(pathToMusic + propsFilePath)));
				String propsStr = br.readLine();
				br.close();
				String[] props = propsStr.split(",");
				bgmStart = Float.parseFloat(props[0]);
				bgmEnd = Float.parseFloat(props[1]);
				bgm = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream(pathToMusic + path));
				bgm.playAsMusic(1.0f, 1.0f, true);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			try {
				bgm = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream(pathToMusic + path));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			bgm.playAsMusic(1.0f, 1.0f, false);
			playOnce = true;
		}
	}
	
	public void setSFX(String path) {
		try {
			sfx = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("audio/" + path));
			sfx.playAsSoundEffect(1.0f,1f,false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void playBGM() {
		
	}
	
	public void playSFX() {
		sfx.playAsSoundEffect(1.0f,1.0f,false);
	}
	
	
	public BufferedImage getAnimation(String s) {
		return animations.get(pathToAnimsTextures + s + ".png");
	}
	
	public String getPathToAnims() {
		return pathToAnimsTextures;
	}
	
	public void addPartyMember(PartyMember n) {
		this.party.add(n);
	}
	
	public GameState getGameState() {
		return gameState;
	}
	
	public SelectionStack getSelectionStack() {
		return selectionStack;
	}
	
	public void setOutputFromSelect(String s) {
		outputFromSelect = s;
	}
	
	public Entity getEntityFromEnum(String name) {
		return allEntities.get(name);
	}
	
	public String[] loadMapNames() {
		File file = new File("maps/");
		String[] mapNames = file.list();
		for (String name : mapNames) {
			this.mapNames.add(name);
		}
		return mapNames;
	}
	
	public ArrayList<String> getMapNames() {
		return mapNames;
	}
	
	public system.map.Map loadMap(String mapname) { 
		return new system.map.Map(mapname,34,34, null, this,true);
	}
	
	public void loadAllEnemyGroups() {
		int x = 0;
		enemySpawnGroups = new HashMap<Integer,EnemySpawnGroup>();
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader("data/enemygroups.data"));
			String rowIds = "";
			String rowPercents = "";
			while ((rowIds  = br.readLine()) != null) {
				String[] enemyIds = rowIds.split(",");
				rowPercents = br.readLine();
				String[] spawnPercs = rowPercents.split(",");
				int[] ids = new int[enemyIds.length];
				float[] percs = new float[spawnPercs.length];
				for (int i = 0; i < enemyIds.length; i++) {
					ids[i] = Integer.parseInt(enemyIds[i]);
				}
				for (int i = 0; i < spawnPercs.length; i++) {
					percs[i] = Float.parseFloat(spawnPercs[i]);
				}
				EnemySpawnGroup esg = new EnemySpawnGroup(x,ids,percs,this);
				enemySpawnGroups.put(x,esg);
				x++;
			}
			
		} catch(IOException e) {
			
		}
	}
	
	public void loadAllPSI() {
		psi = new ArrayList<PSIAttack>();
		try {
			BufferedReader br = new BufferedReader(new FileReader("data/psi.csv"));
			br.readLine();//skip the headers
			String row= "";
			int id = 0;
			String name;
			String desc;
			int target;
			boolean inBattleUsable;
			boolean outBattleUsable;
			int action = 0;
			String anim;
			String classification;
			String family;
			String stage;
			int minDmg;
			int maxDmg;
			int ppUse;
			while ((row = br.readLine()) != null) {
				String[] split = row.split(",");
				id = Integer.parseInt(split[0]);
				name = split[1];
				desc = split[2];
				target = Integer.parseInt(split[3]);
				inBattleUsable = Boolean.parseBoolean(split[4]);
				outBattleUsable = Boolean.parseBoolean(split[5]);
				action = Integer.parseInt(split[6]);
				anim = split[7];
				classification = split[8];
				family = split[9];
				stage = split[10];
				minDmg = Integer.parseInt(split[11]);
				maxDmg = Integer.parseInt(split[12]);
				ppUse = Integer.parseInt(split[13]);
				PSIAttack psiAttack = null;
				if (inBattleUsable && outBattleUsable) {
					psiAttack = new PSIAttackUsableInAndOutOfBattle(id,name,desc,target,action,anim,classification,family,stage,ppUse);
					psi.add(psiAttack);
				} else if (inBattleUsable){
					psiAttack = new PSIAttackUsableInBattle(id,name,desc,target,action,anim,classification,family,stage,ppUse);
					psi.add(psiAttack);
				} else if (outBattleUsable) {
					psiAttack = new PSIAttackUsableOutOfBattle(id,name,desc,target,action,anim,classification,family,stage,ppUse);
					psi.add(psiAttack);
				}
				psiAttack.setMinMaxDmg(minDmg,maxDmg);
				if (anim.equals("undef")) {
					
				} else {
					Animation animate = new Animation(this,anim,0,0,mainWindow.getScreenWidth(),mainWindow.getScreenHeight());
//					animate.createAnimation();
					psiAttack.setAnim(animate);
				}
			}
			//create the classifications ordering now.
			psiClassList = new PSIClassificationList();
			ArrayList<PSIClassification> psiClasses = new ArrayList<PSIClassification>();
			ArrayList<PSIFamily> psiFamilies = new ArrayList<PSIFamily>();
			ArrayList<String> encounteredFamilies = new ArrayList<String>();
			ArrayList<String> encounteredClasses = new ArrayList<String>();
			for (PSIAttack pi : psi) {
				//pass 1, get all unique families from PSIAttacks, adding the stages to the families
				//pass 2, get all unique classifications from families, adding the families to the classifications
				String fam = pi.getFamily();
				if (!encounteredFamilies.contains(fam)) {
					encounteredFamilies.add(fam);
					//for each element that pi != p2, check if p2's family is = fam. if so, add to teh family
					PSIFamily createdFam = new PSIFamily(fam);
					for (PSIAttack pi2 : psi) {
						if (pi2.getFamily().equalsIgnoreCase(fam)) {
							createdFam.addStage(pi2);
						}
					}
					psiFamilies.add(createdFam);
				}
			}
			for (PSIFamily pf : psiFamilies) {
				String classif = pf.getStage(0).getClassification();
				if (!encounteredClasses.contains(classif)) {
					encounteredClasses.add(classif);
					PSIClassification psiClass = new PSIClassification(classif);
					for (PSIFamily pf2 : psiFamilies) {
						String class2 = pf2.getStage(0).getClassification();
						if (classif.equalsIgnoreCase(class2)) {
							psiClass.addFamily(pf2);
						}
					}
					psiClassList.addClassification(psiClass);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void loadAllItems() {
		items = new ArrayList<Item>();
		try {
			BufferedReader br = new BufferedReader(new FileReader("data/items.csv"));
			br.readLine();//skip the headers
			String row= "";
			int id = 0;
			String name;
			String desc;
			int target;
			int equippable;
			boolean inBattleUsable;
			boolean outBattleUsable;
			int action = 0;
			while ((row = br.readLine()) != null) {
				String[] split = row.split(",");
				id = Integer.parseInt(split[0]);
				name = split[1];
				desc = split[2];
				target = Integer.parseInt(split[3]);
				equippable = Integer.parseInt(split[4],16);
				inBattleUsable = Boolean.parseBoolean(split[5]);
				outBattleUsable = Boolean.parseBoolean(split[6]);
				action = Integer.parseInt(split[7]);
				int off = Integer.parseInt(split[8]);
				int def = Integer.parseInt(split[9]);
				int spd = Integer.parseInt(split[10]);
				int luck = Integer.parseInt(split[11]);
				int hp = Integer.parseInt(split[12]);
				int pp = Integer.parseInt(split[13]);
				long resists = Long.parseLong(split[14]);
				String participle = split[15];
				int value = Integer.parseInt(split[16]);
				int useVariable = Integer.parseInt(split[17]);
				if ((equippable & 15) != 0) {
					EquipmentItem newItem = new EquipmentItem(id,name,desc,target,action,equippable,off,def,spd,luck,hp,pp,resists,participle,value);
					newItem.setUsage(false,false);
					items.add(newItem);
				} else {
					Item newItem = new Item(id,name,desc,target,action,equippable,participle,value,useVariable);
					newItem.setUsage(inBattleUsable,outBattleUsable);
					items.add(newItem);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getPathToTilesets() {
		return pathToTilesets;
	}
	
	public void loadAllEntities() {
		//take these in from external file
		File allEntitiesFolder = new File("entities/");
		Entity e;
		for (File f : allEntitiesFolder.listFiles()) {
			e = new Entity(f.getName() + ".png", 0,0,0,0,this,f.getName());
			loadCoordinatesFromFile(f.getName(),e);
			allEntities.put(f.getName(),e);
		}
//		allEntitiesNames = new ArrayList<String>();
//		Entity ness = new Entity("ninten.png",0,0,0,0,this,"ninten");
//		allEntities.put("ninten",ness);
//		loadCoordinatesFromFile("ninten", ness);
//		allEntitiesNames.add("redDressLady");
//		for (String s : allEntitiesNames) {
//			Entity e = new Entity("entities.png",0,0,24,32,this,s);
//			loadCoordinatesFromFile(s, e);
//			allEntities.put(s,e);
//		}
	}
	
	public void loadCoordinatesFromFile(String fname,Entity e) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File("entities/" + fname)));
			String row = "";
			while ((row = br.readLine()) != null) {
				String[] split = row.split(",");
				String pose = split[0];
				int x = Integer.parseInt(split[1]);
				int y = Integer.parseInt(split[2]);
				int width = Integer.parseInt(split[3]);
				int height = Integer.parseInt(split[4]);
				boolean flipped = Boolean.parseBoolean(split[5]);
				if (!e.getSpriteCoordinates().containsKey(pose)) {
					e.getSpriteCoordinates().setPose(pose);
				}
				e.getSpriteCoordinates().addStateToPose(pose,x,y,width,height,flipped);
			}
		} catch (FileNotFoundException err) {
			// TODO Auto-generated catch block
			err.printStackTrace();
		} catch (IOException err) {
			// TODO Auto-generated catch block
			err.printStackTrace();
		}
	}
	
	public int[] stringArrayToIntegerArray(String[] s) {
		int[] newIntArray = new int[s.length];
		for (int i = 0; i < s.length; i++) {
			newIntArray[i] = Integer.parseInt(s[i]);
		}
		return newIntArray;
	}
	
	public void loadCurrentAnimation() {
		
	}
	
	public void loadAllTiles(String tileset) {
		try {
			this.currentTileset = tileset;
			tilesetTexture = BufferedImageUtil.getTexture("", tilesets.get(currentTileset));
			mainWindow.setTilesetTexture(tilesetTexture);
			GL13.glActiveTexture(GL13.GL_TEXTURE2);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, tilesetTexture.getTextureID());
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
//		textureAtlas.setRectByName(tileset);
//		Rectangle tileBounds = textureAtlas.getCurrentRectangle();
		int i = 0;
		try {
			BufferedReader br = new BufferedReader(new FileReader(tileset.substring(0,tileset.length()-4) + ".ts"));//"tilesets/onett.ts"));
			br.readLine();
			String row = br.readLine();
			String tileMetadata = "";
			tileMetadata += row + "\n";
			while (row != null) {
//				row = br.readLine();
//				tileMetadata += row + "\n";
				if (row.startsWith("#")) {
					//parse the metadata
					String[] lines = tileMetadata.split("\n");
					String[] type = lines[0].split(",");
					String[] dims = lines[1].split(",");
					String collision = lines[2] + lines[3] + lines[4] + lines[5];
					String[] collisionsArrayString = collision.split(",");
					int[] colArray = stringArrayToIntegerArray(collisionsArrayString);
					Tile t;
					switch (type[0]) {
					//the type
						case "single" : t = new SingleInstanceTile(i++);
										t.addTileInstance(Integer.parseInt(dims[0]),Integer.parseInt(dims[1]),Integer.parseInt(dims[2]),Integer.parseInt(dims[3]),colArray);
										tileMap.addTile(t);
//										br.readLine();
										break;
						case "flagChange": t = new ChangeWithFlagTile(i++,type[1],Integer.parseInt(type[2]));
										t.addTileInstance(Integer.parseInt(dims[0]),Integer.parseInt(dims[1]),Integer.parseInt(dims[2]),Integer.parseInt(dims[3]),colArray);
										tileMap.addTile(t);
				//						br.readLine();
										break;
						case "multi" : 	t = new MultiInstanceTile(i++);
//										
//										row = br.readLine();
										BufferedReader stringReader = new BufferedReader(new StringReader(tileMetadata));
										row = stringReader.readLine();
										String currentTileData = "";
										row = stringReader.readLine();
										currentTileData += row + "\n";
										while (!row.startsWith("#")) {
											for (int j = 0; j < 4; j++) {
												currentTileData += stringReader.readLine() + "\n";
											}
											lines = currentTileData.split("\n");
											dims = lines[0].split(",");
											collision = lines[1] + lines[2] + lines[3] + lines[4];
											collisionsArrayString = collision.split(",");
											colArray = stringArrayToIntegerArray(collisionsArrayString);
											t.addTileInstance(Integer.parseInt(dims[0]),Integer.parseInt(dims[1]),Integer.parseInt(dims[2]),Integer.parseInt(dims[3]),colArray);
											currentTileData = "";
											row = stringReader.readLine();
											currentTileData += row + "\n";
										}
										tileMap.addTile(t);
										break;
						
						case "pdo" : 	t = new PremadeTileObject(i++,Integer.parseInt(type[1]),Integer.parseInt(type[2]));
						 				stringReader = new BufferedReader(new StringReader(tileMetadata));
						 				row = stringReader.readLine();
										currentTileData = "";
										row = stringReader.readLine();
										currentTileData += row + "\n";
										while (!row.startsWith("#")) {
											for (int j = 0; j < 4; j++) {
												currentTileData += stringReader.readLine() + "\n";
											}
											lines = currentTileData.split("\n");
											dims = lines[0].split(",");
											collision = lines[1] + lines[2] + lines[3] + lines[4];
											collisionsArrayString = collision.split(",");
											colArray = stringArrayToIntegerArray(collisionsArrayString);
											t.addTileInstance(Integer.parseInt(dims[0]),Integer.parseInt(dims[1]),Integer.parseInt(dims[2]),Integer.parseInt(dims[3]),colArray);
											currentTileData = "";
											row = stringReader.readLine();
											currentTileData += row + "\n";
											
										}
										tileMap.addTile(t);
										break;
										
						case "group" :  t = new SingleInstanceTile(i++);
										t.addTileInstance(Integer.parseInt(dims[0]),Integer.parseInt(dims[1]),Integer.parseInt(dims[2]),Integer.parseInt(dims[3]),colArray);
										BufferedReader sr = new BufferedReader(new StringReader(tileMetadata));
										row = sr.readLine();
										currentTileData = "";
										row = sr.readLine();
										currentTileData += row + "\n";
										while (!row.startsWith("#")) {
											for (int j = 0; j < 4; j++) {
												row = sr.readLine();
												currentTileData += row +"\n";
											}
											lines = currentTileData.split("\n");
											dims = lines[0].split(",");
											collision = lines[1] + lines[2] + lines[3] + lines[4];
											collisionsArrayString = collision.split(",");
											colArray = stringArrayToIntegerArray(collisionsArrayString);
											t.addTileInstance(Integer.parseInt(dims[0]),Integer.parseInt(dims[1]),Integer.parseInt(dims[2]),Integer.parseInt(dims[3]),colArray);
											tileMap.addTile(t);
											t = new SingleInstanceTile(i++);
											currentTileData = "";
											row = sr.readLine();
											currentTileData += row + "\n";
										}
										break;
					} 
					tileMetadata = "";
				}
				row = br.readLine();
				tileMetadata += row + "\n";
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void loadAllImages(String s)  {
		File f = new File("img/" + s);
		for (File c : f.listFiles()) {
			imageFileNames.add(c.getPath());
		}
	}
	
	public void loadImageData() {
		for (String filePath : imageFileNames) {
			try {
//				filePath = "img/" + filePath;
				//TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(filePath))
				textures.put(filePath, ImageIO.read(new File(filePath)));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			createAtlas();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void loadTileSets() {
		String filePath;
		File tilesetsDir = new File(pathToTilesets);
		for (File c : tilesetsDir.listFiles()) {
			filePath = pathToTilesets + c.getName();
			try {
				tilesets.put(filePath, ImageIO.read(new File(filePath)));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void loadAllAnims() {
		String filePath;
		File animsDir = new File(pathToAnimsTextures);
		for (File c : animsDir.listFiles()) {
			filePath = pathToAnimsTextures + c.getName();
			try {
				animations.put(filePath, ImageIO.read(new File(filePath)));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void loadAllEnemies() {
		String pathToEntity = "data/enemies.csv";
		File file = new File(pathToEntity);
		enemies = new HashMap<Integer,Enemy>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			//skip headers
			String row = br.readLine();
			int i = 0;
			while ((row=br.readLine()) != null) {
				String[] data = row.split(",");
				String name=data[1];
				int hp=Integer.parseInt(data[2]);
				int pp=Integer.parseInt(data[3]);
				int off=Integer.parseInt(data[4]);
				int def=Integer.parseInt(data[5]);
				int vit=Integer.parseInt(data[6]);
				int iq=Integer.parseInt(data[7]);
				int speed=Integer.parseInt(data[8]);
				int guts=Integer.parseInt(data[9]);
				int luck=Integer.parseInt(data[10]);
				int xp = Integer.parseInt(data[11]);
				int money = Integer.parseInt(data[12]);
				String texture = data[13];
//				int width = Integer.parseInt(data[13]);
//				int height = Integer.parseInt(data[14]);
				String entityName = data[14];
				String[] potentialEnemyActions = data[17].split("_");
				String bgm = data[18];
				String battleBG = data[19];
				String predicate = data[20];
				String resist = data[21];
				int numberAllies = Integer.parseInt(data[22]);
				EnemyAction[] enemyActions = new EnemyAction[potentialEnemyActions.length];
				for (int x = 0; x < potentialEnemyActions.length; x++) {
					enemyActions[x] = this.enemyActions.get(Integer.parseInt(potentialEnemyActions[x]));
				}
//				public EntityStats(int lvl,int chp, int cpp, int hp,int pp,int atk, int def, int iq,int spd,int guts, int luck, int vit,int curxp) {
				EntityStats stats = new EntityStats(0,hp,pp,hp,pp,off,def,iq,speed,guts,luck,vit,xp);
				Enemy e = new Enemy(i,texture,name,stats,xp,money,entityName,this);
				e.setActions(enemyActions);
				e.setBGM(bgm);
				e.setBattleBG(battleBG);
				e.setResistances(Integer.parseInt(resist));
				e.setMaxAllies(numberAllies);
				if (predicate.equals(" ")) {
					e.setPredicate("");
				} else {
					e.setPredicate(predicate);
				}
				
				enemies.put(i++,e);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void createAtlas() throws IOException {
//		textureAtlas = new TextureAtlas();
		int squareSize = 2048;
		if (getCurrentAnimation() != null) {
			squareSize = 4096;
		}
		ImagePacker packer = new ImagePacker(squareSize,squareSize,0,false);
		for (String key : textures.keySet()) {
			BufferedImage t = textures.get(key);
			packer.insertImage(key,t);
		}
//		if (currentTileset != null) {
//			packer.insertImage(currentTileset,tilesets.get(currentTileset));
//		}
		if (getCurrentAnimation() != null) {
			packer.insertImage(getCurrentAnimation(),animations.get(getCurrentAnimation()));
		}
		textureAtlas.setTexture(BufferedImageUtil.getTexture("", packer.getImage()));
		textureAtlas.setRects(packer.getRects());
		mainWindow.setTextureAtlas(textureAtlas);
	}
	
	public void addToDrawables(ArrayList<DrawableObject> d) {
		for (DrawableObject drawable : d) {
			if (!drawables.contains(drawable)) {
				drawables.add(drawable);
			}
		}
		
//		drawables.addAll(d);
	}
	
	public List<DrawableObject> getDrawables() {
		return drawables;
	}
	
	public void clearDrawables() {
		drawables = new ArrayList<DrawableObject>();
//		c = null;
	}
	
	public void setDrawables(List<DrawableObject> l) {
		drawables = l;
	}
	
	public DrawableObject getDrawable(int i) {
		return drawables.get(i);
	}
	
	public void loadAllStrings() {
		textData = new ArrayList<String>();
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader("data/text.txt"));
			String line = "";
			while ((line = br.readLine())!=null) {
				textData.add(line.substring(5));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void init() {
		init(false);
	}
	
	public void createContext() {
		setMainWindow(new MainWindow(textureAtlas,SCREEN_WIDTH,SCREEN_HEIGHT));
		getMainWindow().start();
	}
	
	public void init(boolean justTextData) {
		this.justTextData = justTextData;
		if (justTextData) {
			textEditor = true;
			namesOfCharacters=defaultNames;
			charList = new CharList();
			gameState = new GameState();
			loadMapNames();
			loadAllStrings();
			loadAllEntities();
			loadAllItems();
			loadAllEnemyGroups();
			menuStack = new MenuStack();
			selectionStack = new SelectionStack();
		} else {
			charList = new CharList();
			createContext();
			loadMapNames();
			loadTileSets();
			loadAllImages("");
			loadAllImages("enemies");
			loadImageData();
			loadAllEntities();
			loadAllItems();
//			loadAllAnims();
//			loadAllPSI();
			loadAllEnemyActions();
			loadAllEnemies();
			loadAllStrings();
			loadAllEnemyGroups();
			
			setBGM("motherearth.ogg");
			textureAtlas.getTexture().bind();
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureAtlas.getTexture().getTextureID());
			
			selectionStack = new SelectionStack();
			menuStack = new MenuStack();
				Menu m = new MainMenu(this);
					SelectionTextWindow STW = new SelectionTextWindow(mainWindow.getScreenWidth()/2 - (2*64),mainWindow.getScreenHeight()-(7*64),4,3,this);
					STW.add(new NewGameMenuItem((int) STW.getX(),(int) STW.getY() + 16,this));
					STW.add(new ContinueMenuItem((int) STW.getX(),(int) STW.getY() + 48,this));
					STW.add(new OptionsMenuItem((int) STW.getX(),(int) STW.getY() + 80,this));
					STW.add(new MapPreviewTestButton((int) STW.getX(),(int) STW.getY() + 112,this));
				m.addMenuItem(STW);
			menuStack.push(m);
			//test the music
//			setBGM("eb_title.wav");
//			bgm.playAsMusic(1.0f, 1.0f, false);
		}
		
	}
	
	private void loadAllEnemyActions() {
		// TODO Auto-generated method stub
		String pathToEntity = "data/enemyactions.csv";
		File file = new File(pathToEntity);
		enemyActions = new HashMap<Integer,EnemyAction>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			//skip headers
			String row = br.readLine();
			int i = 0;
			while ((row=br.readLine()) != null) {
				String[] data = row.split(",");
				String text = data[1];
				int action = Integer.parseInt(data[2]);
				int useVar = Integer.parseInt(data[3]);
				int target = Integer.parseInt(data[4]);
				EnemyAction ea = new EnemyAction(text,action,useVar,target);
				enemyActions.put(i++,ea);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Point getMouseCoordinates() {
		int x = Mouse.getX();
		int y = getMainWindow().getScreenHeight() - Mouse.getY();
		return new Point(x, y);
	}
	
	public void setHoldable(boolean b) {
		input.setHoldable(b);
	}
	
	public boolean getBGMEnded() {
		return bgmEnded;
	}
	
	public void resetBGMEnded() {
		bgmEnded = false;
	}
	
	public void setOldBGM() {
		if (prevAudio != null) {
			bgmEnded = true;
			savedAudio = false;
			bgm = prevAudio;
			bgmStart = oldBGMStart;
			bgmEnd = oldBGMEnd;
//			bgm.setPosition(prevPos);
			bgm.playAsMusic(1.0f,1f,false);
			prevAudio = null;
			prevPos = 0.0f;
		}
	}
	
	public void setShouldFadeIn()  {
		setShouldFadeIn(null);
	}
	
	public void update() {
//		if (menuStack.isEmpty()) {
//			setBGM(gameState.getMap().getBGM(),false);
//		}
		if (restoreAudioWhenDone && !bgm.isPlaying()) {
			restoreAudioWhenDone = false;
			setOldBGM();
		}
		if (textEditor && menuStack.isEmpty()) {
			Display.destroy();
		}
		if (currentCutscene != null) {
			if (currentCutscene.needToRemove()) {
				currentCutscene = null;
			} else {
				currentCutscene.doCutscene();
			}
			
		}
		if (fadeOutIsDone) {
			Menu savedMenu = menuStack.pop();
			if (menuStack.peek() != null) {
				menuStack.peek().doDoneFadeOutAction();
			} 
//			else {
//				savedMenu.doDoneFadeOutAction();
//				menuStack.push(savedMenu);
//			}
			
			fadeOutIsDone = false;
		}
		if (shouldFadeIn) {
			menuStack.pop();
			if (needToAddMenu!=null) {
				menuStack.push(needToAddMenu);
				needToAddMenu = null;
			}
			
			shouldFadeIn = false;
			AnimationMenu ffb = new AnimationMenu(this);
			ffb.createAnimMenu(new AnimationFadeFromBlack(this));
			getMenuStack().push(ffb);
		}
		if (bgm != null) {
			if (bgm.isPlaying() && bgm.getPosition() >= bgmEnd && !playOnce) {
				bgm.setPosition(bgmStart);
			}
			if (!bgm.isPlaying() && savedAudio) {
				setOldBGM();
			}
		}
		if (drawAllMenus) {
			for (Menu c : menuStack.getMenus()) {
				//only update the top one 
				ArrayList<DrawableObject> list = new ArrayList<DrawableObject>();
				if (c != null) {
					list.addAll(c.getDrawableObjects());
					list.addAll(c.getMenuItems());
					addToDrawables(list);
				}
			}
		} else {
			Menu c = getMenuStack().peek();
			if (c != null) {
				ArrayList<DrawableObject> list = new ArrayList<DrawableObject>();
				list.addAll(c.getDrawableObjects());
				list.addAll(c.getMenuItems());
				addToDrawables(list);
//				c.updateAll(input);
			}
		}
		
		Menu c = getMenuStack().peek();
		if (c != null) {
			c.updateAll(input);
			if (doShakeScreen) {
//				factor;
				if (shakeTimer >= 60) {
					doShakeScreen = false;
				}
				shakeTimer++;
				for (MenuItem i : c.getMenuItems()) {
					double applyShake = 50*shakeFactor * (1/(4*shakeTimer)) * Math.sin(shakeTimer*Math.PI/4) ;
					i.setShakingY(applyShake);
				}
			}
		}
		
		if (!(menuStack.peek() instanceof BattleMenu) && inBattle && !(menuStack.peek() instanceof AnimationMenu)) {
			battleMenu.updateAll(input);
		}
		
		if (inBattle && eop != null) {
			if (!(menuStack.peek() instanceof SelectTargetMenu)) {
				eop.setSelected(-2);
			}
//			if (menuStack.peek() instanceof SelectTargetMenu) {
				eop.updateAnim();
//			}
		}
		
//		if (battleMenu != null && menuStack.peek() != battleMenu) {
//			battleMenu.updateAll(input);
//		}
		
		
//		input.setHoldable(false);
		Point mouse = getMainWindow().getMouseCoordinates();
		for (Drawable d : getDrawables()) {
			if (d instanceof Hoverable) {
				if (((Hoverable) d).hovered(mouse.getX(), mouse.getY())) {
					((Hoverable) d).hoveredAction();
//					if (d instanceof LeftClickableItem) {
						input.setHoldable(false);
						if (d instanceof MapPreview) {
							input.setHoldable(true);
						}
						((LeftClickableItem)d).checkInputs(input);
						
//						input.setHoldable(false);
//					}
				} else {
					((Hoverable) d).unhoveredAction();
				}
			}
		}
		
//		if (menuStack.isEmpty()) {
//			
//		}
		
		if (gameState != null) {
			gameState.updateTimer();
//			if (!gameState.getFlag("adventureStartFlag")) {
//				if (gameState.getFlag("playPoltergeist")) {
//					//move the camera crazy
////					if (timer % 30 == 0) {
////						cameraShake = (int) (30*Math.random());
////					}
////					gameState.getCamera().changeX(cameraShake);
////					gameState.getCamera().changeY(cameraShake);
//					gameState.setFlag("audioChanged",true);
//					if (gameState.getFlag("audioChanged")) {
//						
//					}
//					gameState.getMap().setBGM("poltergeist.ogg");
////					setAudioOverride(true);
//				}
//				if (gameState.getFlag("dollDead")) {
////					setAudioOverride(false);
//					gameState.setFlag("audioChanged",false);
//					gameState.setFlag("playPoltergeist",false);
////					setBGM(gameState.getMap().getBGM(),true);
//					gameState.getMap().restoreBGM();
//				}
//				if (gameState.getFlag("dadFirstCall")) {
//					gameState.getMap().setBGM("phonering.ogg");
////					setAudioOverride(true);
//				}
//				if (gameState.getFlag("adventureStartFlag")) {
////					gameState.setFlag("dadFirstCall",false);
////					setAudioOverride(false);
//					gameState.setFlag("audioChanged",false);
//					gameState.getMap().restoreBGM();
////					setBGM(gameState.getMap().getBGM(),true);
//				}
//			}
			
			input.setHoldable(true);
			gameState.updatePartyMembers();
			if (gameState.getFlag("buyingItem") && itemToBuy != null) {
				menuStack.pop();
				if (gameState.getFundsOnHand() < itemToBuy.getValue()) {
					SimpleDialogMenu.createDialogBox(this,"Well[WAIT60] this is embarassing.[WAIT30][NEWLINE]You don't appear to have the funds to buy this.");
				} else {
					SimpleDialogMenu.createDialogBox(this,"Alright, it's all yours![ADDITEM_" + itemToBuy.getId() +"] ");
					gameState.spendFunds(itemToBuy.getValue());
				}
				gameState.setFlag("buyingItem",false);
				itemToBuy = null;
			}
			if (gameState.getFlag("saveGame")) {
//				try {
					gameState.setFlag("saveGame",false);
					menuStack.push(new SelectSaveFileMenu(this));
//					gameState.saveData(saveFileName);
//				} catch (FileNotFoundException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			}
			if (doTeleportRoutine)  {
				doTeleportRoutine = false;
				gameState.setFlag("teleporting");
			}
		}
		
		if (!textEditor && gameState != null && (menuStack.isEmpty() || menuStack.peek() instanceof AnimationMenuFadeFromBlack)) {
//			input.setHoldable(true);
			gameState.update(input);
		}
		
		if (!menuStack.isEmpty()) {
			if (menuStack.peek().getCanUpdateGameState()) {
				gameState.update(input);
//				gameState.updateEntities(input,true);
			}
		}
		
		
		for (DrawableObject d : drawables) {
			if (d instanceof SelectionTextWindow || d instanceof InvisibleMenuItem) {
				input.setHoldable(false);
			}
		}
		
		if (gameState != null && menuStack.peek() instanceof AnimationMenu) {
//			if (menuStack.peek().isSwirl()) {
				gameState.updateEntities(input,false);
//			}
		}
		
//		if (setStartFlags) {
//			setStartFlags = false;
////			gameState.createStartPosition();
//			
//		}
		
		//change this from AnimationMenu to when a boolean is set
		if (menuStack.peek() instanceof AnimationMenu && gameState == null) {
			if (((AnimationMenu)menuStack.peek()).isComplete()) {
				menuStack.pop();
				menuStack.pop();
				needToPop = false;
				GameState gs = new GameState(this);
				this.setGameState(gs);
				if (!createNewFile) {
					gs.loadFromSaveFile(saveFileName);
					gameState.loadMapData();
				}else {
//					gs.loadFromSaveFile("test");
					setAudioOverride(true);
					gameState.setDoIntro(true);
				}
			}
		}
	}
	
	
	
	public void render() {
//		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		if (gameState == null) {
			getMainWindow().renderBG("bg.png");
		}
		
		if (shouldDrawBattleBG) {
			shouldDrawBattleBG = false;
			BattleBGShader battleBGShader = new BattleBGShader();
			battleBGShader.start();
			
			canvas.renderer.entities.Entity entity = mainWindow.getBattleBGModel();
			
			 GL13.glActiveTexture(GL13.GL_TEXTURE0);
			 GL11.glBindTexture(GL11.GL_TEXTURE_2D,0);
			 GL13.glActiveTexture(GL13.GL_TEXTURE1);
			 GL11.glBindTexture(GL11.GL_TEXTURE_2D,0);
//			 Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(),
//			           entity.getRx(), entity.getRy(), entity.getRz(), entity.getScale());
//			 battleBGShader.loadTransformationMatrix(transformationMatrix);
//			
//			 battleBGShader.loadProjectionMatrix(renderer.getRenderer().getProjMatrix());
			 
			 GL13.glActiveTexture(GL13.GL_TEXTURE0);
			 GL11.glBindTexture(GL11.GL_TEXTURE_2D,entity.getModel().getModelTexture().getTextureId());
			 
			 GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
			 
			 GL13.glActiveTexture(GL13.GL_TEXTURE1);
			 GL11.glBindTexture(GL11.GL_TEXTURE_2D,mainWindow.getPalette().getTextureID());
			 
			 GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
			 
				
				mainWindow.useShader(battleBGShader);
			 RawModel model = entity.getModel().getModel();
			 GL30.glBindVertexArray(model.getVaoId());
			 GL20.glEnableVertexAttribArray(0);
			 GL20.glEnableVertexAttribArray(1);
				
			 GL11.glDrawElements(GL11.GL_TRIANGLES, entity.getModel().getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);

			 GL20.glDisableVertexAttribArray(0);
			 GL20.glDisableVertexAttribArray(1);
			 GL30.glBindVertexArray(0);
			 
			 GL13.glActiveTexture(GL13.GL_TEXTURE0);
			 GL11.glBindTexture(GL11.GL_TEXTURE_2D,0);
			
			battleBGShader.stop();

			 GL13.glActiveTexture(GL13.GL_TEXTURE0);
			 GL11.glBindTexture(GL11.GL_TEXTURE_2D,textureAtlas.getTexture().getTextureID());
		} else {
			if (gameState != null && gameState.shouldDraw()) {
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, mainWindow.getFrameBufferColorTextureId());
				mainWindow.renderMap();
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
				gameState.drawGameState();
			}
		}
		
		getMainWindow().drawDrawables(drawables);
	}
	
	public void gameLoop(boolean running) {
		
		while(running){
			clearDrawables();
//			//load in the gameState objects
//			if (gameState != null) {
//				addToDrawables(gameState.getDrawables());
//			}
			//load current menu
//			Menu c = getMenuStack().popAndAddMenuItems();
//			if (c != null) {
//				ArrayList<DrawableObject> list = new ArrayList<DrawableObject>();
//				list.addAll(c.getDrawableObjects());
//				list.addAll(c.getMenuItems());
//				addToDrawables(list);
//				getMenuStack().push(c);
//			}
			
			//check for input
			now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			
			if (delta >= 1.0) {
				input.handleInputs();
				update();
				updates++;
				delta--;
			}
			render();
			frames++;
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				SystemState.out.println(updates + " ups, " + frames + " fps");
				updates = 0;
				frames = 0;
			}
			removeMenu();
			if (clearTheMenuStack) {
				menuStack.clear();
				SimpleDialogMenu.createDialogBox(this,resultOfMenuToDisplay);
				clearTheMenuStack = false;
			}
			Display.update();
			Display.sync(60);

			if (Display.isCloseRequested()) {
				Display.destroy();
				if (!textEditor) {
					System.exit(0);
				}
				
			}
			frameCount++;
		}
	}
	
	public void removeMenu() {
		if (needToPop) {
			menuStack.pop();
			needToPop = false;
		} else if (removeThisMenu != null) {
			menuStack.remove(removeThisMenu);
			removeThisMenu = null;
		}
	}
	
	public void setDrawBattleBG(boolean b) {
		shouldDrawBattleBG = b;
	}
	
	public void run(){
		now = System.nanoTime();
		init();
		boolean running = true;
		gameLoop(running);
//		cleanup();
	}

	public MenuStack getMenuStack() {
		return menuStack;
	}

	public void setMenuStack(MenuStack menuStack) {
		this.menuStack = menuStack;
	}

	public MainWindow getMainWindow() {
		return mainWindow;
	}

	public void setMainWindow(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
	}

	public void setGameState(GameState gs) {
		// TODO Auto-generated method stub
		this.gameState = gs;
	}

	public TextureAtlas getTextureAtlas() {
		// TODO Auto-generated method stub
		return textureAtlas;
	}

	public void setControllable(Entity e) {
		// TODO Auto-generated method stub
		c = (Controllable) e;
	}

	public void setCurrentAnimation(String texture) {
		// TODO Auto-generated method stub
		this.currentAnimation = pathToAnimsTextures + texture;
	}

	public String getCurrentAnimation() {
		return currentAnimation;
	}

	public void setToRemove(Menu menu) {
		// TODO Auto-generated method stub
		removeThisMenu = menu;
	}

	public void setClearMenuStack() {
		// TODO Auto-generated method stub
		clearTheMenuStack = true;
	}

	public void setResultOfMenuToDisplay(String string) {
		// TODO Auto-generated method stub
		resultOfMenuToDisplay = string;
	}

	public void setDrawAllMenus(boolean b) {
		// TODO Auto-generated method stub
		drawAllMenus = b;
	}

	public void setAudio(String string) {
		// TODO Auto-generated method stub
		
	}

	public void setAudioOverride(boolean b) {
		// TODO Auto-generated method stub
		audioOverride = b;
	}

	public void setFadeOutDone() {
		// TODO Auto-generated method stub
		fadeOutIsDone = true;
	}

	public void setItemToBuy(Item item) {
		// TODO Auto-generated method stub
		itemToBuy = item;
	}

	public void setTeleportVariables(String newMapName, int newX, int newY, boolean b) {
		// TODO Auto-generated method stub
		teleportDest = newMapName;
		teleportDestX = newX;
		teleportDestY = newY;
		gameState.setTeleportVariables(newMapName,newX,newY);
		doTeleportRoutine = b;
	}

	public void setShakeVariables(int damage, boolean b) {
		// TODO Auto-generated method stub
		shakeTimer = 0;
		doShakeScreen = b;
		shakeFactor = damage;
	}

	public void setCutscene(Cutscene cutscene) {
		// TODO Auto-generated method stub
		currentCutscene = cutscene;
	}

	public Cutscene getCutscene() {
		// TODO Auto-generated method stub
		return currentCutscene;
	}

	public void saveCoordinates() {
		// TODO Auto-generated method stub
		savedXPos = gameState.getPlayer().getX();
		savedYPos = gameState.getPlayer().getY();
		savedMapName = gameState.getMap().getMapId();
	}
	
	public DoorEntity createWarpDoor() {
		//public DoorEntity(String desc,int x, int y, int width, int height, SystemState m, int destX, int destY, String system.map,String text) {
		return new DoorEntity("",gameState.getPlayer().getX(),gameState.getPlayer().getY(),256,256,this,(int)savedXPos,(int)savedYPos,savedMapName,"");
	}

	public DoorEntity createMagicantWarp() {
		// TODO Auto-generated method stub
		return new DoorEntity("",gameState.getPlayer().getX(),gameState.getPlayer().getY(),256,256,this,1698*4,3389*4,"magicant","");
	}

	public void stopBGM() {
		// TODO Auto-generated method stub
		bgm.stop();
	}

	public void setShouldFadeIn(Menu m) {
		// TODO Auto-generated method stub
		if (m != null) {
			needToAddMenu = m;
		}
		shouldFadeIn = true;
	}

	public Menu getSavedMenu() {
		return savedMenu;
	}

	
	public void saveCurrentDialogMenu() {
		// TODO Auto-generated method stub
		savedMenu = menuStack.peek();
	}
	
	public void clearSavedMenu() {
		savedMenu = null;
	}

	public void addSavedMenu(boolean b) {
		// TODO Auto-generated method stub
		needAddSavedMenu = b;
	}

	public void setEOP(EnemyOptionPanel eop) {
		// TODO Auto-generated method stub
		this.eop = eop;
	}

	public void setIndexOfParty(int partyIndex) {
		// TODO Auto-generated method stub
		indexOfParty = partyIndex;
	}
	
	public int getPartyIndex() {
		int i = indexOfParty;
		indexOfParty = -1;
		return i;
	}

	public void createNewGameGameState() {
		// TODO Auto-generated method stub
		createNewFile = true;
	}

	public void setRestoreAudioWhenDone() {
		// TODO Auto-generated method stub
		restoreAudioWhenDone = true;
	}

	public void setDoneIntro() {
		// TODO Auto-generated method stub
		doneIntro = true;
	}
	
	public boolean getDoneIntro() {
		return doneIntro;
	}
}