package entity.Property;

import org.json.simple.JSONObject;

import java.io.Serializable;

import helper.H;

public class PropertyCheckInOut implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String endsAt;
    private String startsAt;

    public PropertyCheckInOut() {
        json = new JSON();
        endsAt="";
        startsAt="";
    }
    public JSON json;



  public String getEndsAt()
  {
    return this.endsAt;
  }

  public String getStartsAt()
  {
    return this.startsAt;
  }

  public void setEndsAt(String paramInt)
  {
    this.endsAt = paramInt;
  }

  public void setStartsAt(String paramInt)
  {
    this.startsAt = paramInt;
  }

    public class JSON implements Serializable {
        private static final long serialVersionUID = 1L;
        public PropertyCheckInOut setJSON(JSONObject obj) {
            PropertyCheckInOut cancelPolicy = new PropertyCheckInOut();
            cancelPolicy.setStartsAt(H.toString(obj, "CHECKIN"));
            cancelPolicy.setEndsAt(H.toString(obj, "CHECKOUT"));
            return cancelPolicy;
        }

    }
}

