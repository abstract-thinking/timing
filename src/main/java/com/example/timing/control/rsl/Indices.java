package com.example.timing.control.rsl;

public enum Indices {
    DAX("^GDAXI", "DAX"),
    MDAX("^MDAXI", "MDAX"),
    TECDAX("^TECDAX", "TecDAX"),
    SDAX("^SDAXI", "SDAX"),
    STOXX50("^STOXX50E", "Euro Stoxx 50"),
    SP500("^GSPC", "S&P 500"),
    DJ("^DJI", "Dow Jones 30"),
    NASDAQ("^IXIC", "Nasdaq Composite"),
    NIKKEI("^N225", "Nikkei 225"),
    // FTSE("^FTSE", "FTSE 100"),
    NYSE("^NYA", "NYSE COMPOSITE (DJ)"),
    CAC("^FCHI", "CAC 40"),
    MOEX("IMOEX.ME", "MOEX Russia Index"),
    RUSSEL("^RUT", "Russell 2000"),
    AMEX("^XAX", "NYSE AMEX COMPOSITE INDEX"),
    EURONEXT("^N100", "EURONEXT 100"),
    BEL("^BFX", "BEL 20"),
    HSI("^HSI", "HANG SENG INDEX"),
    SSE("000001.SS", "SSE Composite Index"),
    ASX("^AXJO", "S&P/ASX 200"),
    AORD("^AORD", "ALL ORDINARIES"),
    BSE("^BSESN", "S&P BSE SENSEX"),
    JKSE("^JKSE", "Jakarta Composite Index"),
    // KLSE("^KLSE", "FTSE Bursa Malaysia KLCI"),
    NZ("^NZ50", "S&P/NZX 50 INDEX GROSS"),
    KSOPI("^KS11", "KOSPI Composite Index"),
    TSEC("^TWII", "TSEC weighted index"),
    TSX("^GSPTSE", "S&P/TSX Composite index"),
    IBOVESPA("^BVSP", "IBOVESPA"),
    IPC("^MXX", "IPC MEXICO"),
    IPSA("^IPSA", "IPSA SANTIAGO DE CHILE"),
    MERVAL("^MERV", "MERVAL"),
    TA("^TA125.TA", "TA-125"),
    EGX("^CASE30", "EGX 30 Price Return Index"),
    TRI("^JN0U.JO", "Top 40 USD Net TRI Index"),
    ATX("^ATX", "Austrian Traded Index"),
    SMI("^SSMI", "Swiss Market Index"),
    OSE("^OSEAX", "Oslo Bors All-share"),
    OMX("^OMX", "OMX Stockholm 30 Index"),
    OMC("^OMXC20", "OMX Copenhagen 20"),
    IBEX("^IBEX", "IBEX"),
    PSE("PSEI.PS", "PSEi INDEX");

    private final String symbol;

    private final String name;

    Indices(String symbol, String name) {
        this.symbol = symbol;
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
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

