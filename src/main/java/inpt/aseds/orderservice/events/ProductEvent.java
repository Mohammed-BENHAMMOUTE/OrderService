package inpt.aseds.orderservice.events;


import inpt.aseds.orderservice.utils.ProductState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductEvent {
    private long orderId;
    private ProductState state;
}

