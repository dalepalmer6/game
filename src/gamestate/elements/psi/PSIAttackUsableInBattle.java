package gamestate.elements.psi;

import gamestate.BattleEntity;
import gamestate.elements.UsableInBattle;

public class PSIAttackUsableInBattle extends PSIAttack implements UsableInBattle {

	public PSIAttackUsableInBattle(int id, String name, String desc, int type, int action, String anim,String classification,String family,String stage, int ppused) {
		super(id, name, desc, type, action, anim,classification,family,stage,ppused);
		// TODO Auto-generated constructor stub
	}

	
}
