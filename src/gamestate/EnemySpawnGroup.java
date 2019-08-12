package gamestate;

import java.util.ArrayList;

import menu.StartupNew;

public class EnemySpawnGroup {
	private int index;
	private int[] enemies;
	private float[] percents;
	private StartupNew state;
	
	public EnemySpawnGroup(int index, int[] enemyIndex, float[] percents, StartupNew state) {
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
