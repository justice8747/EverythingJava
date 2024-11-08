package com.justice.simplyWebApp.controllers;

import com.justice.simplyWebApp.model.Product;
import com.justice.simplyWebApp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class productController {
    @Autowired
    ProductService service;

    @GetMapping("/products")
    public List<Product> getProducts(){
        return service.getProducts();
    }

    @GetMapping("/products/{productId}")
    public Product getProductById( @PathVariable int productId){
        return service.getProductById(productId);
    }

    @PostMapping("/products")
    public void addProduct( @RequestBody Product product){
        service.addproducts(product);
    }

    @PutMapping("/products")
    public void updatProduct(@RequestBody Product product){
        service.updateProduct(product);
    }

    @DeleteMapping("/products/{productId}")
    public void deleteProduct( @PathVariable int productId){
        service.deleetProduct(productId);
    }

}
