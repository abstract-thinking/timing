package com.example.timing.web;

import com.example.timing.InterestRates;
import com.example.timing.Rates;
import com.example.timing.Season;
import com.example.timing.data.GiRepository;
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

    @GetMapping()
    public String showGi(Model model) {
        model.addAttribute("results", repository.findAll(Sort.by(DESC, "date")));

        return "gi";
    }

    @GetMapping("/current")
    public String showRecentGi(Model model) {
        YearMonth now = YearMonth.now();

        Season season = new Season();
        model.addAttribute("season", season.calculate(now));

        InterestRates interestRates = new InterestRates(ratesService.fetchInterestRates());
        model.addAttribute("interest", interestRates.calculate(now));

        Rates inflationRates = new Rates(ratesService.fetchInflationRates());
        model.addAttribute("inflation", inflationRates.calculate(now.minusMonths(2)));

        Rates exchangeRates = new Rates(ratesService.fetchExchangeRates());
        model.addAttribute("exchange", exchangeRates.calculate(now.minusMonths(1)));

        return "gi_recent";
    }
}

