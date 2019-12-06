package gamestate;

import java.util.ArrayList;

import system.SystemState;

public class EnemySpawnGroup {
	private int index;
	private int[] enemies;
	private float[] percents;
	private SystemState state;
	
	public EnemySpawnGroup(int index, int[] enemyIndex, float[] percents, SystemState state) {
		this.enemies = enemyIndex;
		this.percents = percents;
		this.index = index;
		this.state = state;
	}
	
	public int[] getEnemies() {
		return enemies;
	}
	
	public float[] getPercents() {
		return percents;
	}
	
	public int getIndex() {
		return index;
	}

	public String toString() {
		String string = index + ": ";
		for (int i : enemies) {
			string += state.enemies.get(i).getName() + ",";
		}
		return string;
	}
}
