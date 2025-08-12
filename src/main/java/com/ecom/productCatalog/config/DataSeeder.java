package com.ecom.productCatalog.config;

import com.ecom.productCatalog.model.Category;
import com.ecom.productCatalog.model.Product;
import com.ecom.productCatalog.repository.CategoryRepository;
import com.ecom.productCatalog.repository.ProductRepository;
import com.ecom.productCatalog.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    @Transactional // Good practice to run seeder in a single transaction
    public void run(String... args) {
        // Step 1: Clear existing data in the correct order
        cartItemRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        categoryRepository.deleteAllInBatch();

        // Step 2: Create and save categories
        Category electronics = new Category();
        electronics.setName("Electronics");

        Category home = new Category();
        home.setName("Home & Living");

        Category beauty = new Category();
        beauty.setName("Beauty & Personal Care");

        Category toys = new Category();
        toys.setName("Toys & Games");

        Category clothing = new Category();
        clothing.setName("Clothing");

        Category books = new Category();
        books.setName("Books");

        // --- NEW CATEGORIES ---
        Category sports = new Category();
        sports.setName("Sports & Outdoors");

        Category automotive = new Category();
        automotive.setName("Automotive");

        categoryRepository.saveAll(Arrays.asList(home, beauty, toys, electronics, clothing, books, sports, automotive));

        // Step 3: Fetch categories reliably by name and put them in a Map
        Map<String, Category> categoriesByName = categoryRepository.findAll().stream()
                .collect(Collectors.toMap(Category::getName, Function.identity()));

        // Step 4: Create products using the reliably fetched categories

        // --- Electronics ---
        Product phone = new Product();
        phone.setName("Smartphone");
        phone.setDescription("A modern Android smartphone");
        phone.setPrice(new BigDecimal("30000"));
        phone.setImageUrl("https://m.media-amazon.com/images/I/71w3oJ7aWyL._SL1500_.jpg");
        phone.setStockQuantity(10);
        phone.setCategory(categoriesByName.get("Electronics"));

        Product laptop = new Product();
        laptop.setName("Laptop");
        laptop.setDescription("Powerful laptop for work and play");
        laptop.setPrice(new BigDecimal("99000"));
        laptop.setImageUrl("https://m.media-amazon.com/images/I/71jG+e7roXL._SL1500_.jpg");
        laptop.setStockQuantity(5);
        laptop.setCategory(categoriesByName.get("Electronics"));

        Product smartwatch = new Product();
        smartwatch.setName("Smartwatch");
        smartwatch.setDescription("Track your fitness, sleep, and heart rate");
        smartwatch.setPrice(new BigDecimal("6999"));
        smartwatch.setImageUrl("https://m.media-amazon.com/images/I/61b5Z3D8xoL._SL1500_.jpg");
        smartwatch.setStockQuantity(20);
        smartwatch.setCategory(categoriesByName.get("Electronics"));

        // --- Clothing ---
        Product tshirt = new Product();
        tshirt.setName("T-Shirt");
        tshirt.setDescription("100% Cotton T-Shirt");
        tshirt.setPrice(new BigDecimal("1999"));
        tshirt.setImageUrl("https://m.media-amazon.com/images/I/61W9fZIv7tL._UL1500_.jpg");
        tshirt.setStockQuantity(20);
        tshirt.setCategory(categoriesByName.get("Clothing"));

        Product jeans = new Product();
        jeans.setName("Jeans");
        jeans.setDescription("Comfort fit blue jeans");
        jeans.setPrice(new BigDecimal("3999"));
        jeans.setImageUrl("https://m.media-amazon.com/images/I/71QKQ9mwV7L._UL1500_.jpg");
        jeans.setStockQuantity(15);
        jeans.setCategory(categoriesByName.get("Clothing"));

        // --- Home & Living ---
        Product bedsheet = new Product();
        bedsheet.setName("Cotton Bedsheet Set");
        bedsheet.setDescription("100% cotton, king size, 3 pieces");
        bedsheet.setPrice(new BigDecimal("1599"));
        bedsheet.setImageUrl("https://m.media-amazon.com/images/I/81LzIl9IojL._SL1500_.jpg");
        bedsheet.setStockQuantity(30);
        bedsheet.setCategory(categoriesByName.get("Home & Living"));

        Product lamp = new Product();
        lamp.setName("LED Desk Lamp");
        lamp.setDescription("Adjustable brightness, perfect for reading or working");
        lamp.setPrice(new BigDecimal("1299"));
        lamp.setImageUrl("https://m.media-amazon.com/images/I/61USxt9kAHL._SL1500_.jpg");
        lamp.setStockQuantity(25);
        lamp.setCategory(categoriesByName.get("Home & Living"));

        // --- Books ---
        Product novel = new Product();
        novel.setName("Bestselling Novel");
        novel.setDescription("A gripping mystery fiction novel");
        novel.setPrice(new BigDecimal("499"));
        novel.setImageUrl("https://m.media-amazon.com/images/I/81AFxgbV9cL.jpg");
        novel.setStockQuantity(25);
        novel.setCategory(categoriesByName.get("Books"));

        // --- NEW PRODUCTS ---
        Product yogaMat = new Product();
        yogaMat.setName("Yoga Mat");
        yogaMat.setDescription("Non-slip, eco-friendly material for your daily yoga practice.");
        yogaMat.setPrice(new BigDecimal("1499"));
        yogaMat.setImageUrl("https://m.media-amazon.com/images/I/712pA+01WFL._SL1500_.jpg");
        yogaMat.setStockQuantity(40);
        yogaMat.setCategory(categoriesByName.get("Sports & Outdoors"));

        Product dumbbells = new Product();
        dumbbells.setName("Dumbbell Set");
        dumbbells.setDescription("Set of two 5kg rubber-coated dumbbells for home workouts.");
        dumbbells.setPrice(new BigDecimal("2499"));
        dumbbells.setImageUrl("https://m.media-amazon.com/images/I/61fR63sFqjL._SL1091_.jpg");
        dumbbells.setStockQuantity(25);
        dumbbells.setCategory(categoriesByName.get("Sports & Outdoors"));

        Product carPerfume = new Product();
        carPerfume.setName("Car Air Freshener");
        carPerfume.setDescription("Long-lasting ocean breeze fragrance for your car.");
        carPerfume.setPrice(new BigDecimal("349"));
        carPerfume.setImageUrl("https://m.media-amazon.com/images/I/61jC8VjM6iL._SL1500_.jpg");
        carPerfume.setStockQuantity(100);
        carPerfume.setCategory(categoriesByName.get("Automotive"));

        Product microfiberCloth = new Product();
        microfiberCloth.setName("Microfiber Cleaning Cloths");
        microfiberCloth.setDescription("Pack of 5 ultra-soft cloths for car and home cleaning.");
        microfiberCloth.setPrice(new BigDecimal("299"));
        microfiberCloth.setImageUrl("https://m.media-amazon.com/images/I/81s4hA+0siL._SL1500_.jpg");
        microfiberCloth.setStockQuantity(150);
        microfiberCloth.setCategory(categoriesByName.get("Automotive"));

        Product boardGame = new Product();
        boardGame.setName("Strategy Board Game");
        boardGame.setDescription("Fun for the whole family, 2-4 players");
        boardGame.setPrice(new BigDecimal("749"));
        boardGame.setImageUrl("https://m.media-amazon.com/images/I/81TW9PZ5pIL._SL1500_.jpg");
        boardGame.setStockQuantity(15);
        boardGame.setCategory(categoriesByName.get("Toys & Games"));

        Product moisturizer = new Product();
        moisturizer.setName("Hydrating Moisturizer");
        moisturizer.setDescription("Aloe vera infused, suitable for all skin types");
        moisturizer.setPrice(new BigDecimal("399"));
        moisturizer.setImageUrl("https://m.media-amazon.com/images/I/51skJ8DPuJL._SL1000_.jpg");
        moisturizer.setStockQuantity(50);
        moisturizer.setCategory(categoriesByName.get("Beauty & Personal Care"));

        // Step 5: Save all products to the database
        productRepository.saveAll(List.of(
                phone, laptop, tshirt, jeans, novel, bedsheet, lamp, smartwatch,
                yogaMat, dumbbells, carPerfume, microfiberCloth, boardGame, moisturizer
        ));
    }
}