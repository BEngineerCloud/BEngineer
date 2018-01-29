package bengineer.spring.filter;

import java.util.ArrayList;
import java.util.List;

public class ImagetypeFilter {
	private static List<String> filterings = new ArrayList();
	public ImagetypeFilter(){
		if(filterings.isEmpty()) {
			filterings.add(".jpeg");
			filterings.add(".jpg");
			filterings.add(".png");
			filterings.add(".gif");
			filterings.add(".bmp");
			filterings.add(".dxf");
			filterings.add(".ai");
			filterings.add(".psd");
			filterings.add(".eps");
			filterings.add(".svg");
			filterings.add(".tiff");
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