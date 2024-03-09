package org.wsd.app.proto;

import jakarta.annotation.Generated;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 *
 */
@Generated(
        value = "by gRPC proto compiler (version 1.58.0)",
        comments = "Source: schema.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class OrderServiceGrpc {

    private OrderServiceGrpc() {
    }

    public static final String SERVICE_NAME = "OrderService";

    // Static method descriptors that strictly reflect the proto.
    private static volatile io.grpc.MethodDescriptor<OrderRequest,
            OrderResponse> getGetOrderMethod;

    @io.grpc.stub.annotations.RpcMethod(
            fullMethodName = SERVICE_NAME + '/' + "GetOrder",
            requestType = OrderRequest.class,
            responseType = OrderResponse.class,
            methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
    public static io.grpc.MethodDescriptor<OrderRequest,
            OrderResponse> getGetOrderMethod() {
        io.grpc.MethodDescriptor<OrderRequest, OrderResponse> getGetOrderMethod;
        if ((getGetOrderMethod = OrderServiceGrpc.getGetOrderMethod) == null) {
            synchronized (OrderServiceGrpc.class) {
                if ((getGetOrderMethod = OrderServiceGrpc.getGetOrderMethod) == null) {
                    OrderServiceGrpc.getGetOrderMethod = getGetOrderMethod =
                            io.grpc.MethodDescriptor.<OrderRequest, OrderResponse>newBuilder()
                                    .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                                    .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetOrder"))
                                    .setSampledToLocalTracing(true)
                                    .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                                            OrderRequest.getDefaultInstance()))
                                    .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                                            OrderResponse.getDefaultInstance()))
                                    .setSchemaDescriptor(new OrderServiceMethodDescriptorSupplier("GetOrder"))
                                    .build();
                }
            }
        }
        return getGetOrderMethod;
    }

    private static volatile io.grpc.MethodDescriptor<OrderRequest,
            OrderResponse> getGetServerSideOrderStreamingMethod;

    @io.grpc.stub.annotations.RpcMethod(
            fullMethodName = SERVICE_NAME + '/' + "GetServerSideOrderStreaming",
            requestType = OrderRequest.class,
            responseType = OrderResponse.class,
            methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
    public static io.grpc.MethodDescriptor<OrderRequest,
            OrderResponse> getGetServerSideOrderStreamingMethod() {
        io.grpc.MethodDescriptor<OrderRequest, OrderResponse> getGetServerSideOrderStreamingMethod;
        if ((getGetServerSideOrderStreamingMethod = OrderServiceGrpc.getGetServerSideOrderStreamingMethod) == null) {
            synchronized (OrderServiceGrpc.class) {
                if ((getGetServerSideOrderStreamingMethod = OrderServiceGrpc.getGetServerSideOrderStreamingMethod) == null) {
                    OrderServiceGrpc.getGetServerSideOrderStreamingMethod = getGetServerSideOrderStreamingMethod =
                            io.grpc.MethodDescriptor.<OrderRequest, OrderResponse>newBuilder()
                                    .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
                                    .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetServerSideOrderStreaming"))
                                    .setSampledToLocalTracing(true)
                                    .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                                            OrderRequest.getDefaultInstance()))
                                    .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                                            OrderResponse.getDefaultInstance()))
                                    .setSchemaDescriptor(new OrderServiceMethodDescriptorSupplier("GetServerSideOrderStreaming"))
                                    .build();
                }
            }
        }
        return getGetServerSideOrderStreamingMethod;
    }

    private static volatile io.grpc.MethodDescriptor<OrderRequest,
            OrderResponse> getGetClientSideOrderStreamingMethod;

    @io.grpc.stub.annotations.RpcMethod(
            fullMethodName = SERVICE_NAME + '/' + "GetClientSideOrderStreaming",
            requestType = OrderRequest.class,
            responseType = OrderResponse.class,
            methodType = io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
    public static io.grpc.MethodDescriptor<OrderRequest,
            OrderResponse> getGetClientSideOrderStreamingMethod() {
        io.grpc.MethodDescriptor<OrderRequest, OrderResponse> getGetClientSideOrderStreamingMethod;
        if ((getGetClientSideOrderStreamingMethod = OrderServiceGrpc.getGetClientSideOrderStreamingMethod) == null) {
            synchronized (OrderServiceGrpc.class) {
                if ((getGetClientSideOrderStreamingMethod = OrderServiceGrpc.getGetClientSideOrderStreamingMethod) == null) {
                    OrderServiceGrpc.getGetClientSideOrderStreamingMethod = getGetClientSideOrderStreamingMethod =
                            io.grpc.MethodDescriptor.<OrderRequest, OrderResponse>newBuilder()
                                    .setType(io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
                                    .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetClientSideOrderStreaming"))
                                    .setSampledToLocalTracing(true)
                                    .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                                            OrderRequest.getDefaultInstance()))
                                    .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                                            OrderResponse.getDefaultInstance()))
                                    .setSchemaDescriptor(new OrderServiceMethodDescriptorSupplier("GetClientSideOrderStreaming"))
                                    .build();
                }
            }
        }
        return getGetClientSideOrderStreamingMethod;
    }

    private static volatile io.grpc.MethodDescriptor<OrderRequest,
            OrderResponse> getGetBiDirectionalOrderStreamingMethod;

    @io.grpc.stub.annotations.RpcMethod(
            fullMethodName = SERVICE_NAME + '/' + "GetBiDirectionalOrderStreaming",
            requestType = OrderRequest.class,
            responseType = OrderResponse.class,
            methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
    public static io.grpc.MethodDescriptor<OrderRequest,
            OrderResponse> getGetBiDirectionalOrderStreamingMethod() {
        io.grpc.MethodDescriptor<OrderRequest, OrderResponse> getGetBiDirectionalOrderStreamingMethod;
        if ((getGetBiDirectionalOrderStreamingMethod = OrderServiceGrpc.getGetBiDirectionalOrderStreamingMethod) == null) {
            synchronized (OrderServiceGrpc.class) {
                if ((getGetBiDirectionalOrderStreamingMethod = OrderServiceGrpc.getGetBiDirectionalOrderStreamingMethod) == null) {
                    OrderServiceGrpc.getGetBiDirectionalOrderStreamingMethod = getGetBiDirectionalOrderStreamingMethod =
                            io.grpc.MethodDescriptor.<OrderRequest, OrderResponse>newBuilder()
                                    .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
                                    .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetBiDirectionalOrderStreaming"))
                                    .setSampledToLocalTracing(true)
                                    .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                                            OrderRequest.getDefaultInstance()))
                                    .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                                            OrderResponse.getDefaultInstance()))
                                    .setSchemaDescriptor(new OrderServiceMethodDescriptorSupplier("GetBiDirectionalOrderStreaming"))
                                    .build();
                }
            }
        }
        return getGetBiDirectionalOrderStreamingMethod;
    }

    /**
     * Creates a new async stub that supports all call types for the service
     */
    public static OrderServiceStub newStub(io.grpc.Channel channel) {
        io.grpc.stub.AbstractStub.StubFactory<OrderServiceStub> factory =
                new io.grpc.stub.AbstractStub.StubFactory<OrderServiceStub>() {
                    @Override
                    public OrderServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                        return new OrderServiceStub(channel, callOptions);
                    }
                };
        return OrderServiceStub.newStub(factory, channel);
    }

    /**
     * Creates a new blocking-style stub that supports unary and streaming output calls on the service
     */
    public static OrderServiceBlockingStub newBlockingStub(
            io.grpc.Channel channel) {
        io.grpc.stub.AbstractStub.StubFactory<OrderServiceBlockingStub> factory =
                new io.grpc.stub.AbstractStub.StubFactory<OrderServiceBlockingStub>() {
                    @Override
                    public OrderServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                        return new OrderServiceBlockingStub(channel, callOptions);
                    }
                };
        return OrderServiceBlockingStub.newStub(factory, channel);
    }

    /**
     * Creates a new ListenableFuture-style stub that supports unary calls on the service
     */
    public static OrderServiceFutureStub newFutureStub(
            io.grpc.Channel channel) {
        io.grpc.stub.AbstractStub.StubFactory<OrderServiceFutureStub> factory =
                new io.grpc.stub.AbstractStub.StubFactory<OrderServiceFutureStub>() {
                    @Override
                    public OrderServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
                        return new OrderServiceFutureStub(channel, callOptions);
                    }
                };
        return OrderServiceFutureStub.newStub(factory, channel);
    }

    /**
     *
     */
    public interface AsyncService {

        /**
         *
         */
        default void getOrder(OrderRequest request,
                              io.grpc.stub.StreamObserver<OrderResponse> responseObserver) {
            io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetOrderMethod(), responseObserver);
        }

        /**
         *
         */
        default void getServerSideOrderStreaming(OrderRequest request,
                                                 io.grpc.stub.StreamObserver<OrderResponse> responseObserver) {
            io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetServerSideOrderStreamingMethod(), responseObserver);
        }

        /**
         *
         */
        default io.grpc.stub.StreamObserver<OrderRequest> getClientSideOrderStreaming(
                io.grpc.stub.StreamObserver<OrderResponse> responseObserver) {
            return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getGetClientSideOrderStreamingMethod(), responseObserver);
        }

        /**
         *
         */
        default io.grpc.stub.StreamObserver<OrderRequest> getBiDirectionalOrderStreaming(
                io.grpc.stub.StreamObserver<OrderResponse> responseObserver) {
            return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getGetBiDirectionalOrderStreamingMethod(), responseObserver);
        }
    }

    /**
     * Base class for the server implementation of the service OrderService.
     */
    public static abstract class OrderServiceImplBase
            implements io.grpc.BindableService, AsyncService {

        @Override
        public final io.grpc.ServerServiceDefinition bindService() {
            return OrderServiceGrpc.bindService(this);
        }
    }

    /**
     * A stub to allow clients to do asynchronous rpc calls to service OrderService.
     */
    public static final class OrderServiceStub
            extends io.grpc.stub.AbstractAsyncStub<OrderServiceStub> {
        private OrderServiceStub(
                io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @Override
        protected OrderServiceStub build(
                io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            return new OrderServiceStub(channel, callOptions);
        }

        /**
         *
         */
        public void getOrder(OrderRequest request,
                             io.grpc.stub.StreamObserver<OrderResponse> responseObserver) {
            io.grpc.stub.ClientCalls.asyncUnaryCall(
                    getChannel().newCall(getGetOrderMethod(), getCallOptions()), request, responseObserver);
        }

        /**
         *
         */
        public void getServerSideOrderStreaming(OrderRequest request,
                                                io.grpc.stub.StreamObserver<OrderResponse> responseObserver) {
            io.grpc.stub.ClientCalls.asyncServerStreamingCall(
                    getChannel().newCall(getGetServerSideOrderStreamingMethod(), getCallOptions()), request, responseObserver);
        }

        /**
         *
         */
        public io.grpc.stub.StreamObserver<OrderRequest> getClientSideOrderStreaming(
                io.grpc.stub.StreamObserver<OrderResponse> responseObserver) {
            return io.grpc.stub.ClientCalls.asyncClientStreamingCall(
                    getChannel().newCall(getGetClientSideOrderStreamingMethod(), getCallOptions()), responseObserver);
        }

        /**
         *
         */
        public io.grpc.stub.StreamObserver<OrderRequest> getBiDirectionalOrderStreaming(
                io.grpc.stub.StreamObserver<OrderResponse> responseObserver) {
            return io.grpc.stub.ClientCalls.asyncBidiStreamingCall(
                    getChannel().newCall(getGetBiDirectionalOrderStreamingMethod(), getCallOptions()), responseObserver);
        }
    }

    /**
     * A stub to allow clients to do synchronous rpc calls to service OrderService.
     */
    public static final class OrderServiceBlockingStub
            extends io.grpc.stub.AbstractBlockingStub<OrderServiceBlockingStub> {
        private OrderServiceBlockingStub(
                io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @Override
        protected OrderServiceBlockingStub build(
                io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            return new OrderServiceBlockingStub(channel, callOptions);
        }

        /**
         *
         */
        public OrderResponse getOrder(OrderRequest request) {
            return io.grpc.stub.ClientCalls.blockingUnaryCall(
                    getChannel(), getGetOrderMethod(), getCallOptions(), request);
        }

        /**
         *
         */
        public java.util.Iterator<OrderResponse> getServerSideOrderStreaming(
                OrderRequest request) {
            return io.grpc.stub.ClientCalls.blockingServerStreamingCall(
                    getChannel(), getGetServerSideOrderStreamingMethod(), getCallOptions(), request);
        }
    }

    /**
     * A stub to allow clients to do ListenableFuture-style rpc calls to service OrderService.
     */
    public static final class OrderServiceFutureStub
            extends io.grpc.stub.AbstractFutureStub<OrderServiceFutureStub> {
        private OrderServiceFutureStub(
                io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @Override
        protected OrderServiceFutureStub build(
                io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            return new OrderServiceFutureStub(channel, callOptions);
        }

        /**
         *
         */
        public com.google.common.util.concurrent.ListenableFuture<OrderResponse> getOrder(
                OrderRequest request) {
            return io.grpc.stub.ClientCalls.futureUnaryCall(
                    getChannel().newCall(getGetOrderMethod(), getCallOptions()), request);
        }
    }

    private static final int METHODID_GET_ORDER = 0;
    private static final int METHODID_GET_SERVER_SIDE_ORDER_STREAMING = 1;
    private static final int METHODID_GET_CLIENT_SIDE_ORDER_STREAMING = 2;
    private static final int METHODID_GET_BI_DIRECTIONAL_ORDER_STREAMING = 3;

    private static final class MethodHandlers<Req, Resp> implements
            io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
            io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
            io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
            io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
        private final AsyncService serviceImpl;
        private final int methodId;

        MethodHandlers(AsyncService serviceImpl, int methodId) {
            this.serviceImpl = serviceImpl;
            this.methodId = methodId;
        }

        @Override
        @SuppressWarnings("unchecked")
        public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
            switch (methodId) {
                case METHODID_GET_ORDER:
                    serviceImpl.getOrder((OrderRequest) request,
                            (io.grpc.stub.StreamObserver<OrderResponse>) responseObserver);
                    break;
                case METHODID_GET_SERVER_SIDE_ORDER_STREAMING:
                    serviceImpl.getServerSideOrderStreaming((OrderRequest) request,
                            (io.grpc.stub.StreamObserver<OrderResponse>) responseObserver);
                    break;
                default:
                    throw new AssertionError();
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        public io.grpc.stub.StreamObserver<Req> invoke(
                io.grpc.stub.StreamObserver<Resp> responseObserver) {
            switch (methodId) {
                case METHODID_GET_CLIENT_SIDE_ORDER_STREAMING:
                    return (io.grpc.stub.StreamObserver<Req>) serviceImpl.getClientSideOrderStreaming(
                            (io.grpc.stub.StreamObserver<OrderResponse>) responseObserver);
                case METHODID_GET_BI_DIRECTIONAL_ORDER_STREAMING:
                    return (io.grpc.stub.StreamObserver<Req>) serviceImpl.getBiDirectionalOrderStreaming(
                            (io.grpc.stub.StreamObserver<OrderResponse>) responseObserver);
                default:
                    throw new AssertionError();
            }
        }
    }

    public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
        return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
                .addMethod(
                        getGetOrderMethod(),
                        io.grpc.stub.ServerCalls.asyncUnaryCall(
                                new MethodHandlers<
                                        OrderRequest,
                                        OrderResponse>(
                                        service, METHODID_GET_ORDER)))
                .addMethod(
                        getGetServerSideOrderStreamingMethod(),
                        io.grpc.stub.ServerCalls.asyncServerStreamingCall(
                                new MethodHandlers<
                                        OrderRequest,
                                        OrderResponse>(
                                        service, METHODID_GET_SERVER_SIDE_ORDER_STREAMING)))
                .addMethod(
                        getGetClientSideOrderStreamingMethod(),
                        io.grpc.stub.ServerCalls.asyncClientStreamingCall(
                                new MethodHandlers<
                                        OrderRequest,
                                        OrderResponse>(
                                        service, METHODID_GET_CLIENT_SIDE_ORDER_STREAMING)))
                .addMethod(
                        getGetBiDirectionalOrderStreamingMethod(),
                        io.grpc.stub.ServerCalls.asyncBidiStreamingCall(
                                new MethodHandlers<
                                        OrderRequest,
                                        OrderResponse>(
                                        service, METHODID_GET_BI_DIRECTIONAL_ORDER_STREAMING)))
                .build();
    }

    private static abstract class OrderServiceBaseDescriptorSupplier
            implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
        OrderServiceBaseDescriptorSupplier() {
        }

        @Override
        public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
            return Schema.getDescriptor();
        }

        @Override
        public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
            return getFileDescriptor().findServiceByName("OrderService");
        }
    }

    private static final class OrderServiceFileDescriptorSupplier
            extends OrderServiceBaseDescriptorSupplier {
        OrderServiceFileDescriptorSupplier() {
        }
    }

    private static final class OrderServiceMethodDescriptorSupplier
            extends OrderServiceBaseDescriptorSupplier
            implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
        private final String methodName;

        OrderServiceMethodDescriptorSupplier(String methodName) {
            this.methodName = methodName;
        }

        @Override
        public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
            return getServiceDescriptor().findMethodByName(methodName);
        }
    }

    private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

    public static io.grpc.ServiceDescriptor getServiceDescriptor() {
        io.grpc.ServiceDescriptor result = serviceDescriptor;
        if (result == null) {
            synchronized (OrderServiceGrpc.class) {
                result = serviceDescriptor;
                if (result == null) {
                    serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
                            .setSchemaDescriptor(new OrderServiceFileDescriptorSupplier())
                            .addMethod(getGetOrderMethod())
                            .addMethod(getGetServerSideOrderStreamingMethod())
                            .addMethod(getGetClientSideOrderStreamingMethod())
                            .addMethod(getGetBiDirectionalOrderStreamingMethod())
                            .build();
                }
            }
        }
        return result;
    }
}
