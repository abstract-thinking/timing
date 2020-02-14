package com.example.timing;

import com.example.timing.data.GiRepository;
import com.example.timing.data.IndicatorResult;
import com.example.timing.web.RatesService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Month;
import java.time.YearMonth;

@SpringBootApplication
public class TimingApplication {

	public static void main(String[] args) {
		SpringApplication.run(TimingApplication.class, args);
	}

	@Bean
	public CommandLineRunner dataLoader(RatesService ratesService, GiRepository repository) {
		return args -> {
			Season season = new Season();
			InterestRates interestRates = new InterestRates(ratesService.readInterestRates());
			Rates inflationRates = new Rates(ratesService.readInflationRates());
			Rates exchangeRates = new Rates(ratesService.readExchangeRates());

			YearMonth date = YearMonth.of(2000, Month.JANUARY);
			while (date.isBefore(YearMonth.now())) {
				YearMonth nextMonth = date.plusMonths(1);

				PartialIndicatorResult interestResult = interestRates.calculate(nextMonth);
				PartialIndicatorResult seasonResult = season.calculate(nextMonth);
				PartialIndicatorResult exchangeResult = exchangeRates.calculate(date);
				PartialIndicatorResult inflationResult = inflationRates.calculate(date.minusMonths(1));

				final int sumOfPointsThisMonth = seasonResult.getPoint() +
						inflationResult.getPoint() + interestResult.getPoint() + exchangeResult.getPoint();

				IndicatorResult result = IndicatorResult.builder()
						.date(nextMonth.atDay(1))
						.seasonPoint(seasonResult.getPoint())
						.interestRate(interestResult.getRate())
						.interestPoint(interestResult.getPoint())
						.exchangeRate(exchangeResult.getRate())
						.exchangeRageOneYearAgo(exchangeResult.getComparativeRate())
						.exchangePoint(exchangeResult.getPoint())
						.inflationRate(inflationResult.getRate())
						.inflationRateOneYearAgo(inflationResult.getComparativeRate())
						.inflationPoint(inflationResult.getPoint())
						.sumOfPoints(sumOfPointsThisMonth)
						.shouldInvest(decideInvestment(sumOfPointsThisMonth, repository))
						.build();

				repository.save(result);

				date = date.plusMonths(1);
			}
		};
	}

	private boolean decideInvestment(int sumOfPoints, GiRepository repository) {
		if (sumOfPoints > 2) {
			return true;
		} else if (sumOfPoints < 2) {
			return false;
		} else { // currentPoints == 2
			return repository.findBySumOfPointsIsNotOrderByDateDesc(2).get(0).shouldInvest();
		}
	}

}
