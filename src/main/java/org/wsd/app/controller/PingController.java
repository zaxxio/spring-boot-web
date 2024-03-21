package org.wsd.app.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wsd.app.grpc.PingService;

@RestController
@RequiredArgsConstructor
@Tag(name = "Ping Pong Controller")
@SecurityRequirement(name = "BEARER_TOKEN")
@RequestMapping("/api/ping")
public class PingController {
    private final PingService pingService;
    @GetMapping("/{pingId}")
    @ResponseStatus(HttpStatus.FOUND)
    public ResponseEntity<?> getOrderById(@PathVariable("pingId") int pingId) {
        return ResponseEntity.
                ok(pingService.ping());
    }
}
