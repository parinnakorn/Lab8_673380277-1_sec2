package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.Product;
import com.example.demo.model.Review;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.strategy.DiscountContext;

// SRP: ProductService รับผิดชอบเฉพาะ business logic เท่านั้น
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final DiscountContext discountContext;

    public ProductService(ProductRepository productRepository,
                           ReviewRepository reviewRepository,
                           DiscountContext discountContext) {
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
        this.discountContext = discountContext;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public double calculateFinalPrice(Product product) {
        return discountContext.calculatePrice(product.getPrice(), product.getDiscountType());
    }

    public String getDiscountName(String discountType) {
        if (discountType == null) {
            return "ราคาปกติ (0%)";
        }
        return switch (discountType) {
            case "MEMBER" -> "ส่วนลดสมาชิก (10%)";
            case "SEASONAL" -> "ส่วนลดเทศกาล (20%)";
            default -> "ราคาปกติ (0%)";
        };
    }

    // ── Review (1:N) ──
    public List<Review> getReviewsByProductId(Long productId) {
        return reviewRepository.findByProductId(productId);
    }

    public Review addReview(Long productId, Review review) {
        Product product = getProductById(productId);
        if (product == null) {
            return null;
        }
        review.setProduct(product);
        return reviewRepository.save(review);
    }

    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    public double getAverageRating(Product product) {
        List<Review> reviews = product.getReviews();
        if (reviews == null || reviews.isEmpty()) {
            return 0.0;
        }
        return reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
    }
}
