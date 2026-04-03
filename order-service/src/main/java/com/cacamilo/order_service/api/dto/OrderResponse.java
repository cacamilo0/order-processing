package com.cacamilo.order_service.api.dto;

import com.cacamilo.order_service.domain.OrderStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class OrderResponse {

    private UUID orderId;
    private OrderStatus status;
    private BigDecimal total;
    private LocalDateTime createdAt;

}
