package com.example.product_sales.service;


import com.example.product_sales.dto.SaleResponseDto;
import com.example.product_sales.dto.SalesDto;
import com.example.product_sales.entity.Sale;
import jakarta.transaction.Transactional;

import java.util.List;

public interface SalesService {
    @Transactional
    SaleResponseDto addSale(int productId, int quantity);

    List<SalesDto> getAllSales(int page, int size);

    SalesDto getSaleById(int id);

    SaleResponseDto updateSales( SalesDto sale);

    void deleteSale(int id);
}
