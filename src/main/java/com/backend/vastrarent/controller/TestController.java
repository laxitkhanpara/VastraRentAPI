package com.backend.vastrarent.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lol")
@RequiredArgsConstructor
public class TestController {

    @GetMapping
    public String getString() {
        return "Hi"; // Fixed spelling for clarity
    }
}
