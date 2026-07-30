package com.justice.order_service.service;

import com.justice.order_service.client.InventoryClient;
import com.justice.order_service.dto.OrderRequest;
import com.justice.order_service.event.OrderPlacedEvent;
import com.justice.order_service.model.Order;
import com.justice.order_service.repo.OrderRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepo orderRepo;
    private final InventoryClient inventoryClient;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public void placeOrder(OrderRequest orderRequest){
       var isProductInStock = inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());

        if(isProductInStock){
            Order order = new Order();
            order.setOrderNumber(UUID.randomUUID().toString());
            order.setPrice(orderRequest.price());
            order.setSkuCode(orderRequest.skuCode());
            order.setQuantity(orderRequest.quantity());

            orderRepo.save(order);

//            send message to kafka
            OrderPlacedEvent orderplaceEvent = new OrderPlacedEvent(order.getOrderNumber(), orderRequest.userDetails().email());
            log.info("Start - sending OrderPlacedEven {} to kafka topic order-placed", orderplaceEvent);
            kafkaTemplate.send("Order-placed", orderplaceEvent);
            log.info("End - sending OrderPlacedEven {} to kafka topic order-placed", orderplaceEvent);

        }else{
            throw new RuntimeException("Product With The SkuCode " + orderRequest.skuCode() + " Is Not In Stock" );
        }
    }
}
