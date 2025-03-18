package inpt.aseds.orderservice.controller;

import inpt.aseds.orderservice.model.Order;
import inpt.aseds.orderservice.services.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/process")
    public ResponseEntity<?> processOrder(@RequestBody Order order) {
       if(order != null) {
           try {
               orderService.processOrder(order);
               return ResponseEntity.ok().build();
           } catch (Exception e) {
               e.printStackTrace();
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
           }
       }
       return ResponseEntity.badRequest().build();
    }
}
