package bengineer.spring.manager;

public class ManagerDTO {
	private String id;	// 아이디
	private String pw;	// 비밀번호
	private String contents;	// 권한내용
	
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
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	
}
