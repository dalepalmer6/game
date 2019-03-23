package gamestate;

import java.io.File;
import java.util.ArrayList;

import canvas.Drawable;
import mapeditor.Map;
import mapeditor.MapEditMenu;
import menu.StartupNew;

public class GameState {
	private ArrayList<Drawable> drawables = new ArrayList<Drawable>();
	private ArrayList<Entity> entities;
	private Player player;
	private Camera camera;
	private Map map;
	private MapRenderer mapRenderer;
	private StartupNew state;
	
	public Map getMap() {
		return map;
	}
	
	public void loadMap(String mapName) {
		map.parseMap(new File(mapName));
		System.out.println("Successfully loaded.");
	}
	
	public ArrayList<Drawable> getDrawables() {
		return drawables;
	}
	
	public void addToDrawables(ArrayList<Drawable> d) {
		drawables.addAll(d);
	}
	
	public void addToDrawables(Drawable d) {
		drawables.add(d);
	}
	
	public ArrayList<Entity> getEntityList() {
		return entities;
	}
	
	public GameState(StartupNew s) {
		//test map
		this(s,new Map("testMap",100,100,s.tileMap));
	}
	
	public GameState(StartupNew s, Map m) {
		this.entities = new ArrayList<Entity>();
		this.state = s;
		this.camera = new Camera(state);
		this.player = new Player("player.png",400,64,16*4,24*4,16,24,camera,state);
		this.entities.add(player);
		this.entities.add(new Mook("mook.png",800,64,32*4,64*4,32,64,state));
		this.map = m;
		loadMap("TestMap.map");
		this.mapRenderer = new MapRenderer(m,getCamera(),state); 
		camera.setMapRenderer(mapRenderer);
		addToDrawables(mapRenderer);
	}
	
	public void update() {
		if (state.getMenuStack().isEmpty()) {
			for (Entity e : entities) {
				if (!drawables.contains(e)) {
					addToDrawables(e);
				}
				e.update(this);
				checkEntityCollisions();
			}
		}
	}
	
	public void checkEntityCollisions() {
		for (Entity e : entities) {
			for (Entity e2 : entities) {
				if (!(e == e2)) {
					if (e.checkCollision(e2, 8)) {
						e.addToInteractables(e2);
					}
				}
			}
		}
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
	
}
