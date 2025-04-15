package inpt.aseds.orderservice.controller;
    import inpt.aseds.orderservice.model.Order;
    import inpt.aseds.orderservice.services.OrderService;
    import lombok.AllArgsConstructor;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;
    import reactor.core.publisher.Mono;

    @AllArgsConstructor
    @RestController
    @RequestMapping("/orders")
    public class OrderController {
        private final OrderService orderService;

        @PostMapping("/process")
        public Mono<ResponseEntity<Object>> processOrder(@RequestBody Order order) {
            if (order == null) {
                return Mono.just(new ResponseEntity<>("Order cannot be null", HttpStatus.BAD_REQUEST));
            }

            return orderService.processOrder(order)
                    .map(createdOrder -> new ResponseEntity<Object>(createdOrder, HttpStatus.OK))
                    .onErrorResume(e -> Mono.just(new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST)));
        }
        @GetMapping("/{id}")
        public Mono<ResponseEntity<Order>> getOrderById(@PathVariable Long id) {
            return orderService.getOrderById(id)
                    .map(order -> new ResponseEntity<>(order, HttpStatus.OK))
                    .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
    }

