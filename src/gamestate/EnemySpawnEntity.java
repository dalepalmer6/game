package gamestate;

import java.util.ArrayList;

import menu.StartupNew;

public class EnemySpawnEntity extends Entity {
	private ArrayList<EnemyEntity> spawned;
	private ArrayList<Enemy> enemies;
	private float[] rates;
	private boolean done;
	
	public void interact() {
		
	}
	
	public boolean checkCollisions() {
		return false;
	}
	
	public String toString() {
		String value = "#\n";
		value += getName() + "\n";
		value += getX() + "," + getY() + "," + getWidth() + "," + getHeight() + "\n";
		for (Enemy e : enemies) {
			value += e.getName() + ",";
		}
		value += "\n";
		for (float val : rates) {
			value += val + ",";
		}
		value += "\n";
		return value;
	}
	
	public EnemySpawnEntity(int x, int y, int w, int h,StartupNew state, String name) {
		super("enemySpawn.png",x,y,w,h,state,name);
		this.spriteCoordinates = new SpritesheetCoordinates();
		spriteCoordinates.setPose("idle_down");
		spriteCoordinates.addStateToPose("idle_down",0,0,12,12);
	}
	
	@Override
	public void kill() {
		done = false;
		spawned = new ArrayList<EnemyEntity>();
	}
	
	public void update(GameState gs) {
		if (!done) {
			//only do this once when entity is loaded
			spawned = new ArrayList<EnemyEntity>(); 
			for (int i = 0; i < 5; i++) {
				double rand = Math.random();
				if (rand < 0.1) {
					for (int j = 0; j < rates.length; j++) {
						rand = Math.random();
						float rate = rates[j];
						if (rand < rate/100) {
							int randW = (int) (Math.random()*width);
							int randH = (int) (Math.random()*height);
							ArrayList<BattleEntity> picked = new ArrayList<BattleEntity>();
							picked.add(enemies.get(j));
							EnemyEntity en = new EnemyEntity("ninten.png",(x+randW),(y+randH),24*4,32*4,state,"enemies",picked);
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
			} else {
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
			enemies.add(state.enemies.get(name));
		}
	}
	
}
