package symtable;

import helpers.BreakStmt.BreakType;
import helpers.IfElseStmt;
import helpers.Jump;
import helpers.BreakStmt;
import helpers.SwitchCases;

import java.util.ArrayList;
import java.util.Collections;

import symtable.Scope.ScopeType;

// Klasa koja predstavlja tabelu simbola za uneti program
public class SymbolTable {
	
	// Lista opsega
	private ArrayList<Scope> scopes;
	// Lista switch struktura sa njihovim case-ovima
	private ArrayList<SwitchCases> switches;
	
	public SymbolTable() {
		scopes = new ArrayList<>();
		switches = new ArrayList<>();
	}
	
	public void addScope(Scope scope) {
		scopes.add(scope);
	}
	
	public Scope getScope(int i) {
		return scopes.get(i);
	}
	
	public int numberOfScopes() {
		return scopes.size();
	}
	
	// Provera da li dva case-a pripadaju istom switch-u
	public boolean areInSameSwitch(int case1, int case2) {
		for (int i = 0; i<switches.size(); i++) {
			SwitchCases sw = switches.get(i);
			if (sw.contains(case1) && sw.contains(case2))
				return true;
		}
		return false;
	}
	
	// Provera da li je zadati case prvi u okviru zadatog switch-a
	public boolean isFirstCase(int switchId, int caseId) {
		for (int i=0; i<scopes.size(); i++) {
			Scope s = scopes.get(i);
			if (s.getType() == ScopeType.CASE && s.getParentScope().getId() == switchId) {
				if (s.getId() < caseId) return false;
			}
		}
		return true;
	}
	
	// Pronalazi sve početne tačke sekvenci u programu 
	// (prva naredba + sve naredbe na koje može da se dođe sa neke druge naredbe osim prethodne)
	public ArrayList<Integer> findSeqStarts() {
		ArrayList<Integer> seqStarts = new ArrayList<>();
		for (int i=0; i<scopes.size(); i++) {
			Scope scope = scopes.get(i);
			
			// Ako je tekući opseg petlja (a nije do-while)
			// treba dodati prvu naredbu petlje i naredbu posle petlje
			if (scope.getType() == ScopeType.LOOP && !scope.isDoLoop()) {
				if (!seqStarts.contains(scope.getRowStart()))
					seqStarts.add(scope.getRowStart());
				if (!seqStarts.contains(scope.getRowEnd() + 1))
					seqStarts.add(scope.getRowEnd() + 1);
			}
			
			// Ako je tekući opseg if/else
			// treba dodati naredbu nakon opsega
			else if (scope.getType() == ScopeType.IF_ELSE) {
				if (!seqStarts.contains(scope.getRowEnd() + 1))
					seqStarts.add(scope.getRowEnd() + 1);
			}
			
			// Ako je tekuci opseg switch
			// treba dodati novi switch u listu
			else if (scope.getType() == ScopeType.SWITCH) {
				switches.add(new SwitchCases(scope.getId()));
			}
			
			// Ako je tekući opseg case (a nije prvi case u okviru switch-a)
			// treba dodati prvu naredbu opsega
			else if (scope.getType() == ScopeType.CASE) {
				// Dodajemo case u tekuci switch
				if (!switches.isEmpty())
					switches.get(switches.size()-1).addCase(scope.getRowStart());
				if (!isFirstCase(scope.getParentScope().getId(), scope.getId())) {
					if (!seqStarts.contains(scope.getRowStart()))
						seqStarts.add(scope.getRowStart());
				}
			}
		}
		
		// Ako već nismo dodali prvu naredbu, dodajemo je sad
		if (!seqStarts.contains(1))
			seqStarts.add(1);
		
		// Sortiramo sve početke sekvenci (zbog preglednijeg ispisa)
		Collections.sort(seqStarts);
		return seqStarts;
	}
	
	// Pronalazenje if-else strukture kojoj pripada if-opseg
	public IfElseStmt belongsToIfElse(ArrayList<IfElseStmt> ifElseStmts, int scope) {
		for (int i=0; i<ifElseStmts.size(); i++)
			if (ifElseStmts.get(i).getIfScope() == scope)
				return ifElseStmts.get(i);
		return null;
	}
	
	// Dohvata opseg na osnovu njegovog id-a
	public Scope getScopeWithId(int id) {
		for (int i=0; i<scopes.size(); i++)
			if (scopes.get(i).getId() == id)
				return scopes.get(i);
		return null;
	}
	
	// Pronalazi sve skokove u programu
	public ArrayList<Jump> findJumps(ArrayList<IfElseStmt> ifElseStmts, ArrayList<BreakStmt> breakStmts) {
		ArrayList<Jump> jumps = new ArrayList<>();
		for (int i=0; i<scopes.size(); i++) {
			Scope scope = scopes.get(i);
			
			// Ako je opseg petlja (a nije do-while)
			// dodajemo skok (početak petlje -> naredba posle petlje)
			// i skok (kralj petlje -> početak petlje) 
			if (scope.getType() == ScopeType.LOOP && !scope.isDoLoop()) {
				jumps.add(new Jump(scope.getRowStart(), scope.getRowEnd() + 1, false, 1));
				jumps.add(new Jump(scope.getRowEnd(), scope.getRowStart(), true, 2));
			}
			
			// Ako je opseg do-while petlja
			// dodajemo skok (kraj petlje -> početak petlje)
			if (scope.getType() == ScopeType.LOOP && scope.isDoLoop()) 
				jumps.add(new Jump(scope.getRowEnd(), scope.getRowStart(), false, 2));
			
			// Ako je opseg if
			// dodajemo skok (početak opsega -> naredba posle opsega)
			// i skok (kraj opsega -> naredba posle if-else strukture)
			if (scope.getType() == ScopeType.IF_ELSE && !scope.isElse()) {
				jumps.add(new Jump(scope.getRowStart(), scope.getRowEnd() + 1, false, 1));
				
				IfElseStmt ifElse = belongsToIfElse(ifElseStmts, scope.getId());
				if (ifElse != null && ifElse.getElseScope() != -1) {
					Scope s = getScopeWithId(ifElse.getElseScope());
					jumps.add(new Jump(scope.getRowEnd(), s.getRowEnd() + 1, true, 1));
				}
			}
			
			// Ako je opseg case (a nije default)
			// dodajemo skok (početak case-a -> naredba posle case-a)
			if (scope.getType() == ScopeType.CASE && !scope.isDefault())
				jumps.add(new Jump(scope.getRowStart(), scope.getRowEnd() + 1, false, 1));
		}
		
		// Ispitujemo sve break/continue naredbe u programu
		for (int i=0; i<breakStmts.size(); i++) {
			BreakStmt breakStmt = breakStmts.get(i);
			// Za svaki break u okviru switch-a
			// dodajemo skok (break -> naredba posle switch-a)
			if (breakStmt.getType() == BreakType.SWITCH_BREAK) {
				Scope s = getScopeWithId(breakStmt.getScopeId());
				jumps.add(new Jump(breakStmt.getRowNo(), s.getRowEnd() + 1, true, 3));
			}
			// Za svaki break u okviru petlje
			// dodajemo skok (break -> naredba posle petlje)
			else if (breakStmt.getType() == BreakType.LOOP_BREAK) {
				Scope s = getScopeWithId(breakStmt.getScopeId());
				jumps.add(new Jump(breakStmt.getRowNo(), s.getRowEnd() + 1, true, 3));
			}
			// Za svaki continue u okviru petlje
			// dodajemo skok (continue -> početak petlje)
			else if (breakStmt.getType() == BreakType.LOOP_CONT) {
				Scope s = getScopeWithId(breakStmt.getScopeId());
				jumps.add(new Jump(breakStmt.getRowNo(), s.getRowStart(), true, 3));
			}
		}
		
		return jumps;
	}

}
