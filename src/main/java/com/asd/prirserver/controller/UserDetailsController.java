package com.asd.prirserver.controller;


import com.asd.prirserver.service.UserDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserDetailsController {


    private final UserDetailsService userDetailsService;

    public UserDetailsController(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll()
    {
        return  userDetailsService.getAll();
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchUserByUserName(@RequestParam("username")String username)
    {

        return userDetailsService.searchByUserName(username);
    }

    @GetMapping("avatars")
    public ResponseEntity<?> getAvatars()
    {
        return  userDetailsService.getListAvatars();
    }



}
