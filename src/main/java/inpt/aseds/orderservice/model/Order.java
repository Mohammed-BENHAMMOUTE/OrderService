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
    @OneToOne(cascade = CascadeType.ALL , fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "product_price", nullable = false)
    @Positive
    private double price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderState state;
}

