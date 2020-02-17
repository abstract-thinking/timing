package com.example.timing;

import com.example.timing.service.QuotesService;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class QuotesServiceTests {

    @Test
    public void testMe() {
        QuotesService service = new QuotesService();

        service.fetchAll();
    }

    @Test
    public void test() {
        LocalDate date = LocalDate.now();
        WeekFields weekFields = WeekFields.of(Locale.getDefault());

        int weekNumber = date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
        System.out.println(weekNumber);

        int weekNumber1 = date.get(weekFields.weekOfWeekBasedYear());
        System.out.println(weekNumber1);

        for (int i = 0; i < 7; ++i) {
            System.out.println(date.plusDays(i));
            System.out.println(date.plusDays(i).get(IsoFields.WEEK_OF_WEEK_BASED_YEAR));
            System.out.println(date.plusDays(i).get(weekFields.weekOfWeekBasedYear()));
        }

        for (int i = 0; i < 7; ++i) {
            System.out.println(date.minusDays(i));
            System.out.println(date.minusDays(i).get(IsoFields.WEEK_OF_WEEK_BASED_YEAR));
            System.out.println(date.minusDays(i).get(weekFields.weekOfWeekBasedYear()));
        }

    }

    @Test
    public void testTime() {
        LocalDate now = LocalDate.now();
        TemporalField fieldISO = WeekFields.of(Locale.FRANCE).dayOfWeek();
        System.out.println(now.with(fieldISO, 1)); // 2015-02-09 (Monday)
        System.out.println(now.with(fieldISO, 7)); // 2015-02-09 (Monday)


        TemporalField fieldUS = WeekFields.of(Locale.US).dayOfWeek();
        System.out.println(now.with(fieldUS, 1)); // 2015-02-08 (Sunday)
        System.out.println(now.with(fieldUS, 7)); // 2015-02-08 (Sunday)
    }
}
