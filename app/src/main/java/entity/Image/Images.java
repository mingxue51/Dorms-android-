
package entity.Image;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import helper.H;

public class Images implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String BUNDLE = "images";

    private ArrayList<ImageUrl> thumbnail;
    private ArrayList<ImageUrl> main_image;

    public Images() {
        thumbnail = new ArrayList<ImageUrl>();
        main_image = new ArrayList<ImageUrl>();
    }

    public ArrayList<ImageUrl> getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(ArrayList<ImageUrl> thumbnail) {
        this.thumbnail = thumbnail;
    }

    public ArrayList<ImageUrl> getMainImages() {
        return main_image;
    }

    public void setMainImages(ArrayList<ImageUrl> main_image) {
        this.main_image = main_image;
    }

    public void addMainImage(ImageUrl url) {
        main_image.add(url);
    }

    public void addThumbnail(ImageUrl url) {
        thumbnail.add(url);
    }

    public Images setJSON(JSONObject obj) {
        addMainImage(new ImageUrl().setUrl(H.toString(obj, "imageURL")));
        addThumbnail(new ImageUrl().setUrl(H.toString(obj, "imageThumbnailURL")));
        return this;
    }

    public Images setJSONPropertyDetails(JSONObject obj){
        addMainImage(new ImageUrl().setUrl(H.toString(obj, "thumbnails")));
        addThumbnail(new ImageUrl().setUrl(H.toString(obj, "imageThumbnailURL")));
        return this;
    }

    public static ArrayList<ImageUrl> factoryListMain(Object json) {
        ArrayList<ImageUrl> result = new ArrayList<ImageUrl>();
        try {
            JSONArray array = (JSONArray) json;
            for (Object key : array) {
                //JSONObject obj = (JSONObject) key;
                ImageUrl entity = new ImageUrl();
                entity.setUrl((String)key);
                //entity.setJSONMain(obj);
                result.add(entity);
            }
        } catch (Exception e) {
            H.logE("Can't parse images");
            H.logE(e);
        }
        return result;
    }
}
