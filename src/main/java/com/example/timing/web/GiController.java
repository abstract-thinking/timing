package com.example.timing.web;

import com.example.timing.Rates;
import com.example.timing.InterestRates;
import com.example.timing.Season;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.YearMonth;

@RequestMapping("/gi")
@Controller
public class GiController {

    private final RatesService ratesService;

    @Autowired
    public GiController(RatesService ratesService) {
        this.ratesService = ratesService;
    }

    @GetMapping("/current")
    public String showGI(Model model) {
        YearMonth now = YearMonth.now();

        Season season = new Season();
        model.addAttribute("season", season.calculate(now));

        InterestRates interestRates = new InterestRates(ratesService.processInterestRates());
        model.addAttribute("interest", interestRates.calculate(now));

        Rates inflationRates = new Rates(ratesService.processInflationRates());
        model.addAttribute("inflation", inflationRates.calculate(now.minusMonths(2)));

        Rates exchangeRates = new Rates(ratesService.processExchangeRates());
        model.addAttribute("exchange", exchangeRates.calculate(now.minusMonths(1)));

        return "gi";
    }
}

