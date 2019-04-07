package gamestate;

import menu.StartupNew;

public class Ness extends Player {
	
	public Ness(int x, int y, int width, int height, Camera camera, StartupNew s) {
		super("ness.png", x, y, width, height, camera, s);
		// TODO Auto-generated constructor stub
		setSpriteCoordinates(new SpritesheetCoordinates());
		//use an enum 
		getSpriteCoordinates().setPose("idle_down");
		getSpriteCoordinates().addStateToPose("idle_down",0,0,16,24);
		getSpriteCoordinates().setPose("idle_up");
		getSpriteCoordinates().addStateToPose("idle_up",36,0,16,24);
		getSpriteCoordinates().setPose("idle_left");
		getSpriteCoordinates().addStateToPose("idle_left",52,0,16,24);
		getSpriteCoordinates().setPose("idle_right");
		getSpriteCoordinates().addStateToPose("idle_right",52,0,16,24,true);
		getSpriteCoordinates().setPose("idle_upleft");
		getSpriteCoordinates().addStateToPose("idle_upleft",206,0,16,24);
		getSpriteCoordinates().setPose("idle_upright");
		getSpriteCoordinates().addStateToPose("idle_upright",206,0,16,24,true);
		getSpriteCoordinates().setPose("idle_downright");
		getSpriteCoordinates().addStateToPose("idle_downright",172,0,16,24,true);
		getSpriteCoordinates().setPose("idle_downleft");
		getSpriteCoordinates().addStateToPose("idle_downleft",172,0,16,24);
		

		
		
		getSpriteCoordinates().setPose("walking_left");
		getSpriteCoordinates().addStateToPose("walking_left",52,0,16,24);
		getSpriteCoordinates().addStateToPose("walking_left",69,0,16,24);
		
		getSpriteCoordinates().setPose("walking_right");
		getSpriteCoordinates().addStateToPose("walking_right",52,0,16,24,true);
		getSpriteCoordinates().addStateToPose("walking_right",69,0,16,24,true);
		
		getSpriteCoordinates().setPose("walking_up");
		getSpriteCoordinates().addStateToPose("walking_up",36,0,16,24);
		getSpriteCoordinates().addStateToPose("walking_up",36,0,16,24,true);
		
		getSpriteCoordinates().setPose("walking_down");
		getSpriteCoordinates().addStateToPose("walking_down",1,0,16,24);
		getSpriteCoordinates().addStateToPose("walking_down",18,0,16,24);

		getSpriteCoordinates().setPose("walking_upleft");
		getSpriteCoordinates().addStateToPose("walking_upleft",206,0,16,24);
		getSpriteCoordinates().addStateToPose("walking_upleft",223,0,16,24);
		
		getSpriteCoordinates().setPose("walking_upright");
		getSpriteCoordinates().addStateToPose("walking_upright",206,0,16,24,true);
		getSpriteCoordinates().addStateToPose("walking_upright",223,0,16,24,true);
		
		getSpriteCoordinates().setPose("walking_downright");
		getSpriteCoordinates().addStateToPose("walking_downright",172,0,16,24,true);
		getSpriteCoordinates().addStateToPose("walking_downright",189,0,16,24,true);
		
		getSpriteCoordinates().setPose("walking_downleft");
		getSpriteCoordinates().addStateToPose("walking_downleft",172,0,16,24);
		getSpriteCoordinates().addStateToPose("walking_downleft",189,0,16,24);
	}
	
	
}
