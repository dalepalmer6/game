package gamestate;

import font.SimpleDialogMenu;
import global.InputController;
import menu.Menu;
import menu.StartupNew;

public class Cutscene extends Menu {
	protected CutsceneData cutsceneData;
	protected MovementData curMovement;
	private CutsceneMenuItem csMenuItem;
	private String text;
	protected boolean reverseOrder;
	
	public Cutscene(StartupNew m,CutsceneData cd) {
		super(m);
		// TODO Auto-generated constructor stub
		cutsceneData = cd;
		csMenuItem = new CutsceneMenuItem(state);
		menuItems.add(csMenuItem);
	}

	
	public void setReverse() {
		reverseOrder = true;
	}
	
	public void loadEntityToCutsceneData() {
		cutsceneData.loadEntity();
	}
	
	public void update(InputController input) {
		doCutscene();
	}
	
	public void doCutscene() {
		if (cutsceneData.getEntity().getAtTargetPoint()) {
			curMovement = cutsceneData.getMovementData();
			text = cutsceneData.getString();
			if (text != null) {
				SimpleDialogMenu.createDialogBox(state,text);
			} 
			else if (curMovement != null){
				applyMovementToEntity();
			}
			else if (curMovement == null && text == null) {
				end();
				return;
			}
			cutsceneData.step(reverseOrder);
		}
	}
	
	public void end() {
		menuItems.remove(csMenuItem);
		cutsceneData.getEntity().resetMovement();
		onEndAction();
	}

	public void onEndAction() {
		cutsceneData.removeHotSpot();
	}

	private void applyMovementToEntity() {
		Entity e = cutsceneData.getEntity();
		e.applyMovementData(curMovement.getX(),curMovement.getY(),curMovement.getState(),curMovement.getDirectionX(),curMovement.getDirectionY());
	}
	
}
