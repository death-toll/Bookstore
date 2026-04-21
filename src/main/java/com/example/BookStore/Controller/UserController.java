package com.example.BookStore.Controller;

import com.example.BookStore.Repository.User;
import com.example.BookStore.Repository.UserRepo;
import com.example.BookStore.Service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserRepo userrepo;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtService jwtService;

    @PostMapping("/register")
    public User register(@RequestBody User u1){
        return userrepo.save(u1);

    }

    @PostMapping("/login")
    public String login(@RequestBody User user){
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

            if (authentication.isAuthenticated()) {
                return jwtService.generateToken(user.getUsername());
            }
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login failed");
        } catch (AuthenticationException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username/password");
        }
    }

}
