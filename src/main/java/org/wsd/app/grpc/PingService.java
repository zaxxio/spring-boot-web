package org.wsd.app.grpc;

import io.github.resilience4j.retry.annotation.Retry;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import org.wsd.app.proto.PingPongServiceGrpc;
import org.wsd.app.proto.PingRequest;
import org.wsd.app.proto.PingResponse;

@Service
public class PingService {
    @GrpcClient("ping-pong-client")
    private PingPongServiceGrpc.PingPongServiceBlockingStub serviceBlockingStub;

    @Retry(name = "pingRetry")
    public String ping() {
        PingRequest request = PingRequest
                .newBuilder()
                .setMessage("Ping")
                .build();
        PingResponse pingResponse = serviceBlockingStub.pingPong(request);
        return pingResponse.getMessage();
    }
}