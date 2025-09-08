package com.dominio.Catalogo.models;

import org.springframework.web.multipart. MultipartFile;

import jakarta.validation.constraints.*;

public class ProductDto {
	
	@NotEmpty(message = "El nombre es requerido")
	private String name;
	
	@NotEmpty(message = "La marca es requerida")
	private String brand;
	
	@NotEmpty(message = "La categoría es requerida")
	private String category;
	
	@Min(0)
	private double price;
	
	@Size (min= 10, message = "La descripción debe ser al menos de 10 caracteres")
	@Size (max= 2000, message = "La descripción no debe exceder de 2000 caracteres")
	private String description;
	
	private MultipartFile imageFile;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public MultipartFile getImageFile() {
		return imageFile;
	}

	public void setImageFile(MultipartFile imageFile) {
		this.imageFile = imageFile;
	}

}
