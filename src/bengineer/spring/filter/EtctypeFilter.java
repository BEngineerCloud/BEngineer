package bengineer.spring.filter;

import java.util.ArrayList;
import java.util.List;

public class EtctypeFilter {
	private static List<String> filterings = new ArrayList();
	public EtctypeFilter(){
		if(filterings.isEmpty()) {
			filterings.add(".zip");
			filterings.add(".rar");
			filterings.add(".tar");
			filterings.add(".gzip");
			filterings.add(".css");
			filterings.add(".html");
			filterings.add(".php");
			filterings.add(".c");
			filterings.add(".cpp");
			filterings.add(".h");
			filterings.add(".hpp");
			filterings.add(".js");
			filterings.add(".ps");
			filterings.add(".ttf");
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