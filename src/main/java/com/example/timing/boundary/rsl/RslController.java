package com.example.timing.boundary.rsl;

import com.example.timing.control.rsl.RslCalculator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/rsl")
public class RslController {

    private final RslCalculator calculator;

    @Autowired
    public RslController(RslCalculator calculator) {
        this.calculator = calculator;
    }

    @GetMapping
    public String showAllRsl(Model model) {
        log.info("RSL invoked");

        model.addAttribute("results", calculator.calculate());

        return "rsl";
    }

    @GetMapping(value = "{name}")
    public String showRsl(@PathVariable("name") String name, Model model) {
        model.addAttribute("results", calculator.calculate(name));

        return "rsl";
    }
}
