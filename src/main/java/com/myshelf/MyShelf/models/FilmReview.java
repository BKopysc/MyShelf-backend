package com.myshelf.MyShelf.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "filmReviews")
@JsonIgnoreProperties(ignoreUnknown = true)
public class FilmReview {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "text")
    private String text;

    @Column(name = "score")
    private int score;

    @Column(name = "camera_score")
    private int cameraScore;

    @Column(name = "play_score")
    private int playScore;

    @Column(name = "best_actor")
    private String bestActor;

    @Column(name = "should_watch")
    private boolean shouldWatch;

    @JsonIgnore
    @OneToOne(mappedBy = "filmReview")
    private Film film;

    public FilmReview(){

    }

    public FilmReview(String title, String text, int score, int cameraScore, int playScore, String bestActor, boolean shouldWatch) {
        this.title = title;
        this.text = text;
        this.score = score;
        this.cameraScore = cameraScore;
        this.playScore = playScore;
        this.bestActor = bestActor;
        this.shouldWatch = shouldWatch;
    }

    public void cleanData() {
        this.title = null;
        this.text = null;
        this.score = 0;
        this.cameraScore = 0;
        this.playScore = 0;
        this.bestActor = null;
        this.shouldWatch = false;
    }

    @Override
    public String toString() {
        return "FilmReview{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", score=" + score +
                ", cameraScore=" + cameraScore +
                ", playScore=" + playScore +
                ", bestActor='" + bestActor + '\'' +
                ", shouldWatch=" + shouldWatch +
                ", film=" + film +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
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

    public int getCameraScore() {
        return cameraScore;
    }

    public void setCameraScore(int cameraScore) {
        this.cameraScore = cameraScore;
    }

    public int getPlayScore() {
        return playScore;
    }

    public void setPlayScore(int playScore) {
        this.playScore = playScore;
    }

    public String getBestActor() {
        return bestActor;
    }

    public void setBestActor(String bestActor) {
        this.bestActor = bestActor;
    }

    public boolean isShouldWatch() {
        return shouldWatch;
    }

    public void setShouldWatch(boolean shouldWatch) {
        this.shouldWatch = shouldWatch;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }
}
