package gamestate;

import menu.StartupNew;

public class Ness extends Player {
	
	public Ness(int x, int y, int width, int height, Camera camera, StartupNew s) {
		super("ness.png", x, y, width, height, camera, s);
		// TODO Auto-generated constructor stub
		spriteCoordinates = new SpritesheetCoordinates();
		//use an enum 
		spriteCoordinates.setPose("idle_down");
		spriteCoordinates.addStateToPose("idle_down",0,0,16,24);
		spriteCoordinates.setPose("idle_up");
		spriteCoordinates.addStateToPose("idle_up",36,0,16,24);
		spriteCoordinates.setPose("idle_left");
		spriteCoordinates.addStateToPose("idle_left",52,0,16,24);
		spriteCoordinates.setPose("idle_right");
		spriteCoordinates.addStateToPose("idle_right",52,0,16,24,true);
		spriteCoordinates.setPose("idle_upleft");
		spriteCoordinates.addStateToPose("idle_upleft",206,0,16,24);
		spriteCoordinates.setPose("idle_upright");
		spriteCoordinates.addStateToPose("idle_upright",206,0,16,24,true);
		spriteCoordinates.setPose("idle_downright");
		spriteCoordinates.addStateToPose("idle_downright",172,0,16,24,true);
		spriteCoordinates.setPose("idle_downleft");
		spriteCoordinates.addStateToPose("idle_downleft",172,0,16,24);
		

		
		
		spriteCoordinates.setPose("walking_left");
		spriteCoordinates.addStateToPose("walking_left",52,0,16,24);
		spriteCoordinates.addStateToPose("walking_left",69,0,16,24);
		
		spriteCoordinates.setPose("walking_right");
		spriteCoordinates.addStateToPose("walking_right",52,0,16,24,true);
		spriteCoordinates.addStateToPose("walking_right",69,0,16,24,true);
		
		spriteCoordinates.setPose("walking_up");
		spriteCoordinates.addStateToPose("walking_up",36,0,16,24);
		spriteCoordinates.addStateToPose("walking_up",36,0,16,24,true);
		
		spriteCoordinates.setPose("walking_down");
		spriteCoordinates.addStateToPose("walking_down",1,0,16,24);
		spriteCoordinates.addStateToPose("walking_down",18,0,16,24);

		spriteCoordinates.setPose("walking_upleft");
		spriteCoordinates.addStateToPose("walking_upleft",206,0,16,24);
		spriteCoordinates.addStateToPose("walking_upleft",223,0,16,24);
		
		spriteCoordinates.setPose("walking_upright");
		spriteCoordinates.addStateToPose("walking_upright",206,0,16,24,true);
		spriteCoordinates.addStateToPose("walking_upright",223,0,16,24,true);
		
		spriteCoordinates.setPose("walking_downright");
		spriteCoordinates.addStateToPose("walking_downright",172,0,16,24,true);
		spriteCoordinates.addStateToPose("walking_downright",189,0,16,24,true);
		
		spriteCoordinates.setPose("walking_downleft");
		spriteCoordinates.addStateToPose("walking_downleft",172,0,16,24);
		spriteCoordinates.addStateToPose("walking_downleft",189,0,16,24);
	}
	
	
}
