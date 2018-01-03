package bengineer.spring.web;

import java.sql.Timestamp;

public class ClassificationDTO {
	private int chmod;
	private Timestamp term;
	private String contents;
	
	public int getChmod() {
		return chmod;
	}
	public void setChmod(int chmod) {
		this.chmod = chmod;
	}
	public Timestamp getTerm() {
		return term;
	}
	public void setTerm(Timestamp term) {
		this.term = term;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	
	
}
