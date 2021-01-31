package helpers;

import symtable.Scope;

// Klasa koja predstavlja jednu BREAK ili CONTINUE naredbu
public class BreakStmt {
	
	public enum BreakType { SWITCH_BREAK, LOOP_BREAK, LOOP_CONT }
	
	private int rowNo;
	// Id switch/loop bloka na kog se odnosi
	private int scopeId;
	private BreakType type;
	// Opseg u kom se nalazi
	private Scope belongsToScope;
	
	public BreakStmt(int rowNo, int scopeId, BreakType type, Scope belongsToScope) {
		super();
		this.rowNo = rowNo;
		this.scopeId = scopeId;
		this.type = type;
		this.belongsToScope = belongsToScope;
	}

	public int getRowNo() {
		return rowNo;
	}

	public int getScopeId() {
		return scopeId;
	}

	public BreakType getType() {
		return type;
	}

	public Scope getBelongsToScope() {
		return belongsToScope;
	}

}
