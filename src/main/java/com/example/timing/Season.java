package com.example.timing;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class Season implements Point {

    private final static List<Month> FRIENDLY_MONTHS = new ArrayList<>(6);

    static {
        FRIENDLY_MONTHS.add(Month.NOVEMBER);
        FRIENDLY_MONTHS.add(Month.DECEMBER);
        FRIENDLY_MONTHS.add(Month.JANUARY);
        FRIENDLY_MONTHS.add(Month.FEBRUARY);
        FRIENDLY_MONTHS.add(Month.MARCH);
        FRIENDLY_MONTHS.add(Month.APRIL);
    }

    private final Month month;

    public Season(Month month) {
        this.month = month;
    }

    public String asString() {
        String m = month.toString();
        return m.substring(0, 1).toUpperCase() + m.substring(1).toLowerCase();
    }

    @Override
    public int getPoint() {
        return isFriendly() ? 1 : 0;
    }

    private boolean isFriendly() {
        return FRIENDLY_MONTHS.contains(month);
    }

}
