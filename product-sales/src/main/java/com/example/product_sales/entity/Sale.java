package com.example.product_sales.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;

@Entity
@Data
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore
    private Product product;

    private int quantity;

    @Temporal(TemporalType.DATE)
    private Date saleDate;


    public Sale(Product product, int quantity, Date saleDate) {
        this.product = product;
        this.quantity = quantity;
        this.saleDate = saleDate;
    }
    public Sale() {}


}
