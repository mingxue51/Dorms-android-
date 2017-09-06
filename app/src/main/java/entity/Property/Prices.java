package entity.Property;

import java.io.Serializable;

import entity.Generic.Price;

public class Prices implements Serializable {
    private static final long serialVersionUID = 1L;

    private Price displayPrice;
    private Price displaySharedPrice;
    private Price displayPrivatePrice;

    public Prices() {
        displayPrice = new Price();
        displaySharedPrice = new Price();
        displayPrivatePrice = new Price();
    }

    public Price getDisplayPrice() {
        return displayPrice;
    }

    public void setDisplayPrice(Price displayPrice) {
        this.displayPrice = displayPrice;
    }

    public Price getDisplaySharedPrice() {
        return displaySharedPrice;
    }

    public void setDisplaySharedPrice(Price displaySharedPrice) {
        this.displaySharedPrice = displaySharedPrice;
    }

    public Price getDisplayPrivatePrice() {
        return displayPrivatePrice;
    }

    public void setDisplayPrivatePrice(Price displayPrivatePrice) {
        this.displayPrivatePrice = displayPrivatePrice;
    }
}
