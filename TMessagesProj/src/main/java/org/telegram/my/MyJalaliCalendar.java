package org.telegram.my;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Mostafa on 25/01/2016.
 */
public class MyJalaliCalendar extends Calendar {
    Calendar cBase = new GregorianCalendar();

    public MyJalaliCalendar() {
        cBase = new GregorianCalendar();
        _refresh();
    }

    public MyJalaliCalendar(TimeZone zone) {
        cBase = new GregorianCalendar(zone);
        _refresh();
    }

    public MyJalaliCalendar(Locale aLocale) {
        cBase = new GregorianCalendar(aLocale);
        _refresh();
    }

    public MyJalaliCalendar(TimeZone zone, Locale aLocale) {
        cBase = new GregorianCalendar(zone, aLocale);
        _refresh();
    }

    public MyJalaliCalendar(int year, int month, int dayOfMonth) {
        cBase = new GregorianCalendar(year, month, dayOfMonth);
        _refresh();
    }

    public MyJalaliCalendar(int year, int month, int dayOfMonth, int hourOfDay,
                            int minute) {
        cBase = new GregorianCalendar(year, month, dayOfMonth, hourOfDay, minute);
        _refresh();
    }

    public MyJalaliCalendar(int year, int month, int dayOfMonth, int hourOfDay,
                            int minute, int second) {
        cBase = new GregorianCalendar(year, month, dayOfMonth, hourOfDay, minute, second);
        _refresh();
    }

    private void _refresh() {
        YearMonthDate yearMonthDate = new YearMonthDate(cBase.get(YEAR), cBase.get(MONTH), cBase.get(DATE));
        yearMonthDate = gregorianToJalali(yearMonthDate);

        set(YEAR, yearMonthDate.getYear());
        set(MONTH, yearMonthDate.getMonth());
        set(DATE, yearMonthDate.getDate());

        set(HOUR_OF_DAY, cBase.get(HOUR_OF_DAY));
        set(HOUR, cBase.get(HOUR));
        set(MINUTE, cBase.get(MINUTE));
        set(SECOND, cBase.get(SECOND));
        set(MILLISECOND, cBase.get(MILLISECOND));
        set(AM_PM, cBase.get(AM_PM));
        set(ERA, cBase.get(ERA));
        set(ZONE_OFFSET, cBase.get(ZONE_OFFSET));
        set(DST_OFFSET, cBase.get(DST_OFFSET));
        set(DAY_OF_WEEK, cBase.get(DAY_OF_WEEK));

        complete();
    }

    @Override
    public void setTimeInMillis(long milliseconds) {
        cBase.setTimeInMillis(milliseconds);
        _refresh();
    }

    @Override
    public void setTimeZone(TimeZone timezone) {
        super.setTimeZone(timezone);
        if (cBase != null) {
            cBase.setTimeZone(timezone);
            _refresh();
        }
    }

    @Override
    public void add(int field, int value) {
        cBase.add(field, value);
        _refresh();
    }

    @Override
    protected void computeFields() {
    }

    @Override
    protected void computeTime() {
        time = cBase.getTimeInMillis();
    }

    @Override
    public int getGreatestMinimum(int field) {
        return MIN_VALUES[field];
    }

    @Override
    public int getLeastMaximum(int field) {
        return LEAST_MAX_VALUES[field];
    }

    @Override
    public int getMaximum(int field) {
        return MAX_VALUES[field];
    }

    @Override
    public int getMinimum(int field) {
        return MIN_VALUES[field];
    }

    @Override
    public void roll(int field, boolean increment) {
        cBase.roll(field, increment);
        _refresh();
    }

    private static final int ONE_SECOND = 1000;
    private static final int ONE_MINUTE = 60 * ONE_SECOND;
    private static final int ONE_HOUR = 60 * ONE_MINUTE;
    private static final long ONE_DAY = 24 * ONE_HOUR;
    static final int BCE = 0;
    static final int CE = 1;

    public final static int FARVARDIN = 0;
    public final static int ORDIBEHESHT = 1;
    public final static int KHORDAD = 2;
    public final static int TIR = 3;
    public final static int MORDAD = 4;
    public final static int SHAHRIVAR = 5;
    public final static int MEHR = 6;
    public final static int ABAN = 7;
    public final static int AZAR = 8;
    public final static int DEY = 9;
    public final static int BAHMAN = 10;
    public final static int ESFAND = 11;

    public static int gregorianDaysInMonth[] = {31, 28, 31, 30, 31,
            30, 31, 31, 30, 31, 30, 31};
    public static int jalaliDaysInMonth[] = {31, 31, 31, 31, 31, 31,
            30, 30, 30, 30, 30, 29};
    static final int MIN_VALUES[] = {
            BCE,        // ERA
            1,        // YEAR
            FARVARDIN,    // MONTH
            1,        // WEEK_OF_YEAR
            0,        // WEEK_OF_MONTH
            1,        // DAY_OF_MONTH
            1,        // DAY_OF_YEAR
            SATURDAY,        // DAY_OF_WEEK
            1,        // DAY_OF_WEEK_IN_MONTH
            AM,        // AM_PM
            0,        // HOUR
            0,        // HOUR_OF_DAY
            0,        // MINUTE
            0,        // SECOND
            0,        // MILLISECOND
            -13 * ONE_HOUR,    // ZONE_OFFSET (UNIX compatibility)
            0        // DST_OFFSET
    };

    static final int LEAST_MAX_VALUES[] = {
            CE,        // ERA
            292269054,    // YEAR
            ESFAND,    // MONTH
            52,        // WEEK_OF_YEAR
            4,        // WEEK_OF_MONTH
            28,        // DAY_OF_MONTH
            365,        // DAY_OF_YEAR
            FRIDAY,    // DAY_OF_WEEK
            4,        // DAY_OF_WEEK_IN
            PM,        // AM_PM
            11,        // HOUR
            23,        // HOUR_OF_DAY
            59,        // MINUTE
            59,        // SECOND
            999,        // MILLISECOND
            14 * ONE_HOUR,    // ZONE_OFFSET
            20 * ONE_MINUTE    // DST_OFFSET (historical least maximum)
    };

    static final int MAX_VALUES[] = {
            CE,        // ERA
            292278994,    // YEAR
            ESFAND,    // MONTH
            53,        // WEEK_OF_YEAR
            6,        // WEEK_OF_MONTH
            31,        // DAY_OF_MONTH
            366,        // DAY_OF_YEAR
            FRIDAY,    // DAY_OF_WEEK
            6,        // DAY_OF_WEEK_IN
            PM,        // AM_PM
            11,        // HOUR
            23,        // HOUR_OF_DAY
            59,        // MINUTE
            59,        // SECOND
            999,        // MILLISECOND
            14 * ONE_HOUR,    // ZONE_OFFSET
            2 * ONE_HOUR    // DST_OFFSET (double summer time)
    };

    public static YearMonthDate gregorianToJalali(YearMonthDate gregorian) {

        if (gregorian.getMonth() > 11 || gregorian.getMonth() < -11) {
            throw new IllegalArgumentException();
        }
        int jalaliYear;
        int jalaliMonth;
        int jalaliDay;

        int gregorianDayNo, jalaliDayNo;
        int jalaliNP;
        int i;

        gregorian.setYear(gregorian.getYear() - 1600);
        gregorian.setDate(gregorian.getDate() - 1);

        gregorianDayNo = 365 * gregorian.getYear() + (int) Math.floor((gregorian.getYear() + 3) / 4)
                - (int) Math.floor((gregorian.getYear() + 99) / 100)
                + (int) Math.floor((gregorian.getYear() + 399) / 400);
        for (i = 0; i < gregorian.getMonth(); ++i) {
            gregorianDayNo += gregorianDaysInMonth[i];
        }

        if (gregorian.getMonth() > 1 && ((gregorian.getYear() % 4 == 0 && gregorian.getYear() % 100 != 0)
                || (gregorian.getYear() % 400 == 0))) {
            ++gregorianDayNo;
        }

        gregorianDayNo += gregorian.getDate();

        jalaliDayNo = gregorianDayNo - 79;

        jalaliNP = (int) Math.floor(jalaliDayNo / 12053);
        jalaliDayNo = jalaliDayNo % 12053;

        jalaliYear = 979 + 33 * jalaliNP + 4 * (int) (jalaliDayNo / 1461);
        jalaliDayNo = jalaliDayNo % 1461;

        if (jalaliDayNo >= 366) {
            jalaliYear += (int) Math.floor((jalaliDayNo - 1) / 365);
            jalaliDayNo = (jalaliDayNo - 1) % 365;
        }

        for (i = 0; i < 11 && jalaliDayNo >= jalaliDaysInMonth[i]; ++i) {
            jalaliDayNo -= jalaliDaysInMonth[i];
        }
        jalaliMonth = i;
        jalaliDay = jalaliDayNo + 1;

        return new YearMonthDate(jalaliYear, jalaliMonth, jalaliDay);
    }

    public static YearMonthDate jalaliToGregorian(YearMonthDate jalali) {
        if (jalali.getMonth() > 11 || jalali.getMonth() < -11) {
            throw new IllegalArgumentException();
        }

        int gregorianYear;
        int gregorianMonth;
        int gregorianDay;

        int gregorianDayNo, jalaliDayNo;
        int leap;

        int i;
        jalali.setYear(jalali.getYear() - 979);
        jalali.setDate(jalali.getDate() - 1);

        jalaliDayNo = 365 * jalali.getYear() + (int) (jalali.getYear() / 33) * 8
                + (int) Math.floor(((jalali.getYear() % 33) + 3) / 4);
        for (i = 0; i < jalali.getMonth(); ++i) {
            jalaliDayNo += jalaliDaysInMonth[i];
        }

        jalaliDayNo += jalali.getDate();

        gregorianDayNo = jalaliDayNo + 79;

        gregorianYear = 1600 + 400 * (int) Math.floor(gregorianDayNo / 146097); /* 146097 = 365*400 + 400/4 - 400/100 + 400/400 */
        gregorianDayNo = gregorianDayNo % 146097;

        leap = 1;
        if (gregorianDayNo >= 36525) /* 36525 = 365*100 + 100/4 */ {
            gregorianDayNo--;
            gregorianYear += 100 * (int) Math.floor(gregorianDayNo / 36524); /* 36524 = 365*100 + 100/4 - 100/100 */
            gregorianDayNo = gregorianDayNo % 36524;

            if (gregorianDayNo >= 365) {
                gregorianDayNo++;
            } else {
                leap = 0;
            }
        }

        gregorianYear += 4 * (int) Math.floor(gregorianDayNo / 1461); /* 1461 = 365*4 + 4/4 */
        gregorianDayNo = gregorianDayNo % 1461;

        if (gregorianDayNo >= 366) {
            leap = 0;

            gregorianDayNo--;
            gregorianYear += (int) Math.floor(gregorianDayNo / 365);
            gregorianDayNo = gregorianDayNo % 365;
        }

        for (i = 0; gregorianDayNo >= gregorianDaysInMonth[i] + ((i == 1 && leap == 1) ? i : 0); i++) {
            gregorianDayNo -= gregorianDaysInMonth[i] + ((i == 1 && leap == 1) ? i : 0);
        }
        gregorianMonth = i;
        gregorianDay = gregorianDayNo + 1;

        return new YearMonthDate(gregorianYear, gregorianMonth, gregorianDay);

    }

    public static class YearMonthDate {

        public YearMonthDate(int year, int month, int date) {
            this.year = year;
            this.month = month;
            this.date = date;
        }

        private int year;
        private int month;
        private int date;

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getDate() {
            return date;
        }

        public void setDate(int date) {
            this.date = date;
        }

        public String toString() {
            return getYear() + "/" + getMonth() + "/" + getDate();
        }
    }

    public static boolean jalai_IsLeepYear(int year) {
        //Algorithm from www.wikipedia.com
        if ((year % 33 == 1 || year % 33 == 5 || year % 33 == 9 || year % 33 == 13 ||
                year % 33 == 17 || year % 33 == 22 || year % 33 == 26 || year % 33 == 30)) {
            return true;
        } else return false;
    }

    public static int jalai_WeekOfYear(int dayOfYear, int year) {
        switch (jalai_DayOfWeek(jalaliToGregorian(new YearMonthDate(year, 0, 1)))) {
            case 2:
                dayOfYear++;
                break;
            case 3:
                dayOfYear += 2;
                break;
            case 4:
                dayOfYear += 3;
                break;
            case 5:
                dayOfYear += 4;
                break;
            case 6:
                dayOfYear += 5;
                break;
            case 7:
                dayOfYear--;
                break;
        }
        ;
        dayOfYear = (int) Math.floor(dayOfYear / 7);
        return dayOfYear + 1;
    }

    public static int jalai_DayOfWeek(YearMonthDate yearMonthDate) {

        Calendar cal = new GregorianCalendar(yearMonthDate.getYear(), yearMonthDate.getMonth(), yearMonthDate.getDate());
        return cal.get(DAY_OF_WEEK);

    }

    public void jalai_Set(int field, int value) {
        YearMonthDate jalaiBase = gregorianToJalali(new YearMonthDate(cBase.get(YEAR), cBase.get(MONTH), cBase.get(DATE)));
        switch (field) {
            case DATE: {
                jalaiBase.date = value;
                YearMonthDate convertedToBase = jalaliToGregorian(jalaiBase);
                cBase.set(DATE, convertedToBase.date);
                _refresh();
                break;
            }
            case MONTH: {
                jalaiBase.month = value;
                YearMonthDate convertedToBase = jalaliToGregorian(jalaiBase);
                cBase.set(MONTH, convertedToBase.month);
                _refresh();
                break;
            }
            case YEAR: {
                jalaiBase.year = value;
                YearMonthDate convertedToBase = jalaliToGregorian(jalaiBase);
                cBase.set(YEAR, convertedToBase.year);
                _refresh();
                break;
            }
            case DAY_OF_YEAR: {
                break;
            }
            case WEEK_OF_YEAR: {
                break;
            }
            case WEEK_OF_MONTH: {
                break;
            }
            case DAY_OF_WEEK: {
                break;
            }
            case HOUR_OF_DAY:
            case HOUR:
            case MINUTE:
            case SECOND:
            case MILLISECOND:
            case ZONE_OFFSET:
            case DST_OFFSET:
            default: {
                cBase.set(field, value);
                _refresh();
                break;
            }
        }
    }

}
