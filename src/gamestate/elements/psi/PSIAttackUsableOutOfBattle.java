package gamestate.elements.psi;

import gamestate.elements.UsableOutOfBattle;
import gamestate.partymembers.PartyMember;

public class PSIAttackUsableOutOfBattle extends PSIAttack implements UsableOutOfBattle {

	public PSIAttackUsableOutOfBattle(int id, String name, String desc, int type, int action, String anim,String classif,String family,String stage, int ppused) {
		super(id, name, desc, type, action,anim,classif,family,stage,ppused);
		// TODO Auto-generated constructor stub
	}


}
