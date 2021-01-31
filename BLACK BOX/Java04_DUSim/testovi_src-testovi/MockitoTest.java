package testovi;



import simulator.*;
import symtable.Scope;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.*;
import static org.mockito.Mockito.*;

public class MockitoTest {
	//create fake object

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
			File rf = new File("C:\\Users\\lsimo\\OneDrive\\Desktop\\Java04_DUSim\\Test primeri\\DU\\Test4.txt");
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
	//LCSAJ TESTING WITH MOCKITO
	@Test
	public void test_mockito_lcsaj() {
		//Mockito objects
		Lcsaj s1 = mock(Lcsaj.class);	
		Lcsaj s2 = mock(Lcsaj.class);
		
		
		
		//Mockito methods
		when(s1.getStartSeq()).thenReturn(2);
		when(s1.getEndSeq()).thenReturn(5);
		when(s1.getJump()).thenReturn(9);		
		when(s2.getStartSeq()).thenReturn(3);
		when(s2.getEndSeq()).thenReturn(8);
		when(s2.getJump()).thenReturn(13);
		
		
		sim = new Simulator();
		sim.lcsaj = new ArrayList<Lcsaj>();
		sim.lcsaj.add(s1);
		sim.lcsaj.add(s2);
	
		String validPrint = "(2, 5, 9)\n(3, 8, 13)\n\nBroj LCSAJ sekvenci: 2";

		assertEquals("Nije dobar geter lcsaj", validPrint, sim.getLcsaj());
		
		//mockito verify
		verify(s1).getStartSeq();
		verify(s1).getEndSeq();		
	}
	
	@Test
	public void test_definicije() {
		Scope sc1 = mock(Scope.class);	
		Definition def1 = mock(Definition.class);
		Definition def2 = mock(Definition.class);
		
		when(def1.getVariable()).thenReturn("prom");
		when(def1.getRowNo()).thenReturn(1);
		when(def1.getScope()).thenReturn(sc1);
		when(def2.getVariable()).thenReturn("prom2");
		when(def2.getRowNo()).thenReturn(2);
		when(def2.getScope()).thenReturn(sc1);
				
		sim = new Simulator();
		sim.definitions = new ArrayList<Definition>();
		sim.definitions.add(def1);
		sim.definitions.add(def2);
		
		String validPrint = "prom (red 1)\nprom2 (red 2)\n\nBroj definicija: 2";	
		assertEquals("Nije dobar geter definicija", validPrint, sim.getDefinitions());
			
	}
	
	private void create_mockito_objects_duchain(ArrayList<DUChain> du,String prom,int one,int two)
	{
		DUChain d = mock(DUChain.class);
		when(d.getVariable()).thenReturn(prom);
		when(d.getRowNoDef()).thenReturn(one);
		when(d.getRowNoUse()).thenReturn(two);
		du.add(d);
	}
	private void create_mockito_objects_lcsaj(ArrayList<Lcsaj> du,int zero,int one,int two)
	{
		Lcsaj d = mock(Lcsaj.class);
		when(d.getStartSeq()).thenReturn(zero);
		when(d.getEndSeq()).thenReturn(one);
		when(d.getJump()).thenReturn(two);
		du.add(d);
	}
	@Test
	public void test_txt4_whole_example() {
		int i =0;
		
		String expected_definicije = "";
		String expected_upotrebe = "";
	
		ArrayList<DUChain> du;
		ArrayList<Lcsaj> lcsaj;
	    ArrayList<String> vars = new ArrayList<String>();
		
	    sim = new Simulator();
		
		StringBuilder s = new StringBuilder("");
		s.append("gr (red 1)\n").
		append("a (red 2)\n").
		append("i (red 3)\n").
		append("a (red 5)\n").
		append("i (red 6)\n").
		append("\nBroj definicija: 5");
		
		
		expected_definicije = s.toString();
	
		s = new StringBuilder("");
		
		s.append("i (red 4, p-upotreba)\n").
		append("gr (red 4, p-upotreba)\n").
		append("a (red 5, c-upotreba)\n").
		append("i (red 6, c-upotreba)\n\n").
		append("Broj c-upotreba: 2\n").
		append("Broj p-upotreba: 2");
		
		expected_upotrebe = s.toString();
	   
		
		du = new ArrayList<DUChain>();
           
		//mock DUCHains	
		create_mockito_objects_duchain(du,"gr",1,4);
		create_mockito_objects_duchain(du,"a",2,5);
		create_mockito_objects_duchain(du,"i",3,4);
		create_mockito_objects_duchain(du,"i",3,6);
		create_mockito_objects_duchain(du,"a",5,5);
		create_mockito_objects_duchain(du,"i",6,4);
		create_mockito_objects_duchain(du,"i",6,6);
            	
     
        lcsaj = new ArrayList<Lcsaj>();
        //mock lcsaj
        create_mockito_objects_lcsaj(lcsaj,1,4,8);
        create_mockito_objects_lcsaj(lcsaj,1,7,4);
        create_mockito_objects_lcsaj(lcsaj,4,4,8);
        create_mockito_objects_lcsaj(lcsaj,4,7,4);
   

		vars.clear();
		
		sim.analyseCode(myFile, sc);
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
	
}
