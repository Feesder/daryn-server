package com.boyapcky.bookworld.controller;

import com.boyapcky.bookworld.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping(params = {"id"})
    public ResponseEntity getUserById(@RequestParam Long id) {
        try {
            return ResponseEntity.ok(userService.findById(id));
        } catch(Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body("An error has occurred");
        }
    }

    @GetMapping()
    public ResponseEntity getUsers() {
        try {
            return ResponseEntity.ok(userService.getAll());
        } catch(Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body("An error has occurred");
        }
    }

    @DeleteMapping()
    public ResponseEntity getUsers(@RequestParam Long id) {
        try {
            userService.delete(id);
            return ResponseEntity.ok("user deleted successfully");
        } catch(Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body("An error has occurred");
        }
    }

}
