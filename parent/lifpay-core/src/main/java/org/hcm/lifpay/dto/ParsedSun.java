package org.hcm.lifpay.dto;

import lombok.Data;

@Data
public class ParsedSun {
    private final String uidHex;
    private final String counterHex;
    private final int counter;

    public ParsedSun(String uidHex, String counterHex, int counter) {
        this.uidHex = uidHex;
        this.counterHex = counterHex;
        this.counter = counter;
    }

    public String getUidHex() {
        return uidHex;
    }

    public String getCounterHex() {
        return counterHex;
    }

    public int getCounter() {
        return counter;
    }
}
