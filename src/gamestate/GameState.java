package gamestate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import canvas.Controllable;
import canvas.Drawable;
import global.InputController;
import mapeditor.Map;
import mapeditor.MapEditMenu;
import menu.DrawableObject;
import menu.StartupNew;

public class GameState {
	private ArrayList<DrawableObject> drawables = new ArrayList<DrawableObject>();
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
	
	public ArrayList<DrawableObject> getDrawables() {
		return drawables;
	}
	
	public void addToDrawables(ArrayList<DrawableObject> d) {
		drawables.addAll(d);
	}
	
	public void addToDrawables(DrawableObject d) {
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
		this.entities.add(new Mook(800,100,32*4,64*4,state));
		this.player = new Ness(600,150,16*4,24*4,camera,state);
		this.entities.add(new Nooo(1000,100,100*4,64*4,state));
		this.entities.add(player);
		
		this.map = m;
		loadMap("TestMap.map");
		this.mapRenderer = new MapRenderer(m,getCamera(),state); 
		camera.setMapRenderer(mapRenderer);
//		addToDrawables(mapRenderer);
	}
	
	void sort()
    {
        int n = entities.size();
 
        // One by one move boundary of unsorted subarray
        for (int i = 0; i < n-1; i++)
        {
            // Find the minimum element in unsorted array
            int min_idx = i;
            for (int j = i+1; j < n; j++)
                if (entities.get(j).getYOnScreen() + entities.get(j).getHeight()
                		< entities.get(min_idx).getYOnScreen() + entities.get(min_idx).getHeight())
                    min_idx = j;
 
            // Swap the found minimum element with the first
            // element
            Entity temp = entities.get(min_idx);
            entities.remove(min_idx);
            entities.add(min_idx, entities.get(i));
            entities.remove(i);
            entities.add(i,temp);
        }
    }
	
	
	public void drawGameState() {
		drawMapRenderer();
		drawEntities();
	}
	
	public void drawMapRenderer() {
		mapRenderer.draw(state.getMainWindow());
	}
	
	public void drawEntities() {
		for (Entity e : entities) {
			e.draw(state.getMainWindow());
		}
	}
	

	
	public void parseInput(InputController input) {
		for (Entity e : entities) {
			e.handleInput(input);
		}
	}
	
	public void update() {
		sort();
		if (state.getMenuStack().isEmpty()) {
			for (Entity e : entities) {
				
				if (e instanceof Controllable) {
					state.setControllable(e);
				}
				checkEntityCollisions();
				e.update(this);
			}
		}
	}
	
	public void checkEntityCollisions() {
		for (Entity e : entities) {
			for (Entity e2 : entities) {
				if (!(e == e2)) {
					if (e.checkCollision(e2)) {
						e.deltaX = 0;
						e.deltaY = 0;
						e2.deltaX = 0;
						e2.deltaY = 0;
					} 
					if (e.checkCollisionWithTolerance(e2,8)) {
						e.addToInteractables(e2);
					} else {
						e.removeFromInteractables(e2);
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

	public void parseInput() {
		// TODO Auto-generated method stub
		
	}
	
}
