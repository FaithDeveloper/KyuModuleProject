package com.iscreammedia.kyuutilslibrary;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils {
    private DateUtils(){

    }

    private static final String TAG = DateUtils.class.getSimpleName();

    public static String getTodayDateStr() {
        Calendar c = GregorianCalendar.getInstance();

        c.setTime(new Date(System.currentTimeMillis()));

        c.set(GregorianCalendar.HOUR_OF_DAY, 1);
        c.set(GregorianCalendar.MINUTE, 0);
        c.set(GregorianCalendar.SECOND, 0);

        Date todayDate = c.getTime();

        String format = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(todayDate);
    }

//    public static long getTodayStartTime() {
//        Calendar c = GregorianCalendar.getInstance();
//
//        c.setTime(new Date(System.currentTimeMillis()));
//
//        c.set(GregorianCalendar.HOUR_OF_DAY, 0);
//        c.set(GregorianCalendar.MINUTE, 0);
//        c.set(GregorianCalendar.SECOND, 0);
//
//        Date todayDate = c.getTime();
//
//        String format = "yyyy-MM-dd HH:mm";
//
//        SimpleDateFormat sdf = new SimpleDateFormat(format);
//        Log.v(TAG, "today start time : " + sdf.format(todayDate));
//
//        return todayDate.getTime();
//    }

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public static String getFormattedYearEndDate(Date date){
        Calendar c = GregorianCalendar.getInstance();
        c.setTime(date);
        c.set(GregorianCalendar.MONTH, c.getActualMaximum(GregorianCalendar.MONTH));
        c.set(GregorianCalendar.DATE, c.getActualMaximum(GregorianCalendar.DATE));

        return SIMPLE_DATE_FORMAT.format(c.getTime());
    }

    public static String getFormattedYearStartDate(Date date) {
        Calendar c = GregorianCalendar.getInstance();
        c.setTime(date);
        c.set(GregorianCalendar.MONTH, c.getActualMinimum(GregorianCalendar.MONTH));
        c.set(GregorianCalendar.DATE, c.getActualMinimum(GregorianCalendar.DATE));

        return SIMPLE_DATE_FORMAT.format(c.getTime());
    }

    public static String getFormattedMonthStartDate(Date date) {
        Calendar c = GregorianCalendar.getInstance();
        c.setTime(date);
        c.set(GregorianCalendar.DATE, 1);

        return SIMPLE_DATE_FORMAT.format(c.getTime());
    }

    public static String getFormattedMonthStartDateByWeek(Date date) {
        Calendar c = GregorianCalendar.getInstance();
        c.setTime(date);
        c.set(GregorianCalendar.WEEK_OF_MONTH, 1);
        c.set(GregorianCalendar.DAY_OF_WEEK, c.getActualMinimum(GregorianCalendar.DAY_OF_WEEK));

        return SIMPLE_DATE_FORMAT.format(c.getTime());
    }

    public static String getFormattedMonthEndDate(Date date) {
        Calendar c = GregorianCalendar.getInstance();
        c.setTime(date);
        c.set(GregorianCalendar.DATE, c.getActualMaximum(Calendar.DATE));

        return SIMPLE_DATE_FORMAT.format(c.getTime());
    }

    public static String getFormattedMonthEndDateByWeek(Date date) {
        Calendar c = GregorianCalendar.getInstance();
        c.setTime(date);
        c.set(GregorianCalendar.WEEK_OF_MONTH, c.getActualMaximum(GregorianCalendar.WEEK_OF_MONTH));
        c.set(GregorianCalendar.DAY_OF_WEEK, c.getActualMaximum(GregorianCalendar.DAY_OF_WEEK));

        return SIMPLE_DATE_FORMAT.format(c.getTime());
    }

    public static String getFormattedTodayDate() {
        Calendar c = GregorianCalendar.getInstance();
        c.setTime(new Date(System.currentTimeMillis()));

        return SIMPLE_DATE_FORMAT.format(c.getTime());
    }

    public static boolean isThisMonth(Date date) {
        Calendar c = GregorianCalendar.getInstance();
        c.setTime(date);
        int year = c.get(GregorianCalendar.YEAR);
        int month = c.get(GregorianCalendar.MONTH);

        c.setTime(new Date(System.currentTimeMillis()));

        int curYear = c.get(GregorianCalendar.YEAR);
        int curMonth = c.get(GregorianCalendar.MONTH);

        return year == curYear && month == curMonth;
    }


    public static String getFormattedBeforeMonthStartDate(Date date) {
        Calendar c = GregorianCalendar.getInstance();
        c.setTime(date);
        c.add(c.MONTH, -1);
        c.set(GregorianCalendar.DATE, c.getActualMinimum(Calendar.DATE));

        return SIMPLE_DATE_FORMAT.format(c.getTime());
    }

    public static String getFormattedNextMonthEndDate(Date date) {
        Calendar c = GregorianCalendar.getInstance();
        c.setTime(date);
        c.add(c.MONTH, +1);
        c.set(GregorianCalendar.DATE, c.getActualMaximum(Calendar.DATE));

        return SIMPLE_DATE_FORMAT.format(c.getTime());
    }
    public static Calendar getFormattedBeforeMonthEndDateToCalendar(Date date, int index) {
        Calendar c = GregorianCalendar.getInstance();
        c.setTime(date);
        c.add(c.MONTH, -1);
        c.set(GregorianCalendar.DATE, c.getActualMaximum(Calendar.DATE) - index);

        return c;
    }

    public static Calendar getFormattedNextMonthStartDateToCalendar(Date date, int index) {
        Calendar c = GregorianCalendar.getInstance();
        c.setTime(date);
        c.add(c.MONTH, +1);
//        c.set(GregorianCalendar.DATE, c.getActualMinimum(Calendar.DATE) );
        c.set(GregorianCalendar.DATE, 1 + index);
//        c.set(GregorianCalendar.DATE, c.getActualMinimum(Calendar.DATE) + index);

        return c;
    }

    public static boolean isSameMothDate(Date target, Date date){
        Calendar cDate = GregorianCalendar.getInstance();
        cDate.setTime(date);

        Calendar cTarget = GregorianCalendar.getInstance();
        cTarget.setTime(target);

        return  (cDate.get(Calendar.MONTH) == cTarget.get(Calendar.MONTH));
    }
}
