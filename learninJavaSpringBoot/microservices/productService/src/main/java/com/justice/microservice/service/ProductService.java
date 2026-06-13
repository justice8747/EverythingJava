package com.justice.microservice.service;

import com.justice.microservice.dto.ProductRequest;
import com.justice.microservice.dto.ProductResponse;
import com.justice.microservice.model.Product;
import com.justice.microservice.repo.ProductRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepo productRepo;

    public ProductResponse createProduct(ProductRequest productRequest){
        Product product = Product.builder()
                .name(productRequest.name())
                .description(productRequest.description())
                .price(productRequest.price())
                .build();
        productRepo.save(product);
        log.info("Product Created successfully");

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice());
    }

    public List<ProductResponse> getAllProducts() {
       return productRepo.findAll()
                .stream()
                .map(product -> new ProductResponse(
                        product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getPrice()))
                .toList();

    }
}
