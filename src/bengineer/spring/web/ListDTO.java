package bengineer.spring.web;

import java.util.List;

public class ListDTO {
	private int folder_ref;
	private List moveList;
	public int getFolder_ref() {
		return folder_ref;
	}
	public void setFolder_ref(int folder_ref) {
		this.folder_ref = folder_ref;
	}
	public List getMoveList() {
		return moveList;
	}
	public void setMoveList(List moveList) {
		this.moveList = moveList;
	}
}
