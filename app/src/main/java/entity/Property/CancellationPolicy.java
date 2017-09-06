package entity.Property;

import org.json.simple.JSONObject;

import java.io.Serializable;

import helper.H;

public class CancellationPolicy implements Serializable {
    private static final long serialVersionUID = 1L;
    public JSONOperations json;
    private String description;
    private String numberOfDays;

    public CancellationPolicy() {

        json = new JSONOperations();
    }

    public String getDescription() {
        return this.description;
    }

    public String getNumberOfDays() {
        return this.numberOfDays;
    }

    public void setDescription(String pDescription) {
        this.description = pDescription;
    }

    public void setNumberOfDays(String pNumberOfDays) {
        this.numberOfDays = pNumberOfDays;
    }

    public class JSONOperations implements Serializable {
        private static final long serialVersionUID = 1L;
        public CancellationPolicy setJSON(JSONObject obj) {
            CancellationPolicy cancelPolicy = new CancellationPolicy();
            cancelPolicy.setDescription(H.toString(obj, "CANCELLATIONPOLICY"));
            cancelPolicy.setNumberOfDays(H.toString(obj, "CANCELLATIONPERIOD"));
            return cancelPolicy;
        }

    }


}

