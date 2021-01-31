package testovi;



import helpers.*;
import simulator.*;
import static org.junit.Assert.assertEquals;
import org.junit.*;

public class T2 {
	//test all simulator package methods 
	
	//VarsToInclude klasa provera svih metoda
	@Test
	public void vars_to_include_test() {
		VarsToInclude v = new VarsToInclude();
		
		StringBuilder s = new StringBuilder("");
		s.append("ID\nIG\nIS\n");
		//scan vars je void
		v.scanVars("ID IG IS");
		assertEquals("Nije dobar isInlcuded",true,v.isIncluded("ID"));
		assertEquals("Nije dobar inludeAll",false,v.includeAll());
		assertEquals("Nije dobar print",s.toString(),v.printVars());
				
	}
	
	//Token klasa provera svih metoda
	@Test
	public void Token_test() {
		Token t = new Token("d",15);
		
		assertEquals("Nije dobar metod getToken()","d",t.getToken());
		assertEquals("Nije dobar metod getRowNo()",15,t.getRowNo());
		
		t.setToken("f");		
		assertEquals("Nije dobar metod setToken()","f",t.getToken());
		
		t.setRowNo(14);
		assertEquals("Nije dobar metod setRowNo()",14,t.getRowNo());
		
	}
	//DuChain klasa provera svih metoda
	@Test
	public void du_chain_test() {
		DUChain d = new DUChain("y", 6, 10);
	
		assertEquals("Neispravna variabla","y",d.getVariable());
		assertEquals("Neispravan RowNoDef",6,d.getRowNoDef());
		assertEquals("Neispravan RowNoUse",10,d.getRowNoUse());	
	}
	//LCSAJ klasa provera svih metoda
	@Test
	public void lcsaj_class_test() {
		Lcsaj l = new Lcsaj(1,5,15);
		assertEquals("Nije ispravna vrednost startSeq",1,l.getStartSeq());
		assertEquals("Nije ispravna vrednost endSeq",5,l.getEndSeq());
		assertEquals("Nije ispravna vrednost Jump",15,l.getJump());
		
		l.setStartSeq(2);
		assertEquals("Nije ispravna vrednost startSeq, neispravno implementirana metoda setStartSeq",2,l.getStartSeq());
		l.setEndSeq(38);
		assertEquals("Nije ispravna vrednost endSeq, neispravno implementirana metoda setendSeq",38,l.getEndSeq());
		l.setJump(45);
		assertEquals("Nije ispravna vrednost getJump, neispravno implementirana metoda setJump",45,l.getJump());
		
	}
		
	
	//HELPERS PACKAGE TESTING ALL METHODS
	//SwitchCases klasa provera svih metoda
	@Test
	public void switch_cases_test() {
		SwitchCases s = new SwitchCases(2);
		assertEquals("Nije dobar metod getSwitchId()",2,s.getSwitchId());
		
		s.addCase(11);
		assertEquals("Nije dobar metod contains()",true,s.contains(11));
	
	}
	
	//JUMP klasa provera svih metoda
	@Test
	public void jump_test() {
		Jump j = new Jump(3,13,false,9);
		
		assertEquals("Nije dobar metod getStart()",3,j.getStart());
		assertEquals("Nije dobar metod getEnd()",13,j.getEnd());
		assertEquals("Nije dobar metod isMandatory()",false,j.isMandatory());
		assertEquals("Nije dobar metod getPriority()",9,j.getPriority());
	
	}
	//SwitchCases klasa provera svih metoda
	@Test
	public void If_Else_Stmt_test() {
		IfElseStmt i = new IfElseStmt(15);
		assertEquals("Nije dobar metod getIfScope",-1,i.getIfScope());
		assertEquals("Nije dobar metod getElseScope",-1,i.getElseScope());
		assertEquals("Nije dobar metod getparentIfElse",15,i.getParentIfElse());
		
		i.setIfScope(11);
		assertEquals("Nije dobra fja setIfScope()",11,i.getIfScope());
		i.setElseScope(25);
		assertEquals("Nije dobar metod getElseScope",25,i.getElseScope());
	
	}
	//Break klasu nmg da testiram zbog ovog scop-a
	
}
