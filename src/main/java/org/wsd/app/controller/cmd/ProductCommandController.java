/*
 * Copyright (c) 2024. Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions: The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.wsd.app.controller.cmd;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wsd.app.config.SwaggerConfig;
import org.wsd.app.eventsourcing.command.product.CreateProductCommand;
import org.wsd.app.eventsourcing.command.product.DeleteProductCommand;
import org.wsd.app.eventsourcing.command.product.UpdateProductCommand;
import org.wsd.app.eventsourcing.payload.ProductRestModel;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
@Tag(name = "Product Command Controller")
@SecurityRequirement(name = SwaggerConfig.BEARER_TOKEN)
public class ProductCommandController {

    private final CommandGateway commandGateway;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createProduct(@RequestBody ProductRestModel productRestModel) {

        final UUID randomUUID = UUID.randomUUID();

        final CreateProductCommand createProductCommand = CreateProductCommand.builder()
                .id(randomUUID)
                .name(productRestModel.getName())
                .description(productRestModel.getDescription())
                .price(productRestModel.getPrice())
                .quantity(productRestModel.getQuantity())
                .build();

        return ResponseEntity.accepted().body(this.commandGateway.sendAndWait(createProductCommand));
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateProduct(@RequestBody ProductRestModel productRestModel) {

        final UpdateProductCommand updateProductCommand = UpdateProductCommand.builder()
                .id(productRestModel.getId())
                .name(productRestModel.getName())
                .description(productRestModel.getDescription())
                .price(productRestModel.getPrice())
                .quantity(productRestModel.getQuantity())
                .build();

        return ResponseEntity.accepted()
                .body(this.commandGateway.sendAndWait(updateProductCommand));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> deleteProduct(@RequestBody ProductRestModel productRestModel) {

        final DeleteProductCommand deleteProductCommand = DeleteProductCommand.builder()
                .id(productRestModel.getId())
                .name(productRestModel.getName())
                .description(productRestModel.getDescription())
                .price(productRestModel.getPrice())
                .quantity(productRestModel.getQuantity())
                .build();

        this.commandGateway.send(deleteProductCommand);

        return ResponseEntity.noContent().build();
    }

}
