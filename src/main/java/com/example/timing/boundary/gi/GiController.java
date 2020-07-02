package com.example.timing.boundary.gi;

import com.example.timing.control.gi.GiCalculator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequestMapping("/gi")
@Controller
public class GiController {

    private final GiCalculator giCalculator;

    @Autowired
    public GiController(GiCalculator giCalculator) {
        this.giCalculator = giCalculator;
    }

    @GetMapping
    public String showGi(Model model) {
        log.info("GI invoked");

        model.addAttribute("results", giCalculator.calculate());

        return "gi";
    }
}

