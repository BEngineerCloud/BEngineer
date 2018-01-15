package bengineer.spring.manager;

import java.sql.Timestamp;

public class ImposeDTO {
	private String email;
	private	String cause;
	private	int term;
	private	Timestamp startDay;
	private	Timestamp endDay;
	
	public String getEmail() {return email;}
	public void setEmail(String email) {this.email = email;}
	public String getCause() {return cause;}
	public void setCause(String cause) {this.cause = cause;}
	public int getTerm() {return term;}
	public void setTerm(int term) {this.term = term;}
	public Timestamp getStartDay() {return startDay;}
	public void setStartDay(Timestamp startDay) {this.startDay = startDay;}
	public Timestamp getEndDay() {return endDay;}
	public void setEndDay(Timestamp endDay) {this.endDay = endDay;}
}
