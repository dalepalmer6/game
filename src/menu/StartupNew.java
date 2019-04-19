package menu;

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
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.BufferedImageUtil;
import org.newdawn.slick.util.ResourceLoader;

import battlesystem.BattleMenu;
import battlesystem.options.PSI;
import canvas.Controllable;
import canvas.Drawable;
import canvas.Hoverable;
import canvas.MainWindow;
import font.CharList;
import font.SelectionTextWindow;
import font.SimpleDialogMenu;
import font.TextWindow;
import gamestate.Enemy;
import gamestate.Entity;
import gamestate.EntityStats;
import gamestate.GameState;
import gamestate.PartyMember;
import gamestate.elements.items.EquipmentItem;
import gamestate.elements.items.Item;
import gamestate.elements.psi.PSIAttack;
import gamestate.elements.psi.PSIAttackUsableInAndOutOfBattle;
import gamestate.elements.psi.PSIAttackUsableInBattle;
import gamestate.elements.psi.PSIAttackUsableOutOfBattle;
import gamestate.psi.PSIClassification;
import gamestate.psi.PSIClassificationList;
import gamestate.psi.PSIFamily;
import global.ImagePacker;
import global.InputController;
import global.MenuStack;
import global.SelectionStack;
import global.TextureAtlas;
import mapeditor.Tile;
import mapeditor.TileHashMap;
import menu.mainmenu.ContinueMenuItem;
import menu.mainmenu.MainMenu;
import menu.mainmenu.MapPreviewTestButton;
import menu.mainmenu.NewGameMenuItem;
import menu.mainmenu.OptionsMenuItem;
import tiles.MultiInstanceTile;
import tiles.PremadeTileObject;
import tiles.SingleInstanceTile;
import tiles.types.*;

public class StartupNew{
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
	public ArrayList<Enemy> enemies;
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
	private Audio sfx;
	
	public void setBGM(String path) {
		try {
			bgm = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("audio/" + path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setSFX(String path) {
		try {
			sfx = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("audio/" + path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void playBGM() {
		bgm.playAsMusic(1.0f, 1.0f, true);
	}
	
	public void playSFX() {
		sfx.playAsSoundEffect(1.0f,1.0f,false);
	}
	
	
	
	public BufferedImage getAnimation(String s) {
		return animations.get(s);
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
				PSIAttack psiAttack = null;
				if (inBattleUsable && outBattleUsable) {
					psiAttack = new PSIAttackUsableInAndOutOfBattle(id,name,desc,target,action,anim,classification,family,stage);
					psi.add(psiAttack);
				} else if (inBattleUsable){
					psiAttack = new PSIAttackUsableInBattle(id,name,desc,target,action,anim,classification,family,stage);
					psi.add(psiAttack);
				} else if (outBattleUsable) {
					psiAttack = new PSIAttackUsableOutOfBattle(id,name,desc,target,action,anim,classification,family,stage);
					psi.add(psiAttack);
				}
				psiAttack.setMinMaxDmg(minDmg,maxDmg);
				Animation animate = new Animation(this,anim,0,0,mainWindow.getScreenWidth(),mainWindow.getScreenHeight());
				animate.createAnimation();
				psiAttack.setAnim(animate);
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
				equippable = Integer.parseInt(split[4]);
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
				if ((equippable & 15) != 0) {
					items.add(new EquipmentItem(id,name,desc,target,action,equippable,off,def,spd,luck,hp,pp,resists,participle));
				} else {
					items.add(new Item(id,name,desc,target,action,equippable,participle));
				}
				
//				if (inBattleUsable && outBattleUsable) {
//					
//				} else if (inBattleUsable){
//					items.add(new ItemUsableInBattle(id,name,desc,target,action,equippable));
//				} else if (outBattleUsable) {
//					items.add(new ItemUsableOutOfBattle(id,name,desc,target,action,equippable));
//				} 
//				else if (equippable != 0) {
//					items.add(new ItemEquipment(id,name,desc,equippable));
//				}
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
		allEntitiesNames = new ArrayList<String>();
		Entity ness = new Entity("ninten.png",0,0,0,0,this,"ninten");
		allEntities.put("ninten",ness);
		loadCoordinatesFromFile("ninten", ness);
		allEntitiesNames.add("redDressLady");
		for (String s : allEntitiesNames) {
			Entity e = new Entity("entities.png",0,0,24,32,this,s);
			loadCoordinatesFromFile(s, e);
			allEntities.put(s,e);
		}
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
			createAtlas();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		textureAtlas.setRectByName(tileset);
		Rectangle tileBounds = textureAtlas.getCurrentRectangle();
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
	
	public void loadAllImages()  {
		File f = new File("img");
		for (File c : f.listFiles()) {
			imageFileNames.add(c.getName());
		}
		for (String filePath : imageFileNames) {
			try {
				filePath = "img/" + filePath;
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
		enemies = new ArrayList<Enemy>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			//skip headers
			String row = br.readLine();
			while ((row=br.readLine()) != null) {
				String[] data = row.split(",");
				String name=data[0];
				int hp=Integer.parseInt(data[1]);
				int pp=Integer.parseInt(data[2]);
				int off=Integer.parseInt(data[3]);
				int def=Integer.parseInt(data[4]);
				int vit=Integer.parseInt(data[5]);
				int iq=Integer.parseInt(data[6]);
				int speed=Integer.parseInt(data[7]);
				int guts=Integer.parseInt(data[8]);
				int luck=Integer.parseInt(data[9]);
				int xp = Integer.parseInt(data[10]);
				String texture = data[11];
				int width = Integer.parseInt(data[12]);
				int height = Integer.parseInt(data[13]);
//				public EntityStats(int lvl,int chp, int cpp, int hp,int pp,int atk, int def, int iq,int spd,int guts, int luck, int vit,int curxp) {
				EntityStats stats = new EntityStats(0,hp,pp,hp,pp,off,def,iq,speed,guts,luck,vit,xp);
				enemies.add(new Enemy(texture,name,width,height,stats,xp));
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
		if (currentTileset != null) {
			packer.insertImage(currentTileset,tilesets.get(currentTileset));
		}
		if (getCurrentAnimation() != null) {
			packer.insertImage(getCurrentAnimation(),animations.get(getCurrentAnimation()));
		}
		textureAtlas.setTexture(BufferedImageUtil.getTexture("", packer.getImage()));
		textureAtlas.setRects(packer.getRects());
		mainWindow.setTextureAtlas(textureAtlas);
	}
	
	public void addToDrawables(ArrayList<DrawableObject> d) {
		drawables.addAll(d);
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
	
	public void init() {
		charList = new CharList();
		setMainWindow(new MainWindow(textureAtlas,SCREEN_WIDTH,SCREEN_HEIGHT));
		getMainWindow().start();
		loadMapNames();
		loadTileSets();
		loadAllImages();
		loadAllEntities();
		loadAllItems();
		loadAllPSI();
		loadAllAnims();
		loadAllEnemies();
		textureAtlas.getTexture().bind();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureAtlas.getTexture().getTextureID());
		
		selectionStack = new SelectionStack();
		menuStack = new MenuStack();
			Menu m = new MainMenu(this);
				SelectionTextWindow STW = new SelectionTextWindow(300,300,10,5,this);
				STW.add(new NewGameMenuItem(STW.getX(),STW.getY() + 16,this));
				STW.add(new ContinueMenuItem(STW.getX(),STW.getY() + 48,this));
				STW.add(new OptionsMenuItem(STW.getX(),STW.getY() + 80,this));
				STW.add(new MapPreviewTestButton(STW.getX(),STW.getY() + 112,this));
			m.addMenuItem(STW);
		menuStack.push(m);
		//test the music
//		setBGM("eb_title.wav");
//		bgm.playAsMusic(1.0f, 1.0f, false);
	}
	
	public Point getMouseCoordinates() {
		int x = Mouse.getX();
		int y = getMainWindow().getScreenHeight() - Mouse.getY();
		return new Point(x, y);
	}
	
	public void setHoldable(boolean b) {
		input.setHoldable(b);
	}
	
	public void update() {
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
			
		}
		Menu c = getMenuStack().peek();
		if (c != null) {
			ArrayList<DrawableObject> list = new ArrayList<DrawableObject>();
			list.addAll(c.getDrawableObjects());
			list.addAll(c.getMenuItems());
			addToDrawables(list);
			c.updateAll(input);
		}
		
		input.setHoldable(false);
		Point mouse = getMainWindow().getMouseCoordinates();
		for (Drawable d : getDrawables()) {
			if (d instanceof Hoverable) {
				if (((Hoverable) d).hovered(mouse.getX(), mouse.getY())) {
					((Hoverable) d).hoveredAction();
//					if (d instanceof LeftClickableItem) {
						input.setHoldable(true);
						((LeftClickableItem)d).checkInputs(input);
//						input.setHoldable(false);
//					}
				} else {
					((Hoverable) d).unhoveredAction();
				}
			}
		}
		
		
		
		if (gameState != null) {
			gameState.updatePartyMembers();
		}
		
		if (gameState != null && (menuStack.isEmpty() || menuStack.peek() instanceof AnimationMenuFadeFromBlack)) {
			gameState.update(input);
		}
		
		
		if (canLoad && gameState == null) {
			canLoad = false;
			menuStack.pop();
			menuStack.pop();
			needToPop = false;
			GameState gs = new GameState(this);
			this.setGameState(gs);
		}
	}
	
	
	
	public void render() {
//		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		getMainWindow().renderBG("bg.png");
		if (gameState != null) {
			gameState.drawGameState();
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
				System.out.println(updates + " ups, " + frames + " fps");
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
				System.exit(0);
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

	public void setDrawAllMenus() {
		// TODO Auto-generated method stub
		drawAllMenus = true;
	}

	public void setAudio(String string) {
		// TODO Auto-generated method stub
		
	}
}