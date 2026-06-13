package com.justice.microservice.repo;

import com.justice.microservice.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepo extends MongoRepository<Product, String> {
}
