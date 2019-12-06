package menu.animation;

import java.util.ArrayList;

import battlesystem.BattleEntity;
import battlesystem.Enemy;
import battlesystem.menu.BattleMenu;
import gamestate.entities.EnemyEntity;
import gamestate.entities.Entity;
import system.SystemState;
import system.sprites.TileMetadata;

public class SwirlAnimation extends Animation {
	public ArrayList<EnemyEntity> enemies;
	private float timer = 0f;
	private float length = 36f;
	private boolean fadeOut;
	private boolean readyToStart;
	private AnimationFadeToBlack ftb;
	ArrayList<EnemyEntity> enemyEntities = new ArrayList<EnemyEntity>();
	
	public SwirlAnimation(SystemState m, ArrayList<EnemyEntity> enemyEntities) {
		super(m,"swirl",0,0,m.getMainWindow().getScreenWidth(),m.getMainWindow().getScreenHeight());
		state.saveAudio();
		state.setAudioOverride(false);
		state.setBGM("swirlgreen.ogg");
		state.setAudioOverride(true);
		ticksPerFrame = 0.5d;
//		enemies = new ArrayList<EnemyEntity>();
		enemies = enemyEntities;
		state.getGameState().setBattleEnemyList(enemies);
		state.getGameState().setEnemiesCanJoin(false);
		calculateEnemiesThatCanJoin(enemyEntities.get(0));
	}
	
	public SwirlAnimation(SystemState m, EnemyEntity enemyEntity) {
		super(m,"swirl",0,0,m.getMainWindow().getScreenWidth(),m.getMainWindow().getScreenHeight());
		state.saveAudio();
		state.setAudioOverride(false);
		state.setBGM("swirlgreen.ogg");
		state.setAudioOverride(true);
		ticksPerFrame = 0.5d;
		enemies = new ArrayList<EnemyEntity>();
		enemies.add(enemyEntity);
		state.getMainWindow().setBattleTexture(null,null,null);
		state.getGameState().setBattleEnemyList(enemies);
		state.getGameState().setEnemiesCanJoin(true);
		calculateEnemiesThatCanJoin(enemyEntity);
//		state.getGameState().getEntityList().add(enemyEntity);
	}
	
	public void calculateEnemiesThatCanJoin(EnemyEntity enemyEntity) {
		state.getGameState().setMaxAllies(enemyEntity.getMaxAllies());
		ArrayList<EnemyEntity> enemyEntities = state.getGameState().getEnemyEntities();
		state.getGameState().saveEnemyEntities(enemyEntities);
		enemyEntities = sort(enemyEntities);
		
		state.getGameState().getEntityList().removeAll(enemyEntities);
		
		this.enemyEntities = enemyEntities;
		
		//take the first MAXALLIES enemies
		int numAllies = state.getGameState().getMaxAllies();
		ArrayList<EnemyEntity> enemiesThatCanJoin = new ArrayList<EnemyEntity>();
		for (int i = 0; i < numAllies; i++) {
			if (i >= enemyEntities.size()) {
				break;
			}
			EnemyEntity en = enemyEntities.get(i);
			if (en == enemyEntity) {
				numAllies += 1;
			}
			enemiesThatCanJoin.add(en);
		}
		
		state.getGameState().getEntityList().addAll(enemiesThatCanJoin);
	}
	
	ArrayList<EnemyEntity> sort(ArrayList<EnemyEntity> entities)
    {
        int n = entities.size();
 
        // One by one move boundary of unsorted subarray
        for (int i = 0; i < n-1; i++)
        {
            // Find the minimum element in unsorted array
            int min_idx = i;
            for (int j = i+1; j < n; j++)
                if (entities.get(j).getDistanceFromPlayer()
                		< entities.get(min_idx).getDistanceFromPlayer())
                    min_idx = j;
 
            // Swap the found minimum element with the first
            // element
            EnemyEntity temp = entities.get(min_idx);
            entities.remove(min_idx);
            entities.add(min_idx, entities.get(i));
            entities.remove(i);
            entities.add(i,temp);
        }
        return entities;
    }
	
	public void startBattle() {
		state.getGameState().getEntityList().removeAll(this.enemyEntities);
		state.getMenuStack().peek().setToRemove(this);
		state.getGameState().setEnemiesCanJoin(false);
		BattleMenu m = new BattleMenu(state);
		m.startBattle(enemies);
//		state.getMenuStack().push(m);
		state.setShouldFadeIn(m);
	}	
	
	@Override
	public void updateAnim() {
		SystemState.out.println(tickCount);
		
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
