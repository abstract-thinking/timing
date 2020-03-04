package com.example.timing.control.rsl;

import com.example.timing.boundary.rsl.RslResult;
import com.example.timing.services.quotes.HistoryQuote;
import com.example.timing.services.quotes.QuotesService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.example.timing.control.rsl.Indices.DAX;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class RslCalculatorTests {

    @Mock
    private QuotesService service;

    @InjectMocks
    private RslCalculator calculator;

    @Test
    public void shouldCalculateAll() {
        when(service.fetchQuotes(any(), any(), any())).thenReturn(createQuotes());

        List<RslResult> result = calculator.calculate();

        assertThat(result).containsExactlyElementsOf(createExpectedQuotes());
    }


    @Test
    public void shouldCalculateIndex() {
        when(service.fetchQuotes(any(), any(), any())).thenReturn(createQuotes());

        List<RslResult> result = calculator.calculate("DAX");

        assertThat(result).containsExactlyElementsOf(createExpectedQuotes());
    }

    @Test
    public void shouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> calculator.calculate("BLAH"));
    }

    private List<HistoryQuote> createQuotes() {
        List<HistoryQuote> quotes = new ArrayList<>();
        quotes.add(new HistoryQuote(DAX, LocalDate.parse("2020-03-04"), BigDecimal.valueOf(12167.769531)));
        quotes.add(new HistoryQuote(DAX, LocalDate.parse("2020-03-02"), BigDecimal.valueOf(11985.389648)));
        quotes.add(new HistoryQuote(DAX, LocalDate.parse("2020-02-24"), BigDecimal.valueOf(11890.349609)));
        quotes.add(new HistoryQuote(DAX, LocalDate.parse("2020-02-17"), BigDecimal.valueOf(13579.330078)));
        quotes.add(new HistoryQuote(DAX, LocalDate.parse("2020-02-10"), BigDecimal.valueOf(13744.209961)));
        quotes.add(new HistoryQuote(DAX, LocalDate.parse("2020-02-03"), BigDecimal.valueOf(13513.80957)));
        quotes.add(new HistoryQuote(DAX, LocalDate.parse("2020-01-27"), BigDecimal.valueOf(12981.969727)));
        quotes.add(new HistoryQuote(DAX, LocalDate.parse("2020-01-20"), BigDecimal.valueOf(13576.679688)));
        quotes.add(new HistoryQuote(DAX, LocalDate.parse("2020-01-13"), BigDecimal.valueOf(13526.129883)));
        quotes.add(new HistoryQuote(DAX, LocalDate.parse("2020-01-06"), BigDecimal.valueOf(13483.30957)));
        quotes.add(new HistoryQuote(DAX, LocalDate.parse("2019-12-30"), BigDecimal.valueOf(13219.139648)));
        quotes.add(new HistoryQuote(DAX, LocalDate.parse("2019-12-23"), BigDecimal.valueOf(13337.110352)));
        quotes.add(new HistoryQuote(DAX, LocalDate.parse("2019-12-16"), BigDecimal.valueOf(13318.900391)));
        quotes.add(new HistoryQuote(DAX, LocalDate.parse("2019-12-09"), BigDecimal.valueOf(13282.719727)));
        quotes.add(new HistoryQuote(DAX, LocalDate.parse("2019-12-02"), BigDecimal.valueOf(13166.580078)));
        quotes.add(new HistoryQuote(DAX, LocalDate.parse("2019-11-25"), BigDecimal.valueOf(13236.379883)));
        quotes.add(new HistoryQuote(DAX, LocalDate.parse("2019-11-18"), BigDecimal.valueOf(13163.879883)));
        quotes.add(new HistoryQuote(DAX, LocalDate.parse("2019-11-11"), BigDecimal.valueOf(13241.75)));
        quotes.add(new HistoryQuote(DAX, LocalDate.parse("2019-11-04"), BigDecimal.valueOf(13228.55957)));
        quotes.add(new HistoryQuote(DAX, LocalDate.parse("2019-10-28"), BigDecimal.valueOf(12961.049805)));
        quotes.add(new HistoryQuote(DAX, LocalDate.parse("2019-10-21"), BigDecimal.valueOf(12894.509766)));
        quotes.add(new HistoryQuote(DAX, LocalDate.parse("2019-10-14"), BigDecimal.valueOf(12633.599609)));
        quotes.add(new HistoryQuote(DAX, LocalDate.parse("2019-10-07"), BigDecimal.valueOf(12511.650391)));
        quotes.add(new HistoryQuote(DAX, LocalDate.parse("2019-09-30"), BigDecimal.valueOf(12012.80957)));
        quotes.add(new HistoryQuote(DAX, LocalDate.parse("2019-09-23"), BigDecimal.valueOf(12380.94043)));
        quotes.add(new HistoryQuote(DAX, LocalDate.parse("2019-09-16"), BigDecimal.valueOf(12468.009766)));
        quotes.add(new HistoryQuote(DAX, LocalDate.parse("2019-09-09"), BigDecimal.valueOf(12468.530273)));
        quotes.add(new HistoryQuote(DAX, LocalDate.parse("2019-09-02"), BigDecimal.valueOf(12191.730469)));
        quotes.add(new HistoryQuote(DAX, LocalDate.parse("2019-08-26"), BigDecimal.valueOf(11939.280273)));
        quotes.add(new HistoryQuote(DAX, LocalDate.parse("2019-08-19"), BigDecimal.valueOf(11611.509766)));
        quotes.add(new HistoryQuote(DAX, LocalDate.parse("2019-08-12"), BigDecimal.valueOf(11562.740234)));
        quotes.add(new HistoryQuote(DAX, LocalDate.parse("2019-08-05"), BigDecimal.valueOf(11693.799805)));
        quotes.add(new HistoryQuote(DAX, LocalDate.parse("2019-07-29"), BigDecimal.valueOf(11872.44043)));
        quotes.add(new HistoryQuote(DAX, LocalDate.parse("2019-07-22"), BigDecimal.valueOf(12419.900391)));
        quotes.add(new HistoryQuote(DAX, LocalDate.parse("2019-07-15"), BigDecimal.valueOf(12260.070313)));
        quotes.add(new HistoryQuote(DAX, LocalDate.parse("2019-07-08"), BigDecimal.valueOf(12323.320313)));
        quotes.add(new HistoryQuote(DAX, LocalDate.parse("2019-07-01"), BigDecimal.valueOf(12568.530273)));
        quotes.add(new HistoryQuote(DAX, LocalDate.parse("2019-06-24"), BigDecimal.valueOf(12398.799805)));
        quotes.add(new HistoryQuote(DAX, LocalDate.parse("2019-06-17"), BigDecimal.valueOf(12339.919922)));
        quotes.add(new HistoryQuote(DAX, LocalDate.parse("2019-06-10"), BigDecimal.valueOf(12096.400391)));
        quotes.add(new HistoryQuote(DAX, LocalDate.parse("2019-06-03"), BigDecimal.valueOf(12045.379883)));
        quotes.add(new HistoryQuote(DAX, LocalDate.parse("2019-05-27"), BigDecimal.valueOf(11726.839844)));
        quotes.add(new HistoryQuote(DAX, LocalDate.parse("2019-05-20"), BigDecimal.valueOf(12011.040039)));
        quotes.add(new HistoryQuote(DAX, LocalDate.parse("2019-05-13"), BigDecimal.valueOf(12238.94043)));
        quotes.add(new HistoryQuote(DAX, LocalDate.parse("2019-05-06"), BigDecimal.valueOf(12059.830078)));
        quotes.add(new HistoryQuote(DAX, LocalDate.parse("2019-04-29"), BigDecimal.valueOf(12412.75)));
        quotes.add(new HistoryQuote(DAX, LocalDate.parse("2019-04-22"), BigDecimal.valueOf(12315.179688)));
        quotes.add(new HistoryQuote(DAX, LocalDate.parse("2019-04-15"), BigDecimal.valueOf(12222.389648)));
        quotes.add(new HistoryQuote(DAX, LocalDate.parse("2019-04-08"), BigDecimal.valueOf(11999.929688)));
        quotes.add(new HistoryQuote(DAX, LocalDate.parse("2019-04-01"), BigDecimal.valueOf(12009.75)));
        quotes.add(new HistoryQuote(DAX, LocalDate.parse("2019-03-25"), BigDecimal.valueOf(11526.040039)));
        quotes.add(new HistoryQuote(DAX, LocalDate.parse("2019-03-18"), BigDecimal.valueOf(11364.169922)));
        quotes.add(new HistoryQuote(DAX, LocalDate.parse("2019-03-11"), BigDecimal.valueOf(11685.69043)));
        quotes.add(new HistoryQuote(DAX, LocalDate.parse("2019-03-04"), BigDecimal.valueOf(11457.839844)));

        return quotes;
    }

    private List<RslResult> createExpectedQuotes() {
        List<RslResult> expected = new ArrayList<>();
        // That's odd. Yahoo delivers two results. The first for Monday is okay
        // (Mo, 02.03.2020) 0,924589770865324 + (Mi, 04.03.2020) 0,93872338023915 = 0,931656575552237
        expected.add(new RslResult(LocalDate.parse("2020-03-01"), LocalDate.parse("2020-03-07"), 0.9316565755522372));
        expected.add(new RslResult(LocalDate.parse("2020-02-23"), LocalDate.parse("2020-02-29"), 0.9173789471603189));
        expected.add(new RslResult(LocalDate.parse("2020-02-16"), LocalDate.parse("2020-02-22"), 1.0485247121258938));
        expected.add(new RslResult(LocalDate.parse("2020-02-09"), LocalDate.parse("2020-02-15"), 1.0674116952859145));
        expected.add(new RslResult(LocalDate.parse("2020-02-02"), LocalDate.parse("2020-02-08"), 1.055744750273663));
        expected.add(new RslResult(LocalDate.parse("2020-01-26"), LocalDate.parse("2020-02-01"), 1.0190352511047764));
        expected.add(new RslResult(LocalDate.parse("2020-01-19"), LocalDate.parse("2020-01-25"), 1.0674620453893393));
        expected.add(new RslResult(LocalDate.parse("2020-01-12"), LocalDate.parse("2020-01-18"), 1.0675806871320077));
        expected.add(new RslResult(LocalDate.parse("2020-01-05"), LocalDate.parse("2020-01-11"), 1.0679560281083795));
        expected.add(new RslResult(LocalDate.parse("2019-12-29"), LocalDate.parse("2020-01-04"), 1.0498495659396971));
        expected.add(new RslResult(LocalDate.parse("2019-12-22"), LocalDate.parse("2019-12-28"), 1.0617807266516448));
        expected.add(new RslResult(LocalDate.parse("2019-12-15"), LocalDate.parse("2019-12-21"), 1.063457868237124));
        expected.add(new RslResult(LocalDate.parse("2019-12-08"), LocalDate.parse("2019-12-14"), 1.0644171175790862));
        expected.add(new RslResult(LocalDate.parse("2019-12-01"), LocalDate.parse("2019-12-07"), 1.0589992809117383));
        expected.add(new RslResult(LocalDate.parse("2019-11-24"), LocalDate.parse("2019-11-30"), 1.0691989961888597));
        expected.add(new RslResult(LocalDate.parse("2019-11-17"), LocalDate.parse("2019-11-23"), 1.0672551060962125));
        expected.add(new RslResult(LocalDate.parse("2019-11-10"), LocalDate.parse("2019-11-16"), 1.076558389519669));
        expected.add(new RslResult(LocalDate.parse("2019-11-03"), LocalDate.parse("2019-11-09"), 1.079327227404139));
        expected.add(new RslResult(LocalDate.parse("2019-10-27"), LocalDate.parse("2019-11-02"), 1.0601143975625658));
        expected.add(new RslResult(LocalDate.parse("2019-10-20"), LocalDate.parse("2019-10-26"), 1.0567395111823557));
        expected.add(new RslResult(LocalDate.parse("2019-10-13"), LocalDate.parse("2019-10-19"), 1.0374737518216721));
        expected.add(new RslResult(LocalDate.parse("2019-10-06"), LocalDate.parse("2019-10-12"), 1.0294433035581252));
        expected.add(new RslResult(LocalDate.parse("2019-09-29"), LocalDate.parse("2019-10-05"), 0.9899133354226591));
        expected.add(new RslResult(LocalDate.parse("2019-09-22"), LocalDate.parse("2019-09-28"), 1.0217670635796694));
        expected.add(new RslResult(LocalDate.parse("2019-09-15"), LocalDate.parse("2019-09-21"), 1.0321604517888392));
        expected.add(new RslResult(LocalDate.parse("2019-09-08"), LocalDate.parse("2019-09-14"), 1.03468540995846));

        return expected;
    }
}
