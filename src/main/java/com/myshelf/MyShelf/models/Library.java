package com.myshelf.MyShelf.models;
import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "libraries")
public class Library {



        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name = "id")
        private long id;

        @OneToMany(mappedBy="library")
        private Set<Book> books;

        @OneToOne(mappedBy = "library")
        private User user;

    public Library() {

    }

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

    @Override
    public String toString() {
        return "Library{" +
                "id=" + id +
                ", books=" + books +
                ", user=" + user +
                '}';
    }
}
