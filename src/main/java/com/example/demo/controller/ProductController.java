package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.Product;
import com.example.demo.model.Review;
import com.example.demo.service.ProductService;

// SRP: Controller รับผิดชอบเฉพาะรับ-ส่ง HTTP เท่านั้น ไม่มี business logic
@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // READ — แสดงรายการสินค้าทั้งหมด
    @GetMapping
    public String listProducts(Model model) {
        List<Product> products = productService.getAllProducts();

        for (Product product : products) {
            product.setFinalPrice(productService.calculateFinalPrice(product));
            product.setDiscountName(productService.getDiscountName(product.getDiscountType()));
        }

        model.addAttribute("products", products);
        return "products/list";
    }

    // CREATE — แสดงฟอร์มเพิ่มสินค้า (มีฟอร์ม ProductDetail ในหน้าเดียวกัน)
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        return "products/add";
    }

    // CREATE — บันทึกสินค้าใหม่ (+ ProductDetail ผ่าน cascade)
    @PostMapping("/save")
    public String saveProduct(@ModelAttribute Product product, RedirectAttributes redirectAttributes) {
        productService.saveProduct(product);
        redirectAttributes.addFlashAttribute("message", "เพิ่มสินค้า \"" + product.getName() + "\" สำเร็จ");
        return "redirect:/products";
    }

    // UPDATE — แสดงฟอร์มแก้ไข
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        if (product == null) {
            return "redirect:/products";
        }
        model.addAttribute("product", product);
        return "products/edit";
    }

    // UPDATE — อัปเดตข้อมูลสินค้า (+ ProductDetail)
    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable Long id, @ModelAttribute Product product,
                                 RedirectAttributes redirectAttributes) {
        product.setId(id);
        productService.saveProduct(product);
        redirectAttributes.addFlashAttribute("message", "อัปเดตสินค้า \"" + product.getName() + "\" สำเร็จ");
        return "redirect:/products";
    }

    // DELETE — แสดงหน้ายืนยันลบ
    @GetMapping("/delete/{id}")
    public String showDeleteConfirm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        if (product == null) {
            return "redirect:/products";
        }
        model.addAttribute("product", product);
        return "products/delete";
    }

    // DELETE — ลบสินค้า (ProductDetail และ Review ถูกลบตาม cascade)
    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        productService.deleteProduct(id);
        redirectAttributes.addFlashAttribute("message", "ลบสินค้าสำเร็จ");
        return "redirect:/products";
    }

    // ── Review (1:N) ──

    // CREATE — เพิ่มรีวิวให้สินค้า (จากหน้าแก้ไขสินค้า)
    @PostMapping("/{id}/reviews/add")
    public String addReview(@PathVariable Long id, @ModelAttribute Review review,
                             RedirectAttributes redirectAttributes) {
        productService.addReview(id, review);
        redirectAttributes.addFlashAttribute("message", "เพิ่มรีวิวสำเร็จ");
        return "redirect:/products/edit/" + id;
    }

    // DELETE — ลบรีวิว
    @PostMapping("/{id}/reviews/delete/{reviewId}")
    public String deleteReview(@PathVariable Long id, @PathVariable Long reviewId,
                                RedirectAttributes redirectAttributes) {
        productService.deleteReview(reviewId);
        redirectAttributes.addFlashAttribute("message", "ลบรีวิวสำเร็จ");
        return "redirect:/products/edit/" + id;
    }
}
