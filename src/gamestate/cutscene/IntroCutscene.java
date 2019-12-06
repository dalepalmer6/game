package gamestate.cutscene;

import system.SystemState;

public class IntroCutscene extends Cutscene {

	public IntroCutscene(SystemState m, CutsceneData cd) {
		super(m, cd);
		// TODO Auto-generated constructor stub
	}
	
	public void onEndAction() {
		state.setDoneIntro();
	}

}
