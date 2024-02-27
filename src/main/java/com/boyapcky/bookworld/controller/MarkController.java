package com.boyapcky.bookworld.controller;

import com.boyapcky.bookworld.model.MarkRequest;
import com.boyapcky.bookworld.service.MarkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping(value = "api/v1/mark")
@Slf4j
public class MarkController {
    @Autowired
    public MarkService markService;

    @PostMapping
    public ResponseEntity create(@RequestBody MarkRequest markRequests) {
        try {
            markService.create(markRequests);
            return ResponseEntity.ok().body("Success");
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body("An error has occurred");
        }
    }

    @PutMapping
    public ResponseEntity update(@RequestParam(name = "id") Long id, @RequestBody String roadType) {
        try {
            markService.update(id, roadType);
            return ResponseEntity.ok().body("Success");
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body("An error has occurred");
        }
    }

    @GetMapping
    public ResponseEntity get() {
        try {
            return ResponseEntity.ok().body(markService.getAll());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body("An error has occurred");
        }
    }

    @GetMapping(params = { "time" })
    public ResponseEntity get(@RequestParam("time") @DateTimeFormat(iso  = DateTimeFormat.ISO.DATE) Date date) {
        try {
            return ResponseEntity.ok().body(markService.getByTime(date));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body("An error has occurred");
        }
    }
}
