package bengineer.spring.web;

public class MemberDTO {
	private String id;
	private String email;
	private String nickname;
	private String birthday;
	private String gender;
	private int chmod;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	
	
}
