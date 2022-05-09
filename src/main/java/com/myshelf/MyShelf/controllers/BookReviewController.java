package com.myshelf.MyShelf.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.myshelf.MyShelf.exception.ResourceNotFoundException;
import com.myshelf.MyShelf.models.Book;
import com.myshelf.MyShelf.models.BookReview;
import com.myshelf.MyShelf.models.Library;
import com.myshelf.MyShelf.repository.BookRepository;
import com.myshelf.MyShelf.repository.BookReviewRepository;
import com.myshelf.MyShelf.repository.LibraryRepository;
import com.myshelf.MyShelf.repository.UserRepository;
import com.myshelf.MyShelf.security.jwt.JwtUtils;
import com.myshelf.MyShelf.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/* Put, Delete, Get */

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/data")
public class BookReviewController {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    LibraryRepository libraryRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BookReviewRepository bookReviewRepository;

    @GetMapping("/libraries/{lib_id}/books/{book_id}/book_review")
    public ResponseEntity<BookReview> getBookReview(@PathVariable(value="lib_id") long lib_id, @PathVariable(value="book_id") long book_id,
                                        @RequestHeader(name="Authorization", required = false) String token) {

        Optional<Library> libraryData = libraryRepository.findById(lib_id);
        Optional<Book> bookData = bookRepository.findById(book_id);
        //System.out.println(bookData.get().getBookReview().getId());

        if (libraryData.isPresent() && bookData.get().getBookReview() != null) {
            long rev_id = bookData.get().getBookReview().getId();
            Optional<BookReview> reviewData = bookReviewRepository.findById(rev_id);

            if( libraryData.get().getIsPrivate() ){
                //check if ids are the same
                long lib_uid = libraryData.get().getUser().getId();
                String username = "";

                try {
                    username = jwtUtils.getUsernameFromBearer(token);
                } catch(Exception e){
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }

                long uid = userRepository.findByUsername(username).get().getId();

                if(lib_uid == uid){
                    return new ResponseEntity<>(reviewData.get(), HttpStatus.OK);
                }
                else
                {
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }
            }
            else{
                return new ResponseEntity<>(reviewData.get(), HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/libraries/{lib_id}/books/{book_id}/book_review")
    public ResponseEntity<HttpStatus> deleteBookReview(@PathVariable(value="lib_id") long lib_id, @PathVariable(value="book_id") long book_id,
                                                 @RequestHeader(name="Authorization") String token) {

        Optional<Library> libraryData = libraryRepository.findById(lib_id);
        Optional<Book> bookData = bookRepository.findById(book_id);
        long rev_id = bookData.get().getBookReview().getId();

        Optional<BookReview> reviewData = bookReviewRepository.findById(rev_id);

        if (libraryData.isPresent() && reviewData.isPresent()) {

            //check if ids are the same
            long lib_uid = libraryData.get().getUser().getId();
            String username = "";

            try {
                username = jwtUtils.getUsernameFromBearer(token);
            } catch(Exception e){
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            long uid = userRepository.findByUsername(username).get().getId();

            if(lib_uid == uid){
                try {
                    //clean not delete!
                    BookReview rv = reviewData.get();
                    rv.cleanData();
                    bookReviewRepository.save(rv);
                    System.out.println("Delete (clean) review_book with id: " +  rev_id);
                    return new ResponseEntity<>(HttpStatus.OK);
                }
                catch(Exception e) {
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }

            }
            else
            {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //doesnt work in this case.
    @PostMapping("/libraries/{id}/books/{book_id}/book_review")
    public ResponseEntity<BookReview> addBookReview(@PathVariable(value="id") long id, @PathVariable(value="book_id") long book_id,
                                                    @RequestHeader(name="Authorization") String token,
                                                    @RequestBody BookReview bookReviewRequest) {
        System.out.println("Add book_review");

        Library libraryData = libraryRepository.findById(id).orElse(null);
        Book bookData = bookRepository.findById(book_id).orElse(null);

        if(libraryData != null && bookData != null) {
            long lib_uid = libraryData.getUser().getId();
            String username = "";
            try {
                username = jwtUtils.getUsernameFromBearer(token);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            long uid = userRepository.findByUsername(username).get().getId();

            if (lib_uid == uid) {
                bookReviewRequest.setBook(bookData);
                System.out.println(bookReviewRequest);
                return new ResponseEntity<>(bookReviewRepository.save(bookReviewRequest), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }


    @PutMapping("/libraries/{lib_id}/books/{book_id}/book_review")
    public ResponseEntity<BookReview> addBook(@PathVariable(value="lib_id") long lib_id, @PathVariable(value="book_id") long book_id, @RequestBody BookReview bookReviewRequest,
                                        @RequestHeader(name="Authorization") String token) {

        Optional<Library> libraryData = libraryRepository.findById(lib_id);
        Optional<Book> bookData = bookRepository.findById(book_id);
        long rev_id = bookData.get().getBookReview().getId();
        Optional<BookReview> reviewData = bookReviewRepository.findById(rev_id);

        if (libraryData.isPresent() && reviewData.isPresent()) {

            //check if ids are the same
            long lib_uid = libraryData.get().getUser().getId();
            String username = "";

            try {
                username = jwtUtils.getUsernameFromBearer(token);
            } catch(Exception e){
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            long uid = userRepository.findByUsername(username).get().getId();

            if(lib_uid == uid){
                try {
                    BookReview _bookReview = reviewData.get();
                    _bookReview.setTitle(bookReviewRequest.getTitle());
                    _bookReview.setText(bookReviewRequest.getText());
                    _bookReview.setScore(bookReviewRequest.getScore());
                    _bookReview.setShouldRead(bookReviewRequest.isShouldRead());
                    _bookReview.setTimeToRead(bookReviewRequest.getTimeToRead());
                    return new ResponseEntity<>(bookReviewRepository.save(_bookReview), HttpStatus.OK);
                }
                catch(Exception e) {
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }

            }
            else
            {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }





}


