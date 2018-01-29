package bengineer.spring.filter;

import java.util.ArrayList;
import java.util.List;

public class VideotypeFilter {
	private static List<String> filterings = new ArrayList();
	public VideotypeFilter(){
		if(filterings.isEmpty()) {
			filterings.add(".webm");
			filterings.add(".mpeg4");
			filterings.add(".3gpp");
			filterings.add(".mov");
			filterings.add(".avi");
			filterings.add(".mpegps");
			filterings.add(".wmv");
			filterings.add(".flv");
			filterings.add(".ogg");
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