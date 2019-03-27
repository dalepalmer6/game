package gamestate;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

public class SpritesheetCoordinates {
	private HashMap<String, Pose> coordinates;
	
	public SpritesheetCoordinates() {
		this.coordinates = new HashMap<String, Pose>();
	}
	
	public void setPose(String s) {
		this.coordinates.put(s,new Pose());
	}
	
	public void addStateToPose(String pose, int x, int y, int width, int height) {
		addStateToPose(pose,x,y,width,height,false);
	}
	
	public void addStateToPose(String pose, int x, int y, int width, int height, boolean flipped) {
		TileMetadata tm = new TileMetadata(x,y,width,height,flipped);
		coordinates.get(pose).addMetadata(tm);
	}
	
	public Pose getPose(String action, String directionX, String directionY) {
		String pose = action + "_" + directionY + directionX;
		return this.coordinates.get(pose);
	}
}
