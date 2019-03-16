package menu;

import java.awt.List;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.Timer;

import canvas.Clickable;
import canvas.Drawable;
import canvas.Hoverable;
import canvas.MainWindow;
import canvas.MyMouseListener;
import canvas.TileBarMouseListener;
import global.GlobalVars;
import mapeditor.MapMouseListener;
import mapeditor.MapPreview;
import mapeditor.TileBar;
import menu.mainmenu.ContinueMenuItem;
import menu.mainmenu.MainMenu;
import menu.mainmenu.NewGameMenuItem;
import menu.mainmenu.OptionsMenuItem;

public class Startup implements ActionListener{
	//a timer that will fire an event 60 times per second (60fps)
			private Timer timer = new Timer(1000/60, this);
			private MainWindow mainWindow;
			
			public Startup() {
				this.timer.start();
				run();
			}
			
			public void actionPerformed(ActionEvent ev) {
				if (ev.getSource() == timer) {
					if (mainWindow != null) 
						run();
				}
			}
	
	public void run() {
		//all logic happens here
		if (GlobalVars.images.size() == 0) {
			GlobalVars.loadAllImages();
		}
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
			this.mainWindow = new MainWindow();
			GlobalVars.mainWindow = this.mainWindow;
			this.mainWindow.start();
		}
		
		
		
		Point mouse = mainWindow.getMouseCoordinates();
		
		
		
		//draws all the objects that need to be drawn
		boolean somethingIsHovered = false;
		for (Drawable d : GlobalVars.getDrawables()) {
			d.draw();
			if (d instanceof Hoverable) {
				if (((Hoverable) d).hovered(mouse.getX(), mouse.getY())) {
					somethingIsHovered = true;
					((Hoverable) d).hoveredAction();
					if ((Hoverable) d != GlobalVars.somethingHovered) {
						//set the somethingHovered to the current Drawable
						
					}
					if (d instanceof Clickable) {
						//each of these if statements adds the MouseListener if it's of the instance
						
					}
					if (d instanceof MapPreview) {
						System.out.println("Hovering over the MapPreview");
						
					}
					if (d instanceof TileBar) {
						System.out.println("Hovering over the TileBar");
						
					}
				} else {
					((Hoverable) d).unhoveredAction();
				}
			}
				if (somethingIsHovered == false) {
					GlobalVars.somethingHovered = null;
				}
		}
	}
	
}
