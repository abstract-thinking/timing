package com.example.timing.control.gi;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

class SeasonIndicator implements Indicator {

    private final static List<Month> FRIENDLY_MONTHS = new ArrayList<>(6);

    static {
        FRIENDLY_MONTHS.add(Month.NOVEMBER);
        FRIENDLY_MONTHS.add(Month.DECEMBER);
        FRIENDLY_MONTHS.add(Month.JANUARY);
        FRIENDLY_MONTHS.add(Month.FEBRUARY);
        FRIENDLY_MONTHS.add(Month.MARCH);
        FRIENDLY_MONTHS.add(Month.APRIL);
    }

    @Override
    public PartialIndicatorResult indicate(LocalDate date) {
        return PartialIndicatorResult.builder()
                .date(date)
                .rate(0)
                .comparativeDate(date)
                .comparativeRate(0)
                .point(isFriendly(date.getMonth()) ? 1 : 0)
                .build();
    }

    private boolean isFriendly(Month month) {
        return FRIENDLY_MONTHS.contains(month);
    }

}
