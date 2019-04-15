package gamestate.psi;

import java.util.ArrayList;

public class PSIClassification {
	private String name;
	private ArrayList<PSIFamily> families;
	
	public PSIClassification(String name) {
		this.name=name;
		families = new ArrayList<PSIFamily>();
	}
	
	public ArrayList<PSIFamily> getFamilies() {
		return families;
	}
	
	public String getName() {
		return name;
	}
	
	public void addFamily(PSIFamily f) {
		families.add(f);
	}
	
}
