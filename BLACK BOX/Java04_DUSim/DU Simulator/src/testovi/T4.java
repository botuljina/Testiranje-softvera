package testovi;

import gui.*;
import simulator.*;
import static org.junit.Assert.*;

import java.io.*;
import java.util.ArrayList;


import org.junit.*;

//PROVERA KORAK PO KORAK KAKO RADI

public class T4 {
	private String myFile = "";
	private ArrayList<String> vars = new ArrayList<String>();
	private String scannedVars = "";
	private MainWindow main_window;
	public ArrayList<DUChain> du = new ArrayList<DUChain>();
	public ArrayList<Lcsaj> lcsaj = new ArrayList<Lcsaj>();
	

	@Before
	public void ucitavanje_fajla() {
		BufferedReader bufferdReader=null;
		myFile = "";
		try {
			//Ako promenim ovo, ne zaboravi da i expected value svih vrednosti se takodje menja
			File rf = new File("C:\\Users\\lsimo\\OneDrive\\Desktop\\DZ_0423_2017\\DZ2_0423_2017\\Java04_DUSim\\Test primeri\\LCSAJ\\Test1.txt");
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
		
		main_window = new MainWindow();
	}
	
	@Test
	public void start_step_by_step() {
		main_window.simulator.startStepByStep();
		assertTrue("StepByStepNeRadi.", main_window.simulator.isStepByStep());
	}
	
	@Test
	public void test_two_steps() {
		
		String expected_definicije = "";
		String expected_upotrebe = "";
	
		
		main_window.simulator.startStepByStep();
		assertTrue("Bag u koracima - Nije zapoceto step-by-step, a trebalo je.", main_window.simulator.isStepByStep());
		scannedVars = "";
		main_window.simulator.analyseCode(myFile, scannedVars);
		main_window.defArea.setText("");
		main_window.useArea.setText("");
		main_window.duChainsArea.setText("");
		
		//odradimo 2 koraka
		main_window.simulator.nextStep(main_window.defArea, main_window.useArea, main_window.duChainsArea);
	    main_window.simulator.nextStep(main_window.defArea, main_window.useArea, main_window.duChainsArea);
	    main_window.simulator.nextStep(main_window.defArea, main_window.useArea, main_window.duChainsArea);
	    main_window.simulator.nextStep(main_window.defArea, main_window.useArea, main_window.duChainsArea);
	    main_window.simulator.nextStep(main_window.defArea, main_window.useArea, main_window.duChainsArea);

	    StringBuilder s = new StringBuilder("");
		s.append("brojac (red 4)\n")
		.append("x (red 6)\n")
		.append("y (red 7)\n");
		
		
		expected_definicije = s.toString();
	
		s = new StringBuilder("");
		
		s.append("in (red 1, c-upotreba)\n").
		append("brojac (red 8, p-upotreba)\n");
		
		expected_upotrebe = s.toString();
		
		
		assertEquals("Definicije nisu dobre.", expected_definicije, main_window.defArea.getText());	
		assertEquals("Upotrebe nisu dobre.", expected_upotrebe, main_window.useArea.getText());
		
		du.add(new DUChain("brojac", 4, 8));
	
	
		assertEquals("Neispravna variabla",du.get(0).getVariable(), main_window.simulator.duChains.get(0).getVariable());
		assertEquals("Neispravan RowNoDef",du.get(0).getRowNoDef(), main_window.simulator.duChains.get(0).getRowNoDef());
		assertEquals("Neispravan RowNoUse",du.get(0).getRowNoUse(), main_window.simulator.duChains.get(0).getRowNoUse());
		

	    lcsaj.add(new Lcsaj(1, 8, 15));
	    lcsaj.add(new Lcsaj(1, 9, 11));
	    lcsaj.add(new Lcsaj(1, 10, 13));
	    lcsaj.add(new Lcsaj(8, 8, 15));
	    lcsaj.add(new Lcsaj(8, 9, 11));
	    lcsaj.add(new Lcsaj(8, 10, 13));
	    lcsaj.add(new Lcsaj(11, 14, 8));
	    lcsaj.add(new Lcsaj(13, 14, 8));
	    lcsaj.add(new Lcsaj(15, 16,-1));
	    
	    int j = 0;
		while(j < lcsaj.size()) {
			assertEquals("Nije ispravna vrednost startSeq",lcsaj.get(j).getStartSeq(), main_window.simulator.lcsaj.get(j).getStartSeq());
			assertEquals("Nije ispravna vrednost endSeq",lcsaj.get(j).getEndSeq(), main_window.simulator.lcsaj.get(j).getEndSeq());
			assertEquals("Nije ispravna vrednost Jump",lcsaj.get(j).getJump(), main_window.simulator.lcsaj.get(j).getJump());
			j++;
		}
		
	}
	
	
	@After
	public void deleteSimulatorByDeletingMainWindow() {
		vars.clear();
		lcsaj.clear();
		main_window = null;		
		du.clear();
		
	}
	

	
	
	
	
	
}
