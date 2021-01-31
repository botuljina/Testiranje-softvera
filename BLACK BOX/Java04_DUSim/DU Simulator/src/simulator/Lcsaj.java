package simulator;

// Klasa koja predstavlja LCSAJ sekvencu
public class Lcsaj {
	
	// Početak sekvence, kraj sekvence i odredište skoka
	private int startSeq;
	private int endSeq;
	private int jump;
	
	public Lcsaj(int startSeq, int endSeq, int jump) {
		this.startSeq = startSeq;
		this.endSeq = endSeq;
		this.jump = jump;
	}

	public int getStartSeq() {
		return startSeq;
	}

	public void setStartSeq(int startSeq) {
		this.startSeq = startSeq;
	}

	public int getEndSeq() {
		return endSeq;
	}

	public void setEndSeq(int endSeq) {
		this.endSeq = endSeq;
	}

	public int getJump() {
		return jump;
	}

	public void setJump(int jump) {
		this.jump = jump;
	}

}
