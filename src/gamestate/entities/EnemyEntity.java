package gamestate.entities;

import java.util.ArrayList;

import battlesystem.Enemy;
import battlesystem.menu.BattleMenu;
import menu.Menu;
import menu.animation.Animation;
import menu.animation.AnimationMenu;
import menu.animation.SwirlAnimation;
import system.MotherSystemState;
import system.SystemState;

public class EnemyEntity extends MotherEntity {
	private ArrayList<Enemy> enemys;//should only have one
	public void setToRemove(boolean b) {
		super.setToRemove(b);
		MotherSystemState state = (MotherSystemState) getState();
		state.getGameState().addNumEntities(-1);
	}
	
	public EnemyEntity(double x, double y, int width, int height, SystemState m,
			ArrayList<Enemy> enemy) {
		super(enemy.get(0).getEntityId() + ".png", x, y, width, height, m, "enemy_" + enemy.get(0).getName());
		ArrayList<Enemy> clones = new ArrayList<Enemy>();
		for (Enemy e : enemy) {
			clones.add(((Enemy)e).clone());
		}
		this.enemys = clones;
		stepSizeX =4;
		stepSizeY =4;
		movementPattern = 4;
	}
	
//	public void update(GameState gs) {
//		super.update(gs);
//	}

	public void interact() {}
	
	public ArrayList<Enemy> getEnemiesList() {
		return enemys;
	}
	
	public int getMaxAllies() {
		return enemys.get(0).getMaxAllies();
	}

	@Override
	public void act() {
		MotherSystemState state = (MotherSystemState) getState();
		for (Entity e : interactables) {
			if (e instanceof Player && !state.getGameState().isInvincible()) {
				
				if (((Player) e).running) {
					
					//logic to instant win against the enemy
					
					//for now, just kill the enemy
					this.setToRemove(true);
					return;
					
				}
				
				if (state.getGameState().getEnemiesCanJoin()) {
					state.getGameState().addEnemyToBattleList(this);
				} else if (state.getGameState().getCanEncounter()) {
					state.getGameState().setCanEncounter(false);
					//if the player has already been hit, allow any colliding enemies to join
					SwirlAnimation anim = new SwirlAnimation(state, this);
					AnimationMenu animMenu = new AnimationMenu(state);
					animMenu.createAnimMenu(anim);
					anim.createAnimation();
					state.getMenuStack().push(animMenu);
				}
				
//				BattleMenu m = new BattleMenu(state);
//				m.startBattle(this);
			}
//			 else if (state.getGameState().getCanEncounter()){
//				
//			}
		}
	}

}
