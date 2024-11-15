package com.example.product_sales.impl;

import com.example.product_sales.dto.SaleResponseDto;
import com.example.product_sales.dto.SalesDto;
import com.example.product_sales.entity.Product;
import com.example.product_sales.entity.Sale;
import com.example.product_sales.handler.ResourceNotFoundException;
import com.example.product_sales.repository.ProductRepository;
import com.example.product_sales.repository.SalesRepository;
import com.example.product_sales.service.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SaleServiceImpl implements SalesService {

    @Autowired
    SalesRepository salesRepository;

    @Autowired
    ProductRepository productRepository;

    @Override
    public SaleResponseDto addSale(int productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        Sale sale = new Sale();
        SaleResponseDto saleResponseDto = new SaleResponseDto();
        if(product.getQuantity() >=quantity) {
            sale.setProduct(product);
            sale.setQuantity(quantity);
            sale.setSaleDate(new Date(System.currentTimeMillis()));
            salesRepository.save(sale);
            product.setQuantity(product.getQuantity() - quantity);
            productRepository.save(product);
            saleResponseDto.setSale(sale);
            saleResponseDto.setMessage("Success");

        }
        else{
            saleResponseDto.setSale(sale);
            saleResponseDto.setMessage("Desired Quantity not Found");
        }
        return saleResponseDto;
    }

    @Override
    public List<SalesDto> getAllSales(int page, int size) {
        List<Sale> sales = salesRepository.findAll(PageRequest.of(page, size)).getContent();
        return sales.stream()
                .map(sale -> new SalesDto(sale)) // Use a lambda expression
                .collect(Collectors.toList());

    }

    @Override
    public SalesDto getSaleById(int id) {
        Sale sale = salesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sales not found"));
        return new SalesDto(sale);

    }

    @Override
    public SaleResponseDto updateSales( SalesDto sale) {
        Sale existingSale = salesRepository.findById(sale.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found"));
        Product updatedProduct = productRepository.findById(sale.getProduct().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        SaleResponseDto saleResponseDto = new SaleResponseDto();
        if(sale.getQuantity()  <= existingSale.getQuantity()){
            int newQty =   existingSale.getQuantity() - sale.getQuantity();
            updatedProduct.setQuantity(updatedProduct.getQuantity() + newQty);
            existingSale.setQuantity(sale.getQuantity());
            existingSale.setSaleDate(sale.getSaleDate());
            productRepository.save(updatedProduct);
            Sale updatedSale= salesRepository.save(existingSale);
            saleResponseDto.setMessage("Successfully Updated");
            saleResponseDto.setSale(updatedSale);
        }
        else{
            saleResponseDto.setMessage("Sale cannot be updated. Add a quantity less than or equal to the purchased quantity");
            saleResponseDto.setSale(existingSale);
        }
return saleResponseDto;
    }

    @Override
    public void deleteSale(int id) {
        salesRepository.deleteById(id);


    }
}
