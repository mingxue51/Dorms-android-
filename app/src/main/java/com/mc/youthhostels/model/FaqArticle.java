package com.mc.youthhostels.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

import helper.H;

public class FaqArticle {

    private String title;
    private String body;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setJSON(JSONObject obj) {
        setTitle(H.toString(obj, "title"));
        setBody(H.toString(obj, "body"));
    }

    public static List<FaqArticle> faqList(Object json) {
        List<FaqArticle> result = new ArrayList<>();
            try {
                JSONArray array = (JSONArray) json;
                for (Object key : array) {
                    JSONObject obj = (JSONObject) key;
                    FaqArticle articles = new FaqArticle();
                    articles.setJSON(obj);
                    result.add(articles);
                }
            } catch (Exception e) {
                H.logE(e);
            }
            return result;
        }
}
