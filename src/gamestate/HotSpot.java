package gamestate;

import menu.StartupNew;

public class HotSpot extends DoorEntity {
	private String cutsceneName;
	private Cutscene cutscene;
	
	public String getCutsceneName() {
		return cutsceneName;
	}
	
	public void setNewParams(int x, int y, int w, int h, String name, String texture, String appFlag, String disFlag, String cutsceneName) {
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
	public HotSpot(String desc, int x, int y, int width, int height, StartupNew m, int destX, int destY, String map,
			String text,String csname) {
		super(desc, x, y, width, height, m, destX, destY, map, text);
		// TODO Auto-generated constructor stub
		cutsceneName = csname;
		CutsceneData cd = new CutsceneData(state, csname,this);
		cutscene = new Cutscene(state,cd);
	}
	
	public void act() {
		for (Entity e : interactables) {
			if (e instanceof Player && state.getCutscene() == null) {
				state.setCutscene(cutscene);
				cutscene.loadEntityToCutsceneData();
			}
		}
	}

}
