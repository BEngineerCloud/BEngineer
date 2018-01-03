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
	private String owner;
}
