package global;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import canvas.Drawable;
import canvas.Hoverable;
import canvas.MainWindow;
import canvas.MyMouseListener;
import mapeditor.MapPreview;
import mapeditor.Tile;
import mapeditor.TileHashMap;
import menu.Menu;

public class GlobalVars {
	private static List<Drawable> drawables = new ArrayList<Drawable>();
	public static MouseListener ml;
	public static MainWindow mainWindow;
	public static List<String> imageFileNames = new ArrayList<String>();
	public static List<BufferedImage> images = new ArrayList<BufferedImage>();
	public static MenuStack menuStack = new MenuStack();
	public static Menu currentMenu;
	public final static int TILE_SIZE = 32;
	public final static int SCREEN_WIDTH = 1280;
	public final static int SCREEN_HEIGHT = 720;
	public final static TileHashMap tileMap = new TileHashMap();
	public static Hoverable somethingHovered;
	public static int mapPreviewEditTool = 0;
	
	public static Point mousePos = new Point();
	
	public static void loadAllTiles() {
		tileMap.addTile(new Tile(0,"tile1.png"));
		tileMap.addTile(new Tile(1,"tile2.png"));
		tileMap.addTile(new Tile(2,"tile3.png"));
		tileMap.addTile(new Tile(3,"tile4.png"));
	}
	
	public static void loadAllImages()  {
		File f = new File("img");
		for (File c : f.listFiles()) {
			imageFileNames.add(c.getName());
		}
	}
	
	public static void addToDrawables(Drawable d) {
		drawables.add(d);
	}
	
	public static List<Drawable> getDrawables() {
		return drawables;
	}
	
	public static void clearDrawables() {
		drawables = new ArrayList<Drawable>();
	}
	
	public static void setDrawables(List<Drawable> l) {
		drawables = l;
	}
	
	public static Drawable getDrawable(int i) {
		return drawables.get(i);
	}
}
