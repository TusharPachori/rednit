package com.escape.plan.rednit.model;

import com.escape.plan.rednit.model.base.BaseDoc;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "blog")
public class BlogPost extends BaseDoc {

    private String title;
    private String content;
    private String author;

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
