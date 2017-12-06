package com.figengungor.moviesnowplaying;

import org.junit.Test;
import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.temporal.TemporalAdjusters;

import static org.junit.Assert.assertEquals;
import static org.threeten.bp.temporal.ChronoUnit.HOURS;
import static org.threeten.bp.temporal.ChronoUnit.MINUTES;
import static org.threeten.bp.temporal.ChronoUnit.SECONDS;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void dateTimeInterval_isCorrect() throws Exception {

        //LocalDate nowDate = LocalDate.now();
        //LocalDateTime nowTime = LocalDateTime.now();

        LocalDate theDayDate = LocalDate.of(2017, 12, 4);
        LocalDateTime theDayDateTime = theDayDate.atTime(11,0);

        LocalDate nextMondayDate = theDayDate.with(
                TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));
        LocalDateTime nextMondayDateTime = nextMondayDate.atTime(12, 30);

        int nextMondayYear = nextMondayDate.getYear();
        int nextMondayMonth = nextMondayDate.getMonthValue();
        int nextMondayDayOfMonth = nextMondayDate.getDayOfMonth();

        System.out.println("nextMondayYear: " + nextMondayYear);
        System.out.println("nextMondayMonth: " + nextMondayMonth);
        System.out.println("nextMondayDayOfMonth: " + nextMondayDayOfMonth);

        assertEquals("nextMondayYear", nextMondayYear, 2017);
        assertEquals("nextMondayMonth", nextMondayMonth, 12);
        assertEquals("nextMondayDayOfMonth", nextMondayDayOfMonth, 4);


        long hours = HOURS.between(theDayDateTime, nextMondayDateTime);
        long minutes = MINUTES.between(theDayDateTime, nextMondayDateTime);
        long seconds = SECONDS.between(theDayDateTime, nextMondayDateTime);

        System.out.println("hours between: " + hours);
        System.out.println("minutes between: " + minutes);
        System.out.println("seconds between: " + seconds);

        assertEquals("hours between", hours, 1);
        assertEquals("minutes between", minutes, 90);
        assertEquals("seconds between", seconds, 90*60);

    }
}