package com.example.product_sales.controller;


import com.example.product_sales.dto.SaleResponseDto;
import com.example.product_sales.dto.SalesDto;
import com.example.product_sales.entity.Sale;

import com.example.product_sales.impl.SaleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SaleController {

    @Autowired
    SaleServiceImpl salesService;

    @GetMapping
    public List<SalesDto> getAllSales(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return salesService.getAllSales(page, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalesDto> getSaleById(@PathVariable int id) {
        return ResponseEntity.ok(salesService.getSaleById(id));
    }

    @PostMapping
    public ResponseEntity<SaleResponseDto> addSale(@RequestParam int productId, @RequestParam int quantity) {
        SaleResponseDto newSale = salesService.addSale(productId, quantity);
        return new ResponseEntity<>(newSale, HttpStatus.CREATED);
    }

    @PutMapping("/updateSales")
    public ResponseEntity<SaleResponseDto> updateSale( @RequestBody SalesDto sale) {
        SaleResponseDto saleResponseDto =salesService.updateSales( sale);
        return new ResponseEntity<>(saleResponseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSale(@PathVariable int id) {
        salesService.deleteSale(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
