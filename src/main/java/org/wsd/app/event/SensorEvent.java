package org.wsd.app.event;

import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Data
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class SensorEvent implements Serializable {
    private String id;
    private double x;
    private double y;
}
