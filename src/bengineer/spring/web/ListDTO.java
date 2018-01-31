package bengineer.spring.web;

import java.util.List;

public class ListDTO {
	private int num;
	private String string;
	private List list;
	private long longnum;
	public long getLongnum() {
		return longnum;
	}
	public void setLongnum(long longnum) {
		this.longnum = longnum;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getString() {
		return string;
	}
	public void setString(String string) {
		this.string = string;
	}
	public List getList() {
		return list;
	}
	public void setList(List list) {
		this.list = list;
	}
}
