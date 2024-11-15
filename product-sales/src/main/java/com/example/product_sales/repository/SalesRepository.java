package com.example.product_sales.repository;


import com.example.product_sales.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesRepository extends JpaRepository<Sale,Integer> {

}
