package com.example.timing.services.rates;

public enum SeriesKey {

    EXCHANGE_MONTHLY("120.EXR.M.USD.EUR.SP00.E"),
    EXCHANGE_DAILY("EXR.D.USD.EUR.SP00.A"),
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
