package inpt.aseds.orderservice.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "products")
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "product_id")
    private long productId;

    @Column(name = "product_name", nullable = false)
    @Size(max=100)
    private String productName;

    @Column(name = "product_price", nullable = false)
    @Positive
    private double productPrice;

    @Column(name = "product_quantity" , nullable = false)
    @Positive
    private int productQuantity;
}

