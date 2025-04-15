package inpt.aseds.orderservice.events;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data@AllArgsConstructor@NoArgsConstructor
public class OrderEvent {
    private Long orderId;
    private Long productId;
    private int quantity;
}
