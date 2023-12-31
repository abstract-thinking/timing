package com.example.timing.control.gi;

import com.example.timing.control.gi.domain.HarmonisedIndexOfConsumerPrices;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class InflationRatesIndicator implements Indicator {

    private final Map<YearMonth, HarmonisedIndexOfConsumerPrices> inflationRates;

    public InflationRatesIndicator(Map<YearMonth, HarmonisedIndexOfConsumerPrices> inflationRates) {
        this.inflationRates = new TreeMap<>(Comparator.reverseOrder());
        this.inflationRates.putAll(inflationRates);
    }

    @Override
    public PartialIndicatorResult indicate(final LocalDate date) {
        HarmonisedIndexOfConsumerPrices harmonisedIndexOfConsumerPrices = inflationRates.get(YearMonth.from(date));
        if (harmonisedIndexOfConsumerPrices.isEstimated()) {
            harmonisedIndexOfConsumerPrices = inflationRates.get(YearMonth.from(date.minusMonths(1)));
        }

        LocalDate comparativeDate = date.minusYears(1);
        HarmonisedIndexOfConsumerPrices comparativeharmonisedIndexOfConsumerPrices = inflationRates.get(YearMonth.from(comparativeDate));

        return PartialIndicatorResult.builder()
                .date(date)
                .rate(harmonisedIndexOfConsumerPrices.value())
                .comparativeDate(comparativeDate)
                .comparativeRate(comparativeharmonisedIndexOfConsumerPrices.value())
                .point(harmonisedIndexOfConsumerPrices.value() < comparativeharmonisedIndexOfConsumerPrices.value() ? 1 : 0)
                .build();
    }

}
