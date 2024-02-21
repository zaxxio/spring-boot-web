package org.wsd.app.event;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "sensor")
public class SensorEvent implements Serializable {
    @Id
    private String id;
    private double x;
    private double y;
    private Instant instant = Instant.now();
}
