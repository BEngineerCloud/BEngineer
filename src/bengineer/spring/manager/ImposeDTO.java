package bengineer.spring.manager;

import java.sql.Timestamp;

public class ImposeDTO {
	private String email;	// 이메일
	private	String cause;	// 제제 사유
	private	int term;		// 제제 일수
	private	Timestamp startDay; // 제제 시작일
	private	Timestamp endDay;	// 제제 종료일
	
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
