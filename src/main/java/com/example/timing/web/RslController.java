package com.example.timing.web;

import com.example.timing.data.RslRepository;
import com.example.timing.service.QuotesService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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

import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

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
        LocalDate now = LocalDate.now();
        LocalDate startWeek = now.minusWeeks(1);

        repository.saveAll(service.fetchAll());

        MatchOperation filterDate = match(Criteria.where("date").gte(startWeek).lte(now));
        GroupOperation groupByRsl = group("rsl").avg("rsl").as("averageRsl");

        Aggregation aggregation = newAggregation(filterDate, groupByRsl);
        AggregationResults<Document> result = mongoTemplate.aggregate(aggregation, "rsls", Document.class);

        Document document = result.getUniqueMappedResult();
        document.getDouble("averageRsl");

        model.addAttribute("results", repository.findAll(Sort.by(DESC, "date")));

        return "rsl";
    }

}
