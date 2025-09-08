package com.dominio.Catalogo.controllers;

import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.InputStream;
import java.util.Date;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.Valid;

import com.dominio.Catalogo.models.Product;
import com.dominio.Catalogo.models.ProductDto;
import com.dominio.Catalogo.services.ProductsRepository;

@Controller
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductsRepository repo;

    @GetMapping({"", "/"})
    public String showProductList(Model model) {
        List<Product> products = repo.findAll(Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("products", products);
        return "products/index";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {
        ProductDto productDto = new ProductDto();
        model.addAttribute("productDto", productDto);
        return "products/CreateProduct";
    }

    @PostMapping("/create")
    public String createProduct(
            @Valid @ModelAttribute ProductDto productDto,
            BindingResult result
    ) {
        if (productDto.getImageFile().isEmpty()) {
            result.addError(new FieldError("productDto", "imageFile", "El archivo es requerido"));
        }

        if (result.hasErrors()) {
            return "products/CreateProduct";
        }

        // Guardar archivo de imagen
        MultipartFile image = productDto.getImageFile();
        Date createdAt = new Date();
        String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();

        try {
            String uploadDir = "public/images";
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            try (InputStream inputStream = image.getInputStream()) {
                Files.copy(inputStream, Paths.get(uploadDir + "/" + storageFileName), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }

        Product product = new Product();
        product.setName(productDto.getName());
        product.setBrand(productDto.getBrand());
        product.setCategory(productDto.getCategory());
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());
        product.setCreatedAt(createdAt);
        product.setImageFileName(storageFileName);

        repo.save(product);

        return "redirect:/products";
    }

    @GetMapping("/edit")
    public String showEditPage(
            Model model,
            @RequestParam int id
    ) {
        try {
            Product product = repo.findById(id).orElseThrow();
            model.addAttribute("product", product);

            ProductDto productDto = new ProductDto();
            productDto.setName(product.getName());
            productDto.setBrand(product.getBrand());
            productDto.setCategory(product.getCategory());
            productDto.setPrice(product.getPrice());
            productDto.setDescription(product.getDescription());

            model.addAttribute("productDto", productDto);

        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            return "redirect:/products";
        }
        return "products/EditProduct";
    }

    @PostMapping("/edit")
    public String updateProduct(
            Model model,
            @RequestParam int id,
            @Valid @ModelAttribute ProductDto productDto,
            BindingResult result
    ) {
        try {
            Product product = repo.findById(id).orElseThrow();
            model.addAttribute("product", product);

            if (result.hasErrors()) {
                return "products/EditProduct";
            }

            if (!productDto.getImageFile().isEmpty()) {
                // Borrar la imagen existente
                String uploadDir = "public/images/";
                Path oldImagePath = Paths.get(uploadDir + product.getImageFileName());

                try {
                    Files.deleteIfExists(oldImagePath);
                } catch (Exception ex) {
                    System.out.println("Exception: " + ex.getMessage());
                }

                // Guardar la nueva imagen
                MultipartFile image = productDto.getImageFile();
                Date createdAt = new Date();
                String storageFileName = createdAt.getTime() + "_" + image.getOriginalFilename();

                try (InputStream inputStream = image.getInputStream()) {
                    Files.copy(inputStream, Paths.get(uploadDir + storageFileName), StandardCopyOption.REPLACE_EXISTING);
                }

                product.setImageFileName(storageFileName);
            }

            product.setName(productDto.getName());
            product.setBrand(productDto.getBrand());
            product.setCategory(productDto.getCategory());
            product.setPrice(productDto.getPrice());
            product.setDescription(productDto.getDescription());

            repo.save(product);

        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }

        return "redirect:/products";
    }

    @GetMapping("/delete")
    public String deleteProduct(
            @RequestParam int id
    ) {
        try {
            Product product = repo.findById(id).orElseThrow();

            // Aquí solo elimina el producto de la base de datos
            repo.delete(product);

        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }

        return "redirect:/products";
    }

    @GetMapping("/ver")
    public String verProducto(
            @RequestParam int id,
            Model model
    ) {
        try {
            Product product = repo.findById(id).orElseThrow();
            model.addAttribute("product", product);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            return "redirect:/products";
        }
        return "products/VerProduct";
    }
}
