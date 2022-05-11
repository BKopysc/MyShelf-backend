package com.myshelf.MyShelf.controllers;

import com.myshelf.MyShelf.models.Library;
import com.myshelf.MyShelf.models.User;
import com.myshelf.MyShelf.repository.LibraryRepository;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
public class LibraryController {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    LibraryRepository libraryRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/libraries")
    public String TestLib(){
        return "All libs!";
    }

//    @GetMapping("/libraries?uid={id}")
//    public ResponseEntity<Library> getLibraryByUserId(@PathVariable("id") long id) {
//        System.out.println("Get library by uid");
//        Optional<Library> libraryData = libraryRepository.findByUserId(id);
//
//        if (libraryData.isPresent()) {
//            return new ResponseEntity<>(libraryData.get(), HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }

    @GetMapping("/libraries/{id}")
    public ResponseEntity<Library> getLibraryById(@PathVariable("id") long id, @RequestHeader(name="Authorization", required = false) String token) {
        System.out.println("Get library id");

        Optional<Library> libraryData = libraryRepository.findById(id);

        if (libraryData.isPresent()) {

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
                    return new ResponseEntity<>(libraryData.get(), HttpStatus.OK);
                }
                else
                {
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }
            }
            else{

                return new ResponseEntity<>(libraryData.get(), HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/libraries/by_user/{user_name}")
    public ResponseEntity<Long> getLibraryById(@PathVariable("user_name") String userName) {

        System.out.println("Get library id by username: "+ userName);

        Optional<User> userData = userRepository.findByUsername(userName);

        if (userData.isPresent()) {
                //check if ids are the same
                long lib_uid = userData.get().getLibrary().getId();
                System.out.println("Found id: " + lib_uid);
                return new ResponseEntity<>(lib_uid, HttpStatus.OK);
            }

        else {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
    }

    @PutMapping("/libraries/{id}")
        public ResponseEntity<Library> changeLibraryPrivacy(@PathVariable("id") long id, @RequestHeader(name="Authorization") String token,
                                                            @RequestBody Library library) {
            System.out.println("Change library privacy");
            Optional<Library> libraryData = libraryRepository.findById(id);

            if (libraryData.isPresent()) {

                Library _library = libraryData.get();

                long lib_uid = _library.getUser().getId();
                String username = "";

                try {
                    username = jwtUtils.getUsernameFromBearer(token);
                } catch (Exception e) {
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }

                long uid = userRepository.findByUsername(username).get().getId();

                if (lib_uid == uid) {
                    _library.setPrivate(library.getIsPrivate());
                    return new ResponseEntity<>(libraryRepository.save(_library), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }
            }
             else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }


//    //Library bylo juz tworzone przy zakladaniu konta
//    @PostMapping("/libraries")
//    public ResponseEntity<Library> createLibrary(@RequestBody Library library){
//        try {
//            Library _library = libraryRepository
//                    .save(new Library(library., library.getDescription(), false));
//            return new ResponseEntity<>(_tutorial, HttpStatus.CREATED);
//        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }



}
