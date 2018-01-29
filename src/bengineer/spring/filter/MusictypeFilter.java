package bengineer.spring.filter;

import java.util.ArrayList;
import java.util.List;

public class MusictypeFilter {
	private static List<String> filterings = new ArrayList();
	public MusictypeFilter(){
		if(filterings.isEmpty()) {
			filterings.add(".mp3");
			filterings.add(".mpeg");
			filterings.add(".wav");
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