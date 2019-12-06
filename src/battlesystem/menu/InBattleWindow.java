package battlesystem.menu;

import java.util.ArrayList;

import gamestate.partymembers.PartyMember;
import menu.Menu;
import menu.windows.SelectionTextWindow;
import system.SystemState;
import system.controller.InputController;
import system.interfaces.Controllable;

public class InBattleWindow extends Menu implements Controllable{
	protected SelectionTextWindow stw;
	protected PartyMember pm;
	protected ArrayList<PartyMember> party;
	
	public InBattleWindow(SystemState m) {
		super(m);
		stw = new SelectionTextWindow("horizontal",state.getMainWindow().getScreenWidth()/2 - (16/2)*72,32,16,2,state);
		stw.setCurrentOpen(stw.getTextStartX()+96,stw.getTextStartY());
		stw.setTextStart(stw.getTextStartX()+96,stw.getTextStartY());
		stw.setSteps(680,0);
	}

	@Override
	public void handleInput(InputController input) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFocused(boolean b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getFocused() {
		// TODO Auto-generated method stub
		return false;
	} 

}
