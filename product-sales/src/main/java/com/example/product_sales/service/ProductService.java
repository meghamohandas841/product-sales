package com.example.product_sales.service;



import com.example.product_sales.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts(int page, int size);

    Product getProductById(int id);

    void addProduct(Product product);

    void updateProduct(int id, Product product);

    void deleteProduct(int id);
}
