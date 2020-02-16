package com.example.timing.service;

public enum SeriesKey {

    EXCHANGE("120.EXR.M.USD.EUR.SP00.E"),
    INFLATION("122.ICP.M.U2.N.000000.4.ANR"),
    INTEREST("143.FM.B.U2.EUR.4F.KR.MRR_FR.LEV");

    private final String key;

    SeriesKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
