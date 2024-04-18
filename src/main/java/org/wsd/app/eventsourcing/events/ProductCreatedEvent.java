package org.wsd.app.eventsourcing.events;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
public class ProductCreatedEvent implements Serializable {
    private final UUID id;
    private final String name;
    private final String description;
    private final Double price;
}
