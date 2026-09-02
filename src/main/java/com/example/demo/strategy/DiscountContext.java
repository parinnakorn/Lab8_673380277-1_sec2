package com.example.demo.strategy;

import org.springframework.stereotype.Component;

// DIP: DiscountContext ขึ้นกับ interface DiscountStrategy ไม่ใช่ class จริง
@Component
public class DiscountContext {

    private final NoDiscountStrategy noDiscountStrategy;
    private final MemberDiscountStrategy memberDiscountStrategy;
    private final SeasonalSaleStrategy seasonalSaleStrategy;

    public DiscountContext(NoDiscountStrategy noDiscountStrategy,
                            MemberDiscountStrategy memberDiscountStrategy,
                            SeasonalSaleStrategy seasonalSaleStrategy) {
        this.noDiscountStrategy = noDiscountStrategy;
        this.memberDiscountStrategy = memberDiscountStrategy;
        this.seasonalSaleStrategy = seasonalSaleStrategy;
    }

    public double calculatePrice(double originalPrice, String discountType) {
        DiscountStrategy strategy;
        if (discountType == null) {
            strategy = noDiscountStrategy;
        } else {
            switch (discountType) {
                case "MEMBER":
                    strategy = memberDiscountStrategy;
                    break;
                case "SEASONAL":
                    strategy = seasonalSaleStrategy;
                    break;
                case "NONE":
                default:
                    strategy = noDiscountStrategy;
                    break;
            }
        }
        return strategy.applyDiscount(originalPrice);
    }
}
