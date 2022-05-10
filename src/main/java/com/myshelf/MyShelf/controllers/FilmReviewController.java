package com.myshelf.MyShelf.controllers;

import com.myshelf.MyShelf.models.Film;
import com.myshelf.MyShelf.models.FilmReview;
import com.myshelf.MyShelf.models.Library;
import com.myshelf.MyShelf.repository.FilmRepository;
import com.myshelf.MyShelf.repository.FilmReviewRepository;
import com.myshelf.MyShelf.repository.LibraryRepository;
import com.myshelf.MyShelf.repository.UserRepository;
import com.myshelf.MyShelf.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/* Put, Delete, Get */

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/data")
public class FilmReviewController {
    
        @Autowired
        private JwtUtils jwtUtils;

        @Autowired
        LibraryRepository libraryRepository;

        @Autowired
        FilmRepository filmRepository;

        @Autowired
        UserRepository userRepository;

        @Autowired
        FilmReviewRepository filmReviewRepository;

        @GetMapping("/libraries/{lib_id}/films/{film_id}/film_review")
        public ResponseEntity<FilmReview> getfilmReview(@PathVariable(value="lib_id") long lib_id, @PathVariable(value="film_id") long film_id,
                                                        @RequestHeader(name="Authorization", required = false) String token) {

            Optional<Library> libraryData = libraryRepository.findById(lib_id);
            Optional<Film> filmData = filmRepository.findById(film_id);
            //System.out.println(filmData.get().getfilmReview().getId());

            if (libraryData.isPresent() && filmData.get().getFilmReview() != null) {
                long rev_id = filmData.get().getFilmReview().getId();
                Optional<FilmReview> reviewData = filmReviewRepository.findById(rev_id);

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

        @DeleteMapping("/libraries/{lib_id}/films/{film_id}/film_review")
        public ResponseEntity<HttpStatus> deletefilmReview(@PathVariable(value="lib_id") long lib_id, @PathVariable(value="film_id") long film_id,
                                                           @RequestHeader(name="Authorization") String token) {

            Optional<Library> libraryData = libraryRepository.findById(lib_id);
            Optional<Film> filmData = filmRepository.findById(film_id);
            long rev_id = filmData.get().getFilmReview().getId();

            Optional<FilmReview> reviewData = filmReviewRepository.findById(rev_id);

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
                        FilmReview rv = reviewData.get();
                        rv.cleanData();
                        filmReviewRepository.save(rv);
                        System.out.println("Delete (clean) review_film with id: " +  rev_id);
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
        @PostMapping("/libraries/{id}/films/{film_id}/film_review")
        public ResponseEntity<FilmReview> addfilmReview(@PathVariable(value="id") long id, @PathVariable(value="film_id") long film_id,
                                                        @RequestHeader(name="Authorization") String token,
                                                        @RequestBody FilmReview filmReviewRequest) {
            System.out.println("Add film_review");

            Library libraryData = libraryRepository.findById(id).orElse(null);
            Film filmData = filmRepository.findById(film_id).orElse(null);

            if(libraryData != null && filmData != null) {
                long lib_uid = libraryData.getUser().getId();
                String username = "";
                try {
                    username = jwtUtils.getUsernameFromBearer(token);
                } catch (Exception e) {
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }
                long uid = userRepository.findByUsername(username).get().getId();

                if (lib_uid == uid) {
                    filmReviewRequest.setFilm(filmData);
                    System.out.println(filmReviewRequest);
                    return new ResponseEntity<>(filmReviewRepository.save(filmReviewRequest), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }
            }
            else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        }


        @PutMapping("/libraries/{lib_id}/films/{film_id}/film_review")
        public ResponseEntity<FilmReview> addfilm(@PathVariable(value="lib_id") long lib_id, @PathVariable(value="film_id") long film_id,
                                                  @RequestBody FilmReview filmReviewRequest,
                                                  @RequestHeader(name="Authorization") String token) {

            Optional<Library> libraryData = libraryRepository.findById(lib_id);
            Optional<Film> filmData = filmRepository.findById(film_id);
            long rev_id = filmData.get().getFilmReview().getId();
            Optional<FilmReview> reviewData = filmReviewRepository.findById(rev_id);

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
                        FilmReview _filmReview = reviewData.get();
                        _filmReview.setTitle(filmReviewRequest.getTitle());
                        _filmReview.setText(filmReviewRequest.getText());
                        _filmReview.setScore(filmReviewRequest.getScore());
                        _filmReview.setShouldWatch(filmReviewRequest.isShouldWatch());
                        _filmReview.setBestActor(filmReviewRequest.getBestActor());
                        _filmReview.setCameraScore(filmReviewRequest.getCameraScore());
                        _filmReview.setPlayScore(filmReviewRequest.getPlayScore());

                        return new ResponseEntity<>(filmReviewRepository.save(_filmReview), HttpStatus.OK);
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
