package org.wsd.app.grpc;

import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.log4j.Log4j2;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import org.wsd.app.proto.PingPongServiceGrpc;
import org.wsd.app.proto.PingRequest;
import org.wsd.app.proto.PingResponse;

import java.time.Duration;
import java.time.Instant;

@Log4j2
@Service
public class PingService {
    @GrpcClient("ping-pong-client")
    private PingPongServiceGrpc.PingPongServiceBlockingStub serviceBlockingStub;

    @Retry(name = "pingRetry")
    public String ping() {
        final PingRequest request = PingRequest
                .newBuilder()
                .setMessage("Ping")
                .build();

        Instant start = Instant.now();
        PingResponse pingResponse = null;
        for (int i = 0; i < 10000; i++) {
            pingResponse = serviceBlockingStub.pingPong(request);
        }
        Instant end = Instant.now();
        log.info("TIme Taken " + Duration.between(start, end).getSeconds());
        return pingResponse.getMessage();
    }
}