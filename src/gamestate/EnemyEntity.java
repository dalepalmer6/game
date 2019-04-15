package gamestate;

import java.util.ArrayList;

import battlesystem.BattleMenu;
import menu.Menu;
import menu.StartupNew;

public class EnemyEntity extends Entity {
	private ArrayList<BattleEntity> enemys;
	
	public EnemyEntity(String texture, int x, int y, int width, int height,StartupNew m,String name,ArrayList<BattleEntity> enemy) {
		super(texture,x,y,width,height,m,name);
		this.enemys = enemy;
	}
	
	public ArrayList<BattleEntity> getEnemiesList() {
		return enemys;
	}
	
	@Override
	public void act() {
		for (Entity e : interactables) {
			if (e instanceof Player) {
				BattleMenu m = new BattleMenu(state);
				m.startBattle(this);
			}
		}
	}
	
}
