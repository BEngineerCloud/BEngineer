package bengineer.spring.web;

import java.sql.Date;

public class PowerDTO {
	private int chmod;
	private Date term;
	private int randw;
	public int getChmod() {
		return chmod;
	}
	public void setChmod(int chmod) {
		this.chmod = chmod;
	}
	public Date getTerm() {
		return term;
	}
	public void setTerm(Date term) {
		this.term = term;
	}
	public int getRandw() {
		return randw;
	}
	public void setRandw(int randw) {
		this.randw = randw;
	}
}
