package gamestate;

import java.util.ArrayList;

import canvas.MainWindow;
import menu.StartupNew;

public class EnemySpawnEntity extends Entity {
	private ArrayList<EnemyEntity> spawned;
	private ArrayList<Enemy> enemies;
	private float[] rates;
	private boolean done;
	
	public void draw(MainWindow m) {}
	
	public void setNewParams(double x, double y, int w, int h, String name, int v1, int v2, int v3, int v4, float p1, float p2, float p3, float p4) {
		// TODO Auto-generated method stub
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
		this.name = name;
		this.enemies = new ArrayList<Enemy>();
		enemies.add(state.enemies.get(v1));
		enemies.add(state.enemies.get(v2));
		enemies.add(state.enemies.get(v3));
		enemies.add(state.enemies.get(v4));
		rates = new float[]{p1,p2,p3,p4};
		
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
		String value = "#\n";
		value += getName() + "\n";
		value += getX() + "," + getY() + "," + getWidth() + "," + getHeight() + "\n";
		for (Enemy e : enemies) {
			if (e == null) {
				break;
			}
			value += e.getId() + ",";
		}
		value += "\n";
		for (float val : rates) {
			value += val + ",";
		}
		value += "\n";
		return value;
	}
	
	public EnemySpawnEntity(double x, double y, int w, int h,StartupNew state, String name) {
		super("enemySpawn.png",x,y,w,h,state,name);
		this.spriteCoordinates = new SpritesheetCoordinates();
		spriteCoordinates.setPose("idle_down");
		spriteCoordinates.addStateToPose("idle_down",0,0,12,12);
		enemies = new ArrayList<Enemy>();
		enemies.add(state.enemies.get(0));
		enemies.add(state.enemies.get(0));
		enemies.add(state.enemies.get(0));
		enemies.add(state.enemies.get(0));
		rates = new float[] {0f,0f,0f,0f};
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
//							while (en.checkCollisions()) {
//								en = new EnemyEntity((x+randW),(y+randH),24*4,32*4,state,picked);
//							}
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
	
	public void setRates(float[] x) {
		rates = x;
	}
	
	public void setEnemies(String[] enemyNames) {
		enemies = new ArrayList<Enemy>();
		for (String name : enemyNames) {
			enemies.add(state.enemies.get(Integer.parseInt(name)));
		}
	}
	
}
