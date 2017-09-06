package entity.Property;

public class PropertyPricingResponse {

    private boolean error;
    private String message;

    public PropertyPricingResponse(){
        error=true;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
