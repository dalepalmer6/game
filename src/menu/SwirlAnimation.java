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
	
	public SwirlAnimation(StartupNew m, ArrayList<EnemyEntity> enemyEntities) {
		super(m,"swirl",0,0,m.getMainWindow().getScreenWidth(),m.getMainWindow().getScreenHeight());
		state.saveAudio();
		state.setAudioOverride(false);
		state.setBGM("swirlgreen.ogg");
		state.setAudioOverride(true);
		ticksPerFrame = 0.5d;
//		enemies = new ArrayList<EnemyEntity>();
		enemies = enemyEntities;
		state.getGameState().setBattleEnemyList(enemies);
//		state.getGameState().setEnemiesCanJoin(true);
	}
	
	public SwirlAnimation(StartupNew m, EnemyEntity enemyEntity) {
		super(m,"swirl",0,0,m.getMainWindow().getScreenWidth(),m.getMainWindow().getScreenHeight());
		state.saveAudio();
		state.setAudioOverride(false);
		state.setBGM("swirlgreen.ogg");
		state.setAudioOverride(true);
		ticksPerFrame = 0.5d;
		enemies = new ArrayList<EnemyEntity>();
		enemies.add(enemyEntity);
		state.getGameState().setBattleEnemyList(enemies);
		state.getGameState().setEnemiesCanJoin(true);
	}
	
	public void startBattle() {
		state.getMenuStack().peek().setToRemove(this);
		state.getGameState().setEnemiesCanJoin(false);
		BattleMenu m = new BattleMenu(state);
		m.startBattle(enemies);
//		state.getMenuStack().push(m);
		state.setShouldFadeIn(m);
	}	
	
	@Override
	public void updateAnim() {
		System.out.println(tickCount);
		
//		if (readyToStart) {
//			state.getMenuStack().peek().setToRemove(this);
//			state.getGameState().setEnemiesCanJoin(false);
//			BattleMenu m = new BattleMenu(state);
//			m.startBattle(enemies);
////			state.getMenuStack().push(m);
//			state.setShouldFadeIn();
//		}
		
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
//			ftb = new AnimationFadeToBlack(state);
			AnimationMenu m = new AnimationMenu(state);
			m.createAnimMenu();
			state.getMenuStack().push(m);
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
