package entity.Property;

import org.json.simple.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import helper.H;

public class Facility implements Serializable {
    private static final long serialVersionUID = 1L;

    public JSON json;
    private String id;
    private String name;

    public Facility() {
    }


    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setId(String paramString) {
        this.id = paramString;
    }

    public void setName(String paramString) {
        this.name = paramString;
    }

    public class JSON implements Serializable {
        private static final long serialVersionUID = 1L;

        public Facility setJSON(JSONObject obj) {
            setName(H.toString(obj, "landmark_name"));
            setId(H.toString(obj, "type"));
            return Facility.this;
        }
    }

    public static List<Facility> factoryList(Object json) {
        ArrayList<Facility> result = new ArrayList<Facility>();
        try {
            ArrayList array = (ArrayList) json;
            for (int i = 0; i < array.size(); i++) {
                Facility entity = new Facility();
                entity.setName((String) array.get(i));
                //entity.json.setJSON((JSONObject)value);
                result.add(entity);
            }
        } catch (Exception e) {
        }
        return result;
    }
}

