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

	public EnemyEntity(int x, int y, int width, int height, StartupNew m,
			ArrayList<Enemy> enemy) {
		super(enemy.get(0).getEntityId() + ".png", x, y, width, height, m, "enemy_" + enemy.get(0).getName());
		ArrayList<Enemy> clones = new ArrayList<Enemy>();
		for (Enemy e : enemy) {
			clones.add(((Enemy)e).clone());
		}
		this.enemys = clones;
	}
	
	public void update() {
		Player player = state.getGameState().getPlayer();
		if (x - player.getX() < 0) {
			deltaX = 4;
			actionTaken="walking";
			directionX = "right";
		} else if (x - player.getX() > 0){
			deltaX = -4;
			actionTaken="walking";
			directionX = "left";
		} else {
			deltaX = 0;
//			directionX = "";
			actionTaken="idle";
		}
		if (y - player.getY() < 0) {
			deltaY = 4;
			actionTaken="walking";
			directionY = "down";
		} else if (y - player.getY() > 0){
			deltaY = -4;
			actionTaken="walking";
			directionY = "up";
		} else {
			deltaY = 0;
			directionY = "";
			actionTaken = "idle";
		}
	}

	public ArrayList<Enemy> getEnemiesList() {
		return enemys;
	}

	@Override
	public void act() {
		for (Entity e : interactables) {
			if (state.getGameState().getEnemiesCanJoin()) {
				state.getGameState().addEnemyToBattleList(this);
			} else if (state.getGameState().getCanEncounter()){
				if (e instanceof Player) {
					state.getGameState().setCanEncounter(false);
					//if the player has already been hit, allow any colliding enemies to join
					SwirlAnimation anim = new SwirlAnimation(state, "swirl", 0, 0, state.getMainWindow().getScreenWidth(),
							state.getMainWindow().getScreenHeight(), this);
					AnimationMenu animMenu = new AnimationMenu(state);
					animMenu.createAnimMenu(anim);
					anim.createAnimation();
					state.getMenuStack().push(animMenu);
//					BattleMenu m = new BattleMenu(state);
//					m.startBattle(this);
				}
			}
		}
	}

}
