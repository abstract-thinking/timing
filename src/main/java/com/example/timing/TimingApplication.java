package com.example.timing;

import com.example.timing.data.GiRepository;
import com.example.timing.indicator.InterestRatesIndicator;
import com.example.timing.indicator.RatesIndicator;
import com.example.timing.indicator.SeasonIndicator;
import com.example.timing.results.IndicatorResult;
import com.example.timing.results.PartialIndicatorResult;
import com.example.timing.service.RatesService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.Month;
import java.time.YearMonth;

import static com.example.timing.utils.IndicatorResultHelper.createIndicatorResult;

@EnableScheduling
@SpringBootApplication
public class TimingApplication {

    public static void main(String[] args) {
        SpringApplication.run(TimingApplication.class, args);
    }

    @Bean
    public CommandLineRunner loadData(RatesService ratesService, GiRepository repository) {
        return args -> {
            SeasonIndicator seasonIndicator = new SeasonIndicator();
            InterestRatesIndicator interestRatesIndicator = new InterestRatesIndicator(ratesService.readInterestRates());
            RatesIndicator inflationRatesIndicator = new RatesIndicator(ratesService.readInflationRates());
            RatesIndicator exchangeRatesIndicator = new RatesIndicator(ratesService.readExchangeRates());

            YearMonth date = YearMonth.of(2000, Month.JANUARY);
            while (date.isBefore(YearMonth.now())) {
                YearMonth nextMonth = date.plusMonths(1);

                PartialIndicatorResult interestResult = interestRatesIndicator.indicate(nextMonth);
                PartialIndicatorResult seasonResult = seasonIndicator.indicate(nextMonth);
                PartialIndicatorResult exchangeResult = exchangeRatesIndicator.indicate(date);
                PartialIndicatorResult inflationResult = inflationRatesIndicator.indicate(date.minusMonths(1));

                IndicatorResult result = createIndicatorResult(nextMonth,
                        interestResult, seasonResult, exchangeResult, inflationResult, repository);
                repository.save(result);

                date = date.plusMonths(1);
            }
        };
    }


}
