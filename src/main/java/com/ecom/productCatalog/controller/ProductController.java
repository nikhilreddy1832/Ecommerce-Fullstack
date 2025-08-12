package com.ecom.productCatalog.controller;

import com.ecom.productCatalog.model.Product;
import com.ecom.productCatalog.repository.ProductRepository;
import com.ecom.productCatalog.service.ProductService; // Only import service
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/products")
public class ProductController {
    private final ProductRepository productRepository;
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService,ProductRepository productRepository) {
        this.productService = productService;
        this.productRepository=productRepository;
    }

    @GetMapping
    public String getAllProducts(Model model) {
        List<Product> allProducts = productService.getAllProducts();
        model.addAttribute("products", allProducts);
        return "buyer-home";
    }

    @GetMapping("/category/{categoryId}")
    public String getProductsByCategory(@PathVariable Long categoryId, Model model) {
        List<Product> productsByCategory = productService.getProductsByCategory(categoryId);
        model.addAttribute("products", productsByCategory);
        return "buyer-home";
    }

    // ✅ Corrected URL mapping
    @GetMapping("/search")
    public String searchProducts(@RequestParam("query") String query, Model model) {
        List<Product> products = productService.searchByNameOrDescription(query);
        model.addAttribute("products", products);
        return "buyer-home";
    }
    @GetMapping("/{id}")
    public String viewProductDetail(@PathVariable Long id, Model model) {
        // Find the product by its ID from the repository
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Add the found product to the model to be used in the HTML page
        model.addAttribute("product", product);

        // Return the name of the new HTML file we are about to create
        return "product-detail";
    }

}