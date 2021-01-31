package simulator;

import java.util.ArrayList;

// Klasa koja izdvaja tokene iz unetog koda na osnovu specifikacije Jave
public class Lexer {
	
	// Kljucne reci
	private final String[] keywords = {
			"const", "assert", "break", "case", "catch",
			"class", "continue", "default", "do", "else", 
			"enum", "extends", "finally", "for", "goto", 
			"if", "implements", "import", "instanceof", "interface", 
			"native", "new", "package", "return", "strictfp", 
			"super", "switch", "synchronized", "this", "throw", 
			"throws", "try", "void", "while"
	};	
	
	// Ugradjene vrednosti
	private final String[] values = {
			"true", "false", "null"
	};
	
	// Ugradjeni tipovi
	private final String[] types = {
			"boolean", "byte", "char", "double", "float", "int", "long", "short"
	};
	
	// Modifikatori pristupa
	private final String[] accessModifiers = {
			"private", "protected", "public"
	};
	
	// Ključne reči koje mogu da uđu u sastav definicije/deklaracije
	private final String[] literals = {
			"static", "volatile", "final", "transient", "abstract"
	};
	
	// Operatori koji se sastoje iz jednog znaka
	private final char[] operators1 = {
			'(', ')', '[', ']', '.', '~', '?', ':', ','
	};
	
	// Operatori koji mogu medjusobno da se kombinuju i daju nove operatore
	private final char[] operators2 = {
			'+', '-', '!', '*', '/', '%', '<', '>', 
			'=', '&', '^', '|'
	};
	
	// Zagrade koje okruzuju blok
	private final char[] blockBrackets = {
			'{', '}'
	};
	
	// Znak za kraj naredbe
	private final char semiColon = ';';
	
	// Lista u kojoj se pamte tokeni i iterator za obilazak liste
	private ArrayList<Token> tokenList;
	private int currentIndex;
	
	// Liste korisnickih i ugradjenih tipova
	private ArrayList<String> userTypes;
	private ArrayList<String> systemTypes;
	private ArrayList<String> userConsts;
	
	public Lexer() {
		tokenList = new ArrayList<>();
		userTypes = new ArrayList<>();
		systemTypes = new ArrayList<>();
		userConsts = new ArrayList<>();
		
		// (Neki) ugradjeni tipovi 
		systemTypes.add("Runnable");
		systemTypes.add("Boolean");
		systemTypes.add("Byte");
		systemTypes.add("Character");
		systemTypes.add("Double");
		systemTypes.add("Float");
		systemTypes.add("Integer");
		systemTypes.add("Long");
		systemTypes.add("Object");
		systemTypes.add("Short");
		systemTypes.add("String");
		systemTypes.add("System");
		systemTypes.add("Scanner");
		systemTypes.add("Thread");
		systemTypes.add("Exception");
	}
	
	// Skenira unet kod i izdvaja tokene
	public void scanCode(String code) {
		// Rec koja se trenutno cita
		String word = "";
		// Brojac koji koristimo za prolaz kroz kod
		int i = 0;
		// Brojac redova
		int rowNo = 1;
		
		while (i < code.length()) {
			
			// Ako se radi o tokenu koji se sastoji samo iz jednog znaka
			if (isOperator1(code.charAt(i)) || isBlockBracket(code.charAt(i)) || isSemiColon(code.charAt(i))) {
				if (!word.equals("")) {
					Token token = new Token(word, rowNo);
					tokenList.add(token);
					word = "";
				}
				word += code.charAt(i);
				Token token = new Token(word, rowNo);
				tokenList.add(token);
				word = "";
				i++;
			}
			
			// Ako se radi o nekom operatoru koji moze da se kombinuje sa drugim
			else if (isOperator2(code.charAt(i))) {
				if (!word.equals("")) {
					Token token = new Token(word, rowNo);
					tokenList.add(token);
					word = "";
				}
				// Citamo kod sve dok se pojavljuju znakovi koji mogu da se kombinuju
				while (isOperator2(code.charAt(i))) {
					word += code.charAt(i);
					i++;
				}
				Token token = new Token(word, rowNo);
				tokenList.add(token);
				word = "";
			}
			
			// Ako se radi o String vrednosti
			else if (code.charAt(i) == '\"') {
				if (!word.equals("")) {
					Token token = new Token(word, rowNo);
					tokenList.add(token);
					word = "";
				}
				word += code.charAt(i);
				i++;
				while (!(code.charAt(i) == '\"' && code.charAt(i-1) != '\\')) {
					word += code.charAt(i);
					i++;
				}
				word += code.charAt(i);
				i++;
				Token token = new Token(word, rowNo);
				tokenList.add(token);
				word = "";
			}
			
			// Ako se radi o char vrednosti
			else if (code.charAt(i) == '\'') {
				if (!word.equals("")) {
					Token token = new Token(word, rowNo);
					tokenList.add(token);
					word = "";
				}
				word += code.charAt(i);
				i++;
				while (!(code.charAt(i) == '\'' && code.charAt(i-1) != '\\')) {
					word += code.charAt(i);
					i++;
				}
				word += code.charAt(i);
				i++;
				Token token = new Token(word, rowNo);
				tokenList.add(token);
				word = "";
			}
			
			// Ako smo procitali blanko znak, formiramo token od prethodno procitane reci
			else if (code.charAt(i) == ' ') {
				if (!word.equals("")) {
					Token token = new Token(word, rowNo);
					tokenList.add(token);
					word = "";
				}
				i++;
			}
			
			// Ako smo procitali znak za novi red, formiramo token od prethodno procitane reci i inkrementiramo brojac redova
			else if (code.charAt(i) == '\n') {
				if (!word.equals("")) {
					Token token = new Token(word, rowNo);
					tokenList.add(token);
					word = "";
				}
				rowNo++;
				i++;
			}
			
			// Ako smo procitali znak za tabulaciju, samo prelazimo na sledeci znak
			else if (code.charAt(i) == '\t') {
				i++;
			}
			
			// U suprotnom dodajemo procitani znak na tekucu rec
			else {
				word += code.charAt(i);
				i++;
			}
		}
		
		// Resetujemo iterator liste tokena
		currentIndex = 0;
	}
	
	// Dohvata sledeci token i inkrementira iterator
	public Token nextToken() {
		if (currentIndex >= tokenList.size()) {
			currentIndex = 0;
			return null;
		}
		Token token = tokenList.get(currentIndex);
		currentIndex++;
		return token;
	}
	
	// Dohvata sledeci token bez inkrementiranja iteratora
	public Token peakNextToken() {
		if (currentIndex >= tokenList.size()) return null;
		return tokenList.get(currentIndex);
	}
	
	// Provera da li je zadata rec kljucna rec
	public boolean isKeyword(String word) {
		boolean contains = false;
		for (int i=0; i<keywords.length; i++) {
			if (keywords[i].equalsIgnoreCase(word)) {
				contains = true;
				break;
			}
		}
		return contains;
	}
	
	// Provera da li je zadata rec tip
	public boolean isType(String word) {
		if (userTypes.contains(word)) return true;
		if (systemTypes.contains(word)) return true;
		boolean contains = false;
		for (int i=0; i<types.length; i++) {
			if (types[i].equalsIgnoreCase(word)) {
				contains = true;
				break;
			}
		}
		return contains;
	}
	
	// Provera da li je zadata rec modifikator pristupa
	public boolean isAccessModifier(String word) {
		boolean contains = false;
		for (int i=0; i<accessModifiers.length; i++) {
			if (accessModifiers[i].equalsIgnoreCase(word)) {
				contains = true;
				break;
			}
		}
		return contains;
	}
	
	// Provera da li je zadata rec final, volatile, itd.
	public boolean isLiteral(String word) {
		boolean contains = false;
		for (int i=0; i<literals.length; i++) {
			if (literals[i].equalsIgnoreCase(word)) {
				contains = true;
				break;
			}
		}
		return contains;
	}
	
	// Provera da li je zadati znak operator koji se ne kombinuje
	public boolean isOperator1(char sign) {
		boolean contains = false;
		for (int i=0; i<operators1.length; i++) {
			if (operators1[i] == sign) {
				contains = true;
				break;
			}
		}
		return contains;
	}
	
	// Provera da li je zadati znak operator koji moze da se kombinuje
	public boolean isOperator2(char sign) {
		boolean contains = false;
		for (int i=0; i<operators2.length; i++) {
			if (operators2[i] == sign) {
				contains = true;
				break;
			}
		}
		return contains;
	}
	
	// Provera da li je zadati znak zagrada koja okruzuje blok
	public boolean isBlockBracket(char sign) {
		boolean contains = false;
		for (int i=0; i<blockBrackets.length; i++) {
			if (blockBrackets[i] == sign) {
				contains = true;
				break;
			}
		}
		return contains;
	}
	
	// Provera da li zadati znak oznacava kraj naredbe 
	public boolean isSemiColon(char sign) {
		if (semiColon == sign) return true;
		return false;
	}
	
	// Provera da li je zadata rec vrednost
	public boolean isValue(String word) {
		if (word.charAt(0) >= '0' && word.charAt(0) < '9') return true;
		if (word.charAt(0) == '\'' || word.charAt(0) == '\"') return true;
		for (int i=0; i<values.length; i++) {
			if (values[i].equalsIgnoreCase(word)) {
				return true;
			}
		}
		return false;
	}
	
	// Provera da li je zadata reč konstanta
	public boolean isConst(String word) {
		return userConsts.contains(word);
	}
	
	// Provera da li je zadata rec promenljiva
	public boolean isVariable(String word) {
		if ((word.charAt(0) >= 'a' && word.charAt(0) <= 'z') || (word.charAt(0) >= 'A' && word.charAt(0) <= 'Z') || (word.charAt(0) == '_')) {
			if (word.equalsIgnoreCase("this")) return true;
			if (!isKeyword(word) && !isType(word) && !isAccessModifier(word) && !isLiteral(word) && !isValue(word) && !isConst(word)) return true;
		}
		return false;
	}
	
	// Dodavanje novog korisnickog tipa
	public void addUserType(String type) {
		userTypes.add(type);
	}
	
	// Dodavanje nove konstante
	public void addConstant(String con) {
		userConsts.add(con);
	}
	
	// Provera da li promenljiva sadrži ključnu reč "this"
	public boolean hasThis(String var) {
		if (var.contains("this")) return true;
		return false;
	}
	
	// Dohvata objekat čijim poljima se pristupa
	public String getTopObject(String var) {
		String s = "";
		int i = 0;
		while (var.charAt(i) != '.') {
			s += var.charAt(i);
			i++;
		}
		return s;
	}

}
