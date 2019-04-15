package gamestate.psi;

import java.util.ArrayList;

import gamestate.elements.psi.PSIAttack;

public class PSIClassificationList {
	private ArrayList<PSIClassification> psiList;
	
	public PSIClassificationList() {
		psiList = new ArrayList<PSIClassification>();
	}
	
	public void addClassification(PSIClassification pc) {
		psiList.add(pc);
	}
	
	public ArrayList<PSIClassification> getPSIClassifications() {
		return psiList;
	}
	
	public int size() {
		return psiList.size();
	}
}
