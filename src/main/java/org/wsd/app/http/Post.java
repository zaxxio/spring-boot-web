package org.wsd.app.http;

import lombok.Data;

@Data
public class Post {
    private String userId;
    private String id;
    private String title;
    private String body;
}
