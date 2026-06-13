package com.justice.inventory_service.service;

import com.justice.inventory_service.repo.InventoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepo inventoryRepo;

    public Boolean isInStock(String skuCode, Integer quantity){
        return inventoryRepo.existsBySkuCodeAndQuantityIsGreaterThanEqual(skuCode, quantity);
    }

}
