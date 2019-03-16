package menu;

import java.awt.Point;

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
	public void run(){
//		init();
		
		long lastTime = System.nanoTime();
		double delta = 0.0;
		double ns = 1000000000.0 / 60.0;
		long timer = System.currentTimeMillis();
		int updates = 0;
		int frames = 0;
		boolean running = true;
		while(true){
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1.0) {
//				update();
				updates++;
				delta--;
			}
			//render();
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
			
			GlobalVars.mainWindow.renderBG("bg.png");
			GlobalVars.mainWindow.drawDrawables();
		
			frames++;
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println(updates + " ups, " + frames + " fps");
				updates = 0;
				frames = 0;
			}
			//when to close the game
//			if(glfwWindowShouldClose(window) == GL_TRUE){
//				running = false;
//			}
		}

//		cleanup();
	}
//	public void run() {
//		while (true) {
//			//all logic happens here
//			if (GlobalVars.images.size() == 0) {
//				GlobalVars.loadAllImages();
//			}
//			if (GlobalVars.tileMap.size() == 0) {
//				GlobalVars.loadAllTiles();
//			}
//			if (GlobalVars.menuStack.size() == 0) {
//				//the menuStack is empty
//				Menu m = new MainMenu();
//				GlobalVars.menuStack.push(m);
//			}
//			Menu c = GlobalVars.menuStack.popAndAddMenuItems();
//			GlobalVars.menuStack.push(c);
//			
//			if (GlobalVars.mainWindow == null) {
//				GlobalVars.mainWindow = new MainWindow();
//				GlobalVars.mainWindow.start();
//			}
//			Point mouse = GlobalVars.mainWindow.getMouseCoordinates();
//			
//			//draws all the objects that need to be drawn
//			boolean somethingIsHovered = false;
//			for (Drawable d : GlobalVars.getDrawables()) {
//				d.draw();
//				if (d instanceof Hoverable) {
//					if (((Hoverable) d).hovered(mouse.getX(), mouse.getY())) {
//						somethingIsHovered = true;
//						((Hoverable) d).hoveredAction();
//						if ((Hoverable) d != GlobalVars.somethingHovered) {
//							//set the somethingHovered to the current Drawable
//							
//						}
//						if (d instanceof Clickable) {
//							//each of these if statements adds the MouseListener if it's of the instance
//							
//						}
//						if (d instanceof MapPreview) {
//							System.out.println("Hovering over the MapPreview");
//							
//						}
//						if (d instanceof TileBar) {
//							System.out.println("Hovering over the TileBar");
//							
//						}
//					} else {
//						((Hoverable) d).unhoveredAction();
//					}
//				}
//				if (somethingIsHovered == false) {
//					GlobalVars.somethingHovered = null;
//				}
//			}
//			try {
//				sleep(sleepTime);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	}
}
