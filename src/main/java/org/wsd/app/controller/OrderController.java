package org.wsd.app.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wsd.app.grpc.OrderService;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "BEARER_TOKEN")
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/{orderId}")
    @ResponseStatus(HttpStatus.FOUND)
    public ResponseEntity<?> getOrderById(@PathVariable("orderId") int orderId) {
        orderService.getOrderBy(orderId);
        // orderService.getOrderById(orderId);
        return ResponseEntity.
                ok("Success");
    }

}
