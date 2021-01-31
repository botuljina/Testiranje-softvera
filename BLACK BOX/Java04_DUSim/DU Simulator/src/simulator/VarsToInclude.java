package simulator;

import java.util.ArrayList;

// Klasa koja čuva promenljive koje treba uključiti u simulaciji
public class VarsToInclude {
	
	public ArrayList<String> vars;
	
	public VarsToInclude() {
		vars = new ArrayList<>();
	}
	
	// Odvaja promenljive razdvojene razmakom
	public void scanVars(String varsToInclude) {
		int i = 0;
		String var = "";
		
		while (i < varsToInclude.length()) {
			if (varsToInclude.charAt(i) != ' ') 
				var += varsToInclude.charAt(i);
			else {
				if (var != "") vars.add(var);
				var = "";
			}
			i++;
		}
		if (var != "") vars.add(var);
	}
	
	// Provera da li promenljivu treba uključiti u simulaciju
	public boolean isIncluded(String var) {
		if (vars.contains(var)) return true;
		return false;
	}
	
	// Ako nijedna promenljiva nije navedena, potrebno je uključiti sve
	public boolean includeAll() {
		if (vars.size() == 0) return true;
		return false;
	}
	
	// Ispis promenljivih (koristi se samo za testiranje)
	public String printVars() {
		String s = "";
		for (int i=0; i<vars.size(); i++) 
			s += vars.get(i) + "\n";
		return s;
	}

}
