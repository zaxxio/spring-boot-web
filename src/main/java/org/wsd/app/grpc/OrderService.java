/*
 * Copyright (c) of Partha Sutradhar 2024.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.wsd.app.grpc;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.extern.log4j.Log4j2;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import org.wsd.app.proto.OrderRequest;
import org.wsd.app.proto.OrderResponse;
import org.wsd.app.proto.OrderServiceGrpc;

import java.util.Iterator;

@Log4j2
@Service
public class OrderService {

    @GrpcClient("grpc-client")
    public OrderServiceGrpc.OrderServiceBlockingStub orderServiceBlockingStub;

    public void getOrderById(int orderId) {
        log.info("Order Request Id : " + orderId);
        OrderRequest request = OrderRequest.newBuilder().setOrderId(orderId).build();
        OrderResponse orderResponse = orderServiceBlockingStub.getOrder(request);
        log.info("Order Response : " + orderResponse);
    }

    public void getOrderBy(int orderId) {
        log.info("Order Request Id : " + orderId);
        try {
            OrderRequest request = OrderRequest.newBuilder().setOrderId(orderId).build();
            Iterator<OrderResponse> orderResponse = orderServiceBlockingStub.getServerSideOrderStreaming(request);
            while (orderResponse.hasNext()) {
                OrderResponse response = orderResponse.next();
                log.info("Order Name {} and Price {}", response.getOrderName(), response.getOrderPrice());
            }
        } catch (StatusRuntimeException statusRuntimeException) {
            if (statusRuntimeException.getStatus().getCode().toStatus() == Status.NOT_FOUND) {
                log.info("Not Found Error ");
            }else {
                log.info("Error");
            }
        } catch (Exception ex) {
            log.info("Error");
        }
    }

}
