package testovi;


import simulator.*;


import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.*;


public class T3 {
//TESTIRANJE NEISPRAVNIH FUNKCIONALNOSTI 
//IMPLEMENTACIJA SOPSTVENOG TESTA
	

	private String myFile="";
	private String scan="";
	private Simulator sim;
	
	@Before
	public void ucitavanje_fajla() {
		BufferedReader bufferdReader=null;
		myFile = "";
		scan = "";
		try {
			//Ako promenim ovo, ne zaboravi da i expected value svih vrednosti se takodje menja
			File rf = new File("C:\\Users\\lsimo\\OneDrive\\Desktop\\DZ_0423_2017\\DZ2_0423_2017\\Java04_DUSim\\Test primeri\\MOJI\\Test3.txt");
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
		ArrayList<String> vars = new ArrayList<String>();
		
		String expected_definicije = "";
		String expected_upotrebe = "";
		
		//final atribut nije dobro napisan u definiciji
		StringBuilder s = new StringBuilder("");
		s.append("ime (red 3)\npol (red 4)\ngodine (red 5)\ni (red 12)\ni (red 13)\nb (red 14)\ni (red 14)\nc (red 15)\nb (red 15)\ni (red 15)\nc (red 16)\nc (red 17)\nj (red 18)\nb (red 20)\nc (red 20)\ni (red 20)\nime (red 30) \n");
		s.append("\nBroj definicija: 17");
		
		expected_definicije = s.toString();
		
		s = new StringBuilder("");
		
		s.append("ime (red 11, p-upotreba)\ni (red 15, c-upotreba)\ni (red 16, c-upotreba)\ni (red 16, c-upotreba)\nb (red 17, c-upotreba)\nb (red 17, c-upotreba)\ni (red 17, c-upotreba)\ni (red 17, c-upotreba)\nc (red 18, c-upotreba)\nc (red 22, c-upotreba)\nc (red 22, c-upotre ba)\ni (red 22, c-upotreba)\ni (red 22, c-upotreba)\nj (red 23, p-upotreba)\nc (red 24, c-upotreba)\n\nBroj c-upotreba: 13\nBroj p-upotreba: 2");
		expected_upotrebe= s.toString();
		
				
	    vars.clear();
		sim = new Simulator();
		sim.analyseCode(myFile, scan);
		sim.varsToInclude.printVars();
		sim.getUses();
		sim.getDuChains();
		sim.getLcsaj();
		
		
		//provera definicija i upotreba
		assertEquals("Ocekivana definicija nije ista kao definicija u simultoru\n",expected_definicije,sim.getDefinitions());
		assertEquals("Ocekivana upotreba nije ista kao upotreba u simultoru\n",expected_upotrebe,sim.getUses());
				
	}
	
	@After
	public void returnAllvals()
	{
		sim = null;
	}
}
