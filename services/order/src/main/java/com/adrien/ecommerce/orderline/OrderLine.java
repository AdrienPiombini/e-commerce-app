package com.adrien.ecommerce.orderline;

import com.adrien.ecommerce.order.Order;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Getter
@Setter
public class OrderLine {
    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    private Integer productId;
    private double quantity;
}
