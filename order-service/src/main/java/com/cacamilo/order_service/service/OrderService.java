package com.cacamilo.order_service.service;

import com.cacamilo.order_service.api.dto.CreateOrderRequest;
import com.cacamilo.order_service.api.dto.OrderResponse;
import com.cacamilo.order_service.domain.OrderEntity;
import com.cacamilo.order_service.domain.OrderEvent;
import com.cacamilo.order_service.domain.OrderItem;
import com.cacamilo.order_service.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {

        OrderEntity order = OrderEntity.create(request.getCustomerId());

        for (var reqItem : request.getItems()) {
            OrderItem item = new OrderItem(
                    reqItem.getProductId(),
                    reqItem.getName(),
                    reqItem.getQuantity(),
                    reqItem.getPrice()
            );

            order.addItem(item);
        }

        order.recalculateTotal();
        OrderEvent event = OrderEvent.orderCreated(
                order.getId(),
                order.getCustomerId()
        );
        order.addEvent(event);
        orderRepository.save(order);
        log.info("[{}] order-id: [{}] (pending persistence)", event.getEventType(), order.getId());
        return OrderResponse.builder()
                .orderId(order.getId())
                .total(order.getTotalAmount())
                .createdAt(order.getCreatedAt())
                .status(order.getStatus())
                .build();
    }

}
