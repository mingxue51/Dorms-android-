package helper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import entity.Generic.Language;
import entity.Property.Search.SearchProperty;

public class DT {

    public static int getCurrentYear() {
        Calendar startTime = Calendar.getInstance();
        return startTime.get(Calendar.YEAR);
    }

    public static int getCurrentMonth() {
        Calendar startTime = Calendar.getInstance();
        return startTime.get(Calendar.MONTH);
    }

    public static int getCurrentDofMonth() {
        Calendar startTime = Calendar.getInstance();
        return startTime.get(Calendar.DAY_OF_MONTH);
    }

    public static String getClassName(String className) {
        return  String.valueOf(className);
    }

    public static String formatDay(int pDay) {
        return String.format("%02d", pDay);
    }

    public static String formatDay(Date pDate) {
        int day=0;
        if(pDate != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(pDate);
            day = cal.get(Calendar.DAY_OF_MONTH);
        }
        return String.format("%02d", day);
    }

    public static String formateDate(int pYear, int pMonth) {
        return DT.getMonthByNumber(pMonth)+"."+" "+pYear;
    }

    public static String formateDate(Date pDate) {
        int year=0;
        int month=0;
        if(pDate != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(pDate);
            month = cal.get(Calendar.MONTH);
            year = cal.get(Calendar.YEAR);
        }
        String monthByNumber = DT.getMonthByNumber(month);
        return DT.getMonthByNumber(month);
    }

    public static String formateWeekDate(int year, int month, int day) {
        String input_date=String.valueOf(day+"/"+month+"/"+year);
        SimpleDateFormat format1=new SimpleDateFormat("dd/MM/yyyy");
        Date dt1= null;
        try {
            dt1 = format1.parse(input_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat format2=new SimpleDateFormat("EEEE");
        return  format2.format(dt1);
    }

    public static String formateWeekDate(Date pDate) {
        String result="-SUNDAY-";
        if(pDate != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(pDate);
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
            result = dayFormat.format(cal.getTime());
        }
        return  result;
    }

    public static String getMonthByNumber(int month) {
        SimpleDateFormat formatter = new SimpleDateFormat("MMM", Locale.getDefault());
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(Calendar.MONTH, month);
        String format = formatter.format(calendar.getTime());
        return format;
    }

    public static String getDateForAPI(Date pDate) {
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
        String strDt=null;
        try {
            strDt = simpleDate.format(pDate);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return strDt;
    }

    public static Date getDateFromString(String dateStr) {
        Date date = new Date();
        SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = format.parse(dateStr);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date;
    }


	/*public static String formateDate(int year, int month, int day){
		String strYear, strMonth, strDay;
        strYear = ("" + year);

        if((month+1) < 9){
            strMonth = ("0" + (month+1));
        }else {
            strMonth = (""+(month+1));
        }

        if(day < 10){
            strDay  = ("0" + day) ;
        }else {
            strDay = (""+day);
        }
        StringBuilder s = new StringBuilder().append(strYear).append("-").append(strMonth).append("-").append(strDay);
        return s.toString();
	}*/

   /* public static String buildDepartureDate(int year, int month, int day){

        if (month < 12) {
            month = month + 1;
        }else {
            month = 1;
        }
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
            if ((day) == 31) {
                if (month == 12) {
                    month = 1;
                    year = year + 1;
                }else {
                    month = month + 1;
                }
                day = 1;
            }else {
                day = day + 1;
            }

        }else if (month == 2) {
            if (year % 4 == 0) {
                if ((day) == 29) {
                    month = month + 1;
                    day = 1;
                }else {
                    day = day + 1;
                }
            }else {
                if ((day) == 28) {
                    month = month + 1;
                    day = 1;
                }else {
                    day = day + 1;
                }
            }
        }else {
            if ((day) == 30) {
                month = month + 1;
                day = 1;
            }else {
                day = day + 1;
            }
        }

        String strYear, strMonth, strDay;
        if((month) < 9){
            strMonth = ("0" + (month));
        }else {
            strMonth = (""+(month));
        }

        if(day < 10){
            strDay  = ("0" + day) ;
        }else {
            strDay = (""+day);
        }
        strYear = "" + year;
        StringBuilder s = new StringBuilder().append(strYear).append("-").append(strMonth).append("-").append(strDay);
        return s.toString();
    }*/

    public static boolean isDateInThePast(Date paramDate) {
        Calendar localCalendar1 = Calendar.getInstance();
        resetCalendar(localCalendar1);
        Calendar localCalendar2 = Calendar.getInstance();
        localCalendar2.setTime(paramDate);
        return localCalendar2.before(localCalendar1);
    }

    public static int calculateNumberOfNights(Date paramDate1, Date paramDate2) {
        int i = 0;
        Calendar localCalendar1 = Calendar.getInstance();
        Calendar localCalendar2 = Calendar.getInstance();

        localCalendar1.setTime(paramDate1);
        localCalendar2.setTime(paramDate2);

        resetCalendar(localCalendar1);
        resetCalendar(localCalendar2);

        while (localCalendar1.before(localCalendar2)) {
            localCalendar1.add(Calendar.DAY_OF_MONTH, 1);
            i++;
        }
        return i;
    }

    public static void resetCalendar(Calendar paramCalendar) {
        paramCalendar.set(Calendar.HOUR_OF_DAY, 0);
        paramCalendar.set(Calendar.MINUTE, 0);
        paramCalendar.set(Calendar.SECOND, 0);
        paramCalendar.set(Calendar.MILLISECOND, 0);
    }

    public static Date getDefaultCheckIn() {
        return getCheckIn(2);
    }

    public static Date getCheckIn(int paramInt) {
        Calendar localCalendar = Calendar.getInstance();
        localCalendar.add(Calendar.DAY_OF_MONTH, paramInt);
        return localCalendar.getTime();
    }

    public static Date getDefaultCheckOut() {
        return getCheckOut(2, SearchProperty.DEFAULT_NUMBER_OF_NIGHTS);
    }

    public static Date getCheckOut(int paramInt1, int paramInt2) {
        Calendar localCalendar = Calendar.getInstance();
        localCalendar.add(Calendar.DAY_OF_MONTH, paramInt1 + paramInt2);
        return localCalendar.getTime();
    }
}
