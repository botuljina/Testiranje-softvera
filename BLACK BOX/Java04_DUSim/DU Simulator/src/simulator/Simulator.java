package simulator;

import helpers.BreakStmt;
import helpers.BreakStmt.BreakType;
import helpers.IfElseStmt;
import helpers.Jump;
import java.util.ArrayList;
import javax.swing.JTextArea;
import symtable.Scope;
import symtable.SymbolTable;
import symtable.Scope.ScopeType;

// Klasa koja radi obradu unetog koda
public class Simulator {
	
	// Flag koji oznacava da li se nalazimo u step-by-step modu rada
	boolean stepByStep = false;
	// Tekuci red u step-by-step modu rada
	int currentRow = 1;
	
	// Liste u kojima se pamte definicije, upotrebe, DU-lanci i LCSAJ sekvence
	public ArrayList<Definition> definitions;
	public ArrayList<Use> uses;
	public ArrayList<DUChain> duChains;
	public ArrayList<Lcsaj> lcsaj;
	
	// Objekat za izdvajanje tokena u izvornom kodu
	private Lexer lexer;
	
	// Objekat u kom se pamte promenljive koje treba ukljuciti u simulaciju
	public VarsToInclude varsToInclude;
	
	// Lista if-else struktura
	private ArrayList<IfElseStmt> ifElseStmts;
	
	// Lista u kojoj se pamte break i continue naredbe
	private ArrayList<BreakStmt> breakStmts;
	
	// Tabela simbola
	private SymbolTable symTable;
	
	public Simulator() {
		lexer = new Lexer();
		varsToInclude = new VarsToInclude();
		definitions = new ArrayList<>();
		uses = new ArrayList<>();
		duChains = new ArrayList<>();
		lcsaj = new ArrayList<>();
		symTable = new SymbolTable();
		ifElseStmts = new ArrayList<>();
		breakStmts = new ArrayList<>();
		Scope.posId = 0;
	}
	
	// Analiza koda koja se pokrece klikom na dugme "Pokreni"
	// Pretpostavka je da je uneti kod sintaksno i semanticki ispravan
	public void analyseCode(String code, String vars) {
		
		// Izdvajanje tokena i promenljivih koje treba ukljuciti
		lexer.scanCode(code);
		varsToInclude.scanVars(vars);
		
		// Pokrece trazenje definicija i upotreba
		findDefsUses();
		
		// Pokrece trazenje DU-lanaca
		findDuChains();
		
		// Pokrece trazenje LCSAJ sekvenci
		findLcsaj();

	}
	
	public void findDefsUses() {
		// Tekuci i prethodni token u analizi
		Token token = null;
		Token previousToken = null;

		// Flag koji oznacava da li se nalazimo na pocetku naredbe
		boolean newStatement = true;

		// Flagovi koji oznacavaju da li naredne promenljive u naredbi predstavljaju definiciju ili upotrebu
		boolean definition = false;
		boolean use = false;

		// Flag koji oznacava da li se nalazimo u naredbi koja predstavlja kontrolnu rec (if, while, for...)
		boolean controlWord = false;
		
		// Flag koji oznacava da li se nalazimo u naredbi koja predstavlja definiciju konstante
		boolean constant = false;

		// Brojac zagrada koji se koristi kod obrade uslova kontrolne reci
		int brackets = 0;
		
		// Tekuci nivo na kome se nalazi program
		int level = 0;

		// Pomocna promenljiva koja se koristi kad imamo pristup poljima objekta
		String possibleDefUse = "";
		
		// Lista u kojoj se pamte formalni argumenti funkcije
		ArrayList<String> funcArgs = new ArrayList<>();
		
		// Tekuca if-else struktura
		int currentIfElse = -1;
		
		// Otvaranje "universe" opsega
		ScopeType currentType = ScopeType.REGULAR;
		Scope currentScope = new Scope(null, level, currentType);
		currentScope.setRowStart(1);
		Scope previousScope = null;
		symTable.addScope(currentScope);
		
		// Flag koji oznacava da li je tekuci opseg do-while petlja i tekuca do-while petlja
		boolean doLoop = false;
		int currentDoWhile = -1;
		
		// Jos neki pomocni flagovi...
		boolean isElse = false;
		boolean functionCall = false;
		boolean isCondition = false;
		boolean isDefault = false;

		// Citamo sve tokene i obradjujemo ih redom
		while ((token = lexer.nextToken()) != null) {
			String word = token.getToken();

			// Ako se nalazimo na pocetku naredbe
			if (newStatement) {
				// Ako naredba pocinje modifikatorom pristupa, literalom ili tipom, potencijalno se radi o definiciji
				if (lexer.isAccessModifier(word) || lexer.isLiteral(word) || (lexer.isType(word))) {
					definition = true;
					use = false;
				}

				// Ako naredba pocinje promenljivom, sigurno se radi o definiciji
				else if (lexer.isVariable(word) && lexer.peakNextToken() != null && !lexer.peakNextToken().getToken().equals("(")) {
					definition = true;
					use = false;
				}

				// Ako naredba pocinje funkcijom, potencijalno ce doci do upotrebe argumenata
				else if (lexer.isVariable(word) && lexer.peakNextToken() != null && lexer.peakNextToken().getToken().equals("(")) {
					definition = false;
					use = true;
				}

				// Ako naredba pocinje kontrolnom recju, sigurno ce doci do upotrebe promenljivih iz uslova 
				else if (word.equalsIgnoreCase("for") || word.equalsIgnoreCase("if") || word.equalsIgnoreCase("switch") || word.equalsIgnoreCase("while")) {
					definition = false;
					use = true;
					controlWord = true;
					// Ako se radi o for ili while petlji
					if (word.equalsIgnoreCase("for") || word.equalsIgnoreCase("while"))
						currentType = ScopeType.LOOP;
					// Ako se radi o if naredbi
					if (word.equalsIgnoreCase("if")) {
						currentType = ScopeType.IF_ELSE;
						// Otvaranje nove if-else strukture
						IfElseStmt ifElse = new IfElseStmt(currentIfElse);
						ifElseStmts.add(ifElse);
						currentIfElse = ifElseStmts.size() - 1;
					}
					// Ako se radi o switch naredbi
					if (word.equalsIgnoreCase("switch"))
						currentType = ScopeType.SWITCH;
				}
				
				// Ako se radi o do-while petlji
				else if (word.equalsIgnoreCase("do")) {
					currentType = ScopeType.LOOP;
					doLoop = true;
					// Ako se radi o opsegu koji se sastoji iz samo jedne naredbe odmah ga otvaramo
					if (lexer.peakNextToken() != null && !lexer.peakNextToken().getToken().equals("{")) {
						level++;
						previousScope = currentScope;
						currentScope = new Scope(previousScope, level, currentType);
						currentScope.setRowStart(token.getRowNo());
						
						currentScope.setDoLoop(true);
						doLoop = false;
						
						currentType = ScopeType.REGULAR;
						currentScope.setOneStmtScope(true);
						symTable.addScope(currentScope);
						
						previousToken = token;
						continue;
					}
				}

				// Ako se radi o else naredbi
				else if (word.equalsIgnoreCase("else")) {
					currentType = ScopeType.IF_ELSE;
					isElse = true;
					// Ako se radi o opsegu koji se sastoji iz samo jedne naredbe odmah ga otvaramo
					if (lexer.peakNextToken() != null && !lexer.peakNextToken().getToken().equals("{")) {
						level++;
						previousScope = currentScope;
						currentScope = new Scope(previousScope, level, currentType);
						currentScope.setRowStart(token.getRowNo());
						
						if (isElse) {
							currentScope.setElse(true);
							isElse = false;
						}
						
						if (currentScope.getType() == ScopeType.IF_ELSE) {
							ifElseStmts.get(currentIfElse).setElseScope(currentScope.getId());
						}
						
						currentType = ScopeType.REGULAR;
						currentScope.setOneStmtScope(true);
						symTable.addScope(currentScope);
					}
					previousToken = token;
					continue;
				}
				
				// Ako se radi o case ili default
				else if (word.equalsIgnoreCase("case") || word.equalsIgnoreCase("default")) {
					currentType = ScopeType.CASE;
					if (word.equalsIgnoreCase("default"))
						isDefault = true;
				}
				
				// Ako se radi o break naredbi
				else if (word.equalsIgnoreCase("break")) {
					addBreakStmt(currentScope, token.getRowNo());
				}
				
				// Ako se radi o continue naredbi
				else if (word.equalsIgnoreCase("continue")) {
					addContinueStmt(currentScope, token.getRowNo());
				}

				// Ako naredba pocinje operatorom ++ ili --, sigurno se radi o upotrebi i definiciji naredne promenljive
				else if (word.equals("++") || word.equals("--")) {
					previousToken = token;
					token = lexer.nextToken();
					word = token.getToken();
					possibleDefUse = "";
					// Za slucaj da se pristupa poljima objekta, potrebno je nadovezati sva ta polja u pomocnoj promenljivoj
					while (lexer.peakNextToken() != null && !lexer.peakNextToken().getToken().equals(";")) {
						possibleDefUse += token.getToken();
						previousToken = token;
						token = lexer.nextToken();
						word = token.getToken();
					}
					possibleDefUse += token.getToken();
					Token t = new Token(possibleDefUse, token.getRowNo());
					// Dodajemo definiciju i c-upotrebu
					addDefinition(t, currentScope);
					addCUse(t, currentScope);
					possibleDefUse = "";
				}

				// Ako se radi o return naredbi, potencijalno se radi o upotrebi naredne promenljive
				else if (word.equalsIgnoreCase("return")) {
					definition = false;
					use = true;
				}
			} 

			// Ako u naredbi imamo potencijalne definicije
			if (definition) {

				// Ako je tekuci token promenljiva
				if (lexer.isVariable(word)) {
					
					// Ako se radi o definiciji konstante
					if (constant) {
						lexer.addConstant(token.getToken());
						constant = false;
						definition = false;
						use = false;
						newStatement = false;
						previousToken = token;
						continue;
					}
					
					// Ako je prethodni token tip ili ","
					if ((previousToken != null && (lexer.isType(previousToken.getToken()) || previousToken.getToken().equals(",")))) {
						// Ako se radi o deklaraciji funkcije
						// dodajemo sve argumente u pomocnu listu
						if (lexer.peakNextToken() != null && lexer.peakNextToken().getToken().equals("(")) {
							while (!token.getToken().equals(")")) {
								previousToken = token;
								token = lexer.nextToken();
								word = token.getToken();
								if (lexer.isVariable(word))
									funcArgs.add(token.getToken());
							}
							currentType = ScopeType.FUNCTION;
							definition = false;
							use = false;
							newStatement = false;
							previousToken = token;
							continue;
						}
						// Ako se radi o deklaraciji promenljive
						else {
							// Ako se nalazimo u uslovu for petlje, bice potrebno dodati deklarisanu promenljivu u opseg for petlje
							// U suprotnom pamtimo deklaraciju u tekucem opsegu
							if (isCondition)
								funcArgs.add(token.getToken());
							else 
								currentScope.addVar(token.getToken());
						}
					}	
					
					// Citamo sledeci token i na osnovu njega donosimo odluku
					previousToken = token;
					token = lexer.nextToken();
					word = token.getToken();

					// Ako nakon promenljive sledi znak = (a posle = nije new) 
					// dodajemo definiciju prethodno procitane promenljive
					// Sve promenljive nakon znaka dodele predstavljaju upotrebu
					if (word.equals("=") && lexer.peakNextToken() != null && !lexer.peakNextToken().getToken().equalsIgnoreCase("new")) {
						possibleDefUse += previousToken.getToken();
						Token t = new Token(possibleDefUse, previousToken.getRowNo());
						addDefinition(t, currentScope);
						possibleDefUse = "";
						definition = false;
						use = true;
					}
					
					// Ako nakon promenljive sledi znak = (a posle = je new)
					// ne radi se o definiciji, ali moze da sledi upotreba
					else if (word.equals("=") && lexer.peakNextToken() != null && lexer.peakNextToken().getToken().equalsIgnoreCase("new")) {
						definition = false;
						use = true;
					}

					// Ako nakon promenljive sledi ., znaci da se prisupa poljima/metodama objekta
					else if (word.equals(".")) {
						possibleDefUse += previousToken.getToken() + ".";
					}

					// Ako slede naredni znaci dodele 
					// potrebno je dodati i definiciju i c-upotrebu prethodne promenljive.
					// Sve promenljive nakon znaka dodele predstavljaju upotrebu
					else if (word.equals("+=") || word.equals("-=") || word.equals("*=") || word.equals("/=") ||
							word.equals("<<=") || word.equals(">>=") || word.equals(">>>=") || word.equals("&=") ||
							word.equals("^=") || word.equals("|=")) {
						possibleDefUse += previousToken.getToken();
						Token t = new Token(possibleDefUse, previousToken.getRowNo());
						addDefinition(t, currentScope);
						addCUse(t, currentScope);
						possibleDefUse = "";
						definition = false;
						use = true;
					}

					// Ako se radi o postfiksnim operatorima ++ i --
					// potrebno je dodati i definiciju i upotrebu prethodne promenljive.
					else if (word.equals("++") || word.equals("--")) {
						possibleDefUse += previousToken.getToken();
						Token t = new Token(possibleDefUse, previousToken.getRowNo());
						addDefinition(t, currentScope);
						addCUse(t, currentScope);
						possibleDefUse = "";
						definition = false;
						use = false;
					}

					// Ako se radi o pristupu elementu niza
					// potrebno je dodati definiciju prethodne promenljive.
					// Sve promenljive unutar [ i ] predstavljaju upotrebu
					else if (word.equals("[")) {
						possibleDefUse += previousToken.getToken();
						Token t = new Token(possibleDefUse, previousToken.getRowNo());
						addDefinition(t, currentScope);
						possibleDefUse = "";
						definition = false;
						use = true;
					}

					// Ako se radi o pozivu metode
					// imamo potencijalne upotrebe u vidu stvarnih argumenata
					else if (word.equals("(")) {
						possibleDefUse = "";
						definition = false; 
						use = true;
					}

					// Ako se udje u ovu granu znaci da u naredbi nemamo ni definiciju, ni upotrebu
					else if (!word.equals(",")){
						definition = false;
						use = false;
					}
				}
				
				// Ako se radi o konstruktoru
				// pamtimo njegove formalne parametre
				else if (lexer.isType(word) && lexer.peakNextToken() != null && lexer.peakNextToken().getToken().equals("(")) {
					while (!token.getToken().equals(")")) {
						previousToken = token;
						token = lexer.nextToken();
						word = token.getToken();
						if (lexer.isVariable(word))
							funcArgs.add(token.getToken());
					}
					currentType = ScopeType.FUNCTION;
					definition = false;
					use = false;
					newStatement = false;
					previousToken = token;
					continue;
				}
				
			}

			// Ako u naredbi imamo potencijalne upotrebe
			else if (use) {

				// Ako se radi o kontrolnoj reci i ulazimo u zagradu sa uslovom, povecavamo broj otvorenih zagrada
				if (word.equals("(") && controlWord) {
					brackets++;
					// Ako posle zagrade sledi tip
					// znaci da imamo definiciju promenljive u okviru for petlje
					if (lexer.peakNextToken() != null && lexer.isType(lexer.peakNextToken().getToken())) {
						definition = true;
						use = false;
						isCondition = true;
					}
				}

				// Ako se radi o kontrolnoj reci i dolazimo do zatvorene zagrade, smanjujemo broj zatvorenih zagrada
				else if (word.equals(")") && controlWord) {
					brackets--;
					
					// Ako su sve zagrade zatvorene znaci da je uslov zavrsen
					if (brackets == 0) {
						newStatement = true;
						definition = false;
						use = false;
						controlWord = false;
						currentDoWhile = -1;
						
						// Ako se radi o opsegu koji se sastoji iz samo jedne naredbe odmah ga otvaramo
						if (lexer.peakNextToken() != null && !lexer.peakNextToken().getToken().equals("{") && !lexer.peakNextToken().getToken().equals(";")) {
							level++;
							previousScope = currentScope;
							currentScope = new Scope(previousScope, level, currentType);
							currentScope.setRowStart(token.getRowNo());
							
							if (currentScope.getType() == ScopeType.IF_ELSE) {
								ifElseStmts.get(currentIfElse).setIfScope(currentScope.getId());
							}
							
							currentType = ScopeType.REGULAR;
							currentScope.setOneStmtScope(true);
							symTable.addScope(currentScope);
						}

						previousToken = token;
						continue;
					}
				}
				
				// Ako se radi o zatvorenoj zagradi a u toku je poziv funkcije
				else if (word.equals(")") && !controlWord && functionCall) {
					functionCall = false;
				}

				// Ako se radi o operatorima ++ i --, dodajemo i definiciju i upotrebu odgovarajuce promenljive
				else if (word.equals("++") || word.equals("--")) {
					// Postfiksni ++ i --
					if (lexer.isVariable(previousToken.getToken())) {
						Token t = new Token (possibleDefUse, previousToken.getRowNo());
						addDefinition(t, currentScope);
						addCUse(t, currentScope);
						possibleDefUse = "";
					}
					// Prefiksni ++ i --
					else {
						previousToken = token;
						token = lexer.nextToken();
						word = token.getToken();
						while (lexer.isVariable(word) || word.equals(".")) {
							possibleDefUse += word;
							previousToken = token;
							token = lexer.nextToken();
							word = token.getToken();
						}
						Token t = new Token (possibleDefUse, previousToken.getRowNo());
						addDefinition(t, currentScope);
						addCUse(t, currentScope);
						addCUse(t, currentScope);
						possibleDefUse = "";
					}
				}
				
				// Ako se radi o zarezu, a ne nalazimo se u pozivu funkcije
				// potencijalno sledi definicija
				else if (word.equals(",") && !functionCall) {
					definition = true;
					use = false;
				}

				// Ako je tekuca rec promenljiva dodajemo odgovarajucu c ili p upotrebu
				if (lexer.isVariable(word) && lexer.peakNextToken() != null && !lexer.peakNextToken().getToken().equals("(")) {
					if (lexer.peakNextToken().getToken().equals(".")) {
						possibleDefUse += word + ".";
					}
					else {
						possibleDefUse += word;
						Token t = new Token(possibleDefUse, token.getRowNo());
						// Ako se radi o kontrolnoj reci
						if (controlWord) {
							// Ako nije u pitanju do-while, uslov se odnosi na naredni opseg koji tek treba da se otvori
							if (currentDoWhile == -1)
								addPUse(t, currentScope, Scope.posId);
							// Ako je u pitanju do-while, vec znamo na koji opseg se uslov odnosi
							else {
								addPUse(t, currentScope, currentDoWhile);
								Scope s = symTable.getScopeWithId(currentDoWhile);
								s.setRowEnd(t.getRowNo());
							}
						}
						else {
							addCUse(t, currentScope);
						}
						// Ako sledi znak jednakosti potrebno je da dodamo i definiciju tekuce promenljive
						Token next = lexer.peakNextToken();
						if (next != null && (next.getToken().equals("+=") || next.getToken().equals("-=") || next.getToken().equals("*=") || 
								next.getToken().equals("/=") || next.getToken().equals("<<=") || next.getToken().equals(">>=") || next.getToken().equals(">>>=") || 
								next.getToken().equals("&=") || next.getToken().equals("^=") || next.getToken().equals("|=") || next.getToken().equals("="))) 
							addDefinition(t, currentScope);
						// Ako je sledeci token ++ ili --, ne treba da resetujemo pomocnu promenljivu
						if (next != null && !(next.getToken().equals("++") || next.getToken().equals("--")))
							possibleDefUse = "";
					}
				}
				
				// Ako se radi o pozivu funkcije
				else if (lexer.isVariable(word) && lexer.peakNextToken() != null && lexer.peakNextToken().getToken().equals("(")) {
					possibleDefUse = "";
					functionCall = true;
				}
			}

			// Ako se radi o definiciji klase ili nabrajanja, potrebno je da dodamo novi tip u korisnicke tipove
			if (word.equalsIgnoreCase("class") || word.equalsIgnoreCase("enum")) {
				if (word.equalsIgnoreCase("class"))
					currentType = ScopeType.CLASS;
				previousToken = token;
				token = lexer.nextToken();
				word = token.getToken();
				lexer.addUserType(word);
			}
			
			// Ako se radi o definiciji konstante
			if (word.equalsIgnoreCase("final")) {
				constant = true;
			}

			newStatement = false;
			previousToken = token;
			if (word.equals(";") || word.equals("{") || word.equals("}") || word.equals(":")) {
				
				// Ako se radi o pocetku bloka, otvaramo novi opseg
				if (word.equals("{")) {
					level++;
					previousScope = currentScope;
					currentScope = new Scope(previousScope, level, currentType);
					currentScope.setRowStart(token.getRowNo());
					
					if (doLoop) {
						currentScope.setDoLoop(true);
						doLoop = false;
					}
					else if (isElse) {
						currentScope.setElse(true);
						isElse = false;
					}
					
					symTable.addScope(currentScope);
					
					if (currentScope.getType() == ScopeType.IF_ELSE) {
						if (currentScope.isElse()) {
							ifElseStmts.get(currentIfElse).setElseScope(currentScope.getId());
						}
						else {
							ifElseStmts.get(currentIfElse).setIfScope(currentScope.getId());
						}
					}
					
					currentType = ScopeType.REGULAR;
					
					// Ako se radi o funkciji, dodajemo njene formalne argumente u tekuci opseg
					if (!funcArgs.isEmpty()) {
						for (int i=0; i<funcArgs.size(); i++)
							currentScope.addVar(funcArgs.get(i));
						funcArgs = new ArrayList<>();
					}
				}
				
				// Ako se radi o kraju bloka, zatvaramo tekuci opseg
				if (word.equals("}")) {
					level--;
					currentScope.setRowEnd(token.getRowNo());
					
					// Ako smo dosli do kraja if-else strukture, prelazimo na obuhvatajucu if-else strukturu
					if (currentScope.isElse()) {
						currentIfElse = ifElseStmts.get(currentIfElse).getParentIfElse();
					}
					
					// Ako je tekuca petlja do-while, pamtimo njen id
					if (currentScope.getType() == ScopeType.LOOP && currentScope.isDoLoop()) {
						currentDoWhile = currentScope.getId();
					}
					
					boolean ifClosed = false;
					if (currentScope.getType() == ScopeType.IF_ELSE && !currentScope.isElse())
						ifClosed = true;
					
					currentScope = previousScope;
					previousScope = currentScope.getParentScope();
					
					// Ako imamo vise ugnjezdenih opsega koji se sastoje iz jedne naredbe
					// potrebno je da ih sve zatvorimo
					while (currentScope.isOneStmtScope()) {
						// Tekuci opseg jos uvek ne treba zatvoriti ako se radi o do-while petlji ili ako sledi else
						if (ifClosed && lexer.peakNextToken() != null && lexer.peakNextToken().getToken().equals("else"))
							break;
						if (currentScope.getType() == ScopeType.LOOP && currentScope.isDoLoop())
							break;
						
						level--;
						currentScope.setRowEnd(token.getRowNo());
					
						if (currentScope.isElse()) {
							currentIfElse = ifElseStmts.get(currentIfElse).getParentIfElse();
						}
						
						if (currentScope.getType() == ScopeType.LOOP && currentScope.isDoLoop()) {
							currentDoWhile = currentScope.getId();
						}
						
						if (currentScope.getType() == ScopeType.IF_ELSE && !currentScope.isElse())
							ifClosed = true;
						
						currentScope = previousScope;
						previousScope = currentScope.getParentScope();
					}
				}
				
				// Ako smo dosli do kraja naredbe u okviru uslova for-petlje
				// vise nemamo definiciju, ali zato mogu da slede potencijalne upotrebe
				if (word.equals(";") && isCondition) {
					definition = false;
					use = true;
					isCondition = false;
					continue;
				}
				
				// Ako procitamo ":", otvaramo case opseg
				if (word.equals(":") && lexer.peakNextToken() != null && !lexer.peakNextToken().getToken().equalsIgnoreCase("case") && !lexer.peakNextToken().getToken().equalsIgnoreCase("default")) {
					level++;
					previousScope = currentScope;
					currentScope = new Scope(previousScope, level, currentType);
					currentScope.setRowStart(token.getRowNo());
					
					if (isDefault) {
						currentScope.setDefault(true);
						isDefault = false;
					}
					
					symTable.addScope(currentScope);
					currentType = ScopeType.REGULAR;
				}
				
				// Ako smo dosli do kraja naredbe, a tekuci opseg se sastoji samo iz jedne naredbe
				// potrebno je da zatvorimo tekuci opseg
				if (currentScope.isOneStmtScope() && word.equals(";")) {
					boolean ifClosed = false;
					while (currentScope.isOneStmtScope()) {
						level--;
						currentScope.setRowEnd(token.getRowNo());
						
						if (currentScope.isElse()) {
							currentIfElse = ifElseStmts.get(currentIfElse).getParentIfElse();
						}
						
						if (currentScope.getType() == ScopeType.LOOP && currentScope.isDoLoop()) {
							currentDoWhile = currentScope.getId();
						}
						
						if (currentScope.getType() == ScopeType.IF_ELSE && !currentScope.isElse())
							ifClosed = true;
						
						currentScope = previousScope;
						previousScope = currentScope.getParentScope();
						
						// Tekuci opseg jos uvek ne treba zatvoriti ako se radi o do-while petlji ili ako sledi else
						if (ifClosed && lexer.peakNextToken() != null && lexer.peakNextToken().getToken().equals("else"))
							break;
						if (currentScope.getType() == ScopeType.LOOP && currentScope.isDoLoop())
							break;
					}
				}
				
				// Ako smo dosli do kraja naredbe u okviru case bloka, a sledi case, default ili }
				// treba zatvoriti case blok
				if (currentScope.getType() == ScopeType.CASE && word.equals(";")) {
					if (lexer.peakNextToken() != null && (lexer.peakNextToken().getToken().equalsIgnoreCase("case") || lexer.peakNextToken().getToken().equalsIgnoreCase("default") || lexer.peakNextToken().getToken().equals("}"))) {
						level--;
						currentScope.setRowEnd(token.getRowNo());
						currentScope = previousScope;
						previousScope = currentScope.getParentScope();
					}
				}
				// Pocinjemo novu naredbu
				newStatement = true;
				definition = false;
				use = false;
			}
		}
		
		// Postavljanje kraja "universe" opsega
		if (previousToken != null)
			currentScope.setRowEnd(previousToken.getRowNo());
		else
			currentScope.setRowEnd(1);
	}
	
	// Dodavanje break naredbe
	public void addBreakStmt(Scope scope, int rowNo) {
		Scope currentScope = scope;
		while (currentScope != null) {
			if (currentScope.getType() == ScopeType.SWITCH) {
				breakStmts.add(new BreakStmt(rowNo, currentScope.getId(), BreakType.SWITCH_BREAK, scope));
				return;
			}
			else if (currentScope.getType() == ScopeType.LOOP) {
				breakStmts.add(new BreakStmt(rowNo, currentScope.getId(), BreakType.LOOP_BREAK, scope));
				return;
			}
			currentScope = currentScope.getParentScope();
		}
	}
	
	// Dodavanje continue naredbe
	public void addContinueStmt(Scope scope, int rowNo) {
		Scope currentScope = scope;
		while (currentScope != null) {
			if (currentScope.getType() == ScopeType.LOOP) {
				breakStmts.add(new BreakStmt(rowNo, currentScope.getId(), BreakType.LOOP_CONT, scope));
				return;
			}
			currentScope = currentScope.getParentScope();
		}
	}
	
	// Dodavanje nove definicije
	public void addDefinition(Token token, Scope scope) {
		if (varsToInclude.includeAll() || varsToInclude.isIncluded(token.getToken()))
			definitions.add(new Definition(token.getToken(), token.getRowNo(), scope));
	}
	
	// Dodavanje nove c-upotrebe
	public void addCUse(Token token, Scope scope) {
		if (varsToInclude.includeAll() || varsToInclude.isIncluded(token.getToken())) 
			uses.add(new Use(token.getToken(), token.getRowNo(), 'c', scope));
	}
	
	// Dodavanje nove p-upotrebe
	public void addPUse(Token token, Scope scope, int conditionFor) {
		if (varsToInclude.includeAll() || varsToInclude.isIncluded(token.getToken())) {
			Use u = new Use(token.getToken(), token.getRowNo(), 'p', scope);
			u.setConditionFor(conditionFor);
			uses.add(u);
		}
	}
	
	// Dodavanje novog DU-lanca
	public void addDuChain(String var, int def, int use) {
		if (varsToInclude.includeAll() || varsToInclude.isIncluded(var))
			duChains.add(new DUChain(var, def, use));
	}
	
	// Provera da li postoji nova definicija izmedju definicije i njene upotrebe
	// Potrebno je proveriti da li se definicija odnosi na istu promenljivu i da li je obavezna
	public boolean defExistsBetween(String var, int row1, int row2, Scope scopeDef, Scope scopeUse) {
		boolean exists = false;
		int scopeDefId = getScopeWhereVarIsDef(scopeDef, var);
		for (int i=0; i<definitions.size(); i++) {
			Definition def = definitions.get(i);
			if (def.getVariable().equals(var) && def.getRowNo() > row1 && def.getRowNo() < row2) {
				int scope = getScopeWhereVarIsDef(def.getScope(), def.getVariable());
				if (scope == scopeDefId && isMandatory(def.getScope(), scopeDef, scopeUse)) {
					exists = true;
					break;
				}
			}
		}
		return exists;
	}
	
	// Provera da li je definicija obavezna
	public boolean isMandatory(Scope scope, Scope scopeDef, Scope scopeUse) {
		// Ako se nalazi u istom opsegu sa zadatom definicijom ili upotrebom sigurno je obavezna
		if (scope.getId() == scopeDef.getId() || scope.getId() == scopeUse.getId())
			return true;
		
		// Definicija nije obavezna ako se nalazi u okviru if/else/petlje/switch-a,
		// u kome se ne nalaze zadata definicija i upotreba
		Scope currentScope = scope;
		while (currentScope != null) {
			ScopeType type = currentScope.getType();
			if ((type == ScopeType.IF_ELSE || type == ScopeType.LOOP || type == ScopeType.SWITCH) &&
					!isInScope(scopeDef, currentScope.getId()) && !isInScope(scopeUse, currentScope.getId()))
				return false;
			currentScope = currentScope.getParentScope();
		}
		
		return true;
	}
	
	// Provera da li se opseg nalazi u okviru drugog zadatog opsega
	public boolean isInScope(Scope scope, int scopeToFind) {
		Scope currentScope = scope;
		while(currentScope != null) {
			if (currentScope.getId() == scopeToFind)
				return true;
			currentScope = currentScope.getParentScope();
		}
		return false;
	}
	
	// Dohvatanje opsega gde je promenljiva deklarisana
	public int getScopeWhereVarIsDef(Scope scope, String var) {
		// Ako se radi o polju klase
		if (var.contains(".")) {
			boolean hasThis = lexer.hasThis(var);
			String topObj = lexer.getTopObject(var);
			if (hasThis) {
				Scope currentScope = scope;
				while (currentScope != null && currentScope.getType() != ScopeType.CLASS) {
					currentScope = currentScope.getParentScope();
				}
				if (currentScope == null) return 0;
				return currentScope.getId();
			}
			else {
				Scope currentScope = scope;
				while (currentScope != null && !currentScope.containsVar(topObj)) {
					currentScope = currentScope.getParentScope();
				}
				if (currentScope == null) return 0;
				return currentScope.getId();
			}
		}
		// Ako se radi o obicnoj promenljivoj
		else {
			Scope currentScope = scope;
			while (currentScope != null && !currentScope.containsVar(var)) {
				currentScope = currentScope.getParentScope();
			}
			if (currentScope == null) return 0;
			return currentScope.getId();
		}
	}
	
	// Dohvatanje klase u kojoj se opseg nalazi
	public int getClassScope(Scope scope) {
		Scope currentScope = scope;
		while (currentScope != null && currentScope.getType() != ScopeType.CLASS) {
			currentScope = currentScope.getParentScope();
		}
		if (currentScope == null) return 0;
		return currentScope.getId();
	}
	
	// Dohvatanje funkcije u kojoj se opseg nalazi
	public int getFunctionScope(Scope scope) {
		Scope currentScope = scope;
		while (currentScope != null && currentScope.getType() != ScopeType.FUNCTION) {
			currentScope = currentScope.getParentScope();
		}
		if (currentScope == null) return 0;
		return currentScope.getId();
	}
	
	// Dohvatanje opsega na osnovu uslova za taj opseg
	public Scope getLoopScopeCond(Scope scope, int condFor) {
		Scope currentScope = scope;
		while (currentScope != null) {
			if (currentScope.getType() == ScopeType.LOOP && currentScope.getId() == condFor)
				return currentScope;
			currentScope = currentScope.getParentScope();
		}
		return null;
	}
	
	// Dohvatanje zajednicke petlje u kojoj se nalaze i definicija i upotreba
	public Scope getScopeWithLoop(Scope defScope, Scope useScope) {
		Scope currentDef = defScope;
		Scope currentUse = useScope;
		
		while(currentDef.getLevel() > currentUse.getLevel())
			currentDef = currentDef.getParentScope();
		while(currentDef.getLevel() < currentUse.getLevel())
			currentUse = currentUse.getParentScope();
		
		while (currentDef != null && currentUse != null) {
			if (currentDef.getId() == currentUse.getId() && currentDef.getType() == ScopeType.LOOP)
				return currentDef;
			currentDef = currentDef.getParentScope();
			currentUse = currentUse.getParentScope();
		}
		return null;
	}
	
	// Dohvatanje zajednickog switch-a u kom se nalaze i definicija i upotreba
	public Scope getScopeWithSwitch(Scope defScope, Scope useScope) {
		Scope currentDef = defScope;
		Scope currentUse = useScope;
		
		while(currentDef.getLevel() > currentUse.getLevel())
			currentDef = currentDef.getParentScope();
		while(currentDef.getLevel() < currentUse.getLevel())
			currentUse = currentUse.getParentScope();
		
		while (currentDef != null && currentUse != null) {
			if (currentDef.getId() == currentUse.getId() && currentDef.getType() == ScopeType.SWITCH)
				return currentDef;
			currentDef = currentDef.getParentScope();
			currentUse = currentUse.getParentScope();
		}
		return null;
	}
	
	// Provera da li postoji break izmedju dve naredbe u okviru switch-a
	public boolean breakExistsBetween(Definition def, Use use, int switchId) {
		for (int i=0; i<breakStmts.size(); i++) {
			BreakStmt breakStmt = breakStmts.get(i);
			if (breakStmt.getType() != BreakType.SWITCH_BREAK || breakStmt.getScopeId() != switchId)
				continue;
			if (isMandatory(breakStmt.getBelongsToScope(), def.getScope(), use.getScope()) && 
					breakStmt.getRowNo() > def.getRowNo() && breakStmt.getRowNo() < use.getRowNo())
				return true;
		}
		return false;
	}
	
	// Provera da li su dva opsega medjusobno iskljuciva
	public boolean areExclusive(Scope scopeDef, Scope scopeUse) {
		for (int i=0; i<ifElseStmts.size(); i++) {
			IfElseStmt ifElse = ifElseStmts.get(i);
			Scope currentDef = scopeDef;
			while (currentDef != null) {
				int defScopeId = currentDef.getId();
				Scope currentUse = scopeUse;
				while (currentUse != null) {
					int useScopeId = currentUse.getId();
					if (defScopeId != useScopeId && ifElse.areExclusive(defScopeId, useScopeId))
						return true;
					currentUse = currentUse.getParentScope();
				}
				currentDef = currentDef.getParentScope();
			}
		}
		return false;
	}
	
	// Provera da li su dve promenljive iste
	public boolean sameVars(String var1, String var2) {
		if (var1.contains("this"))
			var1 = var1.substring(5, var1.length());
		if (var2.contains("this"))
			var2 = var2.substring(5, var2.length());
		if (var1.equals(var2)) return true;
		return false;
	}
	
	// Pronalazenje DU-lanaca
	public void findDuChains() {
		for (int i=0; i<definitions.size(); i++) {
			Definition def = definitions.get(i);
			for (int j=0; j<uses.size(); j++) {
				Use use = uses.get(j);
				
				// Ako se definicija i upotreba ne odnose na istu promenljivu nastavljamo dalje
				if (!sameVars(def.getVariable(), use.getVariable()))
					continue;
				
				int scopeDef = getScopeWhereVarIsDef(def.getScope(), def.getVariable());
				int scopeUse = getScopeWhereVarIsDef(use.getScope(), use.getVariable());
				if (scopeDef != scopeUse)
					continue;
				
				// Ako definicija i upotreba nisu u istoj klasi nastavljamo dalje
				int classDef = getClassScope(def.getScope());
				int classUse = getClassScope(use.getScope());
				if (classDef != classUse)
					continue;
				
				// Ako definicija i upotreba nisu u istoj funkciji nastavljamo dalje
				int funcDef = getFunctionScope(def.getScope());
				int funcUse = getFunctionScope(use.getScope());
				if (funcDef != funcUse)
					continue;
				
				Scope loopScope = getScopeWithLoop(def.getScope(), use.getScope());
				
				// Ako se definicija i upotreba nalaze u medjusobno iskljucivim opsezima, a nemaju zajednicku petlju
				// nastavljamo dalje
				if (areExclusive(def.getScope(), use.getScope()) && loopScope == null)
					continue;
				
				// Ako se definicija i upotreba nalaze u istom switch-u, izmedju njih postoji break, a nemaju zajednicku petlju
				// nastavljamo dalje
				Scope switchScope = getScopeWithSwitch(def.getScope(), use.getScope());
				if (switchScope != null && breakExistsBetween(def, use, switchScope.getId()) && loopScope == null)
					continue;
				
				// Ako se definicija nalazi pre upotrebe
				// dovoljno je proveriti da li izmedju njih postoji jos neka definicija
				if (def.getRowNo() < use.getRowNo()) {
					if (!defExistsBetween(def.getVariable(), def.getRowNo(), use.getRowNo(), def.getScope(), use.getScope()))
							addDuChain(def.getVariable(), def.getRowNo(), use.getRowNo());
				}
				// Ako se upotreba nalazi pre definicije
				// moguce je da postoji DU-lanac izmedju njih ako se nalaze u zajednickoj petlji
				else {
					Scope defLoop = getLoopScopeCond(def.getScope(), use.getConditionFor());
					// Ako je upotreba u uslovu petlje
					if (defLoop == null) {
						if (loopScope == null)
							continue;
						if (!defExistsBetween(def.getVariable(), loopScope.getRowStart(), use.getRowNo(), def.getScope(), use.getScope()) && 
								!defExistsBetween(def.getVariable(), def.getRowNo(), loopScope.getRowEnd(), def.getScope(), use.getScope()))
							addDuChain(def.getVariable(), def.getRowNo(), use.getRowNo());
					}
					// Ako su i definicija i upotreba u okviru iste petlje
					else {
						if (!defExistsBetween(def.getVariable(), def.getRowNo(), defLoop.getRowEnd(), def.getScope(), use.getScope()))
							addDuChain(def.getVariable(), def.getRowNo(), use.getRowNo());
					}
				}
				
			}
		}
	}
	
	// Dodavanje nove LCSAJ sekvence
	public void addLcsaj(int seqStart, int seqEnd, int jump) {
		lcsaj.add(new Lcsaj(seqStart, seqEnd, jump));
	}
	
	// Provera da li postoji obavezan skok izmedju dve linije koda
	public boolean jumpExistsBetween(ArrayList<Jump> jumps, int start, int end, int priority) {
		for (int i=0; i<jumps.size(); i++) {
			Jump jump = jumps.get(i);
			if (jump.isMandatory() && jump.getStart() > start) {
				if (jump.getStart() < end)
					return true;
				else if (jump.getStart() == end && jump.getPriority() > priority)
					return true;
			}
		}
		return false;
	}
	
	// Pronalazenje LCSAJ sekvenci
	public void findLcsaj() {
		ArrayList<Integer> seqStarts = symTable.findSeqStarts();
		ArrayList<Jump> jumps = symTable.findJumps(ifElseStmts, breakStmts);
		
		for (int i=0; i<seqStarts.size(); i++) {
			int seqStart = seqStarts.get(i);
			for (int j=0; j<jumps.size(); j++) {
				int seqEnd = jumps.get(j).getStart();
				int jump = jumps.get(j).getEnd();
				int priority = jumps.get(j).getPriority();
				// Pocetak sekvence mora da bude pre kraja sekvence
				if (seqStart > seqEnd)
					continue;
				// Potrebno je da izmedju pocetka i kraja sekvence nema obaveznog skoka
				if (!jumpExistsBetween(jumps, seqStart, seqEnd, priority) && (seqStart == seqEnd || !symTable.areInSameSwitch(seqStart, seqEnd))) {
					addLcsaj(seqStart, seqEnd, jump);
				}
			}
		}
		
		// Dodajemo poslednju LCSAJ sekvencu (od neke linije do kraja programa)
		int lastSeqStart = seqStarts.get(seqStarts.size() - 1);
		int lastRow = symTable.getScope(0).getRowEnd();
		if (lastRow > lastSeqStart)
			addLcsaj(lastSeqStart, lastRow, -1);
	}
	
	// Dohvatanje definicija za ispis
	public String getDefinitions() {
		String defStr = "";
		for (int i=0; i<definitions.size(); i++) {
			Definition d = definitions.get(i);
			String var = d.getVariable();
			if (var.contains("this"))
				var = var.substring(5, var.length());
			defStr += var + " (red " + d.getRowNo() + ")\n";
		}
		defStr += "\nBroj definicija: " + definitions.size();
		return defStr;
	}
	
	// Dohvatanje upotreba za ispis
	public String getUses() {
		String useStr = "";
		int cUses = 0;
		int pUses = 0;
		for (int i=0; i<uses.size(); i++) {
			Use u = uses.get(i);
			String var = u.getVariable();
			if (var.contains("this"))
				var = var.substring(5, var.length());
			useStr += var + " (red " + u.getRowNo() + ", " + u.getType() + "-upotreba)\n";
			if (u.getType() == 'c')
				cUses++;
			else
				pUses++;
		}
		useStr += "\nBroj c-upotreba: " + cUses;
		useStr += "\nBroj p-upotreba: " + pUses;
		return useStr;
	}
	
	// Dohvatanje DU-lanaca za ispis
	public String getDuChains() {
		String duStr = "";
		for (int i=0; i<duChains.size(); i++) {
			DUChain du = duChains.get(i);
			String var = du.getVariable();
			if (var.contains("this"))
				var = var.substring(5, var.length());
			duStr += "[" + var + ", " + du.getRowNoDef() + ", " + du.getRowNoUse() + "]\n";
		}
		duStr += "\nBroj DU-lanaca: " + duChains.size();
		return duStr;
	}
	
	// Dohvatanje LCSAJ sekvenci za ispis
	public String getLcsaj() {
		// Ispis tabele simbola (samo za testiranje)
		String s = "";
		for (int i=0; i<symTable.numberOfScopes(); i++) {
			Scope scope = symTable.getScope(i);
			s += scope.getId() + " (" + scope.getRowStart() + "-" + scope.getRowEnd() + ", " + scope.getType() + ", " + scope.isDoLoop() + "): " + scope.getVars() + "\n";
		}
		System.out.println(s);
		
		// Ispis if-else struktura (samo za testiranje)
		s = "";
		for (int i=0; i<ifElseStmts.size(); i++) {
			IfElseStmt ie = ifElseStmts.get(i);
			s += "if: " + ie.getIfScope() + ", else: " + ie.getElseScope() + ", parent: " + ie.getParentIfElse() + "\n";
		}
		System.out.println(s);
		
		// Ispis break/continue naredbi (samo za testiranje)
		s = "";
		for (int i=0; i<breakStmts.size(); i++) {
			BreakStmt bs = breakStmts.get(i);
			s += bs.getType() + ": pripada " + bs.getBelongsToScope().getId() + ", odnosi se na " + bs.getScopeId() + "\n"; 
		}
		System.out.println(s);
		
		s = "";
		for (int i=0; i<lcsaj.size(); i++) {
			Lcsaj lc = lcsaj.get(i);
			if (lc.getJump() != -1)
				s += "(" + lc.getStartSeq() + ", " + lc.getEndSeq() + ", " + lc.getJump() + ")\n";
			else
				s += "(" + lc.getStartSeq() + ", " + lc.getEndSeq() + ")\n";
		}
		s += "\nBroj LCSAJ sekvenci: " + lcsaj.size();
		return s;
	}
	
	// Provera da li se nalazimo u step-by-step modu rada
	public boolean isStepByStep() {
		return stepByStep;
	}
	
	// Zapocinjanje step-by-step moda rada
	public void startStepByStep() {
		stepByStep = true;
		currentRow = 1;
	}
	
	// Prelazak na sledeci korak u step-by-step modu rada
	public boolean nextStep(JTextArea defArea, JTextArea useArea, JTextArea duChainsArea) {
		boolean newDefUseAdded = false;
		int lastRow = symTable.getScope(0).getRowEnd();
		
		// Sve dok ne dodamo novu definiciju/upotrebu ili dok ne dodjemo do kraja programa
		while (!newDefUseAdded && currentRow <= lastRow) {
			
			// Dodajemo sve definicije na tekucoj liniji
			for (int i=0; i<definitions.size(); i++) {
				Definition d = definitions.get(i);
				if (d.getRowNo() == currentRow) {
					String var = d.getVariable();
					if (var.contains("this"))
						var = var.substring(5, var.length());
					defArea.append(var + " (red " + d.getRowNo() + ")\n"); 
					newDefUseAdded = true;
				}
			}
			
			// Dodajemo sve upotrebe na tekucoj liniji
			for (int i=0; i<uses.size(); i++) {
				Use u = uses.get(i);
				if (u.getRowNo() == currentRow) {
					String var = u.getVariable();
					if (var.contains("this"))
						var = var.substring(5, var.length());
					useArea.append(var + " (red " + u.getRowNo() + ", " + u.getType() + "-upotreba)\n");
					newDefUseAdded = true;
				}
			}
			
			// Dodajemo sve DU-lance na tekucoj liniji
			for (int i=0; i<duChains.size(); i++) {
				DUChain du = duChains.get(i);
				if (du.getRowNoDef() >= du.getRowNoUse()) {
					if (du.getRowNoDef() == currentRow) {
						String var = du.getVariable();
						if (var.contains("this"))
							var = var.substring(5, var.length());
						duChainsArea.append("[" + var + ", " + du.getRowNoDef() + ", " + du.getRowNoUse() + "]\n");
					}
				}
				else {
					if (du.getRowNoUse() == currentRow) {
						String var = du.getVariable();
						if (var.contains("this"))
							var = var.substring(5, var.length());
						duChainsArea.append("[" + var + ", " + du.getRowNoDef() + ", " + du.getRowNoUse() + "]\n");
					}
				}
			}
			
			// Prelazimo na sledecu liniju
			currentRow++;
		}
		
		// Ako smo dosli do kraja programa
		if (currentRow > lastRow) {
			int cUses = 0;
			int pUses = 0;
			for (int i=0; i<uses.size(); i++) {
				Use u = uses.get(i);
				if (u.getType() == 'c')
					cUses++;
				else
					pUses++;
			}
			
			// Ispisujemo broj definicija/upotreba/DU-lanaca
			defArea.append("\nBroj definicija: " + definitions.size());
			useArea.append("\nBroj c-upotreba: " + cUses);
			useArea.append("\nBroj p-upotreba: " + pUses);
			duChainsArea.append("\nBroj DU-lanaca: " + duChains.size());
			// Izlazimo iz step-by-step moda
			stepByStep = false;
			return true;
		}
		return false;
	}

}
