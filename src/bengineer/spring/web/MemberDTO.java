package bengineer.spring.web;

import java.sql.Timestamp;

public class MemberDTO {
	private String id; 			//아이디
	private String pw; 			//비밀번호
	private String email; 		//이메일
	private String nickname; 	//닉네임
	private String birthday; 	//생년월일
	private String gender; 		//성별
	private int chmod; 			//권한
	private Timestamp term; 	//기간
	
	//아이디 get,set 메서드
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	//비밀번호 get,set 메서드
	public String getPw() {
		return pw;
	}
	public void setPw(String pw) {
		this.pw = pw;
	}
	
	//이메일 get,set 메서드
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	//닉네임 get,set 메서드
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	//생년월일 get,set 메서드
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	
	//성별 get,set 메서드
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	//권한 get,set 메서드
	public int getChmod() {
		return chmod;
	}
	public void setChmod(int chmod) {
		this.chmod = chmod;
	}
	
	//기간 get,set 메서드
	public Timestamp getTerm() {
		return term;
	}
	public void setTerm(Timestamp term) {
		this.term = term;
	}	
}
