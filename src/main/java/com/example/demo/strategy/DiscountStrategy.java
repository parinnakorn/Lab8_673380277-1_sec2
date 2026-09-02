package com.example.demo.strategy;

// ISP: interface เล็ก มีหน้าที่เดียว — คำนวณราคาหลังส่วนลด
public interface DiscountStrategy {
    double applyDiscount(double originalPrice);
}
