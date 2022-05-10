package com.myshelf.MyShelf.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "films")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String director;

    @Column(name = "genre")
    private String genre;

    @Column(name = "premiere_date")
    private Date premiereDate;

    @Column(name = "description")
    private String description;

    @Column(name = "watched")
    private boolean watched;

    @ManyToOne
    @JoinColumn(name="library_id", nullable=false)
    @JsonIgnore
    private Library library;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "filmReview_id", referencedColumnName = "id")
    private FilmReview filmReview;

//    @OneToOne(cascade = CascadeType.ALL)

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

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Date getPremiereDate() {
        return premiereDate;
    }

    public void setPremiereDate(Date premiereDate) {
        this.premiereDate = premiereDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isWatched() {
        return watched;
    }

    public void setWatched(boolean watched) {
        this.watched = watched;
    }

    public Library getLibrary() {
        return library;
    }

    public void setLibrary(Library library) {
        this.library = library;
    }

    public FilmReview getFilmReview() {
        return filmReview;
    }

    public void setFilmReview(FilmReview filmReview) {
        this.filmReview = filmReview;
    }
//    @JoinColumn(name = "book_review_id", referencedColumnName = "id")



    public Film() {

    }

    public Film(String title, String director, String genre, Date premiereDate, String description, boolean watched) {
        this.title = title;
        this.director = director;
        this.genre = genre;
        this.premiereDate = premiereDate;
        this.description = description;
        this.watched = watched;
    }

    @Override
    public String toString() {
        return "Film{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", director='" + director + '\'' +
                ", genre='" + genre + '\'' +
                ", premiereDate=" + premiereDate +
                ", description='" + description + '\'' +
                ", watched=" + watched +
                ", library=" + library +
                ", filmReview=" + filmReview +
                '}';
    }
}