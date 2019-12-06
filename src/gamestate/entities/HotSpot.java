package gamestate.entities;

import gamestate.cutscene.Cutscene;
import gamestate.cutscene.CutsceneData;
import system.MotherSystemState;
import system.SystemState;

public class HotSpot extends DoorEntity {
	private String cutsceneName;
	private Cutscene cutscene;
	
	public String getCutsceneName() {
		return cutsceneName;
	}
	
	public void setNewParams(double x, double y, int w, int h, String name, String texture, String appFlag, String disFlag, String cutsceneName) {
		// TODO Auto-generated method stub
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
		this.name = name;
		this.appearFlag = appFlag;
		this.disappearFlag = disFlag;
		this.cutsceneName = cutsceneName;
	}
	
	@Override
	public String getInfoForTool() {
		return cutsceneName + ": " + x + "," + y;
	}
	public HotSpot(String desc, double x, double y, int width, int height, SystemState m, int destX, int destY, String map,
			String text,String csname) {
		super(desc, x, y, width, height, m, destX, destY, map, text);
		// TODO Auto-generated constructor stub
		cutsceneName = csname;
		CutsceneData cd = new CutsceneData(state, csname,this);
		cutscene = new Cutscene(state,cd);
	}
	
	public void act() {
		for (Entity e : interactables) {
			if (e instanceof Player && ((MotherSystemState) state).getCutscene() == null) {
				((MotherSystemState) state).setCutscene(cutscene);
				cutscene.loadEntityToCutsceneData();
			}
		}
	}

}
