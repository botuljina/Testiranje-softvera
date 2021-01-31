package simulator;

// Klasa koja predstavlja token koda
public class Token {
	
	private String token;
	private int rowNo;
	
	public Token(String token, int rowNo) {
		super();
		this.token = token;
		this.rowNo = rowNo;
	}
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
	public int getRowNo() {
		return rowNo;
	}
	
	public void setRowNo(int rowNo) {
		this.rowNo = rowNo;
	}

}
