package bengineer.spring.web;

import java.sql.Timestamp;

public class FileDTO {
	private String fileaddress;
	private String filename;
	private String filetype;
	private double filesize;
	private Timestamp uploaddate;
	private Timestamp updatedate;
	private int hitcount;
	private int important;
	private String owner;
	private String otheraddress; // 하위 폴더에 포함된 파일을 검색에서 제거하기 위한 파라미터
	public int getImportant() {
		return important;
	}
	public void setImportant(int important) {
		this.important = important;
	}
	public String getOtheraddress() {
		return otheraddress;
	}
	public String getFileaddress() {
		return fileaddress;
	}
	public void setFileaddress(String fileaddress) {
		this.fileaddress = fileaddress;
		this.otheraddress = fileaddress + "/%";
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getFiletype() {
		return filetype;
	}
	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}
	public double getFilesize() {
		return filesize;
	}
	public void setFilesize(double filesize) {
		this.filesize = filesize;
	}
	public Timestamp getUploaddate() {
		return uploaddate;
	}
	public void setUploaddate(Timestamp uploaddate) {
		this.uploaddate = uploaddate;
	}
	public Timestamp getUpdatedate() {
		return updatedate;
	}
	public void setUpdatedate(Timestamp updatedate) {
		this.updatedate = updatedate;
	}
	public int getHitcount() {
		return hitcount;
	}
	public void setHitcount(int hitcount) {
		this.hitcount = hitcount;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
}
