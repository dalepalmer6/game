package gamestate;

import java.util.ArrayList;

import battlesystem.BattleMenu;
import menu.Animation;
import menu.AnimationMenu;
import menu.Menu;
import menu.StartupNew;
import menu.SwirlAnimation;

public class EnemyEntity extends Entity {
	private ArrayList<Enemy> enemys;//should only have one
	public void setToRemove(boolean b) {
		super.setToRemove(b);
		state.getGameState().addNumEntities(-1);
	}
	
	public EnemyEntity(double x, double y, int width, int height, StartupNew m,
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
		for (Entity e : interactables) {
			if (e instanceof Player && !state.getGameState().isInvincible()) {
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
