package simulator;

// Klasa koja predstavlja definition-use lanac
public class DUChain {
	
	// Promenljiva na koju se odnosi, red u kom je definicija i red u kom je upotreba
	private String variable;
	private int rowNoDef;
	private int rowNoUse;
	
	public DUChain(String variable, int rowNoDef, int rowNoUse) {
		super();
		this.variable = variable;
		this.rowNoDef = rowNoDef;
		this.rowNoUse = rowNoUse;
	}
	
	public String getVariable() {
		return variable;
	}
	
	public int getRowNoDef() {
		return rowNoDef;
	}
	
	public int getRowNoUse() {
		return rowNoUse;
	}
	
}
