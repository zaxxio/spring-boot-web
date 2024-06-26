/*
 * Copyright (c) 2024. Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions: The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.wsd.app.eventsourcing.projection;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.wsd.app.domain.ProductEntity;
import org.wsd.app.eventsourcing.events.product.ProductCreatedEvent;
import org.wsd.app.eventsourcing.events.product.ProductDeletedEvent;
import org.wsd.app.eventsourcing.events.product.ProductUpdatedEvent;
import org.wsd.app.eventsourcing.payload.ProductRestModel;
import org.wsd.app.eventsourcing.query.FetchProductByIdQuery;
import org.wsd.app.eventsourcing.query.FetchProductsQuery;
import org.wsd.app.mapper.ProductMapper;
import org.wsd.app.repository.ProductRepository;

import java.util.List;
import java.util.Optional;


@Data
@Component
@Transactional(rollbackFor = Exception.class)
@ProcessingGroup("product-group")
@RequiredArgsConstructor
public class ProductProjection {
    private final ProductRepository productRepository;

    @EventHandler(payloadType = ProductCreatedEvent.class)
    public void on(ProductCreatedEvent productCreatedEvent) {

        final ProductEntity productEntity = new ProductEntity();
        productEntity.setId(productCreatedEvent.getId());
        productEntity.setName(productCreatedEvent.getName());
        productEntity.setDescription(productCreatedEvent.getDescription());
        productEntity.setPrice(productCreatedEvent.getPrice());

        this.productRepository.save(productEntity);
    }


    @EventHandler(payloadType = ProductUpdatedEvent.class)
    public void on(ProductUpdatedEvent productUpdatedEvent) {
        Optional<ProductEntity> productEntity = productRepository.findById(productUpdatedEvent.getId());
        if (productEntity.isPresent()) {
            ProductEntity productEntityToUpdate = productEntity.get();
            BeanUtils.copyProperties(productUpdatedEvent, productEntityToUpdate);
            this.productRepository.save(productEntityToUpdate);
        }
    }

    @EventHandler(payloadType = ProductDeletedEvent.class)
    public void on(ProductDeletedEvent productDeletedEvent) {
        Optional<ProductEntity> productEntity = productRepository.findById(productDeletedEvent.getId());
        if (productEntity.isPresent()) {
            ProductEntity productEntityToDelete = productEntity.get();
            BeanUtils.copyProperties(productDeletedEvent, productEntityToDelete);
            this.productRepository.delete(productEntityToDelete);
        }
    }


    @QueryHandler
    public List<ProductRestModel> handle(FetchProductsQuery fetchProductsQuery) {
        List<ProductEntity> productEntities = this.productRepository.findAll();
        return productEntities.stream()
                .map(ProductMapper.Instance::toProductRestModel)
                .toList();
    }

    @QueryHandler
    public ProductRestModel handle(FetchProductByIdQuery fetchProductByIdQuery) {
        if (this.productRepository.findById(fetchProductByIdQuery.getId()).isPresent()) {
            ProductEntity productEntity = this.productRepository.findById(fetchProductByIdQuery.getId()).get();
            ProductRestModel productRestModel = new ProductRestModel();
            BeanUtils.copyProperties(productEntity, productRestModel);
            return productRestModel;
        }
        throw new RuntimeException("Product not found");
    }
}
