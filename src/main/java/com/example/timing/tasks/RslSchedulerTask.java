package com.example.timing.tasks;

import org.springframework.stereotype.Component;

@Component
public class RslSchedulerTask {
}

// URL = "https://finance.yahoo.com/quote/{}/history?period1={}&period2={}&interval=1wk&filter=history&frequency=1wk"
//
//
//def fetch_data_with(index):
//
//    now = datetime.now()
//    a_year_ago = now - relativedelta(months=12)
//
//    url = URL.format(index, int(a_year_ago.timestamp()), int(now.timestamp()))
//    page = requests.get(url)
//
//    try:
//        page.raise_for_status()
//    except Exception as exc:
//        from flask import current_app
//        current_app.logger.error('Could not fetch data from Yahoo server: {}', (exc,))
//        return None
//
//    return page.text
