package org.wsd.app.feign;

import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.wsd.app.config.FeignConfig;
import org.wsd.app.photo.Photo;

import java.util.List;

@FeignClient(name = "PHOTO-SERVICE", path = "/photo-service", configuration = FeignConfig.class)
public interface PhotoClient {
    @GetMapping("/photo/all")
    @Retry(name = "photo-service-api", fallbackMethod = "getPhotoException")
    List<Photo> getPhoto();

    default List<Photo> getPhotoException(Exception e) {
        return List.of();
    }
}
