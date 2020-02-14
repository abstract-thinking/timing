package com.example.timing;

import com.example.timing.data.GiRepository;
import com.example.timing.data.IndicatorResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.util.StreamUtils.copyToString;

@SpringBootTest
public class ResearchTests {

    @Autowired
    private GiRepository repository;

    @Test
    public void shouldReadData() throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        Scanner scanner = new Scanner(readHistoryFromFile());
        scanner.useDelimiter(",");
        while (scanner.hasNext()) {
            String lineOfText = scanner.nextLine();
            String[] split = lineOfText.split(",");

            IndicatorResult result = IndicatorResult.builder()
                    .date(LocalDate.parse(split[0], formatter))
                    .seasonPoint(Integer.parseInt(split[1]))
                    .interestRate(Double.parseDouble(split[2]))
                    .interestPoint(Integer.parseInt(split[3]))
                    .inflationRate(Double.parseDouble(split[4]))
                    .inflationRateOneYearAgo(Double.parseDouble(split[5]))
                    .inflationPoint(Integer.parseInt(split[6]))
                    .exchangeRate(Double.parseDouble(split[7]))
                    .exchangeRageOneYearAgo(Double.parseDouble(split[8]))
                    .exchangePoint(Integer.parseInt(split[9]))
                    .sumOfPoints(Integer.parseInt(split[10]))
                    .shouldInvest(Boolean.parseBoolean(split[11]))
                    .build();

            IndicatorResult savedResult = repository.save(result);
            System.out.println(savedResult.toString());
        }

        scanner.close();
    }

    private String readHistoryFromFile() throws IOException {
        return copyToString(new ClassPathResource("data/gi-history.csv").getInputStream(), UTF_8);
    }
}
