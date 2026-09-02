package com.example.lab7_673380277_1_sec2.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.lab7_673380277_1_sec2.model.Game;
import com.example.lab7_673380277_1_sec2.service.GameService;

@Controller
@RequestMapping("/games")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    // READ — แสดงรายการเกมทั้งหมด
    @GetMapping
    public String listGames(Model model) {
        List<Game> games = gameService.getAllGames();

        for (Game game : games) {
            game.setFinalPrice(gameService.calculateFinalPrice(game));
            game.setDiscountName(gameService.getDiscountName(game.getDiscountType()));
        }

        model.addAttribute("games", games);
        return "games/list";
    }

    // CREATE — แสดงฟอร์มเพิ่มเกม
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("game", new Game());
        return "games/add";
    }

    // CREATE — บันทึกเกมใหม่
    @PostMapping("/save")
    public String saveGame(@ModelAttribute Game game, RedirectAttributes redirectAttributes) {
        gameService.saveGame(game);
        redirectAttributes.addFlashAttribute("message", "เพิ่มเกม \"" + game.getTitle() + "\" สำเร็จ");
        return "redirect:/games";
    }

    // UPDATE — แสดงฟอร์มแก้ไข
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Game game = gameService.getGameById(id);
        if (game == null) {
            return "redirect:/games";
        }
        model.addAttribute("game", game);
        return "games/edit";
    }

    // UPDATE — อัปเดตข้อมูลเกม
    @PostMapping("/update/{id}")
    public String updateGame(@PathVariable Long id, @ModelAttribute Game game,
                              RedirectAttributes redirectAttributes) {
        game.setId(id);
        gameService.saveGame(game);
        redirectAttributes.addFlashAttribute("message", "อัปเดตเกม \"" + game.getTitle() + "\" สำเร็จ");
        return "redirect:/games";
    }

    // DELETE — แสดงหน้ายืนยันลบ
    @GetMapping("/delete/{id}")
    public String showDeleteConfirm(@PathVariable Long id, Model model) {
        Game game = gameService.getGameById(id);
        if (game == null) {
            return "redirect:/games";
        }
        model.addAttribute("game", game);
        return "games/delete";
    }

    // DELETE — ลบเกม
    @PostMapping("/delete/{id}")
    public String deleteGame(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        gameService.deleteGame(id);
        redirectAttributes.addFlashAttribute("message", "ลบเกมสำเร็จ");
        return "redirect:/games";
    }
}