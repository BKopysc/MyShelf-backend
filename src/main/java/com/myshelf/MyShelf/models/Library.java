package com.myshelf.MyShelf.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "libraries")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Library {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name = "id")
        private long id;

        @Column(name = "is_private")
        private boolean isPrivate;

        @OneToMany(mappedBy="library")
        private Set<Book> books;

        @JsonIgnore
        @OneToOne(mappedBy = "library")
        private User user;

    public Library() {
    }

        public Library(boolean isPrivate) {
            this.isPrivate = isPrivate;
        }
//
//    public Library(User user) {
//        this.user = user;
//    }

        public long getId() {
            return id;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    public boolean getIsPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    @Override
    public String toString() {
        return "Library{" +
                "id=" + id +
                ", isPrivate=" + isPrivate +
                ", books=" + books +
                ", user=" + user +
                '}';
    }
}
