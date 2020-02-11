package com.example.timing.data;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Date;

@ReadingConverter
public class YearMonthReadConverter implements Converter<Date, YearMonth>  {

    @Override public YearMonth convert(Date date) {
       return YearMonth.from(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
    }
}
