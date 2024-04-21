package org.wsd.app.eventsourcing.aggregate;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.wsd.app.eventsourcing.command.product.CreateProductCommand;
import org.wsd.app.eventsourcing.command.product.DeleteProductCommand;
import org.wsd.app.eventsourcing.command.product.UpdateProductCommand;
import org.wsd.app.eventsourcing.events.product.ProductCreatedEvent;
import org.wsd.app.eventsourcing.events.product.ProductDeletedEvent;
import org.wsd.app.eventsourcing.events.product.ProductUpdatedEvent;

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
    private Double quantity;

    @CommandHandler(payloadType = CreateProductCommand.class)
    public ProductAggregate(CreateProductCommand createProductCommand) {
        final ProductCreatedEvent productCreatedEvent = ProductCreatedEvent.builder()
                .id(createProductCommand.getId())
                .name(createProductCommand.getName())
                .description(createProductCommand.getDescription())
                .price(createProductCommand.getPrice())
                .quantity(createProductCommand.getQuantity())
                .build();
        AggregateLifecycle.apply(productCreatedEvent);
    }

    @EventSourcingHandler(payloadType = ProductCreatedEvent.class)
    public void on(ProductCreatedEvent productCreatedEvent) {
        this.id = productCreatedEvent.getId();
        this.name = productCreatedEvent.getName();
        this.description = productCreatedEvent.getDescription();
        this.price = productCreatedEvent.getPrice();
        this.quantity = productCreatedEvent.getQuantity();
    }

    @CommandHandler(payloadType = UpdateProductCommand.class)
    public void handle(UpdateProductCommand updateProductCommand) {
        ProductUpdatedEvent productUpdatedEvent = ProductUpdatedEvent.builder()
                .id(updateProductCommand.getId())
                .name(updateProductCommand.getName())
                .description(updateProductCommand.getDescription())
                .price(updateProductCommand.getPrice())
                .quantity(updateProductCommand.getQuantity())
                .build();
        AggregateLifecycle.apply(productUpdatedEvent);
    }

    @EventSourcingHandler(payloadType = ProductUpdatedEvent.class)
    public void on(ProductUpdatedEvent productUpdatedEvent) {
        this.id = productUpdatedEvent.getId();
        this.name = productUpdatedEvent.getName();
        this.description = productUpdatedEvent.getDescription();
        this.price = productUpdatedEvent.getPrice();
        this.quantity = productUpdatedEvent.getQuantity();
    }

    @CommandHandler(payloadType = DeleteProductCommand.class)
    public void handle(DeleteProductCommand deleteProductCommand) {
        ProductDeletedEvent productDeletedEvent = ProductDeletedEvent.builder()
                .id(deleteProductCommand.getId())
                .name(deleteProductCommand.getName())
                .description(deleteProductCommand.getDescription())
                .price(deleteProductCommand.getPrice())
                .quantity(deleteProductCommand.getQuantity())
                .build();
        AggregateLifecycle.apply(productDeletedEvent);
    }

    @EventSourcingHandler(payloadType = ProductDeletedEvent.class)
    public void on(ProductDeletedEvent productDeletedEvent) {
        this.id = productDeletedEvent.getId();
        this.name = productDeletedEvent.getName();
        this.description = productDeletedEvent.getDescription();
        this.price = productDeletedEvent.getPrice();
        this.quantity = productDeletedEvent.getQuantity();
    }

}
