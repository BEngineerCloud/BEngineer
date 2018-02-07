package bengineer.spring.board;

import java.sql.Timestamp;
// ��������
public class BoardDTO {
	private int num;		// �۹�ȣ
	private String title;	// ����
	private String content;	// ����
	private String id; 		// �ۼ���
	private String reply; 		// �ۼ���
	//private int hit; 		// ��ȸ��
	private Timestamp reg_date; // �ۼ���
	
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
	/*
	public int getHit() {
		return hit;
	}
	public void setHit(int hit) {
		this.hit = hit;
	}
	*/
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
