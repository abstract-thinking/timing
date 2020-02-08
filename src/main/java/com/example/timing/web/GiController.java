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
        Season season = new Season(LocalDate.now().getMonth());
        model.addAttribute("season", season);

        InterestRates interestRates = new InterestRates(ratesService.processInterestRates());
        model.addAttribute("interest", interestRates);

        Rates inflationRates = new Rates(ratesService.processInflationRates(), YearMonth.now().minusMonths(2));
        model.addAttribute("inflation", inflationRates);

        Rates exchangeRates = new Rates(ratesService.processExchangeRates(), YearMonth.now().minusMonths(1));
        model.addAttribute("exchange", exchangeRates);

        return "gi";
    }
}

