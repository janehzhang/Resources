package tydic.reports.customerSatisfied;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class EwamOAPushTest {

	public static void main(String[] args) {
		String str = "20160707";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		try {
		    Date myDate = formatter.parse(str);
		    //Calendar c = Calendar.getInstance();
		    GregorianCalendar gc = new GregorianCalendar();
		    //c.setTime(myDate);
		    gc.setTime(myDate);
		    //int dayNum = c.get(Calendar.DAY_OF_WEEK);
		    //c.add(Calendar.MONTH, 8);
		    gc.add(5, -6);
		    myDate = gc.getTime();
		    System.out.println(formatter.format(myDate));
		    //System.out.println(dayNum);
		} catch (ParseException e1) {
		    e1.printStackTrace();
		}
	}
}


