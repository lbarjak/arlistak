package arlistak;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Dates {
	
	public String now() {
        Date today = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd-HHmm");
        String now = formatter.format(today);
        return now;
    }
}
