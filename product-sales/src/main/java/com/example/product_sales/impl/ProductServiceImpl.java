package com.example.product_sales.impl;


import com.example.product_sales.entity.Product;
import com.example.product_sales.handler.ResourceNotFoundException;
import com.example.product_sales.repository.ProductRepository;
import com.example.product_sales.repository.SalesRepository;
import com.example.product_sales.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SalesRepository salesRepository;

    @Override
    public List<Product> getAllProducts(int page, int size) {
        return productRepository.findAll(PageRequest.of(page, size)).getContent();
    }

    @Override
    public Product getProductById(int id) {
        return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    @Override
    public void addProduct(Product product) {
        productRepository.save(product);
    }

    @Override
    public void updateProduct(int id, Product product) {
        Product existingProduct = getProductById(id);
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setQuantity(product.getQuantity());

        productRepository.save(existingProduct);
    }
    @Override
    public void deleteProduct(int id) {
        productRepository.deleteById(id);
    }


    public double getTotalRevenue() {
        return salesRepository.findAll().stream()
                .mapToDouble(sale -> {
                    Product product = sale.getProduct();
                    return (product != null) ? product.getPrice() * sale.getQuantity() : 0;
                })
                .sum();
    }

    public double getRevenueByProduct(int productId) {
        return salesRepository.findAll().stream()
                .filter(sale -> {
                    Integer saleProductId = sale.getProduct().getId();
                    return saleProductId.equals(productId);
                })
                .mapToDouble(sale -> {
                    Product product = sale.getProduct(); // Directly get the Product from the Sale
                    return (product != null) ? product.getPrice() * sale.getQuantity() : 0;
                })
                .sum();
    }

    public void updateQtyAfterSale(Product product){

    }
}
