package com.example.product_sales.dto;

import com.example.product_sales.entity.Sale;
import lombok.Data;

@Data
public class SaleResponseDto {

    private String message;

    private Sale sale;
}
