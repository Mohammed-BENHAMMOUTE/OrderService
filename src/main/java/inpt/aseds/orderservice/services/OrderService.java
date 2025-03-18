package inpt.aseds.orderservice.services;

import inpt.aseds.orderservice.client.ProductGrpcClient;
import inpt.aseds.orderservice.proto.Order;
import inpt.aseds.orderservice.proto.Product;
import inpt.aseds.orderservice.proto.ProductServiceGrpc;
import inpt.aseds.orderservice.repository.OrderRepository;
import inpt.aseds.orderservice.utils.OrderState;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderService {
    private final ProductGrpcClient productGrpcClient;
    private final OrderRepository orderRepository;

    public void processOrder(inpt.aseds.orderservice.model.Order order) throws Exception {
        Product productById = productGrpcClient.getProductById(order.getId());

        if(productById != null) {
            double price = productById.getProductPrice();
            OrderState state = OrderState.PROCESSING;
            order.setState(state);
            order.setPrice(price);
            orderRepository.save(order);
        } else {
            OrderState state = OrderState.FAILED;
            order.setState(state);
            orderRepository.save(order);
            throw new Exception("Product not found");
        }
    }

}
