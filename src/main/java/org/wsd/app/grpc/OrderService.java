package org.wsd.app.grpc;

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
    OrderServiceGrpc.OrderServiceBlockingStub orderServiceBlockingStub;

    public void getOrderById(int orderId) {
        log.info("Order Request Id : " + orderId);
        OrderRequest request = OrderRequest.newBuilder().setOrderId(orderId).build();
        OrderResponse orderResponse = orderServiceBlockingStub.getOrder(request);
        log.info("Order Response : " + orderResponse);
    }

    public void getOrderBy(int orderId) {
        log.info("Order Request Id : " + orderId);
        OrderRequest request = OrderRequest.newBuilder().setOrderId(orderId).build();
        Iterator<OrderResponse> orderResponse = orderServiceBlockingStub.getServerSideOrderStreaming(request);
        while (orderResponse.hasNext()) {
            OrderResponse response = orderResponse.next();
            log.info("Order Name {} and Price {}", response.getOrderName(), response.getOrderPrice());
        }
    }

}
