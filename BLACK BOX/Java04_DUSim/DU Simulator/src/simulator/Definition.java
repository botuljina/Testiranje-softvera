package simulator;

import symtable.Scope;

// Klasa koja predstavlja definiciju
public class Definition {
	
	// Promenljiva koja se definiše, red i opseg u kom se nalazi definicija
	private String variable;
	private int rowNo;
	private Scope scope;
	
	public Definition(String variable, int rowNo, Scope scope) {
		super();
		this.variable = variable;
		this.rowNo = rowNo;
		this.scope = scope;
	}
	
	public String getVariable() {
		return variable;
	}
	
	public int getRowNo() {
		return rowNo;
	}
	
	public Scope getScope() {
		return scope;
	}
	
}
