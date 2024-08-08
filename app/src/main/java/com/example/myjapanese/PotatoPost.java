package com.example.myjapanese;

import com.google.gson.annotations.SerializedName;
import java.util.Date;

public class PotatoPost {

    @SerializedName("title")
    private String title;

    @SerializedName("content")
    private String content;

    @SerializedName("published_date")
    private Date publishedDate;

    // Getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }
}
