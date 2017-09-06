package entity.Generic;


import android.text.Html;
import android.text.Spanned;

import org.json.simple.JSONObject;

import java.io.Serializable;

import helper.H;

public class Content implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String TAC = "terms";
    public static final String PRIVACY = "privacy";

    private String title;
    private String content;

    public Content(){
        title="";
        content="";
    }
    public Content(String pTitle, String pContent){
        title=pTitle;
        content=pContent;
    }

    public String getTitle() {
        return title;
    }

    public Content setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Spanned getContentHTML() {
        return Html.fromHtml(content);
    }


    public Content setContent(String content) {
        this.content = content;
        return this;
    }

    public static Content fromJSON(Object json) {
        Content result = new Content();
        try {
            JSONObject obj = (JSONObject) json;
            result.setTitle(H.toString(obj, "title"));
            result.setContent(H.toString(obj, "content"));

        } catch (Exception e) {
        }
        return result;
    }

}

