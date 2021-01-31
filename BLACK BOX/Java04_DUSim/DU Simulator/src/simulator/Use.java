package simulator;

import symtable.Scope;

// Klasa koja predstavlja upotrebu promenljive
public class Use {
	
	// Promenljiva koja se upotrebljava, red i opseg u kome se nalazi, tip (c ili p)
	private String variable;
	private int rowNo;
	private char type;
	private Scope scope;
	
	// Ako je u pitanju p-upotreba, pamtimo na koji opseg se odnosi
	private int conditionFor;
	
	public Use(String variable, int rowNo, char type, Scope scope) {
		super();
		this.variable = variable;
		this.rowNo = rowNo;
		this.type = type;
		this.scope = scope;
		this.conditionFor = -1;
	}
	
	public int getConditionFor() {
		return conditionFor;
	}

	public void setConditionFor(int conditionFor) {
		this.conditionFor = conditionFor;
	}

	public String getVariable() {
		return variable;
	}
	
	public int getRowNo() {
		return rowNo;
	}
	
	public char getType() {
		return type;
	}
	
	public Scope getScope() {
		return scope;
	}
	
}
