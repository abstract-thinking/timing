package com.example.timing.tasks;

import com.example.timing.data.GiRepository;
import com.example.timing.indicator.IndicatorResult;
import com.example.timing.indicator.InterestRatesIndicator;
import com.example.timing.indicator.PartialIndicatorResult;
import com.example.timing.indicator.RatesIndicator;
import com.example.timing.indicator.SeasonIndicator;
import com.example.timing.web.RatesService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.timing.utils.IndicatorResultHelper.createIndicatorResult;

@Slf4j
@Component
public class GiSchedulerTask {

    private final RatesService ratesService;

    private final GiRepository repository;

    @Autowired
    public GiSchedulerTask(GiRepository repository, RatesService ratesService) {
        this.repository = repository;
        this.ratesService = ratesService;
    }

    @Scheduled(cron = "0 0 0 1 1/1 *")
    public void processGi() {
        SeasonIndicator seasonIndicator = new SeasonIndicator();
        InterestRatesIndicator interestRatesIndicator = new InterestRatesIndicator(ratesService.fetchInterestRates());
        RatesIndicator exchangeRatesIndicator = new RatesIndicator(ratesService.fetchExchangeRates());
        RatesIndicator inflationRatesIndicator = new RatesIndicator(ratesService.fetchInflationRates());

        final YearMonth now = YearMonth.now();
        final YearMonth oneMonthBefore = now.minusMonths(1);
        updateResult(oneMonthBefore, seasonIndicator, interestRatesIndicator, exchangeRatesIndicator, inflationRatesIndicator);
        insertResult(now, seasonIndicator, interestRatesIndicator, exchangeRatesIndicator, inflationRatesIndicator);
        backupResult(now, oneMonthBefore, interestRatesIndicator, exchangeRatesIndicator, inflationRatesIndicator);
    }

    private void updateResult(YearMonth date, SeasonIndicator seasonIndicator, InterestRatesIndicator interestRatesIndicator, RatesIndicator exchangeRatesIndicator, RatesIndicator inflationRatesIndicator) {
        PartialIndicatorResult seasonResult = seasonIndicator.indicate(date);
        PartialIndicatorResult interestResult = interestRatesIndicator.indicate(date);
        PartialIndicatorResult exchangeResultOneMonthAgo = exchangeRatesIndicator.indicate(date.minusMonths(1));
        PartialIndicatorResult inflationResultTwoMonthsAgo = inflationRatesIndicator.indicate(date.minusMonths(2));

        IndicatorResult result = createIndicatorResult(date,
                interestResult, seasonResult, exchangeResultOneMonthAgo, inflationResultTwoMonthsAgo, repository);
        result.setId(repository.findOneByDate(date).getId());
        repository.save(result);
    }

    private void insertResult(YearMonth date, SeasonIndicator seasonIndicator, InterestRatesIndicator interestRatesIndicator, RatesIndicator exchangeRatesIndicator, RatesIndicator inflationRatesIndicator) {
        PartialIndicatorResult seasonResult = seasonIndicator.indicate(date);
        PartialIndicatorResult interestResult = interestRatesIndicator.indicate(date);
        PartialIndicatorResult exchangeResultOneMonthAgo = exchangeRatesIndicator.indicate(date.minusMonths(1));
        PartialIndicatorResult inflationResultTwoMonthsAgo = inflationRatesIndicator.indicate(date.minusMonths(2));

        IndicatorResult result = createIndicatorResult(date,
                interestResult, seasonResult, exchangeResultOneMonthAgo, inflationResultTwoMonthsAgo, repository);
        repository.save(result);
    }

    private void backupResult(YearMonth date, YearMonth dateBefore, InterestRatesIndicator interestRatesIndicator, RatesIndicator exchangeRatesIndicator, RatesIndicator inflationRatesIndicator) {
        try {
            File sourceFile = new ClassPathResource("gi-history.csv").getFile();
            File destinationFile = new ClassPathResource("gi-history-backup.csv").getFile();
            FileCopyUtils.copy(sourceFile, destinationFile);

            List<String> lines = FileUtils.readLines(sourceFile, StandardCharsets.UTF_8);
            List<String> updatedLines = lines.stream().filter(s -> !s.contains(dateBefore.toString())).collect(Collectors.toList());

            updatedLines.add(dateBefore + "," +
                    interestRatesIndicator.indicate(dateBefore).getRate() + "," +
                    inflationRatesIndicator.indicate(dateBefore).getRate() + "," +
                    exchangeRatesIndicator.indicate(dateBefore).getRate());
            updatedLines.add(date + "," +
                    interestRatesIndicator.indicate(date).getRate() + "," +
                    inflationRatesIndicator.indicate(date).getRate() + "," +
                    exchangeRatesIndicator.indicate(date).getRate());

            FileUtils.writeLines(sourceFile, updatedLines, false);
        } catch (IOException ex) {
            log.error("Could not update files", ex);
        }
    }
}
