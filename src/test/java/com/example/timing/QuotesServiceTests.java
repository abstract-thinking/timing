package com.example.timing;

import com.example.timing.service.QuotesService;
import org.junit.jupiter.api.Test;

public class QuotesServiceTests {

    @Test
    public void testMe() {
        QuotesService service = new QuotesService();

        service.fetchAll();
    }
}
