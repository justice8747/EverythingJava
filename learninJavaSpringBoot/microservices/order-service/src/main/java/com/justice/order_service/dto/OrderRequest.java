package com.justice.order_service.dto;

import java.math.BigDecimal;

public record OrderRequest(
        Long id,
        String orderNumber,
        String skuCode,
        BigDecimal price,
        Integer quantity,
        UserDetils userDetails
) {
    public record UserDetils(String email, String firstName, String lastName){}
}
