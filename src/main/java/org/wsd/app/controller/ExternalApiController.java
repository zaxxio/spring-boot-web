package org.wsd.app.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wsd.app.http.PhotoHttpService;
import org.wsd.app.http.Post;

import java.util.List;

@RestController
@RequestMapping("/api/external")
@RequiredArgsConstructor
@Tag(name = "External API Controller")
@SecurityRequirement(name = "BEARER_TOKEN")
public class ExternalApiController {
    private final PhotoHttpService photoHttpService;

    @GetMapping("/posts")
    public List<Post> getPosts() {
        return photoHttpService.findAll();
    }

}
