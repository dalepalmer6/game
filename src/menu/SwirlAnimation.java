package menu;

import java.util.ArrayList;

import battlesystem.BattleMenu;
import gamestate.BattleEntity;
import gamestate.Enemy;
import gamestate.EnemyEntity;
import gamestate.TileMetadata;

public class SwirlAnimation extends Animation {
	public ArrayList<EnemyEntity> enemies;
	private float timer = 0f;
	private float length = 36f;
	private boolean fadeOut;
	private boolean readyToStart;
	public SwirlAnimation(StartupNew m, String texture, int x, int y, int w, int h, EnemyEntity enemyEntity) {
		super(m,texture,x,y,w,h);
		ticksPerFrame = 0.2d;
		enemies = new ArrayList<EnemyEntity>();
		enemies.add(enemyEntity);
	}
	
	@Override
	public void updateAnim() {
		System.out.println(tickCount);
		
		if (readyToStart) {
//			if (((AnimationFadeToBlack)((AnimationMenu) state.getMenuStack().peek()).getAnimation()).isComplete()) {
				state.getMenuStack().peek().setToRemove(this);
				BattleMenu m = new BattleMenu(state);
				m.startBattle(enemies);
				state.getMenuStack().push(m);
				AnimationMenu ffb = new AnimationMenu(state);
				ffb.createAnimMenu(new AnimationFadeFromBlack(state));
				state.getMenuStack().push(ffb);
//			}
		}
		
		int i = (int) Math.min(coordinates.getPose(0).getNumStates(),tickCount % coordinates.getPose(0).getNumStates());
		if (timer < length) {
			timer += ticksPerFrame;
			if (timer + ticksPerFrame >= length) {
				timer += ticksPerFrame;
				fadeOut = true;
			} 
		}
		
		if (tickCount <= coordinates.getPose(0).getNumStates()-1) {
			tickCount += ticksPerFrame;
		}
		
		if (fadeOut) {
			fadeOut = false;
			Animation ftb = new AnimationFadeToBlack(state);
			AnimationMenu animMenu = new AnimationMenu(state);
			animMenu.createAnimMenu(ftb);
			state.getMenuStack().push(animMenu);
			tickCount = 0;
			readyToStart = true;
		}
		
		TileMetadata tm = coordinates.getPose(0).getStateByNum(i);
		this.tm = tm;
	}
}
