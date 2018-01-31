package bengineer.spring.web;

import java.sql.Date;

public class TrashcanDTO {
	private int num;
	private int filenum;
	private Date deletedate;
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public int getFilenum() {
		return filenum;
	}
	public void setFilenum(int filenum) {
		this.filenum = filenum;
	}
	public Date getDeletedate() {
		return deletedate;
	}
	public void setDeletedate(Date deletedate) {
		this.deletedate = deletedate;
	}
}
