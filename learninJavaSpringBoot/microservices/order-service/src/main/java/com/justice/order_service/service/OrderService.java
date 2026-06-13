package com.justice.order_service.service;

import com.justice.order_service.client.InventoryClient;
import com.justice.order_service.dto.OrderRequest;
import com.justice.order_service.model.Order;
import com.justice.order_service.repo.OrderRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepo orderRepo;
    private final InventoryClient inventoryClient;

    public void placeOrder(OrderRequest orderRequest){
       var isProductInStock = inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());

        if(isProductInStock){
            Order order = new Order();
            order.setOrderNumber(UUID.randomUUID().toString());
            order.setPrice(orderRequest.price());
            order.setSkuCode(orderRequest.skuCode());
            order.setQuantity(orderRequest.quantity());

            orderRepo.save(order);
        }else{
            throw new RuntimeException("Product With The SkuCode " + orderRequest.skuCode() + " Is Not In Stock" );
        }
    }
}
