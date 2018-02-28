package bengineer.spring.board;

import java.sql.Timestamp;

public class BoardDTO {
	private int num;		// 글번호
	private String title;	// 제목
	private String content;	// 문의내용
	private String id; 		// 아이디
	private String reply; 		// 답변
	private Timestamp reg_date; // 작성일
	
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Timestamp getReg_date(){
		return reg_date;
	}
	public void setReg_date(Timestamp reg_date) {
		this.reg_date = reg_date;
	}
	public String getReply() {
		return reply;
	}
	public void setReply(String reply) {
		this.reply = reply;
	}
}
