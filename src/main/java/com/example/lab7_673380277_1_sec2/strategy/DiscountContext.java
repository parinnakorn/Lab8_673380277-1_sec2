package com.example.lab7_673380277_1_sec2.strategy;

import org.springframework.stereotype.Component;

@Component
public class DiscountContext {

    private final NoDiscountStrategy noDiscountStrategy;
    private final StudentDiscountStrategy studentDiscountStrategy;
    private final SeasonalSaleStrategy seasonalSaleStrategy;

    public DiscountContext(NoDiscountStrategy noDiscountStrategy,
                            StudentDiscountStrategy studentDiscountStrategy,
                            SeasonalSaleStrategy seasonalSaleStrategy) {
        this.noDiscountStrategy = noDiscountStrategy;
        this.studentDiscountStrategy = studentDiscountStrategy;
        this.seasonalSaleStrategy = seasonalSaleStrategy;
    }

    public double calculatePrice(double originalPrice, String discountType) {
        DiscountStrategy strategy;
        switch (discountType) {
            case "STUDENT":
                strategy = studentDiscountStrategy;
                break;
            case "SEASONAL":
                strategy = seasonalSaleStrategy;
                break;
            case "NONE":
            default:
                strategy = noDiscountStrategy;
                break;
        }
        return strategy.applyDiscount(originalPrice);
    }
}