package entity.Map.Location.Distance;

import android.content.Context;

import java.io.Serializable;
import java.math.BigDecimal;

import helper.H;
import helper.Session;

public class Distance implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final int DECIMAL_PLACES = 1;
    public static final int ROUND_UP = BigDecimal.ROUND_UP;
    public static final int MAX_DISTANCE_IN_KILOMETERS = 50;

    public static final Unit DEFAULT_DISTANCE_UNIT = Unit.KM;

    private Decorator mDecorator;

    private Unit unit;
    private BigDecimal value;

    public Distance()
    {
        mDecorator = new Decorator();
        value = new BigDecimal(0);
    }
    public Distance(BigDecimal pValue, Unit unit)
    {
        setValue(pValue);
        this.unit = unit;
        mDecorator = new Decorator();
    }

    public Unit getUnit() {
        return unit;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value.setScale(DECIMAL_PLACES, ROUND_UP);
    }

    public class Decorator implements  Serializable{
        private static final long serialVersionUID = 1L;
        protected String getFormattedValue()
        {
            return value.toString() + " " + unit.getShortName();
        }

        public  String getFormattedDistance() {
            String result="";
            if(isDistanceZero()){
                result = getFormattedValue();
            }
            return result;
        }
        public  String getDistance() {
            String result="";
            if(isDistanceZero()){
                result = value.toString();
            }
            return result;
        }

        public boolean isDistanceZero(){
            if(Float.valueOf(value.floatValue())!=0){
                return true;
            }
            return false;
        }

        public boolean checkForAllowedDistance(){
            if(Float.valueOf(value.floatValue())<=MAX_DISTANCE_IN_KILOMETERS){
                return true;
            }
            return false;
        }
    }

    public Decorator getDecorator() {
        return mDecorator;
    }

    public void setDecorator(Decorator mDecorator) {
        this.mDecorator = mDecorator;
    }


    public enum Unit {
        KM("Km"), MILES("Miles");

        String shortName;

        Unit(String shortName) {
            this.shortName = shortName;
        }

        public String getShortName() {
            return shortName;
        }

        public static Unit getByType(String jsonType) {
            for (Unit unit : values()) {
                jsonType = jsonType.toLowerCase();
                final String shortName = unit.getShortName().toLowerCase();

                if (shortName.contains(jsonType)) {
                    return unit;
                }
            }

            H.logE("unit with type " + jsonType + " not found");

            return KM;
        }
    }

    public static void setDistanceType(Context pContext, String type) {
        Session.setStringValue(pContext, Session.SELECTED_DISTANCE_UNIT, type);
    }

    public static String getDistanceType(Context pContext) {
        final String type = Session.getStringValue(pContext, Session.SELECTED_DISTANCE_UNIT);

        if (type == null || type.isEmpty()) {
            return DEFAULT_DISTANCE_UNIT.getShortName();
        }

        return type;
    }
}
