package com.example.timing;

import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class Season implements Calculator {

    private final static List<Month> FRIENDLY_MONTHS = new ArrayList<>(6);

    static {
        FRIENDLY_MONTHS.add(Month.NOVEMBER);
        FRIENDLY_MONTHS.add(Month.DECEMBER);
        FRIENDLY_MONTHS.add(Month.JANUARY);
        FRIENDLY_MONTHS.add(Month.FEBRUARY);
        FRIENDLY_MONTHS.add(Month.MARCH);
        FRIENDLY_MONTHS.add(Month.APRIL);
    }

//    public String asString() {
//        String m = month.toString();
//        return m.substring(0, 1).toUpperCase() + m.substring(1).toLowerCase();
//    }

    @Override
    public PartialIndicatorResult calculate(YearMonth yearMonth) {
        return PartialIndicatorResult.builder()
                .date(yearMonth)
                .rate(0)
                .comparativeDate(yearMonth)
                .comparativeRate(0)
                .point(isFriendly(yearMonth.getMonth()) ? 1 : 0)
                .build();
    }

    private boolean isFriendly(Month month) {
        return FRIENDLY_MONTHS.contains(month);
    }

}
