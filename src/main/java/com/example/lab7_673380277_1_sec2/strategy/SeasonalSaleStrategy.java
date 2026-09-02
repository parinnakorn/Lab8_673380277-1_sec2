package com.example.lab7_673380277_1_sec2.strategy;

import org.springframework.stereotype.Component;

@Component
public class SeasonalSaleStrategy implements DiscountStrategy {

    @Override
    public double applyDiscount(double originalPrice) {
        return originalPrice * 0.80;
    }
}