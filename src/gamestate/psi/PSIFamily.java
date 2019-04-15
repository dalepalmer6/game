package gamestate.psi;

import java.util.ArrayList;

import gamestate.elements.psi.PSIAttack;

public class PSIFamily {
	private ArrayList<PSIAttack> psiAttacks;
	private String name;
	
	public PSIFamily(String name) {
		this.name = name;
		psiAttacks = new ArrayList<PSIAttack>();
	}
	
	public String getName() {
		return name;
	}
	
	public PSIAttack getStage(int i) {
		return psiAttacks.get(i);
	}
	
	public void addStage(PSIAttack a) {
		psiAttacks.add(a);
	}

	public ArrayList<PSIAttack> getStages() {
		// TODO Auto-generated method stub
		return psiAttacks;
	}
}
