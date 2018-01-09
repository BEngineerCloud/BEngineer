package bengineer.spring.web;

public class ShareDTO {
	private int num;
	private String share_key;
	private int chmod;
	private String nickname;
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getShare_key() {
		return share_key;
	}
	public void setShare_key(String share_key) {
		this.share_key = share_key;
	}
	public int getChmod() {
		return chmod;
	}
	public void setChmod(int chmod) {
		this.chmod = chmod;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
}
