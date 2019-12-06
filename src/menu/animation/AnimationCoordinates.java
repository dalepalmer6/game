package menu.animation;

import java.util.HashMap;

import system.sprites.Pose;
import system.sprites.TileMetadata;

public class AnimationCoordinates {
	private HashMap<Integer,Pose> coordinates;
	
	public AnimationCoordinates() {
		coordinates = new HashMap<Integer,Pose>();
	}
	
	public boolean containsKey(int s) {
		return coordinates.containsKey(s);
	}
	
	public void setPose(int s) {
		this.coordinates.put(s,new Pose());
	}
	
	public void addStateToPose(int pose, int x, int y, int width, int height) {
		addStateToPose(pose,x,y,width,height,false);
	}
	
	public void addStateToPose(int pose, int x, int y, int width, int height, boolean flipped) {
		TileMetadata tm = new TileMetadata(x,y,width,height,flipped);
		coordinates.get(pose).addMetadata(tm);
	}
	
	public Pose getPose(int pose) {
		return this.coordinates.get(pose);
	}
}
