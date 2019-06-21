package gamestate;

import menu.StartupNew;

public class IntroCutscene extends Cutscene {

	public IntroCutscene(StartupNew m, CutsceneData cd) {
		super(m, cd);
		// TODO Auto-generated constructor stub
	}
	
	public void onEndAction() {
		state.setDoneIntro();
	}

}
