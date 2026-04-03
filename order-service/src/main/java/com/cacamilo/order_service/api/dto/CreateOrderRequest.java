package com.cacamilo.order_service.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CreateOrderRequest {

    private UUID customerId;
    private List<ItemDto> items;

}
