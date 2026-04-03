package com.cacamilo.order_service.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class ItemDto {

    private UUID productId;
    private String name;
    private Integer quantity;
    private BigDecimal price;

}
