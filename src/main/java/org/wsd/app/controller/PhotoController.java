package org.wsd.app.controller;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.wsd.app.config.BulkheadConfiguration;
import org.wsd.app.config.LimiterConfig;
import org.wsd.app.domain.PhotoEntity;
import org.wsd.app.feign.PhotoClient;
import org.wsd.app.photo.Photo;
import org.wsd.app.repository.PhotoRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Log4j2
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "BEARER_TOKEN")
public class PhotoController {
    private final PhotoClient photoClient;
    private final PhotoRepository photoRepository;

    @GetMapping("/photo")
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    @RateLimiter(name = LimiterConfig.PHOTO_SERVICE_API, fallbackMethod = "fallback")
    @Bulkhead(name = BulkheadConfiguration.PHOTO_SERVICE_API, fallbackMethod = "fallback")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getPhoto() {
        Optional<PhotoEntity> photo = this.photoRepository.findById(1L);
        Optional<PhotoEntity> photo1 = this.photoRepository.findById(1L);
        Optional<PhotoEntity> photo2 = this.photoRepository.findById(1L);
        Optional<PhotoEntity> photo3 = this.photoRepository.findById(1L);
        return ResponseEntity.status(HttpStatus.OK)
                .body(photoClient.getPhoto());
    }


    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public ResponseEntity<List<Photo>> fallback(Exception ex) {


        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body(Collections.emptyList());
    }

}
