package bengineer.spring.web;

import java.sql.Timestamp;

public class MemberDTO {
	private String id;
	private String pw;
	private String email;
	private String nickname;
	private String birthday;
	private String gender;
	private int chmod;
	private Timestamp term;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPw() {
		return pw;
	}
	public void setPw(String pw) {
		this.pw = pw;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
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
}
