package com.davidcampbell.popularmovies.domain;

import java.io.Serializable;

/**
 * PopularMovies
 * Created by david on 2016-04-17.
 */
public class Review implements Serializable {
    private String id;
    private String author;
    private String content;
    private String url;
    private String movieTitle;

    public Review() {
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    @Override
    public String toString() {
        return "Review{" +
                "id='" + id + '\'' +
                ", author='" + author + '\'' +
                ", content='" + content + '\'' +
                ", url='" + url + '\'' +
                ", movieTitle='" + movieTitle + '\'' +
                '}';
    }

}
