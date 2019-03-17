package menu;

import java.awt.Point;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import canvas.Clickable;
import canvas.Drawable;
import canvas.Hoverable;
import canvas.MainWindow;
import global.GlobalVars;
import mapeditor.MapPreview;
import mapeditor.TileBar;
import menu.mainmenu.MainMenu;

public class StartupNew{
	private long sleepTime = 1000L / 60L;
	
	public void init() {
		if (GlobalVars.tileMap.size() == 0) {
			GlobalVars.loadAllTiles();
		}
		if (GlobalVars.menuStack.size() == 0) {
			//the menuStack is empty
			Menu m = new MainMenu();
			GlobalVars.menuStack.push(m);
		}
		Menu c = GlobalVars.menuStack.popAndAddMenuItems();
		GlobalVars.menuStack.push(c);
		
		if (GlobalVars.mainWindow == null) {
			GlobalVars.mainWindow = new MainWindow();
			GlobalVars.mainWindow.start();
		}
	}
	
	public Point getMouseCoordinates() {
		int x = Mouse.getX();
		int y = GlobalVars.mainWindow.getScreenHeight() - Mouse.getY();
		return new Point(x, y);
	}
	
	public boolean mouseLeft() {
		if (Mouse.isButtonDown(0)) {
			return true;
		} else {
			return false;
		}
	}
	
	public void update() {
		//
		Point mouse = GlobalVars.mainWindow.getMouseCoordinates();
		for (Drawable d : GlobalVars.getDrawables()) {
			if (d instanceof Hoverable) {
				if (((Hoverable) d).hovered(mouse.getX(), mouse.getY())) {
					((Hoverable) d).hoveredAction();
					if (d instanceof Clickable) {
						if (mouseLeft()) {
							((Clickable) d).execute();
						}
					}
				} else {
					((Hoverable) d).unhoveredAction();
				}
			}
		}
	}
	
	public void render() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GlobalVars.mainWindow.drawDrawables();
	}
	
	public void gameLoop(boolean running) {
		long lastTime = System.nanoTime();
		double delta = 0.0;
		double ns = 1000000000.0 / 60.0;
		long timer = System.currentTimeMillis();
		int updates = 0;
		int frames = 0;
		while(running){
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1.0) {
				System.out.println("Updating gamelogic");
				update();
				updates++;
				delta--;
			}
			System.out.println("Rendering");
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
}