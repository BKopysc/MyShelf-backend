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
import com.myshelf.MyShelf.repository.LibraryRepository;
import com.myshelf.MyShelf.repository.UserRepository;
import com.myshelf.MyShelf.security.jwt.JwtUtils;
import com.myshelf.MyShelf.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/data")
public class BookController {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    LibraryRepository libraryRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/libraries/{lib_id}/books/{book_id}")
    public ResponseEntity<Book> getBook(@PathVariable(value="lib_id") long lib_id, @PathVariable(value="book_id") long book_id,
                                        @RequestHeader(name="Authorization", required = false) String token) {

        Optional<Library> libraryData = libraryRepository.findById(lib_id);
        Optional<Book> bookData = bookRepository.findById(book_id);

        if (libraryData.isPresent() && bookData.isPresent()) {

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
                    return new ResponseEntity<>(bookData.get(), HttpStatus.OK);
                }
                else
                {
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }
            }
            else{
                return new ResponseEntity<>(bookData.get(), HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/libraries/{lib_id}/books/{book_id}")
    public ResponseEntity<HttpStatus> deleteBook(@PathVariable(value="lib_id") long lib_id, @PathVariable(value="book_id") long book_id,
                           @RequestHeader(name="Authorization") String token) {

        Optional<Library> libraryData = libraryRepository.findById(lib_id);
        Optional<Book> bookData = bookRepository.findById(book_id);

        if (libraryData.isPresent() && bookData.isPresent()) {

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
                        bookRepository.deleteById(book_id);
                        System.out.println("Delete book with id: " +  book_id);
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

    @PostMapping("/libraries/{id}/books")
    public ResponseEntity<Book> addBook(@PathVariable(value="id") long id, @RequestHeader(name="Authorization") String token, @RequestBody Book bookRequest) {
        System.out.println("Add book");

        Library libraryData = libraryRepository.findById(id).orElse(null);

        if(libraryData != null) {
            long lib_uid = libraryData.getUser().getId();
            String username = "";
            try {
                username = jwtUtils.getUsernameFromBearer(token);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            long uid = userRepository.findByUsername(username).get().getId();

            if (lib_uid == uid) {
                bookRequest.setLibrary(libraryData);
                bookRequest.setBookReview(new BookReview());
                return new ResponseEntity<>(bookRepository.save(bookRequest), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        }
            else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

    }

//        Optional<Library> libraryData = libraryRepository.findById(id);
//
//        if (libraryData.isPresent()) {
//
//            if( libraryData.get().getIsPrivate() ){
//                //check if ids are the same
//                long lib_uid = libraryData.get().getUser().getId();
//                String username = "";
//
//                try {
//                    username = jwtUtils.getUsernameFromBearer(token);
//                } catch(Exception e){
//                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//                }
//
//                long uid = userRepository.findByUsername(username).get().getId();
//
//                if(lib_uid == uid){
//                    return new ResponseEntity<>(libraryData.get(), HttpStatus.OK);
//                }
//                else
//                {
//                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//                }
//            }
//            else{
//
//                return new ResponseEntity<>(libraryData.get(), HttpStatus.OK);
//            }
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }

    @PutMapping("/libraries/{lib_id}/books/{book_id}")
    public ResponseEntity<Book> addBook(@PathVariable(value="lib_id") long lib_id, @PathVariable(value="book_id") long book_id, @RequestBody Book bookRequest,
                                        @RequestHeader(name="Authorization") String token) {

        Optional<Library> libraryData = libraryRepository.findById(lib_id);
        Optional<Book> bookData = bookRepository.findById(book_id);

        if (libraryData.isPresent() && bookData.isPresent()) {

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
                    Book newBook = bookData.get();
                    newBook.setAuthor(bookRequest.getAuthor());
                    newBook.setTitle(bookRequest.getTitle());
                    newBook.setDescription(bookRequest.getDescription());
                    newBook.setGenre(bookRequest.getGenre());
                    newBook.setRead(bookRequest.isRead());
                    return new ResponseEntity<>(bookRepository.save(newBook), HttpStatus.OK);
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


