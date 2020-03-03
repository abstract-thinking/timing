package com.example.timing.boundary.gi;

import com.example.timing.data.gi.GiRepository;
import com.example.timing.indicator.InterestRatesIndicator;
import com.example.timing.indicator.RatesIndicator;
import com.example.timing.indicator.SeasonIndicator;
import com.example.timing.services.rates.RatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.YearMonth;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RequestMapping("/gi")
@Controller
public class GiController {

    private final GiRepository repository;

    private final RatesService ratesService;

    @Autowired
    public GiController(GiRepository repository, RatesService ratesService) {
        this.repository = repository;
        this.ratesService = ratesService;
    }

    @GetMapping
    public String showGi(Model model) {
        model.addAttribute("results", repository.findAll(Sort.by(DESC, "date")));

        return "gi";
    }

    @GetMapping("/current")
    public String showCurrentGi(Model model) {
        YearMonth now = YearMonth.now();

        SeasonIndicator seasonIndicator = new SeasonIndicator();
        model.addAttribute("season", seasonIndicator.indicate(now));

        InterestRatesIndicator interestRatesIndicator = new InterestRatesIndicator(ratesService.fetchInterestRates());
        model.addAttribute("interest", interestRatesIndicator.indicate(now));

        RatesIndicator inflationRatesIndicator = new RatesIndicator(ratesService.fetchInflationRates());
        model.addAttribute("inflation", inflationRatesIndicator.indicate(now.minusMonths(2)));

        RatesIndicator exchangeRatesIndicator = new RatesIndicator(ratesService.fetchExchangeRates());
        model.addAttribute("exchange", exchangeRatesIndicator.indicate(now.minusMonths(1)));

        return "gi_current";
    }
}

