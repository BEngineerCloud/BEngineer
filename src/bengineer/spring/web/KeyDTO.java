package bengineer.spring.web;

public class KeyDTO {
	private String share_key;
	private int filenum;
	private int chmod;
	public String getShare_key() {
		return share_key;
	}
	public void setShare_key(String share_key) {
		this.share_key = share_key;
	}
	public int getFilenum() {
		return filenum;
	}
	public void setFilenum(int filenum) {
		this.filenum = filenum;
	}
	public int getChmod() {
		return chmod;
	}
	public void setChmod(int chmod) {
		this.chmod = chmod;
	}
}
