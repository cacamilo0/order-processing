package com.cacamilo.order_service.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CreateOrderRequest {

    @NotNull(message = "customerId is required")
    private UUID customerId;

    @NotNull(message = "items is required")
    @NotEmpty(message = "items cannot be empty")
    @Valid
    private List<ItemDto> items;

}
