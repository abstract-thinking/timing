package com.example.timing.web;

import com.example.timing.data.RslRepository;
import com.example.timing.results.RslResult;
import com.example.timing.service.QuotesService;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

@Slf4j
@Controller
@RequestMapping("/rsl")
public class RslController {

    private final RslRepository repository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    QuotesService service;

    @Autowired
    public RslController(RslRepository repository) {
        this.repository = repository;
    }

    @GetMapping()
    public String showRsl(Model model) {
        List<RslResult> rslResults = service.fetchAll();
        repository.saveAll(rslResults);

        LocalDate now = LocalDate.now();
        TemporalField fieldUS = WeekFields.of(Locale.US).dayOfWeek();
        LocalDate begin = now.with(fieldUS, 1);
        LocalDate end = now.with(fieldUS, 7);

        MatchOperation filterDate = match(Criteria.where("date").gte(begin).lte(end));
        GroupOperation groupByRsl = group().avg("rsl").as("averageRsl");

        Aggregation aggregation = newAggregation(filterDate, groupByRsl);
        AggregationResults<Document> result = mongoTemplate.aggregate(aggregation, "rslresults", Document.class);

        Document document = result.getUniqueMappedResult();
        Double averageRsl = document.getDouble("averageRsl");

        model.addAttribute("result", new Result(begin, end, averageRsl));

        return "rsl";
    }

    @Value
    private static class Result {
        LocalDate begin;
        LocalDate end;
        Double rsl;
    }

}
