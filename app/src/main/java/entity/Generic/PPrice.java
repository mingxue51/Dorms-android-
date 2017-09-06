package entity.Generic;

import android.text.Html;

import java.io.Serializable;
import java.math.BigDecimal;

public class PPrice implements Serializable {
    private static final long serialVersionUID = 1L;

    private String currency;
    private BigDecimal price;

    public PPrice(){
    }

    public PPrice(BigDecimal pPrice){
        price=pPrice;
    }

    public PPrice(BigDecimal pPrice, String pCurrency){
        currency=pCurrency;
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

    public String getPriceToDisplay(){
        return String.valueOf(Html.fromHtml(currency+""+price));

    }

    public PPrice setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }
}

