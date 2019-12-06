package gamestate.entities;

import system.MotherSystemState;
import system.SystemState;

public class MotherEntity extends Entity {
	protected MotherSystemState state;
	
	public MotherEntity(String texture, double x, double y, int width, int height, SystemState m, String name) {
		super(texture, x, y, width, height, m, name);
		this.state = (MotherSystemState) m;
	}
	
	public SystemState getState() {
		return state;
	}
	
}
