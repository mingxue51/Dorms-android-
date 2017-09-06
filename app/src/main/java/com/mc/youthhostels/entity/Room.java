package com.mc.youthhostels.entity;

import org.json.simple.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import helper.H;


public class Room {
    private String id;
    private String name;
    private String nameTranslated;
    private int blockbeds;
    private int beds;

    /**
    * send_room_number - 1 or 0 - if its 1 user should select room number instead of beds BEDS / BLOCKBEDS = number of rooms.
    * This is only the case with private rooms for HC properties.
    */
    private boolean sendRoomNumber;

    private int selected;
    private List<BigDecimal> prices;

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNameTranslated(String nameTranslated) {
        this.nameTranslated = nameTranslated;
    }

    public void setBlockbeds(int blockbeds) {
        this.blockbeds = blockbeds;
    }

    public void setBeds(int beds) {
        this.beds = beds;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public void setJSON(JSONObject obj) {
        setId(H.toString(obj, "ID"));
        setName(H.toString(obj, "NAME"));
        setBlockbeds(H.toInt(obj, "BLOCKBEDS"));
        setBeds(H.toInt(obj, "BEDS"));
        setName(H.toString(obj, "NAME"));
        setNameTranslated(H.toString(obj, "NAME_TRANSLATED"));
        sendRoomNumber = H.toInt(obj, "send_room_number") == 1;

        if(obj.containsKey("NIGHTS")){
            JSONObject nights = (JSONObject) obj.get("NIGHTS");
            parsePrices(nights);
        }
    }

    public void parsePrices(JSONObject json) {
        prices = new ArrayList<>();
        try {
            Iterator<String> iter = json.keySet().iterator();
            while (iter.hasNext()) {
                String key = iter.next();
                parsePrice((JSONObject) json.get(key));
            }
        } catch (Exception e) {
            H.logE(e);
        }
    }

    public void parsePrice(JSONObject json) {
        try {
            for (String key : (Iterable<String>) json.keySet()) {
                Object value = json.get(key);
                BigDecimal minprice = H.toBigDecimal(((JSONObject) value), "MINPRICE");
                if (key.equals("CUSTOMER")) {
                    prices.add(minprice);
                }
            }
        } catch (Exception e) {
            H.logE(e);
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNameTranslated() {
        return nameTranslated;
    }

    public int getBlockbeds() {
        return blockbeds;
    }

    public int getBeds() {
        return beds;
    }

    public int getSelected() {
        return selected;
    }

    public BigDecimal getAveragePrice() {
        BigDecimal sum = new BigDecimal(0);
        if (prices != null && prices.size() > 0) {
            for (BigDecimal price : prices) {
                sum = sum.add(price);
            }

            return sum.divide(BigDecimal.valueOf(prices.size()), 2, RoundingMode.HALF_DOWN);
        }
        return sum;
    }

    public BigDecimal getTotalPricePerGuest() {
        BigDecimal sum = new BigDecimal(0);
        if (prices != null && prices.size() > 0) {
            for (BigDecimal price : prices) {
                sum = sum.add(price);
            }
        }
        return sum;
    }

    public boolean isSendRoomNumber() {
        return sendRoomNumber;
    }

    public int getSelectedForApi() {
        return sendRoomNumber ? selected / blockbeds : selected;
    }
}
