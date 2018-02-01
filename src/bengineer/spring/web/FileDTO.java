package bengineer.spring.web;

import java.sql.Timestamp;

public class FileDTO {
	private int num;
	private int folder_ref;
	private String filename;
	private String orgname;
	private String filetype;
	private long filesize;
	private Timestamp uploaddate;
	private Timestamp updatedate;
	private int hitcount;
	private int important;
	private String owner;
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public int getFolder_ref() {
		return folder_ref;
	}
	public void setFolder_ref(int folder_ref) {
		this.folder_ref = folder_ref;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getOrgname() {
		return orgname;
	}
	public void setOrgname(String orgname) {
		this.orgname = orgname;
	}
	public String getFiletype() {
		return filetype;
	}
	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}
	public long getFilesize() {
		return filesize;
	}
	public void setFilesize(long filesize) {
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
	public int getImportant() {
		return important;
	}
	public void setImportant(int important) {
		this.important = important;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
}
