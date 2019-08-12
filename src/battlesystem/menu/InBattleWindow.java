package battlesystem.menu;

import java.util.ArrayList;

import canvas.Controllable;
import font.SelectionTextWindow;
import gamestate.PartyMember;
import global.InputController;
import menu.Menu;
import menu.StartupNew;

public class InBattleWindow extends Menu implements Controllable{
	protected SelectionTextWindow stw;
	protected PartyMember pm;
	protected ArrayList<PartyMember> party;
	
	public InBattleWindow(StartupNew m) {
		super(m);
		stw = new SelectionTextWindow("horizontal",state.getMainWindow().getScreenWidth()/2 - (16/2)*72,32,16,2,state);
		stw.setCurrentOpen(stw.getTextStartX()+96,stw.getTextStartY());
		stw.setTextStart(stw.getTextStartX()+96,stw.getTextStartY());
		stw.setSteps(680,0);
		// TODO Auto-generated constructor stub
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
