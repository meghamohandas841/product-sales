package com.example.product_sales.dto;


import com.example.product_sales.entity.Product;
import lombok.Data;

@Data
public class ProductDto {
    private Integer id;
    private String name;


    public ProductDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
    }

    public ProductDto() {
    }
}
