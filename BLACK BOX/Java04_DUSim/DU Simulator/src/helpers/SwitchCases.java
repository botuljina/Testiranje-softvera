package helpers;

import java.util.ArrayList;

// Klasa koja pamti switch i njegove case-ove
// Pomocna klasa kod pronalazenja LCSAJ sekvenci
public class SwitchCases {

	private int switchId;
	private ArrayList<Integer> cases;
	
	public SwitchCases(int switchId) {
		super();
		this.switchId = switchId;
		cases = new ArrayList<>();
	}
	
	public int getSwitchId() {
		return switchId;
	}
	
	public void addCase(int caseId) {
		cases.add(caseId);
	}
	
	public boolean contains(int caseId) {
		return cases.contains(caseId);
	}
	
}
