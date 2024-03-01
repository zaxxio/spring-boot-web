package org.wsd.app.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wsd.app.service.DataService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "BEARER_TOKEN")
public class DataController {
    private final DataService dataService;

    @GetMapping("/")
    public ResponseEntity<List<String>> getData() {
        return ResponseEntity.ok(dataService.getData());
    }
}
