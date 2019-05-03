package gamestate;

import menu.StartupNew;

public class HotSpot extends DoorEntity {
	private String cutsceneName;
	private Cutscene cutscene;
	
	public String getCutsceneName() {
		return cutsceneName;
	}
	
	public HotSpot(String desc, int x, int y, int width, int height, StartupNew m, int destX, int destY, String map,
			String text,String csname) {
		super(desc, x, y, width, height, m, destX, destY, map, text);
		// TODO Auto-generated constructor stub
		cutsceneName = csname;
		CutsceneData cd = new CutsceneData(state, csname);
		cutscene = new Cutscene(state,cd);
	}
	
	public void act() {
		for (Entity e : interactables) {
			if (e instanceof Player) {
				state.getMenuStack().push(cutscene);
				cutscene.loadEntityToCutsceneData();
			}
		}
	}

}
