package com.cacamilo.order_service.domain;

public enum OrderStatus {

    CREATED,
    PAYMENT_PENDING,
    PAYMENT_APPROVED,
    PAYMENT_REJECTED,
    PREPARING,
    READY,
    DELIVERED

}
