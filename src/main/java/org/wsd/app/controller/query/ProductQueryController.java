/*
 * Copyright (c) 2024. Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions: The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.wsd.app.controller.query;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.*;
import org.wsd.app.config.SwaggerConfig;
import org.wsd.app.eventsourcing.payload.ProductRestModel;
import org.wsd.app.eventsourcing.query.FetchProductByIdQuery;
import org.wsd.app.eventsourcing.query.FetchProductsQuery;

import java.util.List;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
@Tag(name = "Product Query Controller")
@SecurityRequirement(name = SwaggerConfig.BEARER_TOKEN)
public class ProductQueryController {

    private final QueryGateway queryGateway;

    @GetMapping
    public List<ProductRestModel> getProducts() {
        final FetchProductsQuery query = new FetchProductsQuery();
        return this.queryGateway.query(query, ResponseTypes.multipleInstancesOf(ProductRestModel.class)).join();
    }

    @GetMapping("/{productId}")
    public ProductRestModel getProduct(@PathVariable("productId") String productId) {
        final FetchProductByIdQuery fetchProductByIdQuery = new FetchProductByIdQuery();
        fetchProductByIdQuery.setId(UUID.fromString(productId));
        return this.queryGateway.query(fetchProductByIdQuery, ResponseTypes.instanceOf(ProductRestModel.class)).join();
    }

}
