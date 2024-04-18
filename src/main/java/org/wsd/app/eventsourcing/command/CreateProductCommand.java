package org.wsd.app.eventsourcing.command;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
public class CreateProductCommand implements Serializable {
    @TargetAggregateIdentifier
    private final UUID id;
    private final String name;
    private final String description;
    private final Double price;
}
