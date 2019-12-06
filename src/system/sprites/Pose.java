package system.sprites;

import java.util.ArrayList;
import java.util.HashMap;

public class Pose {
	private ArrayList<TileMetadata> poses;
	
	public Pose() {
		poses = new ArrayList<TileMetadata>();
	}
	
	public int getNumStates() {
		return poses.size();
	}
	
	public TileMetadata getStateByNum(int i) {
		return poses.get(i);
	}
	
	public void addMetadata(TileMetadata tm) {
		poses.add(tm);
	}
	
}
