package testovi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.junit.Before;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import simulator.*;
import static org.junit.Assert.*;
import java.util.ArrayList;


public class T5 {
	private String myFile = "";
	private ArrayList<String> vars = new ArrayList<String>();
	private String scannedVars = "";
	public ArrayList<DUChain> du = new ArrayList<DUChain>();
	public ArrayList<Lcsaj> lcsaj = new ArrayList<Lcsaj>();
	private Simulator sim;
	
	
	@Before
	public void ucitavanje_fajla() {
		BufferedReader bufferdReader=null;
		myFile = "";
		try {
			//Ako promenim ovo, ne zaboravi da i expected value svih vrednosti se takodje menja
			File rf = new File("C:\\Users\\lsimo\\OneDrive\\Desktop\\Java04_DUSim\\Test primeri\\DU\\Test2.txt");
			bufferdReader = new BufferedReader(new FileReader(rf));
			String line = bufferdReader.readLine();
			
			while(line!=null) {
				myFile+=line;
				myFile+="\n";
				line = bufferdReader.readLine();
			}
			
		}catch(Exception e) {
			System.out.println("Fajl ne moze da se ucita! Proveri putanju fajla");
		}finally{
			try {
				bufferdReader.close();
			} catch (IOException e) {
				System.out.println("Nisi uspeo da zatvoris buffered reader,znaci da je do greske u mom kodu.");
			}
		}
		
		
	}
	@Before
	public void setVarsToInclude() {
		scannedVars = "ID";
		vars.add("ID");
		vars.add("IS");
	}
	
	
		@ParameterizedTest
		@CsvFileSource(resources = "vars.csv", numLinesToSkip = 1)
		public void testingScanVars(String vars_from_csv_file) {
			
			VarsToInclude vti = new VarsToInclude();
			vti.scanVars(vars_from_csv_file);
			scannedVars = "";
			int i = 0;
			for (i = 0; i < vars_from_csv_file.length(); i++) {
				if (vars_from_csv_file.charAt(i) == ' ') {
					scannedVars += "\n";
				}else {
					scannedVars += vars_from_csv_file.charAt(i);
				}
			}
			if (vars_from_csv_file.length() > 0 && (vars_from_csv_file.charAt(vars_from_csv_file.length() - 1) != ' '))
					scannedVars += "\n";
			
			
			assertEquals("Provera promenljivih koje su skenirane", scannedVars, vti.printVars());
			
			
			
		}
	
		
		@ParameterizedTest
		@CsvFileSource(resources = "vars2.csv", numLinesToSkip = 1)
		public void testing_whole_example(String vars_from_csv_file) {		
			ucitavanje_fajla();	
			VarsToInclude vti = new VarsToInclude();
			vti.scanVars(vars_from_csv_file);
			scannedVars = "";
			int i = 0;
					
			String expected_definicije = "";
			String expected_upotrebe = "";
		
			ArrayList<DUChain> du;
			ArrayList<Lcsaj> lcsaj;
		    ArrayList<String> vars = new ArrayList<String>();
			
		    sim = new Simulator();
			
			StringBuilder s = new StringBuilder("");
			s.append("locks (red 1)\n").
			append("stocks (red 3)\n").
			append("locks (red 6)\n").
			append("\nBroj definicija: 3");
			
			
			expected_definicije = s.toString();
		
			s = new StringBuilder("");
			
			s.append("locks (red 2, p-upotreba)\n").
			append("locks (red 4, c-upotreba)\n").
			append("stocks (red 5, c-upotreba)\n\n").
			append("Broj c-upotreba: 2\n").
			append("Broj p-upotreba: 1");
			
			expected_upotrebe = s.toString();
		   
			
			du = new ArrayList<DUChain>(); 
	             
	            	du.add(new DUChain("locks",1,2)); 
	            	du.add(new DUChain("locks",1,4)); 
	            	du.add(new DUChain("stocks",3,5));
	            	du.add(new DUChain("locks",6,2)); 
	            	du.add(new DUChain("locks",6,4)); 
	            
	        
	        lcsaj = new ArrayList<Lcsaj>(); 
	            
	        lcsaj.add(new Lcsaj(1,2,7)); 
	        lcsaj.add(new Lcsaj(1,6,2)); 
	        lcsaj.add(new Lcsaj(2,2,7));
	        lcsaj.add(new Lcsaj(2,6,2)); 
	        lcsaj.add(new Lcsaj(7,8,11));
	        lcsaj.add(new Lcsaj(7,10,12));
	            	     
		
			vars.clear();
			//System.out.println(myFile+"*************");
			sim.analyseCode(myFile, vars_from_csv_file);
			sim.varsToInclude.printVars();
			sim.getUses();
			sim.getDuChains();
			sim.getLcsaj();
			
		
			//String s= sim.getDefinitions();
			
			//definicije and upotrebe check
			assertEquals("Ocekivana definicija nije ista kao definicija u simultoru",expected_definicije,sim.getDefinitions());
			assertEquals("Ocekivana upotreba nije ista kao upotreba u simultoru",expected_upotrebe,sim.getUses());
			
			i =0;
			for (DUChain elem : du) {
				assertEquals("Neispravna variabla",elem.getVariable(),sim.duChains.get(i).getVariable());
				assertEquals("Neispravan RowNoDef",elem.getRowNoDef(),sim.duChains.get(i).getRowNoDef());
				assertEquals("Neispravan RowNoUse",elem.getRowNoUse(),sim.duChains.get(i).getRowNoUse());
				i++;
			}
			i = 0;
			for (Lcsaj elem : lcsaj) {
				assertEquals("Nije ispravna vrednost startSeq",elem.getStartSeq(),sim.lcsaj.get(i).getStartSeq());
				assertEquals("Nije ispravna vrednost endSeq",elem.getEndSeq(),sim.lcsaj.get(i).getEndSeq());
				assertEquals("Nije ispravna vrednost Jump",elem.getJump(),sim.lcsaj.get(i).getJump());
				i++;
			}
			
			
		}
	
	
	
}
