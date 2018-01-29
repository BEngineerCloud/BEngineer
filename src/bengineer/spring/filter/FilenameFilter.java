package bengineer.spring.filter;

import java.util.ArrayList;
import java.util.List;

public class FilenameFilter {
	private static List<String> filterings = new ArrayList();
	public void addFilterings(String filtering) {filterings.add(filtering);}
	public boolean removeFilterings(String filtering) {
		return filterings.remove(filtering);
	}
	public static boolean nameFilter(String orgname, String filename) {
		for(int i = 0; i < filterings.size(); i++) {
			if(filename.contains(filterings.get(i))) {
				return true;
			}
			if(orgname.contains(filterings.get(i))) {
				return true;
			}
		}
		return false;
	}
	public static boolean nameFilter(String filename) {
		for(int i = 0; i < filterings.size(); i++) {
			if(filename.contains(filterings.get(i))) {
				return true;
			}
		}
		return false;
	}
	public static List<String> getFilterings() {
		return filterings;
	}
}