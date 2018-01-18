package bengineer.spring.web;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TrashHolder {
	private List trashList = new ArrayList();
	public void addTrash(String path, String name, int num) {
		Trash trash = new Trash();
		trash.setName(name);
		trash.setPath(path);
		trash.setNum(num);
		trashList.add(trash);
	}
	public List getTrashList() {
		return trashList;
	}
	public void addallTrash(TrashHolder trashcan) {
		List trashlist = trashcan.getTrashList();
		if(trashlist != null && trashlist.size() > 0) {
			this.trashList.addAll(trashlist);
		}
	}
}
