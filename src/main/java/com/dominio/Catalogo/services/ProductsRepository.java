package com.dominio.Catalogo.services;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dominio.Catalogo.models.Product; 


public interface ProductsRepository extends JpaRepository<Product, Integer> {

}
