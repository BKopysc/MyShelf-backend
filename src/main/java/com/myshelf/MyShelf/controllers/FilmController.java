package com.myshelf.MyShelf.controllers;

import com.myshelf.MyShelf.models.Film;
import com.myshelf.MyShelf.models.FilmReview;
import com.myshelf.MyShelf.models.Library;
import com.myshelf.MyShelf.repository.FilmRepository;
import com.myshelf.MyShelf.repository.LibraryRepository;
import com.myshelf.MyShelf.repository.UserRepository;
import com.myshelf.MyShelf.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/data")
public class FilmController {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    LibraryRepository libraryRepository;

    @Autowired
    FilmRepository filmRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/libraries/{lib_id}/films/{film_id}")
    public ResponseEntity<Film> getFilm(@PathVariable(value="lib_id") long lib_id, @PathVariable(value="film_id") long film_id,
                                        @RequestHeader(name="Authorization", required = false) String token) {

        Optional<Library> libraryData = libraryRepository.findById(lib_id);
        Optional<Film> filmData = filmRepository.findById(film_id);

        if (libraryData.isPresent() && filmData.isPresent()) {

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
                    return new ResponseEntity<>(filmData.get(), HttpStatus.OK);
                }
                else
                {
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }
            }
            else{
                return new ResponseEntity<>(filmData.get(), HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/libraries/{lib_id}/films/{film_id}")
    public ResponseEntity<HttpStatus> deleteFilm(@PathVariable(value="lib_id") long lib_id, @PathVariable(value="film_id") long film_id,
                                                 @RequestHeader(name="Authorization") String token) {

        Optional<Library> libraryData = libraryRepository.findById(lib_id);
        Optional<Film> filmData = filmRepository.findById(film_id);

        if (libraryData.isPresent() && filmData.isPresent()) {

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
                    filmRepository.deleteById(film_id);
                    System.out.println("Delete film with id: " +  film_id);
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

    @PostMapping("/libraries/{id}/films")
    public ResponseEntity<Film> addFilm(@PathVariable(value="id") long id, @RequestHeader(name="Authorization") String token, @RequestBody Film filmRequest) {
        System.out.println("Add film");

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
                filmRequest.setLibrary(libraryData);
                filmRequest.setFilmReview(new FilmReview());
                return new ResponseEntity<>(filmRepository.save(filmRequest), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }


    @PutMapping("/libraries/{lib_id}/films/{film_id}")
    public ResponseEntity<Film> addFilm(@PathVariable(value="lib_id") long lib_id, @PathVariable(value="film_id") long film_id, @RequestBody Film filmRequest,
                                        @RequestHeader(name="Authorization") String token) {

        Optional<Library> libraryData = libraryRepository.findById(lib_id);
        Optional<Film> filmData = filmRepository.findById(film_id);

        if (libraryData.isPresent() && filmData.isPresent()) {

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
                    Film newfilm = filmData.get();
                    newfilm.setDirector(filmRequest.getDirector());
                    newfilm.setTitle(filmRequest.getTitle());
                    newfilm.setDescription(filmRequest.getDescription());
                    newfilm.setGenre(filmRequest.getGenre());
                    newfilm.setWatched(filmRequest.isWatched());
                    newfilm.setPremiereDate(filmRequest.getPremiereDate());
                    return new ResponseEntity<>(filmRepository.save(newfilm), HttpStatus.OK);
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
