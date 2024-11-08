package com.justice.simplyWebApp.service;

import com.justice.simplyWebApp.model.Product;
import com.justice.simplyWebApp.repos.ProductsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ProductService {
/*
    List<Product> products = new ArrayList<>(
        Arrays.asList(
            new Product(10, "Samsung", 10000),
            new Product(20, "Iphone", 1000),
            new Product(30, "itel", 2000)
    ));
*/
    @Autowired
    ProductsRepo repo;

    public List<Product> getProducts(){
        return repo.findAll();
    }

    public Product getProductById(int productId) {
        return repo.findById(productId).orElse(new Product());

    }

    public void addproducts(Product product){
      repo.save(product);
    }

    public void updateProduct(Product product) {
      repo.save(product);
    }

    public void deleetProduct(int productId) {
        repo.deleteById(productId);
    }
}
