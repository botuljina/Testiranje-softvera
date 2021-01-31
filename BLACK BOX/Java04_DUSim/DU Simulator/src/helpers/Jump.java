package helpers;

// Klasa koja predstavlja skok u programu
public class Jump {
	
	private int start;
	private int end;
	private boolean isMandatory;
	
	// 3 - break/continue skokovi
	// 2 - skokovi na pocetak petlje
	// 1 - ostali skokovi
	// Prioritet nam je potreban kad imamo vise obaveznih skokova sa iste linije u kodu
	private int priority;
	
	public Jump(int start, int end, boolean isMandatory, int priority) {
		super();
		this.start = start;
		this.end = end;
		this.isMandatory = isMandatory;
		this.priority = priority;
	}
	
	public int getStart() {
		return start;
	}
	
	public int getEnd() {
		return end;
	}

	public boolean isMandatory() {
		return isMandatory;
	}
	
	public int getPriority() {
		return priority;
	}

}
