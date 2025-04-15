package inpt.aseds.orderservice.client;

import inpt.aseds.orderservice.proto.GetProductRequest;
import inpt.aseds.orderservice.proto.Product;
import inpt.aseds.orderservice.proto.ProductServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

@Component
public class ProductGrpcClient {
    private ManagedChannel channel;
    private ProductServiceGrpc.ProductServiceBlockingStub blockingStub;

    @Value("${grpc.product-service.host:localhost}")
    private String host;

    @Value("${grpc.product-service.port:9090}")
    private int port;

    @PostConstruct
    private void init() {
        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        blockingStub = ProductServiceGrpc.newBlockingStub(channel);
    }

    public Product getProductById(long id) {
        return blockingStub.getProductById(GetProductRequest.newBuilder().setId(id).build());
    }

    @PreDestroy
    private void shutdown() {
        if(channel != null && !channel.isShutdown()){
            try {
                channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}