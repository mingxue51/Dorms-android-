
package entity.Image;

import android.graphics.Bitmap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import helper.H;

public class ImageUrl implements Serializable {
    private static final long serialVersionUID = 1L;

    private String url;

    public String getUrl() {
        return url;
    }

    public ImageUrl setUrl(String url) {
        this.url = url.replaceAll("\\\\/","/");
        return this;
    }

    public ImageUrl setJSONMain(String obj) {
        setUrl(obj);
        return this;
    }

    public ImageUrl setJSON(JSONObject obj) {
        setUrl(H.toString(obj, "main_pics"));
        return this;
    }



    public static ArrayList<ImageUrl> factoryList(Object json) {
        ArrayList<ImageUrl> result = new ArrayList<ImageUrl>();
        try {
            JSONArray array = (JSONArray) json;
            for (Object key : array) {
                String obj = (String) key;
                ImageUrl entity = new ImageUrl();
                entity.setJSONMain(obj);
                result.add(entity);
            }
        } catch (Exception e) {
            H.logE("Can't parse image url.");
            H.logE(e);
        }
        return result;
    }
}
