package com.mc.youthhostels.events;

import entity.Generic.Currency;

public class CurrencyChangedEvent {
    private Currency currency;

    public CurrencyChangedEvent(Currency currency) {
        this.currency = currency;
    }

    public Currency getCurrency() {
        return currency;
    }
}
