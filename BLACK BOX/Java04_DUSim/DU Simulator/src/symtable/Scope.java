package symtable;

import java.util.ArrayList;

// Klasa koja predstavlja jedan opseg u programu
public class Scope {
	
	// Mogući tipovi opsega u programu
	public static enum ScopeType { REGULAR, LOOP, IF_ELSE, SWITCH, CASE, FUNCTION, CLASS }
	
	// Opsezi se zbog lakšeg snalaženja identifikuju svojim id-jem
	public static int posId = 0;
	private int id;
	
	// Lista promenljivih koje su deklarisane u datom opsegu
	private ArrayList<String> vars;
	
	// Roditeljski opseg, nivo, tip
	private Scope parentScope;
	private int level;
	private ScopeType type;
	
	// Početna i krajnja linija opsega 
	private int rowStart, rowEnd;
	
	// Flagovi koji označavaju da li je opseg do-while/else/default, kao i da li se sastoji iz samo 1 naredbe
	private boolean doLoop;
	private boolean isElse;
	private boolean isDefault;
	private boolean oneStmtScope;
	
	public Scope(Scope parentScope, int level, ScopeType type) {
		id = posId++;
		this.parentScope = parentScope;
		vars = new ArrayList<>();
		this.level = level;
		this.type = type;
		doLoop = false;
		isElse = false;
		isDefault = false;
		oneStmtScope = false;
	}

	public int getId() {
		return id;
	}

	public Scope getParentScope() {
		return parentScope;
	}
	
	public int getLevel() {
		return level;
	}
	
	public ScopeType getType() {
		return type;
	}
	
	public int getRowStart() {
		return rowStart;
	}

	public int getRowEnd() {
		return rowEnd;
	}

	public void setRowStart(int rowStart) {
		this.rowStart = rowStart;
	}

	public void setRowEnd(int rowEnd) {
		this.rowEnd = rowEnd;
	}

	public boolean isDoLoop() {
		return doLoop;
	}

	public void setDoLoop(boolean doLoop) {
		this.doLoop = doLoop;
	}

	public boolean isElse() {
		return isElse;
	}

	public void setElse(boolean isElse) {
		this.isElse = isElse;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public boolean isOneStmtScope() {
		return oneStmtScope;
	}

	public void setOneStmtScope(boolean oneStmtScope) {
		this.oneStmtScope = oneStmtScope;
	}

	public void addVar(String var) {
		vars.add(var);
	}
	
	public boolean containsVar(String var) {
		return vars.contains(var);
	}
	
	public String getVars() {
		String s = "";
		for (int i=0; i<vars.size(); i++)
			s += vars.get(i) + " ";
		return s;
	}

}
