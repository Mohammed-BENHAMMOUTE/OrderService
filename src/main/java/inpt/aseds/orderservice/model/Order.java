package inpt.aseds.orderservice.model;

import inpt.aseds.orderservice.utils.OrderState;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
@Getter
@Setter
public class Order {
    @Id @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "product_id", nullable = false)
    private long productId;

    @Column(name = "product_price", nullable = false)
    @Positive
    private double price;

    @Column(name = "quantity", nullable = false)
    @Positive
    private int quantity = 1;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderState state;
}