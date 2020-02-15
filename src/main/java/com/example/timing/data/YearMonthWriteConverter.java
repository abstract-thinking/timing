package com.example.timing.data;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Date;

@WritingConverter
public class YearMonthWriteConverter implements Converter<YearMonth, Date> {

    @Override
    public Date convert(YearMonth yearMonth) {
        return Date.from(yearMonth.atDay(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}