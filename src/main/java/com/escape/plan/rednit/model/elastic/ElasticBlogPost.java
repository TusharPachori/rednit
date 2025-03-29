package com.escape.plan.rednit.model.elastic;

import com.escape.plan.rednit.model.BlogPost;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "post")
public class ElasticBlogPost extends BaseElasticEntity {
    private String title;
    private String content;
    private String author;

    public ElasticBlogPost(BlogPost blogPost) {
        this.title = blogPost.getTitle();
        this.content = blogPost.getContent();
        this.author = blogPost.getAuthor();
    }

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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
