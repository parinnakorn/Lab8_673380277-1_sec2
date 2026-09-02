package com.example.lab7_673380277_1_sec2.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.lab7_673380277_1_sec2.model.Game;
import com.example.lab7_673380277_1_sec2.repository.GameRepository;
import com.example.lab7_673380277_1_sec2.strategy.DiscountContext;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final DiscountContext discountContext;

    public GameService(GameRepository gameRepository, DiscountContext discountContext) {
        this.gameRepository = gameRepository;
        this.discountContext = discountContext;
    }

    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    public Game getGameById(Long id) {
        return gameRepository.findById(id).orElse(null);
    }

    public Game saveGame(Game game) {
        return gameRepository.save(game);
    }

    public void deleteGame(Long id) {
        gameRepository.deleteById(id);
    }

    public double calculateFinalPrice(Game game) {
        return discountContext.calculatePrice(game.getPrice(), game.getDiscountType());
    }

    public String getDiscountName(String discountType) {
        if (discountType == null) {
            return "ราคาปกติ (0%)";
        }
        return switch (discountType) {
            case "STUDENT" -> "ส่วนลดนักศึกษา (10%)";
            case "SEASONAL" -> "ส่วนลดเทศกาล (20%)";
            default -> "ราคาปกติ (0%)";
        };
    }
}