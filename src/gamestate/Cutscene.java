package gamestate;

import font.SimpleDialogMenu;
import menu.Menu;
import menu.StartupNew;

public class Cutscene extends Menu {
	private CutsceneData cutsceneData;
	private MovementData curMovement;
	private CutsceneMenuItem csMenuItem;
	
	public Cutscene(StartupNew m,CutsceneData cd) {
		super(m);
		// TODO Auto-generated constructor stub
		cutsceneData = cd;
		csMenuItem = new CutsceneMenuItem(state);
		menuItems.add(csMenuItem);
	}

	
	public void loadEntityToCutsceneData() {
		cutsceneData.loadEntity();
	}
	
	public void update() {
		doCutscene();
	}
	
	public void doCutscene() {
		String text = cutsceneData.getString();
		curMovement = cutsceneData.getNextMovementData();
		
		if (text != null) {
			SimpleDialogMenu.createDialogBox(state,text);
		}
		
		if (curMovement == null) {
			menuItems.remove(csMenuItem);
			cutsceneData.getEntity().resetMovement();
			return;
		}
		applyMovementToEntity();
//		killThis();
	}


	private void applyMovementToEntity() {
		Entity e = cutsceneData.getEntity();
		e.applyMovementData(curMovement.getX(),curMovement.getY(),curMovement.getState(),curMovement.getDirectionX(),curMovement.getDirectionY());
	}
	
}
