package com.myshelf.MyShelf.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "bookReviews")
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookReview {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "text")
    private String text;

    @Column(name = "score")
    private int score;

    @Column(name = "time_to_read")
    private int timeToRead;

    @Column(name = "should_read")
    private boolean shouldRead;

    @JsonIgnore
    @OneToOne(mappedBy = "bookReview")
    private Book book;

    public long getId() {
        return id;
    }

    public BookReview(){

    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", score=" + score +
                ", timeToRead=" + timeToRead +
                ", shouldRead=" + shouldRead +
                ", book=" + book +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void cleanData() {
        this.title=null;
        this.text=null;
        this.score=0;
        this.timeToRead=0;
        this.shouldRead=false;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTimeToRead() {
        return timeToRead;
    }

    public void setTimeToRead(int timeToRead) {
        this.timeToRead = timeToRead;
    }

    public boolean isShouldRead() {
        return shouldRead;
    }

    public void setShouldRead(boolean shouldRead) {
        this.shouldRead = shouldRead;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
