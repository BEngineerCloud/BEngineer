package bengineer.spring.web;

import java.sql.Timestamp;

public class KeyDTO {
	private String share_key;
	private int filenum;
	private Timestamp enddate;
	private int rw;
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
	public Timestamp getEnddate() {
		return enddate;
	}
	public void setEnddate(Timestamp enddate) {
		this.enddate = enddate;
	}
	public int getRw() {
		return rw;
	}
	public void setRw(int rw) {
		this.rw = rw;
	}
}
