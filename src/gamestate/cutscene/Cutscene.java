package gamestate.cutscene;

import gamestate.entities.Entity;
import menu.Menu;
import menu.animation.AnimationMenu;
import menu.windows.SimpleDialogMenu;
import system.SystemState;
import system.controller.InputController;

public class Cutscene extends Menu {
	protected CutsceneData cutsceneData;
	protected MovementData curMovement;
	private CutsceneMenuItem csMenuItem;
	private String text;
	protected boolean reverseOrder;
	private boolean ended;
	private boolean lastTriggerWasText;
	
	public Cutscene(SystemState m,CutsceneData cd) {
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
	
//	public void update(InputController input) {
//		doCutscene();
//	}
	
	public void doCutscene() {
		boolean canGoIn = false;
		if (state.getMenuStack().peek() instanceof AnimationMenu) {
			return;
		}
		if (cutsceneData.getEntity() != null) {
			canGoIn = cutsceneData.getEntity().getAtTargetPoint();
			if (!state.getMenuStack().isEmpty()) {
				if (!(state.getMenuStack().peek() instanceof AnimationMenu) && !state.getMenuStack().peek().getCanUpdateGameState()) {
					canGoIn = false;
				}
			}
		} else {
			canGoIn = true;
		}
		if (lastTriggerWasText) {
			if (state.getMenuStack().isEmpty()) {
				lastTriggerWasText = false;
				canGoIn = true;
			} else {
				canGoIn = canGoIn || false;
			}
		}
		if (canGoIn) {
			curMovement = cutsceneData.getMovementData();
			if (cutsceneData.getEntity() != null && curMovement != null) {
				cutsceneData.getEntity().setAtTargetPoint(false);
			}
			
			text = cutsceneData.getString();
			if (text != null) {
				lastTriggerWasText=true;
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
		ended = true;
		if (cutsceneData.getEntity() != null) {
			cutsceneData.getEntity().resetMovement();
		}
		
		onEndAction();
	}

	public void onEndAction() {
		cutsceneData.removeHotSpot();
	}
	
	public boolean needToRemove() {
		return ended;
	}

	private void applyMovementToEntity() {
		Entity e = cutsceneData.getEntity();
		e.applyMovementData(curMovement.getX(),curMovement.getY(),curMovement.getState(),curMovement.getDirectionX(),curMovement.getDirectionY());
	}
	
}
