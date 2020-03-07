package com.example.timing.control.rsl;

public enum Indices {

    // major indices
    SP500("^GSPC", "S&P 500", "US", true),
    NASDAQ("^IXIC", "Nasdaq", "US", true),
    RUSSEL2000("^RUT", "SmallCap 2000", "US", true),
    TSX("^GSPTSE", "S&P/TSX", "Canada", true),
    Bovespa("^BVSP", "Bovespa", "Brazil", true),
    BMVIPC("^MXX", "S&P/BMV IPC", "Mexico", true),
    DAX("^GDAXI", "DAX 30", "Germany", true),
    // FTSE100("^FTSE", "FTSE 100", "Great Britain", true),
    CAC40("^FCHI", "CAC 40", "France", true),
    STOXX50("^STOXX50E", "Euro Stoxx 50", "Europe", true),
    AEX("^AEX", "AEX", "Netherlands", true),
    IBEX35("^IBEX", "IBEX 35", "Spain", true),
    // MIB("FTSEMIB.MI", "FTSE MIB", "Italy"),
    SMI("^SSMI", "SMI", "Switzerland", true),
    PSI20("PSI20.LS", "PSI 20", "Portugal", true),
    BEL20("^BFX", "BEL 20", "Belgium", true),
    ATX("^ATX", "ATX", "Austria", true),
    OMXS30("^OMX", "OMXS3", "Sweden", true),
    OMXC25("^OMXC25", "OMXC25", "Denmark", true),
    MOEX("IMOEX.ME", "MOEX", "Russia", true),
    RTSI("RTSI.ME", "RTSI", "Russia", true),
    WIG20("WIG.PA", "WIG20", "Poland", true), // ETF
    // Budapest SE	("^HTL", "Budapest SE", "Hungarian", true),
    BIST100("XU100.IS", "BIST 100", "Turkey", true),
    TA35("TA35.TA", "TA 35", "Israel", true),
    TASI("^TASI.SR", "Tadawul All Share", "Saudi Arabia", true),
    NIKKEI225("^N225", "Nikkei 225", "Japan", true),
    ASX("^AXJO", "S&P/ASX 200", "Australia", true),
    NZ50("^NZ50", "DJ New Zealand", "New Zealand", true),
    SS("000001.SS", "Shanghai", "China", true),
    SZSE("399001.SZ", "SZSE Component", "China", true),
    A50("XIN9.FGI", "China A50", "China", true),
    HSI("^HSI", "Hang Seng", "China", true),
    TWI("^TWII", "Taiwan Weighted", "Taiwan", true),
    SETI("SETI.KL", "SETI", "Malaysia", true),
    KOSPI("^KS11", "KOSPI", "South Korea", true),
    IDX("^JKSE", "IDX Composite - Jakarta Composite Index", "Indonesia", true),
    Nifty50("^NSEI", "Nifty 50", "India", true),
    BSE("^BSESN", "BSE Sensex", "India", true),
    PSEi("PSEI.PS", "PSEi Composite", "Philippines", true),
    STI("^STI", "STI Index", "Singapore", true),
    // Karachi 100("^KSE", "Karachi 100", "Pakistan", true),
    // HNX30("FVTT.L", "FTSE Vietnam Index - HNX 30", "Vietnam", true),
    CSE("^CSE", "CSE All-Share", "Sri Lanka", true),

    // minor indices
    MDAX("^MDAXI", "MDAX", "Germany", false),
    TECDAX("^TECDAX", "TecDAX", "Germany", false),
    SDAX("^SDAXI", "SDAX", "Germany", false),
    DOW30("^DJI", "Dow 30", "US", false),
    NYSE("^NYA", "NYSE COMPOSITE (DJ)", "US", false),
    AMEX("^XAX", "NYSE AMEX COMPOSITE INDEX", "US", false),
    EURONEXT("^N100", "EURONEXT 100", "Europe", false),
    AORD("^AORD", "ALL ORDINARIES", "Australia", false),
    // KLSE("^KLSE", "FTSE Bursa Malaysia KLCI", "Malaysia", false),
    IPSA("^IPSA", "IPSA SANTIAGO DE CHILE", "Chile", false),
    MERVAL("^MERV", "MERVAL", "Argentina", false),
    TA("^TA125.TA", "TA-125", "Israel", false),
    EGX("^CASE30", "EGX 30 Price Return Index", "Egypt", false),
    TRI("^JN0U.JO", "Top 40 USD Net TRI Index", "South Africa", false),
    OSE("^OSEAX", "Oslo Bors All-share", "Norway", false);

    private final String symbol;

    private final String description;

    private final String country;

    private final boolean isMajor;

    Indices(String symbol, String description, String country, boolean isMajor) {
        this.symbol = symbol;
        this.description = description;
        this.country = country;
        this.isMajor = isMajor;
    }

    public String getSymbol() {
        return symbol;
    }

    public boolean isMajor() {
        return isMajor;
    }

    public static Indices from(String symbol) {
        for (Indices indices : Indices.values()) {
            if (indices.getSymbol().contains(symbol)) {
                return indices;
            }
        }

        throw new IllegalArgumentException("Unknown symbol " + symbol);
    }
}
