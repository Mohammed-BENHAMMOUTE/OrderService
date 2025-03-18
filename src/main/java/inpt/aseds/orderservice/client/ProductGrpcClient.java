package inpt.aseds.orderservice.client;

import inpt.aseds.orderservice.proto.GetProductRequest;
import inpt.aseds.orderservice.proto.Product;
import inpt.aseds.orderservice.proto.ProductServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

@Component
public class ProductGrpcClient {
    private ManagedChannel channel;
    private ProductServiceGrpc.ProductServiceBlockingStub blockingStub;
    
    @PostConstruct
    private void init() {
        channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();
        blockingStub = ProductServiceGrpc.newBlockingStub(channel);
    }

    public Product getProductById(long id) {
        return blockingStub.getProductById(GetProductRequest.newBuilder().setId(id).build());
    }

    @PreDestroy
    private void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }
}