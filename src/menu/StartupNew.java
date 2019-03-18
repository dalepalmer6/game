package menu;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import canvas.Drawable;
import canvas.Hoverable;
import canvas.MainWindow;
import global.InputController;
import global.MenuStack;
import mapeditor.Tile;
import mapeditor.TileHashMap;
import menu.mainmenu.MainMenu;

public class StartupNew{
	private long sleepTime = 1000L / 60L;
	private InputController input = new InputController();
	private MainWindow mainWindow;
	private MenuStack menuStack;
	private List<Drawable> drawables = new ArrayList<Drawable>();
	public List<String> imageFileNames = new ArrayList<String>();
	public Map<String, Texture> textures = new HashMap<String,Texture>(); 
	public final int TILE_SIZE = 32;
	public final int SCREEN_WIDTH = 1920;
	public final int SCREEN_HEIGHT = 1080;
	public final TileHashMap tileMap = new TileHashMap();
//	public static int mapPreviewEditTool = 0;
	
	public void loadAllTiles(Texture t) {
		for (int i = 0; i < t.getImageWidth()*t.getImageHeight() / 256; i++) {
			tileMap.addTile(new Tile(i,t));
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
				textures.put(filePath, TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(filePath)));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void addToDrawables(Drawable d) {
		drawables.add(d);
	}
	
	public List<Drawable> getDrawables() {
		return drawables;
	}
	
	public void clearDrawables() {
		drawables = new ArrayList<Drawable>();
	}
	
	public void setDrawables(List<Drawable> l) {
		drawables = l;
	}
	
	public Drawable getDrawable(int i) {
		return drawables.get(i);
	}
	
	public void init() {
		setMainWindow(new MainWindow(SCREEN_WIDTH,SCREEN_HEIGHT));
		getMainWindow().start();
		loadAllImages();
		loadAllTiles(textures.get("img/tiles.png"));
		menuStack = new MenuStack();
		Menu m = new MainMenu(this);
		menuStack.push(m);
	}
	
	public Point getMouseCoordinates() {
		int x = Mouse.getX();
		int y = getMainWindow().getScreenHeight() - Mouse.getY();
		return new Point(x, y);
	}
	
	
	
	public void update() {
		//
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
		}
	}
	
	
	
	public void render() {
//		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		getMainWindow().renderBG("bg.png");	
		getMainWindow().drawDrawables(drawables);
	}
	
	public void gameLoop(boolean running) {
		long lastTime = System.nanoTime();
		double delta = 0.0;
		double ns = 1000000000.0 / 60.0;
		long timer = System.currentTimeMillis();
		int updates = 0;
		int frames = 0;
		while(running){
			//load current menu
			Menu c = getMenuStack().popAndAddMenuItems();
			clearDrawables();
			setDrawables(c.getMenuItems());
			getMenuStack().push(c);
			//check for input
			input.handleInputs();
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1.0) {
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

			Display.update();
			Display.sync(60);

			if (Display.isCloseRequested()) {
				Display.destroy();
				System.exit(0);
			}
			//when to close the game
//			if(glfwWindowShouldClose(window) == GL_TRUE){
//				running = false;
//			}
		}
	}
	
	public void run(){
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
}