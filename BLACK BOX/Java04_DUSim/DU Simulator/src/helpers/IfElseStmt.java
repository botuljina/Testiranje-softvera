package helpers;

// Klasa koja cuva informacije o if-else strukturi
// To nam treba da bismo znali koje naredbe su medjusobno iskljucive
public class IfElseStmt {
	
	private int ifScope;
	private int elseScope;
	private int parentIfElse;
	
	public IfElseStmt(int parentIfElse) {
		super();
		this.ifScope = -1;
		this.parentIfElse = parentIfElse;
		this.elseScope = -1;
	}

	public int getIfScope() {
		return ifScope;
	}

	public int getElseScope() {
		return elseScope;
	}

	public int getParentIfElse() {
		return parentIfElse;
	}

	public void setElseScope(int elseScope) {
		this.elseScope = elseScope;
	}

	public void setIfScope(int ifScope) {
		this.ifScope = ifScope;
	}
	
	public boolean areExclusive(int defScope, int useScope) {
		if (defScope == ifScope && useScope == elseScope)
			return true;
		if (defScope == elseScope && useScope == ifScope)
			return true;
		return false;
	}

}
