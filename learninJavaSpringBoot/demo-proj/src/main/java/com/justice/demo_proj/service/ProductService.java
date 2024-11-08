package com.justice.demo_proj.service;

import com.justice.demo_proj.model.Product;
import com.justice.demo_proj.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepo repo;

    public List<Product> getAllProducts() {
      return  repo.findAll();
    }

    public Product getProductById( int id) {
        return repo.findById(id).orElse(null);
    }

    public Product addProduct(Product product, MultipartFile imagefile) throws IOException {
        product.setImageName(imagefile.getOriginalFilename());
        product.setImageType(imagefile.getContentType());
        product.setImageData(imagefile.getBytes());
        return repo.save(product);
    }

    public Product updateProduct(int productId, Product product, MultipartFile imageFile) throws IOException {
        product.setImageData(imageFile.getBytes());
        product.setImageType(imageFile.getContentType());
        product.setImageName(imageFile.getOriginalFilename());
        return repo.save(product);

    }

    public void deleteProduct(int productId) {
        repo.deleteById(productId);
    }


    public List<Product> searchProducts(String keyword) {
        return repo.searchProducts(keyword);
    }
}
