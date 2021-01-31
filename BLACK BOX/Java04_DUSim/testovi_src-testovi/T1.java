package testovi;


import simulator.*;
import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.*;

public class T1 {

	private String myFile="";
	private String sc="";
	private Simulator sim;
	
	@Before
	public void ucitavanje_fajla() {
		BufferedReader bufferdReader=null;
		myFile = "";
		sc = "";
		try {
			//Ako promenim ovo, ne zaboravi da i expected value svih vrednosti se takodje menja
			File rf = new File("C:\\Users\\lsimo\\OneDrive\\Desktop\\Java04_DUSim\\Test primeri\\DU\\Test1.txt");
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
	
	
	@Test
	public void test() {
		int i =0;
		
		String expected_definicije = "";
		String expected_upotrebe = "";
	
		ArrayList<DUChain> du;
		ArrayList<Lcsaj> lcsaj;
	    ArrayList<String> vars = new ArrayList<String>();
		
	    sim = new Simulator();
		
		StringBuilder s = new StringBuilder("");
		s.append("x (red 2)\n").
		append("x (red 4)\n").
		append("\nBroj definicija: 2");
		
		
		expected_definicije = s.toString();
	
		s = new StringBuilder("");
		
		s.append("exp (red 1, p-upotreba)\n").
		append("case1 (red 5, p-upotreba)\n").
		append("x (red 6, c-upotreba)\n").
		append("x (red 8, c-upotreba)\n\n").
		append("Broj c-upotreba: 2\n").
		append("Broj p-upotreba: 2");
		
		expected_upotrebe = s.toString();
	   
		
		du = new ArrayList<DUChain>();
            
            	du.add(new DUChain("x",2,6)); 
            	du.add(new DUChain("x",2,8)); 
            	du.add(new DUChain("x",4,6));
            	du.add(new DUChain("x",4,8)); 
            
        
        lcsaj = new ArrayList<Lcsaj>();
            
        lcsaj.add(new Lcsaj(1,1,3)); 
        lcsaj.add(new Lcsaj(1,2,5)); 
        lcsaj.add(new Lcsaj(3,5,7));
        lcsaj.add(new Lcsaj(3,6,9)); 
        lcsaj.add(new Lcsaj(5,5,7));
        lcsaj.add(new Lcsaj(5,6,9));
            
        
     
	
		vars.clear();
		
		sim.analyseCode(myFile, sc);
		sim.varsToInclude.printVars();
		sim.getUses();
		sim.getDuChains();
		sim.getLcsaj();
		
	
		//provera definicija i upotreba
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
	
	@After
	public void returnAllvals()
	{
		sim = null;
	}
	
}
