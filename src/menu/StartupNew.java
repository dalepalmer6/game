package menu;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.BufferedImageUtil;
import org.newdawn.slick.util.ResourceLoader;

import canvas.Controllable;
import canvas.Drawable;
import canvas.Hoverable;
import canvas.MainWindow;
import font.CharList;
import font.SelectionTextWindow;
import font.TextWindow;
import gamestate.Entity;
import gamestate.GameState;
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
	private ArrayList<Controllable> c = new ArrayList<Controllable>();
	private SelectionStack selectionStack;
	private List<DrawableObject> drawables = new ArrayList<DrawableObject>();
	public List<String> imageFileNames = new ArrayList<String>();
	public Map<String, BufferedImage> textures = new HashMap<String,BufferedImage>(); 
	public final int TILE_SIZE = 32;
	public final int SCREEN_WIDTH = 1920;
	public final int SCREEN_HEIGHT = 1080;
	public CharList charList;
	public final TileHashMap tileMap = new TileHashMap();
	private String outputFromSelect = "";
	public GameState gameState;
	private TextureAtlas textureAtlas = new TextureAtlas();
	public boolean needToPop;
//	public static int mapPreviewEditTool = 0;
	
	public GameState getGameState() {
		return gameState;
	}
	
	public SelectionStack getSelectionStack() {
		return selectionStack;
	}
	
	public void setOutputFromSelect(String s) {
		outputFromSelect = s;
	}
	
	
	public void loadAllTiles() {
		textureAtlas.setRectByName("img/tiles.png");
		Rectangle tileBounds = textureAtlas.getCurrentRectangle();
//		for (int i = 0; i < tileBounds.width*tileBounds.height / 256; i++) {
//			tileMap.addTile(new Tile(i,tileBounds));
//		} 
		tileMap.addTile(new Plain(0));
		tileMap.addTile(new Path(1));
		tileMap.addTile(new Mountain(2));
		tileMap.addTile(new Tree(3));
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
	
	public void createAtlas() throws IOException {
		ImagePacker packer = new ImagePacker(1024,1024,0,false);
		for (String key : textures.keySet()) {
			BufferedImage t = textures.get(key);
			packer.insertImage(key,t);
		}
		textureAtlas.setTexture(BufferedImageUtil.getTexture("", packer.getImage()));
		textureAtlas.setRects(packer.getRects());
	}
	
	public void addToDrawables(ArrayList<DrawableObject> d) {
		drawables.addAll(d);
	}
	
	public List<DrawableObject> getDrawables() {
		return drawables;
	}
	
	public void clearDrawables() {
		drawables = new ArrayList<DrawableObject>();
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
		loadAllImages();
		textureAtlas.getTexture().bind();
		loadAllTiles();
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
	}
	
	public Point getMouseCoordinates() {
		int x = Mouse.getX();
		int y = getMainWindow().getScreenHeight() - Mouse.getY();
		return new Point(x, y);
	}
	
	
	
	public void update() {
		input.setHoldable(false);
		Point mouse = getMainWindow().getMouseCoordinates();
		for (Drawable d : getDrawables()) {
			if (d instanceof Hoverable) {
				if (((Hoverable) d).hovered(mouse.getX(), mouse.getY())) {
					((Hoverable) d).hoveredAction();
					if (d instanceof LeftClickableItem) {
						((LeftClickableItem) d).checkInputs(input);
					}
				} else {
					((Hoverable) d).unhoveredAction();
				}
			}
			if (d instanceof Controllable) {
				if (!c.isEmpty()) {
					this.c.remove(0);
				}
				this.c.add((Controllable) d);
			}
		}
		this.c.get(this.c.size() -1).handleInput(input);
		
		if (gameState != null && menuStack.isEmpty()) {
			gameState.update();
		}
		
		Menu c = getMenuStack().popAndAddMenuItems();
		if (c != null) {
			getMenuStack().push(c);
			c.update();
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
			Menu c = getMenuStack().popAndAddMenuItems();
			if (c != null) {
				addToDrawables((ArrayList<DrawableObject>) c.getMenuItems());
				getMenuStack().push(c);
			}
			
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
		c.add((Controllable) e);
	}
}