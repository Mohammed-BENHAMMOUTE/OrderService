package inpt.aseds.orderservice.services.kafkaService;

import inpt.aseds.orderservice.events.OrderEvent;
import inpt.aseds.orderservice.events.ProductEvent;
import inpt.aseds.orderservice.model.Order;
import inpt.aseds.orderservice.repository.OrderRepository;
import inpt.aseds.orderservice.utils.OrderState;
import inpt.aseds.orderservice.utils.ProductState;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service @RequiredArgsConstructor
@Slf4j
public class KafkaOrderService {
    private final OrderRepository orderRepository;
    private final KafkaTemplate<String , Object> kafkaTemplate;

    @Transactional
    public void processNewOrderEvent(Order order){
        order.setState(OrderState.CREATED);
        Order savedOrder = orderRepository.save(order);
        log.info("Order {} is created." , savedOrder.getId());
        // Send order event to Kafka
        OrderEvent orderEvent = new OrderEvent(savedOrder.getId(), savedOrder.getProductId() , savedOrder.getQuantity());
        kafkaTemplate.send("order-events" , orderEvent);
    }

    @KafkaListener(topics = "product-events", groupId = "product-service")
    @Transactional
    public Order processProductEvent(ProductEvent productEvent) {
        // Find the order
        Optional<Order> optionalOrder = orderRepository.findById(productEvent.getOrderId());
        if (optionalOrder.isEmpty()) {
            log.warn("Non-existent order ID: {}", productEvent.getOrderId());
            return null;
        }

        // Update the order state
        Order order = optionalOrder.get();
        if (productEvent.getState() == ProductState.AVAILABLE) {
            order.setState(OrderState.PROCESSING);
        } else if (productEvent.getState() == ProductState.OUT_OF_STOCK) {
            order.setState(OrderState.FAILED);
        } else {
            log.error("Unknown state: {}", productEvent.getState());
        }

        // Save and return the updated order
        return orderRepository.save(order);
    }
}
