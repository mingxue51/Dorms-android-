package com.mc.youthhostels.entity;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import entity.Generic.Price;
import helper.DT;
import helper.H;

public class Order {
    private List<Room> rooms;

    private BigDecimal depositPercentage;

    private Date departureDate;
    private Date arrivalDate;

    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    private String nationality;
    private String phoneNumber;

    private String checkInTime;

    private String creditCardType;
    private String ccv;
    private String month;
    private String year;
    private String creditCardNumber;
    private String cardHolderNamee;

    private String propertyNumber;
    private String propertyName;

    private List<String> cardTypesAvailable;

    public void parseRooms(Object json) {
        rooms = new ArrayList<>();
        try {
            JSONArray array = (JSONArray) json;
            for (Object key : array) {
                JSONObject obj = (JSONObject) key;
                if (obj.containsKey("DEPOSITPERCENTAGE")) {
                    setDepositPercentage(H.toBigDecimal(obj, "DEPOSITPERCENTAGE"));
                } else {
                    Room room = new Room();
                    room.setJSON(obj);
                    rooms.add(room);
                }
            }
        } catch (Exception e) {
            H.logE(e);
        }
    }

    public void parseCardTypes(Object json) {
        cardTypesAvailable = new ArrayList<>();
        try {
            JSONArray array = (JSONArray) json;
            for (Object key : array) {
                cardTypesAvailable.add((String) key);
            }
        } catch (Exception e) {
            H.logE(e);
        }
    }

    public List<String> getCardTypesAvailable() {
        return cardTypesAvailable;
    }

    public BigDecimal getDepositPercentage() {
        return depositPercentage;
    }

    public void setDepositPercentage(BigDecimal depositPercentage) {
        this.depositPercentage = depositPercentage;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public List<Room> getDorms() {
        List<Room> results = new ArrayList<>();

        for (Room room : rooms) {
            if (room.getBlockbeds() == 0) {
                results.add(room);
            }
        }

        return results;
    }

    public List<Room> getPrivateRooms() {
        List<Room> results = new ArrayList<>();

        for (Room room : rooms) {
            if (room.getBlockbeds() != 0) {
                results.add(room);
            }
        }

        return results;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public BigDecimal getTotalPrice() {
        BigDecimal totalPrice = new BigDecimal(0);
        for (Room room : rooms) {
            BigDecimal roomPrice = room.getTotalPricePerGuest().multiply(new BigDecimal(room.getSelected()));
            totalPrice = totalPrice.add(roomPrice);
        }
        return totalPrice;
    }

    public int getTotalGuests() {
        int guests = 0;
        for (Room room : rooms) {
            guests += room.getSelected();
        }
        return  guests;
    }

    public String getPersons() {
        StringBuilder sb = new StringBuilder();
        for (Room room : rooms) {
            if (room.getSelected() > 0) {
                sb.append(room.getSelectedForApi()).append(",");
            }
        }

        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    public String getRoomsNumbers() {
        StringBuilder sb = new StringBuilder();
        for (Room room : rooms) {
            if (room.getSelected() > 0) {
                sb.append(room.getId()).append(",");
            }
        }

        sb.setLength(sb.length() - 1);
        return sb.toString();
    }


    public int getNightsCount() {
        return DT.calculateNumberOfNights(getArrivalDate(), getDepartureDate());
    }

    public BigDecimal getPayNowPrice() {
        BigDecimal totalPrice = getTotalPrice();
        BigDecimal percent = getDepositPercentage();
        return totalPrice.multiply(percent.divide(BigDecimal.valueOf(100))).setScale(Price.DECIMAL_PLACES, RoundingMode.UP);
    }

    public BigDecimal getUponArrival() {
        return getTotalPrice().subtract(getPayNowPrice());
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getCreditCardType() {
        return creditCardType;
    }

    public void setCreditCardType(String creditCardType) {
        this.creditCardType = creditCardType;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCcv() {
        return ccv;
    }

    public void setCcv(String ccv) {
        this.ccv = ccv;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public void setPropertyNumber(String propertyNumber) {
        this.propertyNumber = propertyNumber;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyNumber() {
        return propertyNumber;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getCardHolderNamee() {
        return cardHolderNamee;
    }

    public void setCardHolderNamee(String cardHolderNamee) {
        this.cardHolderNamee = cardHolderNamee;
    }
}
