package mapeditor;

import java.util.ArrayList;

import gamestate.DoorEntity;
import gamestate.Entity;

public class DoorEnumerator {
	private ArrayList<DoorEntity> a = new ArrayList<DoorEntity>();
	
	public ArrayList<DoorEntity> getDoors() {
		return a;
	}
	
	public DoorEnumerator(ArrayList<Entity> entityList) {
		for (Entity e : entityList) {
			if (e instanceof DoorEntity) {
				a.add((DoorEntity) e);
			}
		}
	}
}
