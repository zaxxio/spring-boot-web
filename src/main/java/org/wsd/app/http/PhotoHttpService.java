package org.wsd.app.http;

import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

@HttpExchange
public interface PhotoHttpService {
    @GetExchange("/posts")
    List<Post> findAll();
}
