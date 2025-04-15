package inpt.aseds.orderservice.services;
import inpt.aseds.orderservice.services.kafkaService.*;

import inpt.aseds.orderservice.client.ProductGrpcClient;
import inpt.aseds.orderservice.model.Order;
import inpt.aseds.orderservice.proto.Product;
import inpt.aseds.orderservice.proto.ProductServiceGrpc;
import inpt.aseds.orderservice.repository.OrderRepository;
import inpt.aseds.orderservice.utils.OrderState;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@AllArgsConstructor
public class OrderService {
    private final ProductGrpcClient productGrpcClient;
    private final OrderRepository orderRepository;
    private final KafkaOrderService kafkaOrderService;

    public Mono<Order> processOrder(Order order) {
        return Mono
                .fromCallable(() -> productGrpcClient.getProductById(order.getProductId()))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(productById -> {
                    if (productById != null) {
                        double price = productById.getProductPrice();
                        order.setState(OrderState.CREATED);
                        order.setPrice(price);

                        return Mono.fromCallable(() -> orderRepository.save(order))
                                .subscribeOn(Schedulers.boundedElastic())
                                .doOnNext(newOrder -> {
                                    System.out.println("Order created with ID: " + newOrder.getId());
                                    kafkaOrderService.processNewOrderEvent(newOrder);
                                });
                    } else {
                        order.setState(OrderState.FAILED);
                        return Mono.fromCallable(() -> orderRepository.save(order))
                                .subscribeOn(Schedulers.boundedElastic())
                                .flatMap(savedOrder -> Mono.error(new Exception("Product not found")));
                    }
                });
    };

    public Mono<Order> getOrderById(Long id) {
        return Mono.fromCallable(() -> orderRepository.findById(id))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(optionalOrder -> {
                    if (optionalOrder.isPresent()) {
                        return Mono.just(optionalOrder.get());
                    } else {
                        return Mono.empty();
                    }
                });

    }
}