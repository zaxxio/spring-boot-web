package org.wsd.app.photo;

import lombok.Data;

import java.util.UUID;

@Data
public class Photo {
    private Long id;
    private String filename;
    private UUID uuid = UUID.randomUUID();
}
