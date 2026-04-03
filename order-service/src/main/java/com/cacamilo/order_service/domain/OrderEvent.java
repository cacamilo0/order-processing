package com.cacamilo.order_service.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Getter
@Entity
@Table(name = "order_events")
public class OrderEvent {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

    @Column(name = "event_type", length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderEventType eventType;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "payload", columnDefinition = "jsonb")
    private Map<String, Object> payload;

    @Column(name = "occurred_at", nullable = false, updatable = false)
    private LocalDateTime occurredAt;

    protected OrderEvent() {}

    public OrderEvent(OrderEventType eventType, Map<String, Object> payload) {
        this.eventType = eventType;
        this.payload = payload;
        this.occurredAt = LocalDateTime.now();
    }

    void setOrder(OrderEntity order) {
        this.order = order;
    }

    public static OrderEvent orderCreated(UUID orderId, UUID customerId) {
        return new OrderEvent(
                OrderEventType.ORDER_CREATED,
                Map.of(
                        "orderId", orderId,
                        "customerId", customerId
                )
        );
    }

}
