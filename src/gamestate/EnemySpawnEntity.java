package gamestate;

import java.util.ArrayList;

import canvas.MainWindow;
import menu.StartupNew;

public class EnemySpawnEntity extends Entity {
	private int index;
	private ArrayList<EnemyEntity> spawned;
	private ArrayList<Enemy> enemies;
	private float[] rates;
	private boolean done;
	
	public int getIndex() {
		return index;
	}
	
	public void draw(MainWindow m) {}
	
	public void setNewParams(int index, double x, double y, int w, int h) {
		this.index = index;
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
	}
	
	public ArrayList<Enemy> getEnemies() {
		return enemies;
	}
	
	public float[] getRates() {
		return rates;
	}
	
	@Override
	public String getInfoForTool() {
		String enemyList = "" + x + "," + y + ":";
		for (Enemy e : enemies) {
			enemyList += e.getName() + ",";
		}
		return enemyList;
	}
	
	public boolean checkCollisions() {
		return false;
	}
	
	public String toString() {
		String value = "";
		value += index + "," + getX() + "," + getY() + "," + getWidth() + "," + getHeight() + "\n";
		return value;
	}
	
	public EnemySpawnEntity(int index,double x, double y, int w, int h,StartupNew state, String name) {
		super("enemySpawn.png",x,y,w,h,state,name);
		this.spriteCoordinates = new SpritesheetCoordinates();
		spriteCoordinates.setPose("idle_down");
		spriteCoordinates.addStateToPose("idle_down",0,0,12,12);
		EnemySpawnGroup esg = state.enemySpawnGroups.get(index);
		int[] ids = esg.getEnemies();
		rates = esg.getPercents();
		enemies = new ArrayList<Enemy>();
		if (!state.justTextData) {
			for (int i = 0; i < ids.length; i++) {
				enemies.add(state.enemies.get(ids[i]));
			}
		}
		this.index = index;
	}
	
	public String getName() {
		return name;
	}
	
	public void interact() {}
	
	@Override
	public void kill() {
		done = false;
		spawned = new ArrayList<EnemyEntity>();
	}
	
	public void update(GameState gs) {
		for (int i = 0; i < enemies.size(); i++) {
			if (enemies.get(i) == null) {
				for (int j = i; j < enemies.size(); j++) {
					enemies.remove(j);
				}
				break;
			}
		}
		if (!done) {
			//only do this once when entity is loaded
			spawned = new ArrayList<EnemyEntity>(); 
			for (int i = 0; i < 3; i++) {
				double rand = Math.random();
				if (rand < 0.3) { //base rate
					for (int j = 0; j < rates.length; j++) {
						rand = Math.random();
						float rate = rates[j];
						if (rand < rate/100 && state.getGameState().getNumEnemies() < 4) {
							int randW = (int) (Math.random()*width);
							int randH = (int) (Math.random()*height);
							ArrayList<Enemy> picked = new ArrayList<Enemy>();
							picked.add(enemies.get(j));
							EnemyEntity en = new EnemyEntity((x+randW),(y+randH),24*4,32*4,state,picked);
							while (en.checkCollisions()) {
								randW = (int) (Math.random()*width);
								randH = (int) (Math.random()*height);
								en = new EnemyEntity((x+randW),(y+randH),24*4,32*4,state,picked);
							}
							Entity eStatic = state.allEntities.get("redDressLady");
							en.setSpriteCoords(eStatic.getSpriteCoordinates());
							spawned.add(en);
						}
					}
				}
			}
			if (spawned.size() > 3) {
				state.getGameState().getEntityList().add(spawned.get(0));
				state.getGameState().getEntityList().add(spawned.get(1));
				state.getGameState().getEntityList().add(spawned.get(2));
				state.getGameState().addNumEntities(3);
			} else {
				state.getGameState().addNumEntities(spawned.size());
				state.getGameState().getEntityList().addAll(spawned);
			}
			done = true;
		}
	}
	
}
