import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Util {
	public static String getCurrentDateTime() {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		Calendar c1 = Calendar.getInstance();
		String strToday = sdf.format(c1.getTime());
		
		return strToday;
	}
}
