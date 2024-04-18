package org.wsd.app.eventsourcing.aggregate;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.wsd.app.eventsourcing.command.CreateProductCommand;
import org.wsd.app.eventsourcing.events.ProductCreatedEvent;

import java.io.Serializable;
import java.util.UUID;

@Data
@Aggregate
@NoArgsConstructor
public class ProductAggregate implements Serializable {

    @AggregateIdentifier
    private UUID id;
    private String name;
    private String description;
    private Double price;

    @CommandHandler(payloadType = CreateProductCommand.class)
    public ProductAggregate(CreateProductCommand createProductCommand) {
        final ProductCreatedEvent productCreatedEvent = ProductCreatedEvent.builder()
                .id(createProductCommand.getId())
                .name(createProductCommand.getName())
                .description(createProductCommand.getDescription())
                .price(createProductCommand.getPrice())
                .build();
        AggregateLifecycle.apply(productCreatedEvent);
    }


    @EventSourcingHandler(payloadType = ProductCreatedEvent.class)
    public void on(ProductCreatedEvent productCreatedEvent) {
        this.id = productCreatedEvent.getId();
        this.name = productCreatedEvent.getName();
        this.description = productCreatedEvent.getDescription();
        this.price = productCreatedEvent.getPrice();
    }

}
