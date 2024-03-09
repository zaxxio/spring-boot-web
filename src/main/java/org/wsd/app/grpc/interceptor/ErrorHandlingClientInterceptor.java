package org.wsd.app.grpc.interceptor;

import io.grpc.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class ErrorHandlingClientInterceptor implements ClientInterceptor {

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
            MethodDescriptor<ReqT, RespT> method,
            CallOptions callOptions,
            Channel next) {
        ClientCall<ReqT, RespT> call = next.newCall(method, callOptions);

        return new ForwardingClientCall.SimpleForwardingClientCall<>(call) {
            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                Listener<RespT> wrappedListener = new ForwardingClientCallListener.SimpleForwardingClientCallListener<>(responseListener) {
                    @Override
                    public void onClose(Status status, Metadata trailers) {
                        if (!status.isOk()) {
                            // Handle error status (e.g., logging, convert to a custom exception, etc.)
                            // Example: Log error details
                            System.err.println("gRPC call failed with status: " + status);
                        }
                        super.onClose(status, trailers);
                    }
                };
                super.start(wrappedListener, headers);
            }
        };
    }
}
