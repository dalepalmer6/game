package system;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
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
import org.newdawn.slick.util.BufferedImageUtil;
import org.newdawn.slick.util.ResourceLoader;

import battlesystem.menu.BattleMenu;
import font.CharList;
import gamestate.GameState;
import gamestate.cutscene.Cutscene;
import gamestate.entities.Entity;
import menu.DrawableObject;
import menu.LeftClickableItem;
import menu.Menu;
import menu.actionmenu.goodsmenu.InvisibleMenuItem;
import menu.animation.AnimationFadeFromBlack;
import menu.animation.AnimationMenu;
import menu.animation.AnimationMenuFadeFromBlack;
import menu.mainmenu.ContinueMenuItem;
import menu.mainmenu.MainMenu;
import menu.mainmenu.MapPreviewTestButton;
import menu.mainmenu.NewGameMenuItem;
import menu.mainmenu.OptionsMenuItem;
import menu.mapeditmenu.mappreview.MapPreview;
import menu.text.TextEngine;
import menu.windows.SelectionTextWindow;
import system.controller.InputController;
import system.interfaces.Drawable;
import system.interfaces.Hoverable;
import system.map.Tile;
import system.map.TileHashMap;
import tiles.ChangeWithFlagTile;
import tiles.MultiInstanceTile;
import tiles.PremadeTileObject;
import tiles.SingleInstanceTile;

public class SystemState{
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
	protected MainWindow mainWindow;
	private MenuStack menuStack;
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
	public GameState gameState;
	protected TextureAtlas textureAtlas = new TextureAtlas();
	public boolean needToPop;
	public ArrayList<String> mapNames = new ArrayList<String>();
	private Map<String, BufferedImage> animations = new HashMap<String,BufferedImage>();
	private String currentTileset = null;
	public boolean canLoad;
	private Menu removeThisMenu;
	private boolean clearTheMenuStack;
	private boolean drawAllMenus;
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
	public Texture tilesetTexture;
	private float oldBGMStart;
	private float oldBGMEnd;
	private Cutscene currentCutscene;
	public ArrayList<String> textData;
	private boolean textEditor;
	private Menu needToAddMenu;
	private Menu savedMenu;
	private boolean needAddSavedMenu;
	private boolean createNewFile;
	private boolean bgmEnded;
	private String saveFileName;
	public boolean justTextData;
	private boolean restoreAudioWhenDone;
	
	public void setSaveFileName(String s) {
		saveFileName = s;
	}
	
	public void setNeedAddSavedMenu(Menu m) {
		needToAddMenu = m;
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
	
	public GameState getGameState() {
		return gameState;
	}
	
	public SelectionStack getSelectionStack() {
		return selectionStack;
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
	}
	
	public void loadCoordinatesFromFile(String fname,Entity e) {
		try (BufferedReader br = new BufferedReader(new FileReader(new File("entities/" + fname)))){
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
			err.printStackTrace();
		} catch (IOException err) {
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
		try (BufferedReader br = new BufferedReader(new FileReader(tileset.substring(0,tileset.length()-4) + ".ts"))){
			//"tilesets/onett.ts"));
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
				textures.put(filePath, ImageIO.read(new File(filePath)));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			createAtlas();
		} catch (IOException e) {
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
				e.printStackTrace();
			}
		}
	}
	
	//when all textures are finalized this method should not exist so we dont 
	//generate this on startup. (This introduces unnecessary overhead)
	public void createAtlas() throws IOException {
		int squareSize = 2048;
		ImagePacker packer = new ImagePacker(squareSize,squareSize,0,false);
		for (String key : textures.keySet()) {
			BufferedImage t = textures.get(key);
			packer.insertImage(key,t);
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
			e.printStackTrace();
		} catch (IOException e) {
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
			charList = new CharList();
			gameState = new GameState();
			loadMapNames();
			loadAllStrings();
			loadAllEntities();
			menuStack = new MenuStack();
			selectionStack = new SelectionStack();
		} else {
			charList = new CharList();
			createContext();
			loadMapNames();
			loadTileSets();
			loadAllImages("");
			loadImageData();
			loadAllEntities();
			
			setBGM("motherearth.ogg");
			textureAtlas.getTexture().bind();
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureAtlas.getTexture().getTextureID());
			
			selectionStack = new SelectionStack();
			menuStack = new MenuStack();
			
			//create the main menu
			Menu m = new MainMenu(this);
			SelectionTextWindow STW = new SelectionTextWindow(mainWindow.getScreenWidth()/2 - (2*64),mainWindow.getScreenHeight()-(7*64),4,3,this);
			STW.add(new NewGameMenuItem((int) STW.getX(),(int) STW.getY() + 16,this));
			STW.add(new ContinueMenuItem((int) STW.getX(),(int) STW.getY() + 48,this));
			STW.add(new OptionsMenuItem((int) STW.getX(),(int) STW.getY() + 80,this));
			STW.add(new MapPreviewTestButton((int) STW.getX(),(int) STW.getY() + 112,this));
			m.addMenuItem(STW);
			menuStack.push(m);
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
		if (fadeOutIsDone) {
			if (menuStack.peek() != null) {
				menuStack.peek().doDoneFadeOutAction();
			} 
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
			}
		}
		
		Menu c = getMenuStack().peek();
		if (c != null) {
			c.updateAll(input);
		}

		Point mouse = getMainWindow().getMouseCoordinates();
		for (Drawable d : getDrawables()) {
			if (d instanceof Hoverable) {
				if (((Hoverable) d).hovered(mouse.getX(), mouse.getY())) {
					((Hoverable) d).hoveredAction();
						input.setHoldable(false);
						if (d instanceof MapPreview) {
							input.setHoldable(true);
						}
						((LeftClickableItem)d).checkInputs(input);
				} else {
					((Hoverable) d).unhoveredAction();
				}
			}
		}
		
		GameState gameState = getGameState();
		
		if (gameState != null) {
			gameState.updateTimer();
			input.setHoldable(true);
		}
		
		if (gameState != null && (menuStack.isEmpty() || menuStack.peek() instanceof AnimationMenuFadeFromBlack)) {
			gameState.update(input);
		}
		
		if (!menuStack.isEmpty()) {
			if (menuStack.peek().getCanUpdateGameState()) {
				gameState.update(input);
			}
		}
		
		for (DrawableObject d : drawables) {
			if (d instanceof SelectionTextWindow || d instanceof InvisibleMenuItem) {
				input.setHoldable(false);
			}
		}
		
		if (gameState != null && menuStack.peek() instanceof AnimationMenu) {
			gameState.updateEntities(input,false);
		}
		
		//change this from AnimationMenu to when a boolean is set
		//this is where the GameState is init
		if (menuStack.peek() instanceof AnimationMenu && gameState == null) {
			if (((AnimationMenu)menuStack.peek()).isComplete()) {
				menuStack.pop();
				menuStack.pop();
				needToPop = false;
				
				GameState gs = createGameState();
				setGameState(gs);
				if (!createNewFile) {
					gs.loadFromSaveFile(saveFileName);
					gs.loadMapData();
				}else {
//					gs.loadFromSaveFile("test");
					setAudioOverride(true);
					gs.setDoIntro(true);
				}
			}
		}
	}
	
	public GameState createGameState() {
		return new GameState(this);
	}
	
	public void drawGameState() {
		GameState gameState = getGameState();
		if (gameState != null && gameState.shouldDraw()) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, mainWindow.getFrameBufferColorTextureId());
			mainWindow.renderMap();
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
			gameState.drawGameState();
		}
	}
	
	public void render() {
		GameState gameState = getGameState();
		if (gameState == null) {
			getMainWindow().renderBG("bg.png");
		}
		
		drawGameState();
		
		getMainWindow().drawDrawables(drawables);
	}
	
	public void gameLoop(boolean running) {
		
		while(running){
			clearDrawables();
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
		this.gameState = gs;
	}

	public TextureAtlas getTextureAtlas() {
		return textureAtlas;
	}

	public void setToRemove(Menu menu) {
		removeThisMenu = menu;
	}

	public void setClearMenuStack() {
		clearTheMenuStack = true;
	}

	//TODO is this used?
	public void setResultOfMenuToDisplay(String string) {
//		resultOfMenuToDisplay = string;
	}

	public void setDrawAllMenus(boolean b) {
		drawAllMenus = b;
	}

	public void setAudioOverride(boolean b) {
		
		audioOverride = b;
	}

	public void setFadeOutDone() {
		fadeOutIsDone = true;
	}

	public void stopBGM() {
		bgm.stop();
	}

	public void setShouldFadeIn(Menu m) {
		if (m != null) {
			needToAddMenu = m;
		}
		shouldFadeIn = true;
	}

	public Menu getSavedMenu() {
		return savedMenu;
	}
	
	public void saveCurrentDialogMenu() {
		savedMenu = menuStack.peek();
	}
	
	public void clearSavedMenu() {
		savedMenu = null;
	}

	public void addSavedMenu(boolean b) {
		needAddSavedMenu = b;
	}

	public void createNewGameGameState() {
		createNewFile = true;
	}

	public void setRestoreAudioWhenDone() {
		restoreAudioWhenDone = true;
	}
	
	public TextEngine createTextEngine(boolean drawAll, String text, int x, int y, int w, int h) {
		return new TextEngine(drawAll, text ,x , y ,w, h, charList);
	}

//	public void setCurrentAnimation(String string) {
//		
//	}

	public Cutscene getCutscene() {
		// TODO Auto-generated method stub
		return currentCutscene;
	}
	
	public void setCutscene(Cutscene cutscene) {
		// TODO Auto-generated method stub
		currentCutscene = cutscene;
	}
}