package entity.Generic;

import android.content.Context;

import java.io.Serializable;
import java.math.BigDecimal;

import helper.App;
import service.Localization;

public class Price implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;

    public static final int DECIMAL_PLACES = 2;
    public static final int ROUND_DOWN = BigDecimal.ROUND_HALF_DOWN;

    private String currency;
    private BigDecimal price;

    public Price(){
        currency="";
        price = new BigDecimal(0);
    }
    public Price(BigDecimal pPrice,Currency pCurrency){
        price=pPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Price setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public static BigDecimal multiply(BigDecimal b, BigDecimal b2){
        BigDecimal res = new BigDecimal(0);
        if(b==null ||b2==null){
            return res;
        }
        res = b.multiply(b2);
        res.setScale(DECIMAL_PLACES, ROUND_DOWN);
        return res;
    }

    @Override
    public String toString() {
        return Localization.getPriceLocalized(App.getInstance().getBaseContext(), price);
    }
}

