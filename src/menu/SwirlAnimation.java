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
	private AnimationFadeToBlack ftb;
	
	public SwirlAnimation(StartupNew m, String texture, int x, int y, int w, int h, EnemyEntity enemyEntity) {
		super(m,texture,x,y,w,h);
		state.saveAudio();
		state.setBGM("swirlgreen.ogg");
		ticksPerFrame = 0.5d;
		enemies = new ArrayList<EnemyEntity>();
		enemies.add(enemyEntity);
		state.getGameState().setBattleEnemyList(enemies);
		state.getGameState().setEnemiesCanJoin(true);
	}
	
	@Override
	public void updateAnim() {
		System.out.println(tickCount);
		
		if (readyToStart) {
			state.getMenuStack().peek().setToRemove(this);
			state.getGameState().setEnemiesCanJoin(false);
			BattleMenu m = new BattleMenu(state);
			m.startBattle(enemies);
//			state.getMenuStack().push(m);
			state.setShouldFadeIn();
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
			ftb = new AnimationFadeToBlack(state);
			state.getMenuStack().peek().addToMenuItems(ftb);
		}
		
		if (ftb != null) {
			if (ftb.isComplete()) {
				complete = true;
				readyToStart = true;
			}
		}
		
		TileMetadata tm = coordinates.getPose(0).getStateByNum(i);
		this.tm = tm;
	}
}
