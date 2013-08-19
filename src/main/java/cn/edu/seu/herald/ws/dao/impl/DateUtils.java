package cn.edu.seu.herald.ws.dao.impl;

import java.util.Calendar;
import java.util.Date;

class DateUtils {

    public static int workdaysBetween(Date from, Date to) {
        Calendar fromCal = Calendar.getInstance();
        fromCal.setTime(from);
        Calendar toCal = Calendar.getInstance();
        toCal.setTime(to);
        int fromDayOfWeek = fromCal.get(Calendar.DAY_OF_WEEK);
        int toDayOfWeek = toCal.get(Calendar.DAY_OF_WEEK);

        if (fromDayOfWeek == toDayOfWeek) {
            return daysBetween(from.getTime(), to.getTime()) * 5 / 7;
        }

        int yesterdayOfFrom = dayOfYesterday(fromDayOfWeek);
        return isWorkday(yesterdayOfFrom)
                ? workdaysBetween(yesterday(from), to) - 1
                : workdaysBetween(yesterday(from), to);
    }

    public static int daysBetween(long from, long to) {
        return (int) (
                (to - from) / (1000 * 60 * 60 * 24)
        );
    }

    public static boolean isWorkday(int dayOfWeek) {
        return dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY;
    }

    public static int dayOfYesterday(int dayOfWeek) {
        return (Calendar.SUNDAY == dayOfWeek)
                ? Calendar.SATURDAY
                : dayOfWeek - 1;
    }

    public static Date yesterday(Date today) {
        long todayTime = today.getTime();
        long yesterdayTime = todayTime - 24 * 60 * 60 * 1000;
        return new Date(yesterdayTime);
    }
}
