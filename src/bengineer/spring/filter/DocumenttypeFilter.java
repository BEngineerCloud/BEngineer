package bengineer.spring.filter;

import java.util.ArrayList;
import java.util.List;

public class DocumenttypeFilter {
	private static List<String> filterings = new ArrayList();
	public DocumenttypeFilter(){
		if(filterings.isEmpty()) {
			filterings.add(".txt");
			filterings.add(".doc");
			filterings.add(".docx");
			filterings.add(".xls");
			filterings.add(".xlsx");
			filterings.add(".ppt");
			filterings.add(".pptx");
			filterings.add(".pdf");
			filterings.add(".xps");
		}
	}
	public void addFilterings(String filtering) {filterings.add(filtering);}
	public boolean removeFilterings(String filtering) {
		return filterings.remove(filtering);
	}
	public static boolean typeFilter(String filetype) {
		if(filterings.contains(filetype)) {
			return true;
		}
		return false;
	}
	public static List<String> getFilterings() {
		return filterings;
	}
}