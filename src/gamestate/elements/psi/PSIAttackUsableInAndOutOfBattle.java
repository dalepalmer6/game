package gamestate.elements.psi;

import gamestate.elements.UsableInBattle;
import gamestate.elements.UsableOutOfBattle;

public class PSIAttackUsableInAndOutOfBattle extends PSIAttack implements UsableInBattle, UsableOutOfBattle {

	public PSIAttackUsableInAndOutOfBattle(int id, String name, String desc, int type, int action, String anim) {
		super(id, name, desc, type, action, anim);
		// TODO Auto-generated constructor stub
	}

}
